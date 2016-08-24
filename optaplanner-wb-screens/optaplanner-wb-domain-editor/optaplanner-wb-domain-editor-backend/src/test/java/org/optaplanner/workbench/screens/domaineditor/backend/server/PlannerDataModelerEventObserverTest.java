/*
 * Copyright 2016 Red Hat, Inc. and/or its affiliates.
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

package org.optaplanner.workbench.screens.domaineditor.backend.server;

import org.drools.workbench.screens.drltext.service.DRLTextEditorService;
import org.guvnor.common.services.project.model.Package;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kie.workbench.common.screens.datamodeller.events.DataObjectDeletedEvent;
import org.kie.workbench.common.screens.datamodeller.events.DataObjectSavedEvent;
import org.kie.workbench.common.services.datamodeller.core.Annotation;
import org.kie.workbench.common.services.datamodeller.core.DataObject;
import org.kie.workbench.common.services.shared.project.KieProjectService;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.optaplanner.core.api.score.Score;
import org.optaplanner.core.api.score.buildin.simple.SimpleScore;
import org.uberfire.backend.vfs.Path;
import org.uberfire.backend.vfs.PathFactory;
import org.uberfire.io.IOService;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PlannerDataModelerEventObserverTest {

    @Mock
    private KieProjectService kieProjectService;

    @Mock
    private IOService ioService;

    @Mock
    private DRLTextEditorService drlTextEditorService;

    private PlannerDataModelerEventObserver observer;

    public PlannerDataModelerEventObserverTest() {
    }

    @Before
    public void setUp() {
        observer = new PlannerDataModelerEventObserver( kieProjectService, ioService, drlTextEditorService );
    }

    @Test
    public void onDataObjectSavedDrlFileExists() {
        testDrlFileDeletedAndCreated( true, true );
    }

    @Test
    public void onDataObjectSavedDrlFileDoesntExist() {
        testDrlFileDeletedAndCreated( true, false );
    }

    @Test
    public void onDataObjectDeletedDrlFileExists() {
        testDrlFileDeletedAndCreated( false, true );
    }

    @Test
    public void onDataObjectDeletedDrlFileDoesntExist() {
        testDrlFileDeletedAndCreated( false, false );
    }

    private void testDrlFileDeletedAndCreated( boolean createDrlFile, boolean drlFileExists ) {
        Path resourcesPath = mock( Path.class );
        when( resourcesPath.toURI() ).thenReturn( "default://PlannerDataModelerEventObserverTest/src/main/resources/foo" );

        org.guvnor.common.services.project.model.Package resourcesPackage = mock( Package.class );
        when( resourcesPackage.getPackageMainResourcesPath() ).thenReturn( resourcesPath );
        when( kieProjectService.resolvePackage( any() ) ).thenReturn( resourcesPackage );

        when( ioService.exists( any() ) ).thenReturn( drlFileExists );

        DataObject dataObject = mock( DataObject.class );
        when( dataObject.getAnnotation( PlanningSolution.class.getName() ) ).thenReturn( mock( Annotation.class ) );
        when( dataObject.getSuperClassName() ).thenReturn( "org.optaplanner.core.impl.domain.solution.AbstractSolution<" + SimpleScore.class.getName() + ">" );

        Path dataObjectPath = mock( Path.class );
        when( dataObjectPath.toURI() ).thenReturn( "default://PlannerDataModelerEventObserverTest/src/main/foo/DataObject.java" );

        if ( createDrlFile ) {
            DataObjectSavedEvent event = (DataObjectSavedEvent) new DataObjectSavedEvent().withCurrentDataObject( dataObject ).withPath( dataObjectPath );
            observer.onDataObjectSaved( event );
        } else {
            DataObjectDeletedEvent event = (DataObjectDeletedEvent) new DataObjectDeletedEvent().withCurrentDataObject( dataObject ).withPath( dataObjectPath );
            observer.onDataObjectDeleted( event );
        }

        if ( drlFileExists ) {
            Path drlFilePath = PathFactory.newPath( "scoreHolderGlobalDefinition.drl", "default://PlannerDataModelerEventObserverTest/src/main/resources/foo/scoreHolderGlobalDefinition.drl" );

            verify( drlTextEditorService, times( 1 ) ).delete( drlFilePath, "Delete existing score holder definition." );
        } else {
            verify( drlTextEditorService, times( 0 ) ).delete( any( Path.class ), anyString() );
        }

        if ( createDrlFile ) {
            verify( drlTextEditorService, times( 1 ) ).create( resourcesPath,
                                                               "scoreHolderGlobalDefinition.drl",
                                                               getScoreHolderDrlFileContent( SimpleScore.class ),
                                                               "Create score holder definition for score type " + SimpleScore.class.getSimpleName() );
        }
    }

    private String getScoreHolderDrlFileContent( Class<? extends Score> scoreType ) {
        return String.format( "import %s;%n" +
                              "%n" +
                              "// This file was automatically generated to provide scoreHolder variable in Guided rule editor%n" +
                              "%n" +
                              "global %sHolder scoreHolder;%n",
                              scoreType.getName(), scoreType.getSimpleName() );
    }

}
