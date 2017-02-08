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

package org.optaplanner.workbench.screens.domaineditor.backend;

import java.util.Arrays;
import java.util.Collection;

import org.guvnor.common.services.shared.validation.model.ValidationMessage;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kie.workbench.common.screens.datamodeller.service.DataModelerService;
import org.kie.workbench.common.services.datamodeller.core.DataObject;
import org.kie.workbench.common.services.datamodeller.core.impl.AnnotationImpl;
import org.kie.workbench.common.services.datamodeller.core.impl.DataObjectImpl;
import org.kie.workbench.common.services.datamodeller.util.DriverUtils;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.optaplanner.workbench.screens.domaineditor.backend.server.validation.PlanningSolutionSaveValidator;
import org.optaplanner.workbench.screens.domaineditor.validation.PlanningSolutionToBeDuplicatedMessage;
import org.uberfire.backend.vfs.Path;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class PlanningSolutionSaveValidatorTest {

    @Mock
    private DataModelerService dataModelerService;

    private PlanningSolutionSaveValidator saveValidator;

    @Before
    public void setUp() {
        saveValidator = new PlanningSolutionSaveValidator( dataModelerService );
    }

    @Test
    public void checkNotAPlanningSolution() {
        DataObject dataObject = new DataObjectImpl( "test",
                                                    "Test" );

        Collection<ValidationMessage> result = saveValidator.validate( mock( Path.class ),
                                                                       dataObject );
        assertTrue( result.isEmpty() );
    }

    @Test
    public void checkPlanningSolutionNoOtherExists() {
        DataObject dataObject = new DataObjectImpl( "test",
                                                    "Test" );
        dataObject.addAnnotation( new AnnotationImpl( DriverUtils.buildAnnotationDefinition( PlanningSolution.class ) ) );

        Path dataObjectPath = mock( Path.class );
        when( dataModelerService.findClassUsages( dataObjectPath,
                                                  PlanningSolution.class.getName() ) ).thenReturn( Arrays.asList( dataObjectPath ) );

        Collection<ValidationMessage> result = saveValidator.validate( dataObjectPath,
                                                                       dataObject );
        assertTrue( result.isEmpty() );
    }

    @Test
    public void checkPlanningSolutionOtherExists() {
        DataObject dataObject = new DataObjectImpl( "test",
                                                    "Test" );
        dataObject.addAnnotation( new AnnotationImpl( DriverUtils.buildAnnotationDefinition( PlanningSolution.class ) ) );

        Path dataObjectPath = mock( Path.class );
        Path otherDataObjectPath = mock( Path.class );
        when( dataModelerService.findClassUsages( dataObjectPath,
                                                  PlanningSolution.class.getName() ) ).thenReturn( Arrays.asList( otherDataObjectPath ) );

        Collection<ValidationMessage> result = saveValidator.validate( dataObjectPath,
                                                                       dataObject );
        assertEquals( 1, result.size() );

        ValidationMessage message = result.iterator().next();
        assertTrue( message instanceof PlanningSolutionToBeDuplicatedMessage );
    }
}
