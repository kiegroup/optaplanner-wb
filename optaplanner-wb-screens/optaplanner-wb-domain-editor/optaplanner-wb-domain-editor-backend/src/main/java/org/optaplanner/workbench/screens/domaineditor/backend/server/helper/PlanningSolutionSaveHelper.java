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

import java.util.Arrays;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.drools.workbench.screens.globals.model.Global;
import org.drools.workbench.screens.globals.model.GlobalsModel;
import org.drools.workbench.screens.globals.service.GlobalsEditorService;
import org.guvnor.common.services.shared.metadata.MetadataService;
import org.kie.workbench.common.screens.datamodeller.backend.server.helper.DataModelerSaveHelper;
import org.kie.workbench.common.screens.datamodeller.model.GenerationResult;
import org.kie.workbench.common.screens.datamodeller.service.DataModelerService;
import org.kie.workbench.common.services.datamodeller.core.DataObject;
import org.kie.workbench.common.services.shared.project.KieModuleService;
import org.optaplanner.workbench.screens.domaineditor.backend.server.validation.ScoreHolderUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.uberfire.backend.server.util.Paths;
import org.uberfire.backend.vfs.Path;
import org.uberfire.io.IOService;
import org.uberfire.java.nio.file.Files;

import static org.optaplanner.workbench.screens.domaineditor.model.PlannerDomainAnnotations.PLANNING_SOLUTION_ANNOTATION;

@ApplicationScoped
public class PlanningSolutionSaveHelper implements DataModelerSaveHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(PlanningSolutionSaveHelper.class);

    private static final String SCORE_HOLDER = "scoreHolder";

    private static final String SCORE_HOLDER_GLOBAL_FILE_SUFFIX = "ScoreHolderGlobal.gdrl";

    private DataModelerService dataModelerService;

    private IOService ioService;

    private GlobalsEditorService globalsEditorService;

    private KieModuleService kieModuleService;

    private ScoreHolderUtils scoreHolderUtils;

    private MetadataService metadataService;

    public PlanningSolutionSaveHelper() {
    }

    @Inject
    public PlanningSolutionSaveHelper(@Named("ioStrategy") final IOService ioService,
                                      final DataModelerService dataModelerService,
                                      final GlobalsEditorService globalsEditorService,
                                      final KieModuleService kieModuleService,
                                      final ScoreHolderUtils scoreHolderUtils,
                                      final MetadataService metadataService) {
        this.ioService = ioService;
        this.dataModelerService = dataModelerService;
        this.globalsEditorService = globalsEditorService;
        this.kieModuleService = kieModuleService;
        this.scoreHolderUtils = scoreHolderUtils;
        this.metadataService = metadataService;
    }

    @Override
    public void postProcess(final Path sourcePath,
                            final Path destinationPath) {

        String dataObjectSource = ioService.readAllString(Paths.convert(destinationPath));
        GenerationResult generationResult = dataModelerService.loadDataObject(destinationPath,
                                                                              dataObjectSource,
                                                                              destinationPath);
        org.uberfire.java.nio.file.Path source = Paths.convert(kieModuleService.resolvePackage(sourcePath).getPackageMainResourcesPath());
        org.uberfire.java.nio.file.Path sourcePackage = Files.isDirectory(source) ? source : source.getParent();

        String sourceSolutionFileName = sourcePath.getFileName().substring(0,
                                                                           sourcePath.getFileName().indexOf("."));
        org.uberfire.java.nio.file.Path sourceScoreHolderGlobalPath = sourcePackage.resolve(sourceSolutionFileName + SCORE_HOLDER_GLOBAL_FILE_SUFFIX);

        if (generationResult.hasErrors()) {
            LOGGER.warn("Path " + destinationPath + " parsing as a data object has failed. Score holder global generation will be skipped.");
        } else {
            DataObject dataObject = generationResult.getDataObject();
            if (dataObject.getAnnotation(PLANNING_SOLUTION_ANNOTATION) != null) {
                String sourceScoreTypeFqn = scoreHolderUtils.extractScoreTypeFqn(dataObject);
                String scoreHolderTypeFqn = scoreHolderUtils.getScoreHolderTypeFqn(sourceScoreTypeFqn);

                if (scoreHolderTypeFqn == null) {
                    LOGGER.warn("'scoreHolder' global variable will not be generated, as the selected score type is not supported");
                    return;
                }

                if (sourcePath.equals(destinationPath)) {
                    boolean scoreHolderGlobalFileExists = ioService.exists(sourceScoreHolderGlobalPath);

                    if (scoreHolderGlobalFileExists) {
                        GlobalsModel globalsModel = globalsEditorService.load(Paths.convert(sourceScoreHolderGlobalPath));
                        globalsModel.setGlobals(Arrays.asList(new Global(SCORE_HOLDER,
                                                                         scoreHolderTypeFqn)));
                        globalsEditorService.save(Paths.convert(sourceScoreHolderGlobalPath),
                                                  globalsModel,
                                                  metadataService.getMetadata(Paths.convert(sourceScoreHolderGlobalPath)),
                                                  "Auto generate scoreHolder global");
                    } else {
                        createScoreHolderGlobalFile(Paths.convert(sourcePackage),
                                                    scoreHolderTypeFqn,
                                                    sourceSolutionFileName);
                    }
                } else {
                    org.uberfire.java.nio.file.Path destination = Paths.convert(kieModuleService.resolvePackage(destinationPath).getPackageMainResourcesPath());
                    org.uberfire.java.nio.file.Path destinationPackage = Files.isDirectory(destination) ? destination : destination.getParent();

                    ioService.deleteIfExists(sourceScoreHolderGlobalPath);

                    String destinationScoreHolderFileSimpleName = destinationPath.getFileName().substring(0,
                                                                                                          destinationPath.getFileName().indexOf("."));
                    createScoreHolderGlobalFile(Paths.convert(destinationPackage),
                                                scoreHolderTypeFqn,
                                                destinationScoreHolderFileSimpleName);
                }
            } else {
                ioService.deleteIfExists(sourceScoreHolderGlobalPath);
            }
        }
    }

    private void createScoreHolderGlobalFile(final Path folderPath,
                                             final String scoreHolderTypeFqn,
                                             final String solutionFileName) {
        GlobalsModel globalsModel = new GlobalsModel();
        globalsModel.setGlobals(Arrays.asList(new Global(SCORE_HOLDER,
                                                         scoreHolderTypeFqn)));

        globalsEditorService.generate(folderPath,
                                      solutionFileName + SCORE_HOLDER_GLOBAL_FILE_SUFFIX,
                                      globalsModel,
                                      "Auto generate Score holder global variable based on a @PlanningSolution " + solutionFileName);
    }
}
