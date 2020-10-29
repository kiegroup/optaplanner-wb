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

package org.optaplanner.workbench.screens.domaineditor.backend.server.validation;

import java.util.Arrays;
import java.util.Collection;

import org.guvnor.common.services.shared.validation.model.ValidationMessage;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kie.workbench.common.screens.datamodeller.model.GenerationResult;
import org.kie.workbench.common.screens.datamodeller.service.DataModelerService;
import org.kie.workbench.common.services.datamodeller.core.DataObject;
import org.kie.workbench.common.services.datamodeller.core.impl.AnnotationImpl;
import org.kie.workbench.common.services.datamodeller.core.impl.DataObjectImpl;
import org.kie.workbench.common.services.datamodeller.util.DriverUtils;
import org.kie.workbench.common.services.refactoring.service.AssetsUsageService;
import org.kie.workbench.common.services.refactoring.service.ResourceType;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.optaplanner.core.api.score.Score;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScoreHolder;
import org.optaplanner.core.api.score.buildin.simple.SimpleScore;
import org.optaplanner.core.api.score.buildin.simple.SimpleScoreHolder;
import org.optaplanner.workbench.screens.domaineditor.validation.ScoreHolderGlobalTypeToBeChangedMessage;
import org.uberfire.backend.server.util.Paths;
import org.uberfire.backend.vfs.Path;
import org.uberfire.backend.vfs.PathFactory;
import org.uberfire.io.IOService;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PlanningSolutionScoreHolderSaveValidatorTest {

    @Mock
    private DataModelerService dataModelerService;

    @Mock
    private AssetsUsageService assetsUsageService;

    @Mock
    private IOService ioService;

    @Mock
    private ScoreHolderUtils scoreHolderUtils;

    private PlanningSolutionScoreHolderSaveValidator validator;

    @Before
    public void setUp() {
        validator = new PlanningSolutionScoreHolderSaveValidator(dataModelerService,
                                                                 ioService,
                                                                 scoreHolderUtils,
                                                                 assetsUsageService);
    }

    @Test
    public void scoreTypeChanged() {
        Path dataObjectPath = PathFactory.newPath("Test.java",
                                                  "file:///dataObjects");
        when(ioService.readAllString(Paths.convert(dataObjectPath))).thenReturn("testResult");

        DataObject originalDataObject = createDataObject(HardSoftScore.class);
        GenerationResult generationResult = new GenerationResult();
        generationResult.setDataObject(originalDataObject);
        when(dataModelerService.loadDataObject(any(),
                                               any(),
                                               any())).thenReturn(generationResult);
        when(scoreHolderUtils.extractScoreTypeFqn(originalDataObject)).thenReturn(HardSoftScore.class.getName());
        when(scoreHolderUtils.getScoreHolderTypeFqn(HardSoftScore.class.getName())).thenReturn(HardSoftScoreHolder.class.getName());

        when(assetsUsageService.getAssetUsages(HardSoftScoreHolder.class.getName(),
                                               ResourceType.JAVA, dataObjectPath)).thenReturn(Arrays.asList(mock(Path.class)));

        DataObject updatedDataObject = createDataObject(SimpleScore.class);
        when(scoreHolderUtils.extractScoreTypeFqn(updatedDataObject)).thenReturn(SimpleScore.class.getName());
        when(scoreHolderUtils.getScoreHolderTypeFqn(SimpleScore.class.getName())).thenReturn(SimpleScoreHolder.class.getName());

        Collection<ValidationMessage> result = validator.validate(dataObjectPath,
                                                                  updatedDataObject);
        assertEquals(1,
                     result.size());

        ValidationMessage message = result.iterator().next();
        assertTrue(message instanceof ScoreHolderGlobalTypeToBeChangedMessage);
    }

    @Test
    public void dataObjectToAPlanningSolution() {
        Path dataObjectPath = PathFactory.newPath("Test.java",
                                                  "file:///dataObjects");
        when(ioService.readAllString(Paths.convert(dataObjectPath))).thenReturn("testResult");

        DataObject originalDataObject = createDataObject(null);
        GenerationResult generationResult = new GenerationResult();
        generationResult.setDataObject(originalDataObject);
        when(dataModelerService.loadDataObject(any(),
                                               anyString(),
                                               any())).thenReturn(generationResult);

        DataObject updatedDataObject = createDataObject(SimpleScore.class);

        Collection<ValidationMessage> result = validator.validate(dataObjectPath,
                                                                  updatedDataObject);
        assertTrue(result.isEmpty());
    }

    @Test
    public void dataObjectToADataObject() {
        Path dataObjectPath = PathFactory.newPath("Test.java",
                                                  "file:///dataObjects");
        when(ioService.readAllString(Paths.convert(dataObjectPath))).thenReturn("testResult");

        DataObject originalDataObject = createDataObject(null);
        GenerationResult generationResult = new GenerationResult();
        generationResult.setDataObject(originalDataObject);
        when(dataModelerService.loadDataObject(any(),
                                               anyString(),
                                               any())).thenReturn(generationResult);

        DataObject updatedDataObject = createDataObject(null);

        Collection<ValidationMessage> result = validator.validate(dataObjectPath,
                                                                  updatedDataObject);
        assertTrue(result.isEmpty());
    }

    @Test
    public void dataObjectNull() {
        Path dataObjectPath = PathFactory.newPath("Test.java",
                                                  "file:///dataObjects");
        when(ioService.readAllString(Paths.convert(dataObjectPath))).thenReturn("testResult");

        GenerationResult generationResult = new GenerationResult();
        generationResult.setDataObject(null);
        when(dataModelerService.loadDataObject(any(),
                                               anyString(),
                                               any())).thenReturn(generationResult);

        DataObject updatedDataObject = createDataObject(null);

        Collection<ValidationMessage> result = validator.validate(dataObjectPath,
                                                                  updatedDataObject);
        assertTrue(result.isEmpty());
    }

    @Test
    public void scoreHolderTypeNotRecognized() {
        DataObject originalDataObject = createDataObject(HardSoftScore.class);
        GenerationResult generationResult = new GenerationResult();
        generationResult.setDataObject(originalDataObject);
    }

    private DataObject createDataObject(Class<? extends Score> scoreClass) {
        DataObject dataObject = new DataObjectImpl("test",
                                                   "Test");
        if (scoreClass != null) {
            dataObject.addAnnotation(new AnnotationImpl(DriverUtils.buildAnnotationDefinition(PlanningSolution.class)));
            dataObject.addProperty("score",
                                   scoreClass.getName());
        }
        return dataObject;
    }
}
