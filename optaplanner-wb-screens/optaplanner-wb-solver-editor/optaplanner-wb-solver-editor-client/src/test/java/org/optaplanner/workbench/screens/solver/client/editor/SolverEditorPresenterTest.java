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

import java.util.List;
import java.util.function.Supplier;

import com.google.gwt.core.client.GWT;
import com.google.gwtmockito.GwtMock;
import com.google.gwtmockito.GwtMockitoTestRunner;
import org.guvnor.common.services.shared.metadata.model.Metadata;
import org.guvnor.common.services.shared.metadata.model.Overview;
import org.guvnor.common.services.shared.validation.model.ValidationMessage;
import org.guvnor.messageconsole.client.console.widget.button.AlertsButtonMenuItemBuilder;
import org.jboss.errai.common.client.api.Caller;
import org.jboss.errai.common.client.api.ErrorCallback;
import org.jboss.errai.common.client.api.RemoteCallback;
import org.jboss.errai.ui.client.local.spi.TranslationService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kie.workbench.common.widgets.client.popups.validation.ValidationPopup;
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
import org.uberfire.ext.editor.commons.service.support.SupportsSaveAndRename;
import org.uberfire.mvp.Command;
import org.uberfire.mvp.PlaceRequest;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(GwtMockitoTestRunner.class)
public class SolverEditorPresenterTest {

    @GwtMock
    SolverEditorImageResources solverEditorImageResources;

    @GwtMock
    SolverEditorResources solverEditorResources;

    @Mock
    KieEditorWrapperView kieView;

    @Mock
    ObservablePath path;

    @Mock
    VersionRecordManager versionRecordManager;

    @Mock
    AlertsButtonMenuItemBuilder alertsButtonMenuItemBuilder;

    @Mock
    private SolverEditorView view;

    private SolverEditorPresenter presenter;

    private TerminationConfigModel terminationConfigModel = new TerminationConfigModel();

    private ScoreDirectorFactoryConfigModel scoreDirectorFactoryConfig = new ScoreDirectorFactoryConfigModel();

    private SolverConfigModel model;

    private SolverResourceType resourceType;

    private ServiceMock solverService;

    @Before
    public void setUp() throws Exception {
        model = new SolverConfigModel();
        model.setTerminationConfig(terminationConfigModel);
        model.setScoreDirectorFactoryConfig(scoreDirectorFactoryConfig);
        resourceType = GWT.create(SolverResourceType.class);

        when(resourceType.getSuffix()).thenReturn("solver.xml");
        when(resourceType.accept(path)).thenReturn(true);
        when(resourceType.accept(path)).thenReturn(false);

        when(versionRecordManager.getCurrentPath()).thenReturn(path);

        solverService = new ServiceMock();
        presenter = spy(new SolverEditorPresenter(view,
                                                  resourceType,
                                                  mock(XMLViewer.class),
                                                  new NotificationEventMock(),
                                                  solverService,
                                                  mock(ValidationPopup.class),
                                                  mock(TranslationService.class)) {
            {
                kieView = mock(KieEditorWrapperView.class);
                versionRecordManager = SolverEditorPresenterTest.this.versionRecordManager;
                alertsButtonMenuItemBuilder = SolverEditorPresenterTest.this.alertsButtonMenuItemBuilder;
                overviewWidget = mock(OverviewWidgetPresenter.class);
            }

            @Override
            protected Command getSaveAndRename() {
                return mock(Command.class);
            }

            protected void makeMenuBar() {
            }

            protected void addSourcePage() {
            }
        });
    }

    @Test
    public void load() throws Exception {
        presenter.onStartup(path,
                            mock(PlaceRequest.class));

        verify(view).setTerminationConfigModel(terminationConfigModel);
        verify(view).setScoreDirectorFactoryConfig(scoreDirectorFactoryConfig,
                                                   path);
    }

    @Test
    public void testGetContentSupplier() throws Exception {

        final SolverConfigModel content = mock(SolverConfigModel.class);

        doReturn(content).when(presenter).getModel();

        final Supplier<SolverConfigModel> contentSupplier = presenter.getContentSupplier();

        assertEquals(content, contentSupplier.get());
    }

    @Test
    public void testGetSaveAndRenameServiceCaller() throws Exception {

        final Caller<? extends SupportsSaveAndRename<SolverConfigModel, Metadata>> serviceCaller = presenter.getSaveAndRenameServiceCaller();

        assertEquals(this.solverService, serviceCaller);
    }

    private class ServiceMock
            implements Caller<SolverEditorService> {

        RemoteCallback remoteCallback;
        private SolverEditorService service = new SolverEditorServiceMock();

        @Override
        public SolverEditorService call() {
            return service;
        }

        @Override
        public SolverEditorService call(RemoteCallback<?> remoteCallback) {
            return call(remoteCallback,
                        null);
        }

        @Override
        public SolverEditorService call(RemoteCallback<?> remoteCallback,
                                        ErrorCallback<?> errorCallback) {
            this.remoteCallback = remoteCallback;
            return call();
        }

        private class SolverEditorServiceMock implements SolverEditorService {

            @Override
            public SolverModelContent loadContent(Path path) {

                final Overview overview = new Overview() {{
                    setMetadata(mock(Metadata.class));
                }};
                final SolverModelContent content = new SolverModelContent(model,
                                                                          overview);
                remoteCallback.callback(content);

                return null;
            }

            @Override
            public List<ValidationMessage> smokeTest(Path path,
                                                     SolverConfigModel config) {
                return null;
            }

            @Override
            public Path copy(Path path,
                             String newName,
                             String comment) {
                return null;
            }

            @Override
            public Path copy(Path path,
                             String newName,
                             Path targetDirectory,
                             String comment) {
                return null;
            }

            @Override
            public Path create(Path context,
                               String fileName,
                               SolverConfigModel content,
                               String comment) {
                return null;
            }

            @Override
            public void delete(Path path,
                               String comment) {

            }

            @Override
            public SolverConfigModel load(Path path) {
                return null;
            }

            @Override
            public Path rename(Path path,
                               String newName,
                               String comment) {
                return null;
            }

            @Override
            public Path save(Path path,
                             SolverConfigModel content,
                             Metadata metadata,
                             String comment) {
                return null;
            }

            @Override
            public List<ValidationMessage> validate(Path path,
                                                    SolverConfigModel content) {
                return null;
            }

            @Override
            public String toSource(Path path,
                                   SolverConfigModel model) {
                return null;
            }

            @Override
            public Path saveAndRename(final Path path,
                                      final String newFileName,
                                      final Metadata metadata,
                                      final SolverConfigModel content,
                                      final String comment) {
                return null;
            }
        }
    }
}
