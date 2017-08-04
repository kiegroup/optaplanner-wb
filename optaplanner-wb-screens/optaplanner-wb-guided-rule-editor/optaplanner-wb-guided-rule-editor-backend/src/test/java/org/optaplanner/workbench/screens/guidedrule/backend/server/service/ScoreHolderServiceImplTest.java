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

import java.util.Arrays;
import java.util.Collection;

import org.drools.workbench.screens.globals.model.Global;
import org.drools.workbench.screens.globals.model.GlobalsModel;
import org.drools.workbench.screens.globals.service.GlobalsEditorService;
import org.guvnor.common.services.project.model.Package;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kie.workbench.common.screens.datamodeller.model.GenerationResult;
import org.kie.workbench.common.screens.datamodeller.service.DataModelerService;
import org.kie.workbench.common.screens.javaeditor.type.JavaResourceTypeDefinition;
import org.kie.workbench.common.services.datamodeller.core.DataObject;
import org.kie.workbench.common.services.datamodeller.core.ObjectProperty;
import org.kie.workbench.common.services.datamodeller.core.impl.AnnotationImpl;
import org.kie.workbench.common.services.datamodeller.core.impl.DataObjectImpl;
import org.kie.workbench.common.services.datamodeller.core.impl.ObjectPropertyImpl;
import org.kie.workbench.common.services.datamodeller.util.DriverUtils;
import org.kie.workbench.common.services.refactoring.service.AssetsUsageService;
import org.kie.workbench.common.services.refactoring.service.ResourceType;
import org.kie.workbench.common.services.shared.project.KieModuleService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.optaplanner.core.api.domain.solution.PlanningScore;
import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.optaplanner.core.api.score.buildin.bendable.BendableScore;
import org.optaplanner.workbench.screens.guidedrule.model.BendableScoreLevelsWrapper;
import org.optaplanner.workbench.screens.guidedrule.model.ScoreInformation;
import org.uberfire.backend.server.util.Paths;
import org.uberfire.backend.vfs.Path;
import org.uberfire.backend.vfs.PathFactory;
import org.uberfire.io.IOService;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ScoreHolderServiceImplTest {

    @Mock
    private KieModuleService kieModuleService;

    @Mock
    private IOService ioService;

    @Mock
    private GlobalsEditorService globalsEditorService;

    @Mock
    private DataModelerService dataModelerService;

    @Mock
    private JavaResourceTypeDefinition javaResourceTypeDefinition;

    @Mock
    private AssetsUsageService assetsUsageService;

    @InjectMocks
    private ScoreHolderServiceImpl scoreHolderService;

    @Test
    public void getProjectScoreInformation() {
        Path dataObjectPath = PathFactory.newPath("Test.java",
                                                  "file:///dataObjects");
        when(assetsUsageService.getAssetUsages(PlanningSolution.class.getName(),
                                               ResourceType.JAVA,
                                               dataObjectPath)).thenReturn(Arrays.asList(dataObjectPath));
        when(javaResourceTypeDefinition.accept(dataObjectPath)).thenReturn(true);

        // scoreHolder definitions
        Package resourcesPackage = mock(Package.class);
        when(resourcesPackage.getPackageMainResourcesPath()).thenReturn(PathFactory.newPath("Test.java",
                                                                                            "file:///dataObjects"));
        when(kieModuleService.resolvePackage(dataObjectPath)).thenReturn(resourcesPackage);
        when(ioService.exists(any(org.uberfire.java.nio.file.Path.class))).thenReturn(true);

        GlobalsModel globalsModel = new GlobalsModel();
        globalsModel.setGlobals(Arrays.asList(new Global("scoreHolder",
                                                         BendableScore.class.getName())));
        when(globalsEditorService.load(any(Path.class))).thenReturn(globalsModel);

        // Bendable score information
        when(ioService.readAllString(Paths.convert(dataObjectPath))).thenReturn("testContent");
        GenerationResult generationResult = new GenerationResult();
        generationResult.setDataObject(createPlanningSolution());
        when(dataModelerService.loadDataObject(dataObjectPath,
                                               "testContent",
                                               dataObjectPath)).thenReturn(generationResult);

        ScoreInformation scoreInformation = scoreHolderService.getProjectScoreInformation(dataObjectPath);

        assertNotNull(scoreInformation);
        Collection<String> scoreHolderFqnTypeFqns = scoreInformation.getScoreHolderFqnTypeFqns();
        assertNotNull(scoreHolderFqnTypeFqns);
        assertEquals(1,
                     scoreHolderFqnTypeFqns.size());
        assertEquals(BendableScore.class.getName(),
                     scoreHolderFqnTypeFqns.iterator().next());

        Collection<BendableScoreLevelsWrapper> bendableScoreLevelsWrappers = scoreInformation.getBendableScoreLevelsWrappers();
        assertNotNull(bendableScoreLevelsWrappers);
        assertEquals(1,
                     bendableScoreLevelsWrappers.size());
        BendableScoreLevelsWrapper bendableScoreLevelsWrapper = bendableScoreLevelsWrappers.iterator().next();
        assertEquals(1,
                     bendableScoreLevelsWrapper.getHardScoreLevels());
        assertEquals(2,
                     bendableScoreLevelsWrapper.getSoftScoreLevels());
    }

    private DataObject createPlanningSolution() {
        DataObject dataObject = new DataObjectImpl("test",
                                                   "TestSolution");

        AnnotationImpl planningSolutionAnnotation = new AnnotationImpl(DriverUtils.buildAnnotationDefinition(PlanningSolution.class));
        dataObject.addAnnotation(planningSolutionAnnotation);

        ObjectProperty scoreObjectProperty = new ObjectPropertyImpl("score",
                                                                    BendableScore.class.getName(),
                                                                    false);
        dataObject.addProperty(scoreObjectProperty);

        AnnotationImpl planningScoreAnnotation = new AnnotationImpl(DriverUtils.buildAnnotationDefinition(PlanningScore.class));
        planningScoreAnnotation.setValue("bendableHardLevelsSize",
                                         1);
        planningScoreAnnotation.setValue("bendableSoftLevelsSize",
                                         2);
        scoreObjectProperty.addAnnotation(planningScoreAnnotation);

        return dataObject;
    }
}
