/*
 * Copyright 2017 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.optaplanner.workbench.screens.domaineditor.backend.server.helper;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.kie.workbench.common.screens.datamodeller.backend.server.helper.DataModelerRenameWorkaroundHelper;
import org.kie.workbench.common.screens.datamodeller.model.GenerationResult;
import org.kie.workbench.common.screens.datamodeller.service.DataModelerService;
import org.kie.workbench.common.services.datamodeller.core.DataObject;
import org.kie.workbench.common.services.shared.project.KieModuleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.uberfire.backend.server.util.Paths;
import org.uberfire.backend.vfs.Path;
import org.uberfire.io.IOService;
import org.uberfire.java.nio.file.Files;

import static org.optaplanner.workbench.screens.domaineditor.model.PlannerDomainAnnotations.PLANNING_SOLUTION_ANNOTATION;

/**
 * TODO Change to RenameHelper implementation once DataModelerServiceImpl.rename() uses RenameService instead of IOService.move()
 */
@ApplicationScoped
public class PlanningSolutionRenameWorkaroundHelper implements DataModelerRenameWorkaroundHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(PlanningSolutionRenameWorkaroundHelper.class);

    private static final String SCORE_HOLDER_GLOBAL_FILE_SUFFIX = "ScoreHolderGlobal.gdrl";

    private IOService ioService;

    private DataModelerService dataModelerService;

    private KieModuleService kieModuleService;

    public PlanningSolutionRenameWorkaroundHelper() {
    }

    @Inject
    public PlanningSolutionRenameWorkaroundHelper(@Named("ioStrategy") final IOService ioService,
                                                  final DataModelerService dataModelerService,
                                                  final KieModuleService kieModuleService) {
        this.ioService = ioService;
        this.dataModelerService = dataModelerService;
        this.kieModuleService = kieModuleService;
    }

    @Override
    public void postProcess(final Path sourcePath,
                            final Path destinationPath) {
        String dataObjectSource = ioService.readAllString(Paths.convert(destinationPath));
        GenerationResult generationResult = dataModelerService.loadDataObject(destinationPath,
                                                                              dataObjectSource,
                                                                              destinationPath);

        if (generationResult.hasErrors()) {
            LOGGER.warn("Path " + sourcePath + " parsing as a data object has failed. Score holder global generation will be skipped.");
        } else {
            DataObject dataObject = generationResult.getDataObject();
            if (dataObject.getAnnotation(PLANNING_SOLUTION_ANNOTATION) != null) {
                org.uberfire.java.nio.file.Path source = Paths.convert(kieModuleService.resolvePackage(sourcePath).getPackageMainResourcesPath());
                org.uberfire.java.nio.file.Path sourcePackage = Files.isDirectory(source) ? source : source.getParent();
                String sourceDataObjectFileName = sourcePath.getFileName().substring(0,
                                                                                     sourcePath.getFileName().indexOf("."));

                org.uberfire.java.nio.file.Path destination = Paths.convert(kieModuleService.resolvePackage(destinationPath).getPackageMainResourcesPath());
                org.uberfire.java.nio.file.Path destinationPackage = Files.isDirectory(destination) ? destination : destination.getParent();
                String destinationDataObjectFileName = destinationPath.getFileName().substring(0,
                                                                                               destinationPath.getFileName().indexOf("."));

                boolean scoreHolderGlobalFileExists = ioService.exists(sourcePackage.resolve(sourceDataObjectFileName + SCORE_HOLDER_GLOBAL_FILE_SUFFIX));

                if (scoreHolderGlobalFileExists) {
                    ioService.move(sourcePackage.resolve(sourceDataObjectFileName + SCORE_HOLDER_GLOBAL_FILE_SUFFIX),
                                   destinationPackage.resolve(destinationDataObjectFileName + SCORE_HOLDER_GLOBAL_FILE_SUFFIX));
                }
            }
        }
    }
}
