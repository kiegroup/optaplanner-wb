/*
 * Copyright 2018 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.optaplanner.workbench.screens.solver.client.editor;

import java.util.ArrayList;

import com.google.gwt.core.client.GWT;
import com.google.gwtmockito.GwtMockitoTestRunner;
import com.google.gwtmockito.WithClassesToStub;
import org.guvnor.common.services.shared.validation.model.ValidationMessage;
import org.gwtbootstrap3.client.ui.Modal;
import org.jboss.errai.ui.client.local.spi.TranslationService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kie.workbench.common.test.MockProvider;
import org.kie.workbench.common.widgets.metadata.client.KieEditorWrapperView;
import org.kie.workbench.common.widgets.metadata.client.widget.OverviewWidgetPresenter;
import org.mockito.Mock;
import org.optaplanner.workbench.screens.solver.client.type.SolverResourceType;
import org.optaplanner.workbench.screens.solver.model.SolverConfigModel;
import org.optaplanner.workbench.screens.solver.service.SolverEditorService;
import org.uberfire.backend.vfs.Path;
import org.uberfire.ext.editor.commons.client.history.VersionRecordManager;
import org.uberfire.java.nio.IOException;
import org.uberfire.mocks.CallerMock;
import org.uberfire.mvp.Command;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(GwtMockitoTestRunner.class)
@WithClassesToStub({Modal.class})
public class SolverEditorPresenterValidationTest {

    @Mock
    private SolverEditorView view;

    @Mock
    private SolverEditorService solverEditorService;

    @Mock
    private Command afterValidation;

    private SolverEditorPresenter presenter;

    @Before
    public void setUp() {
        SolverResourceType resourceType = GWT.create(SolverResourceType.class);
        presenter = new SolverEditorPresenter(view,
                                              resourceType,
                                              mock(XMLViewer.class),
                                              new NotificationEventMock(),
                                              new CallerMock<>(solverEditorService),
                                              MockProvider.getMockValidationPopup(),
                                              mock(TranslationService.class)) {
            {
                kieView = mock(KieEditorWrapperView.class);
                versionRecordManager = mock(VersionRecordManager.class);
                overviewWidget = mock(OverviewWidgetPresenter.class);
            }
        };
    }

    @Test
    public void commandIsCalled() {
        doReturn(new ArrayList<ValidationMessage>()).when(solverEditorService).validate(any(Path.class),
                                                                                        any(SolverConfigModel.class));

        presenter.onValidate(afterValidation);
        verify(afterValidation).execute();
    }

    @Test
    public void callFailsAndCommandIsCalled() {
        doThrow(new IOException()).when(solverEditorService).validate(any(Path.class),
                                                                      any(SolverConfigModel.class));

        presenter.onValidate(afterValidation);
        verify(afterValidation).execute();
    }
}
