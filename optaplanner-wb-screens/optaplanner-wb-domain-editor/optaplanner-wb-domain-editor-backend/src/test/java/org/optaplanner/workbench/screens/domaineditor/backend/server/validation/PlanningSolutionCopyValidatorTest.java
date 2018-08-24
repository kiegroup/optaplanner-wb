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

import java.util.Collection;
import java.util.Collections;

import org.guvnor.common.services.shared.validation.model.ValidationMessage;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kie.workbench.common.screens.datamodeller.model.DataModelerError;
import org.kie.workbench.common.screens.datamodeller.model.GenerationResult;
import org.kie.workbench.common.screens.datamodeller.service.DataModelerService;
import org.kie.workbench.common.services.datamodeller.core.DataObject;
import org.kie.workbench.common.services.datamodeller.core.impl.AnnotationImpl;
import org.kie.workbench.common.services.datamodeller.core.impl.DataObjectImpl;
import org.kie.workbench.common.services.datamodeller.util.DriverUtils;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.optaplanner.workbench.screens.domaineditor.validation.PlanningSolutionToBeDuplicatedMessage;
import org.uberfire.backend.server.util.Paths;
import org.uberfire.backend.vfs.Path;
import org.uberfire.io.IOService;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PlanningSolutionCopyValidatorTest {

    @Mock
    private DataModelerService dataModelerService;

    @Mock
    private IOService ioService;

    @Mock
    private Path dataObjectPath;

    private PlanningSolutionCopyValidator copyValidator;

    @Before
    public void setUp() {
        this.copyValidator = new PlanningSolutionCopyValidator(dataModelerService, ioService);
        when(dataObjectPath.toURI()).thenReturn("file:///project/Test.java");
        when(dataObjectPath.getFileName()).thenReturn("Test.java");
        when(ioService.readAllString(any(org.uberfire.java.nio.file.Path.class))).thenReturn("");
    }

    @Test
    public void accept() {
        assertTrue(copyValidator.accept(dataObjectPath));
        Path propertiesPath = mock(Path.class);
        when(propertiesPath.getFileName()).thenReturn("Test.properties");
        assertFalse(copyValidator.accept(propertiesPath));
    }

    @Test
    public void checkDataObjectIsNotAPlanningSolution() {
        DataObject dataObject = new DataObjectImpl("test",
                                                   "Test");

        Collection<ValidationMessage> result = copyValidator.validate(dataObjectPath,
                                                                      dataObject);
        assertTrue(result.isEmpty());
    }

    @Test
    public void checkDataObjectIsAPlanningSolution() {
        DataObject dataObject = new DataObjectImpl("test",
                                                   "Test");
        dataObject.addAnnotation(new AnnotationImpl(DriverUtils.buildAnnotationDefinition(PlanningSolution.class)));

        Collection<ValidationMessage> result = copyValidator.validate(dataObjectPath,
                                                                      dataObject);
        assertEquals(1,
                     result.size());

        ValidationMessage message = result.iterator().next();
        assertTrue(message instanceof PlanningSolutionToBeDuplicatedMessage);
    }

    @Test
    public void checkDataObjectIsNull() {
        Collection<ValidationMessage> result = copyValidator.validate(dataObjectPath,
                                                                      null);
        assertEquals(0, result.size());
    }

    @Test
    public void pathValidation_dataObjectPathIsPassedToIoService() {
        when(dataModelerService.loadDataObject(any(), anyString(), any())).thenReturn(new GenerationResult());

        copyValidator.validate(dataObjectPath);

        verify(ioService).readAllString(Paths.convert(dataObjectPath));
    }

    @Test
    public void pathValidation_pathIsNull() {
        Collection<ValidationMessage> result = copyValidator.validate(null);
        assertEquals(0, result.size());
    }

    @Test
    public void pathValidation_noMessagesWhenGenerationFails() {
        GenerationResult generationResult = new GenerationResult();
        generationResult.setErrors(Collections.singletonList(new DataModelerError()));

        DataObject dataObject = new DataObjectImpl("test", "Test");
        dataObject.addAnnotation(new AnnotationImpl(DriverUtils.buildAnnotationDefinition(PlanningSolution.class)));
        generationResult.setDataObject(dataObject);

        when(dataModelerService.loadDataObject(any(),
                                               anyString(),
                                               any())).thenReturn(generationResult);

        Collection<ValidationMessage> result = copyValidator.validate(dataObjectPath);
        assertEquals(0, result.size());
    }

    @Test
    public void pathValidation_dataObjectIsNull() {
        GenerationResult generationResult = new GenerationResult();
        generationResult.setDataObject(null);
        when(dataModelerService.loadDataObject(any(),
                                               anyString(),
                                               any())).thenReturn(generationResult);

        Collection<ValidationMessage> result = copyValidator.validate(dataObjectPath);
        assertEquals(0, result.size());
    }

    @Test
    public void pathValidation_dataObjectIsNotPlanningSolution() {
        DataObject dataObject = new DataObjectImpl("test", "Test");

        GenerationResult generationResult = new GenerationResult();
        generationResult.setDataObject(dataObject);
        when(dataModelerService.loadDataObject(any(),
                                               anyString(),
                                               any())).thenReturn(generationResult);

        Collection<ValidationMessage> result = copyValidator.validate(dataObjectPath);
        assertEquals(0, result.size());
    }

    @Test
    public void pathValidation_dataObjectIsPlanningSolution() {
        DataObject dataObject = new DataObjectImpl("test", "Test");
        dataObject.addAnnotation(new AnnotationImpl(DriverUtils.buildAnnotationDefinition(PlanningSolution.class)));

        GenerationResult generationResult = new GenerationResult();
        generationResult.setDataObject(dataObject);
        when(dataModelerService.loadDataObject(any(),
                                               anyString(),
                                               any())).thenReturn(generationResult);

        Collection<ValidationMessage> result = copyValidator.validate(dataObjectPath);
        assertEquals(1, result.size());
        assertTrue(result.iterator().next() instanceof PlanningSolutionToBeDuplicatedMessage);
    }
}
