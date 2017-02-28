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
import org.kie.workbench.common.screens.datamodeller.model.GenerationResult;
import org.kie.workbench.common.screens.datamodeller.service.DataModelerService;
import org.kie.workbench.common.services.datamodeller.core.DataObject;
import org.kie.workbench.common.services.datamodeller.core.impl.AnnotationImpl;
import org.kie.workbench.common.services.datamodeller.core.impl.DataObjectImpl;
import org.kie.workbench.common.services.datamodeller.util.DriverUtils;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScoreHolder;
import org.optaplanner.workbench.screens.domaineditor.backend.server.validation.PlanningSolutionScoreHolderDeleteValidator;
import org.optaplanner.workbench.screens.domaineditor.backend.server.validation.ScoreHolderUtils;
import org.optaplanner.workbench.screens.domaineditor.validation.ScoreHolderGlobalToBeRemovedMessage;
import org.optaplanner.workbench.screens.domaineditor.validation.ScoreHolderGlobalTypeNotRecognizedMessage;
import org.uberfire.backend.server.util.Paths;
import org.uberfire.backend.vfs.Path;
import org.uberfire.backend.vfs.PathFactory;
import org.uberfire.io.IOService;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class PlanningSolutionScoreHolderDeleteValidatorTest {

    @Mock
    private DataModelerService dataModelerService;

    @Mock
    private IOService ioService;

    @Mock
    private ScoreHolderUtils scoreHolderUtils;

    private PlanningSolutionScoreHolderDeleteValidator validator;

    @Before
    public void setUp() {
        validator = new PlanningSolutionScoreHolderDeleteValidator( dataModelerService,
                                                                    ioService,
                                                                    scoreHolderUtils );
    }

    @Test
    public void planningSolution() {
        Path dataObjectPath = PathFactory.newPath( "Test.java",
                                                   "file:///dataObjects" );
        when( ioService.readAllString( Paths.convert( dataObjectPath ) ) ).thenReturn( "testResult" );

        DataObject originalDataObject = new DataObjectImpl( "test",
                                                            "Test" );
        originalDataObject.addAnnotation( new AnnotationImpl( DriverUtils.buildAnnotationDefinition( PlanningSolution.class ) ) );

        GenerationResult generationResult = new GenerationResult();
        generationResult.setDataObject( originalDataObject );
        when( dataModelerService.loadDataObject( any(),
                                                 anyString(),
                                                 any() ) ).thenReturn( generationResult );

        when( scoreHolderUtils.extractScoreTypeFqn( originalDataObject,
                                                    dataObjectPath ) ).thenReturn( HardSoftScore.class.getName() );
        when( scoreHolderUtils.getScoreHolderTypeFqn( HardSoftScore.class.getName() ) ).thenReturn( HardSoftScoreHolder.class.getName() );

        when( dataModelerService.findClassUsages( dataObjectPath,
                                                  HardSoftScoreHolder.class.getName() ) ).thenReturn( Arrays.asList( mock( Path.class ) ) );

        DataObject updatedDataObject = new DataObjectImpl( "test",
                                                           "Test" );
        updatedDataObject.addAnnotation( new AnnotationImpl( DriverUtils.buildAnnotationDefinition( PlanningSolution.class ) ) );

        Collection<ValidationMessage> result = validator.validate( dataObjectPath,
                                                                   updatedDataObject );
        assertEquals( 1,
                      result.size() );

        ValidationMessage message = result.iterator().next();
        assertTrue( message instanceof ScoreHolderGlobalToBeRemovedMessage );
    }

    @Test
    public void notAPlanningSolution() {
        Path dataObjectPath = PathFactory.newPath( "Test.java",
                                                   "file:///dataObjects" );
        when( ioService.readAllString( Paths.convert( dataObjectPath ) ) ).thenReturn( "testResult" );

        DataObject originalDataObject = new DataObjectImpl( "test",
                                                            "Test" );

        GenerationResult generationResult = new GenerationResult();
        generationResult.setDataObject( originalDataObject );
        when( dataModelerService.loadDataObject( any(),
                                                 anyString(),
                                                 any() ) ).thenReturn( generationResult );

        DataObject updatedDataObject = new DataObjectImpl( "test",
                                                           "Test" );

        Collection<ValidationMessage> result = validator.validate( dataObjectPath,
                                                                   updatedDataObject );
        assertTrue( result.isEmpty() );
    }

    @Test
    public void scoreHolderTypeNotRecognized() {
        Path dataObjectPath = PathFactory.newPath( "Test.java",
                                                   "file:///dataObjects" );
        when( ioService.readAllString( Paths.convert( dataObjectPath ) ) ).thenReturn( "testResult" );

        DataObject originalDataObject = new DataObjectImpl( "test",
                                                            "Test" );
        originalDataObject.addAnnotation( new AnnotationImpl( DriverUtils.buildAnnotationDefinition( PlanningSolution.class ) ) );

        GenerationResult generationResult = new GenerationResult();
        generationResult.setDataObject( originalDataObject );
        when( dataModelerService.loadDataObject( any(),
                                                 anyString(),
                                                 any() ) ).thenReturn( generationResult );

        when( scoreHolderUtils.extractScoreTypeFqn( originalDataObject,
                                                    dataObjectPath ) ).thenReturn( "UnknownScoreClassName" );
        when( scoreHolderUtils.getScoreHolderTypeFqn( "UnknownScoreClassName" ) ).thenReturn( null );

        DataObject updatedDataObject = new DataObjectImpl( "test",
                                                           "Test" );

        Collection<ValidationMessage> result = validator.validate( dataObjectPath,
                                                                   updatedDataObject );
        assertEquals( 1,
                      result.size() );

        ValidationMessage message = result.iterator().next();
        assertTrue( message instanceof ScoreHolderGlobalTypeNotRecognizedMessage );
    }
}
