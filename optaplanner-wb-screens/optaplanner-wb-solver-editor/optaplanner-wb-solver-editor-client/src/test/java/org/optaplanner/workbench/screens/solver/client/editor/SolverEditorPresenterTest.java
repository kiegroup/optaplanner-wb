/*
 * Copyright 2015 Red Hat, Inc. and/or its affiliates.
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
package org.optaplanner.workbench.screens.solver.client.editor;

import java.lang.annotation.Annotation;
import java.util.List;
import javax.enterprise.event.Event;

import com.google.gwtmockito.GwtMock;
import com.google.gwtmockito.GwtMockitoTestRunner;
import org.guvnor.common.services.shared.metadata.model.Metadata;
import org.guvnor.common.services.shared.metadata.model.Overview;
import org.guvnor.common.services.shared.validation.model.ValidationMessage;
import org.jboss.errai.common.client.api.Caller;
import org.jboss.errai.common.client.api.ErrorCallback;
import org.jboss.errai.common.client.api.RemoteCallback;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kie.workbench.common.widgets.metadata.client.KieEditorWrapperView;
import org.kie.workbench.common.widgets.metadata.client.widget.OverviewWidgetPresenter;
import org.mockito.Mock;
import org.optaplanner.workbench.screens.solver.client.resources.SolverEditorResources;
import org.optaplanner.workbench.screens.solver.client.resources.images.SolverEditorImageResources;
import org.optaplanner.workbench.screens.solver.client.type.SolverResourceType;
import org.optaplanner.workbench.screens.solver.model.ScoreDirectorFactoryConfigModel;
import org.optaplanner.workbench.screens.solver.model.SolverConfigModel;
import org.optaplanner.workbench.screens.solver.model.SolverModelContent;
import org.optaplanner.workbench.screens.solver.model.TerminationConfigModel;
import org.optaplanner.workbench.screens.solver.service.SolverEditorService;
import org.uberfire.backend.vfs.ObservablePath;
import org.uberfire.backend.vfs.Path;
import org.uberfire.ext.editor.commons.client.history.VersionRecordManager;
import org.uberfire.mvp.PlaceRequest;
import org.uberfire.workbench.events.NotificationEvent;

import static org.mockito.Mockito.*;

@RunWith(GwtMockitoTestRunner.class)
public class SolverEditorPresenterTest {

    @GwtMock
    SolverEditorImageResources solverEditorImageResources;

    @GwtMock
    SolverEditorResources solverEditorResources;

    @Mock
    private SolverEditorView view;

    private SolverEditorPresenter presenter;
    private TerminationConfigModel terminationConfigModel = new TerminationConfigModel();
    private ScoreDirectorFactoryConfigModel scoreDirectorFactoryConfig = new ScoreDirectorFactoryConfigModel();

    @Mock
    KieEditorWrapperView kieView;

    @Mock
    ObservablePath path;

    @Mock
    SolverResourceType resourceType;

    @Mock
    VersionRecordManager versionRecordManager;

    private SolverConfigModel model;

    @Before
    public void setUp() throws Exception {

        model = new SolverConfigModel();
        model.setTerminationConfig( terminationConfigModel );
        model.setScoreDirectorFactoryConfig( scoreDirectorFactoryConfig );

        when( resourceType.getSuffix() ).thenReturn( "solver.xml" );
        when( resourceType.accept( path ) ).thenReturn( true );
        when( resourceType.accept( path ) ).thenReturn( false );

        when( versionRecordManager.getCurrentPath() ).thenReturn( path );

        presenter = new SolverEditorPresenter( view,
                                               resourceType,
                                               mock( XMLViewer.class ),
                                               new NotificationEventMock(),
                                               new ServiceMock() ) {
            {
                kieView = mock( KieEditorWrapperView.class );
                versionRecordManager = SolverEditorPresenterTest.this.versionRecordManager;
                overviewWidget = mock( OverviewWidgetPresenter.class );
            }

            protected void makeMenuBar() {
            }

            protected void addSourcePage() {
            }
        };
    }

    @Test
    public void testLoad() throws Exception {
        presenter.onStartup( path,
                             mock( PlaceRequest.class ) );

        verify( view ).setTerminationConfigModel( terminationConfigModel );
        verify( view ).setScoreDirectorFactoryConfig( scoreDirectorFactoryConfig,
                                                      path );
    }

    private class NotificationEventMock
            implements Event<NotificationEvent> {

        @Override public void fire( NotificationEvent notificationEvent ) {

        }

        @Override public Event<NotificationEvent> select( Annotation... annotations ) {
            return null;
        }

        @Override public <U extends NotificationEvent> Event<U> select( Class<U> aClass, Annotation... annotations ) {
            return null;
        }
    }

    private class ServiceMock
            implements Caller<SolverEditorService> {

        private SolverEditorService service = new SolverEditorServiceMock();
        RemoteCallback remoteCallback;

        @Override public SolverEditorService call() {
            return service;
        }

        @Override public SolverEditorService call( RemoteCallback<?> remoteCallback ) {
            return call( remoteCallback, null );
        }

        @Override public SolverEditorService call( RemoteCallback<?> remoteCallback, ErrorCallback<?> errorCallback ) {
            this.remoteCallback = remoteCallback;
            return call();
        }

        private class SolverEditorServiceMock implements SolverEditorService {

            @Override public SolverModelContent loadContent( Path path ) {

                SolverModelContent content = new SolverModelContent( model,
                                                                     new Overview() );
                remoteCallback.callback( content );

                return null;
            }

            @Override public Path copy( Path path, String newName, String comment ) {
                return null;
            }

            @Override public Path create( Path context, String fileName, SolverConfigModel content, String comment ) {
                return null;
            }

            @Override public void delete( Path path, String comment ) {

            }

            @Override public SolverConfigModel load( Path path ) {
                return null;
            }

            @Override public Path rename( Path path, String newName, String comment ) {
                return null;
            }

            @Override public Path save( Path path, SolverConfigModel content, Metadata metadata, String comment ) {
                return null;
            }

            @Override public List<ValidationMessage> validate( Path path, SolverConfigModel content ) {
                return null;
            }

            @Override public String toSource( Path path, SolverConfigModel model ) {
                return null;
            }
        }
    }
}