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
import java.util.Collections;

import org.guvnor.common.services.shared.validation.model.ValidationMessage;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kie.workbench.common.services.datamodeller.core.DataObject;
import org.kie.workbench.common.services.datamodeller.core.impl.AnnotationImpl;
import org.kie.workbench.common.services.datamodeller.core.impl.DataObjectImpl;
import org.kie.workbench.common.services.datamodeller.util.DriverUtils;
import org.kie.workbench.common.services.refactoring.service.AssetsUsageService;
import org.kie.workbench.common.services.refactoring.service.ResourceType;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.optaplanner.workbench.screens.domaineditor.validation.PlanningSolutionToBeDuplicatedMessage;
import org.uberfire.backend.vfs.Path;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PlanningSolutionSaveValidatorTest {

    @Mock
    private AssetsUsageService assetsUsageService;

    private PlanningSolutionSaveValidator saveValidator;

    @Before
    public void setUp() {
        saveValidator = new PlanningSolutionSaveValidator(assetsUsageService);
    }

    @Test
    public void accept() {
        Path dataObjectPath = mock(Path.class);
        when(dataObjectPath.getFileName()).thenReturn("Test.java");
        assertTrue(saveValidator.accept(dataObjectPath));
        Path propertiesPath = mock(Path.class);
        when(propertiesPath.getFileName()).thenReturn("Test.properties");
        assertFalse(saveValidator.accept(propertiesPath));
    }

    @Test
    public void checkDataObjectIsNull() {
        Collection<ValidationMessage> result = saveValidator.validate(mock(Path.class),
                                                                      null);
        assertEquals(0, result.size());
    }

    @Test
    public void checkNotAPlanningSolution() {
        DataObject dataObject = new DataObjectImpl("test",
                                                   "Test");

        Collection<ValidationMessage> result = saveValidator.validate(mock(Path.class),
                                                                      dataObject);
        assertTrue(result.isEmpty());
        verify(assetsUsageService, never()).getAssetUsages(any(), any(), any());
    }

    @Test
    public void checkPlanningSolutionNoOtherExists() {
        DataObject dataObject = new DataObjectImpl("test",
                                                   "Test");
        dataObject.addAnnotation(new AnnotationImpl(DriverUtils.buildAnnotationDefinition(PlanningSolution.class)));

        Path dataObjectPath = mock(Path.class);
        when(assetsUsageService.getAssetUsages(PlanningSolution.class.getName(),
                                               ResourceType.JAVA,
                                               dataObjectPath)).thenReturn(Arrays.asList(dataObjectPath));

        Collection<ValidationMessage> result = saveValidator.validate(dataObjectPath,
                                                                      dataObject);
        assertTrue(result.isEmpty());
    }

    @Test
    public void checkPlanningSolutionNoOtherExistsFirstSave() {
        DataObject dataObject = new DataObjectImpl("test",
                                                   "Test");
        dataObject.addAnnotation(new AnnotationImpl(DriverUtils.buildAnnotationDefinition(PlanningSolution.class)));

        Path dataObjectPath = mock(Path.class);
        when(assetsUsageService.getAssetUsages(PlanningSolution.class.getName(),
                                               ResourceType.JAVA,
                                               dataObjectPath)).thenReturn(Collections.emptyList());

        Collection<ValidationMessage> result = saveValidator.validate(dataObjectPath,
                                                                      dataObject);
        assertTrue(result.isEmpty());
    }

    @Test
    public void checkPlanningSolutionOtherExists() {
        DataObject dataObject = new DataObjectImpl("test",
                                                   "Test");
        dataObject.addAnnotation(new AnnotationImpl(DriverUtils.buildAnnotationDefinition(PlanningSolution.class)));

        Path dataObjectPath = mock(Path.class);
        Path otherDataObjectPath = mock(Path.class);
        when(assetsUsageService.getAssetUsages(PlanningSolution.class.getName(),
                                               ResourceType.JAVA,
                                               dataObjectPath)).thenReturn(Arrays.asList(otherDataObjectPath));

        Collection<ValidationMessage> result = saveValidator.validate(dataObjectPath,
                                                                      dataObject);
        assertEquals(1,
                     result.size());

        ValidationMessage message = result.iterator().next();
        assertTrue(message instanceof PlanningSolutionToBeDuplicatedMessage);
    }
}
