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

package org.optaplanner.workbench.screens.guidedrule.backend.server.service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.inject.Named;

import org.drools.workbench.screens.globals.model.Global;
import org.drools.workbench.screens.globals.model.GlobalsModel;
import org.drools.workbench.screens.globals.service.GlobalsEditorService;
import org.kie.workbench.common.screens.datamodeller.model.GenerationResult;
import org.kie.workbench.common.screens.datamodeller.service.DataModelerService;
import org.kie.workbench.common.screens.javaeditor.type.JavaResourceTypeDefinition;
import org.kie.workbench.common.services.datamodeller.core.Annotation;
import org.kie.workbench.common.services.datamodeller.core.DataObject;
import org.kie.workbench.common.services.datamodeller.core.ObjectProperty;
import org.kie.workbench.common.services.refactoring.service.AssetsUsageService;
import org.kie.workbench.common.services.refactoring.service.ResourceType;
import org.kie.workbench.common.services.shared.project.KieModuleService;
import org.optaplanner.core.api.domain.solution.PlanningScore;
import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.optaplanner.core.api.score.buildin.bendable.BendableScore;
import org.optaplanner.core.api.score.buildin.bendablebigdecimal.BendableBigDecimalScore;
import org.optaplanner.core.api.score.buildin.bendablelong.BendableLongScore;
import org.optaplanner.workbench.screens.guidedrule.model.BendableScoreLevelsWrapper;
import org.optaplanner.workbench.screens.guidedrule.model.ScoreInformation;
import org.optaplanner.workbench.screens.guidedrule.service.ScoreHolderService;
import org.uberfire.backend.server.util.Paths;
import org.uberfire.backend.vfs.Path;
import org.uberfire.io.IOService;
import org.uberfire.java.nio.file.Files;

public class ScoreHolderServiceImpl implements ScoreHolderService {

    private static final String SCORE_HOLDER_GLOBAL_FILE_SUFFIX = "ScoreHolderGlobal.gdrl";

    private KieModuleService kieModuleService;

    private IOService ioService;

    private GlobalsEditorService globalsEditorService;

    private DataModelerService dataModelerService;

    private AssetsUsageService assetsUsageService;

    private JavaResourceTypeDefinition javaResourceTypeDefinition;

    public ScoreHolderServiceImpl() {
    }

    @Inject
    public ScoreHolderServiceImpl(final KieModuleService kieModuleService,
                                  @Named("ioStrategy") final IOService ioService,
                                  final GlobalsEditorService globalsEditorService,
                                  final DataModelerService dataModelerService,
                                  final JavaResourceTypeDefinition javaResourceTypeDefinition,
                                  final AssetsUsageService assetsUsageService) {
        this.kieModuleService = kieModuleService;
        this.ioService = ioService;
        this.globalsEditorService = globalsEditorService;
        this.dataModelerService = dataModelerService;
        this.javaResourceTypeDefinition = javaResourceTypeDefinition;
        this.assetsUsageService = assetsUsageService;
    }

    @Override
    public ScoreInformation getProjectScoreInformation(final Path projectPath) {
        List<Path> classUsages = assetsUsageService.getAssetUsages(PlanningSolution.class.getName(),
                                                                   ResourceType.JAVA,
                                                                   projectPath);

        return new ScoreInformation(extractProjectScoreTypeFqns(classUsages),
                                    getBendableScoreLevelsSize(classUsages));
    }

    private Collection<String> extractProjectScoreTypeFqns(final List<Path> classUsages) {
        return classUsages.stream().filter(this.javaResourceTypeDefinition::accept).flatMap(p -> extractSolutionScoreTypeFqns(p).stream()).collect(Collectors.toList());
    }

    private Collection<BendableScoreLevelsWrapper> getBendableScoreLevelsSize(final List<Path> classUsages) {
        return classUsages.stream().filter(this.javaResourceTypeDefinition::accept).map(p -> extractSolutionBendableScoreLevelsSize(p)).collect(Collectors.toList());
    }

    private Collection<String> extractSolutionScoreTypeFqns(final Path solutionObjectPath) {
        org.uberfire.java.nio.file.Path source = Paths.convert(kieModuleService.resolvePackage(solutionObjectPath).getPackageMainResourcesPath());
        org.uberfire.java.nio.file.Path sourcePackage = Files.isDirectory(source) ? source : source.getParent();

        String sourceSolutionFileName = solutionObjectPath.getFileName().substring(0,
                                                                                   solutionObjectPath.getFileName().indexOf("."));
        org.uberfire.java.nio.file.Path sourceScoreHolderGlobalPath = sourcePackage.resolve(sourceSolutionFileName + SCORE_HOLDER_GLOBAL_FILE_SUFFIX);

        boolean scoreHolderGlobalFileExists = ioService.exists(sourceScoreHolderGlobalPath);

        if (scoreHolderGlobalFileExists) {
            GlobalsModel globalsModel = globalsEditorService.load(Paths.convert(sourceScoreHolderGlobalPath));
            return globalsModel.getGlobals().stream()
                    .filter(global -> "scoreHolder".equals(global.getAlias()))
                    .map(Global::getClassName)
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    public BendableScoreLevelsWrapper extractSolutionBendableScoreLevelsSize(final Path solutionPath) {
        String solutionString = ioService.readAllString(Paths.convert(solutionPath));

        GenerationResult generationResult = dataModelerService.loadDataObject(solutionPath,
                                                                              solutionString,
                                                                              solutionPath);
        if (!generationResult.hasErrors()) {
            DataObject dataObject = generationResult.getDataObject();

            ObjectProperty scoreObjectProperty = dataObject.getProperty("score");
            if (scoreObjectProperty != null) {
                if (isBendableScore(scoreObjectProperty.getClassName())) {
                    Annotation annotation = scoreObjectProperty.getAnnotation(PlanningScore.class.getName());
                    if (annotation != null) {
                        Object hardLevelsSize = annotation.getValue("bendableHardLevelsSize");
                        Object softLevelsSize = annotation.getValue("bendableSoftLevelsSize");
                        return new BendableScoreLevelsWrapper(hardLevelsSize == null ? 0 : (int) hardLevelsSize,
                                                              softLevelsSize == null ? 0 : (int) softLevelsSize);
                    }
                }
            }
        }
        return new BendableScoreLevelsWrapper();
    }

    private boolean isBendableScore(final String planningSolutionScoreType) {
        return BendableScore.class.getName().equals(planningSolutionScoreType)
                || BendableLongScore.class.getName().equals(planningSolutionScoreType)
                || BendableBigDecimalScore.class.getName().equals(planningSolutionScoreType);
    }
}
