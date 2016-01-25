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
import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.enterprise.event.Event;
import javax.inject.Inject;

import com.google.gwt.user.client.ui.IsWidget;
import org.guvnor.common.services.shared.validation.model.ValidationMessage;
import org.jboss.errai.common.client.api.Caller;
import org.jboss.errai.common.client.api.RemoteCallback;
import org.kie.workbench.common.widgets.client.popups.validation.ValidationPopup;
import org.kie.workbench.common.widgets.client.resources.i18n.CommonConstants;
import org.kie.workbench.common.widgets.metadata.client.KieEditor;
import org.optaplanner.workbench.screens.solver.client.resources.i18n.SolverEditorConstants;
import org.optaplanner.workbench.screens.solver.client.type.SolverResourceType;
import org.optaplanner.workbench.screens.solver.model.SolverConfigModel;
import org.optaplanner.workbench.screens.solver.model.SolverModelContent;
import org.optaplanner.workbench.screens.solver.service.SolverEditorService;
import org.uberfire.backend.vfs.ObservablePath;
import org.uberfire.client.annotations.WorkbenchEditor;
import org.uberfire.client.annotations.WorkbenchMenu;
import org.uberfire.client.annotations.WorkbenchPartTitle;
import org.uberfire.client.annotations.WorkbenchPartTitleDecoration;
import org.uberfire.client.annotations.WorkbenchPartView;
import org.uberfire.client.views.pfly.multipage.PageImpl;
import org.uberfire.ext.widgets.common.client.callbacks.DefaultErrorCallback;
import org.uberfire.ext.widgets.common.client.callbacks.HasBusyIndicatorDefaultErrorCallback;
import org.uberfire.lifecycle.OnClose;
import org.uberfire.lifecycle.OnMayClose;
import org.uberfire.lifecycle.OnStartup;
import org.uberfire.mvp.Command;
import org.uberfire.mvp.PlaceRequest;
import org.uberfire.workbench.events.NotificationEvent;
import org.uberfire.workbench.model.menu.Menus;

/**
 * Uberfire Editor for OptaPlanner Solver Configuration
 */
@Dependent
@WorkbenchEditor(identifier = "OptaPlannerSolverEditor", supportedTypes = { SolverResourceType.class }, priority = 10)
public class SolverEditorPresenter
        extends KieEditor {

    private Caller<SolverEditorService> solverService;

    private Event<NotificationEvent> notification;

    private SolverResourceType solverResourceType;

    private XMLViewer xmlViewer;

    private SolverEditorView view;
    private SolverConfigModel model;

    @Inject
    public SolverEditorPresenter( final SolverEditorView view,
                                  final SolverResourceType solverResourceType,
                                  final XMLViewer xmlViewer,
                                  final Event<NotificationEvent> notification,
                                  final Caller<SolverEditorService> solverService ) {
        super( view );

        this.xmlViewer = xmlViewer;
        this.view = view;
        this.solverResourceType = solverResourceType;
        this.notification = notification;
        this.solverService = solverService;
    }

    @OnStartup
    public void onStartup( final ObservablePath path,
                           final PlaceRequest place ) {
        super.init( path,
                    place,
                    solverResourceType );
    }

    protected void loadContent() {
        view.showLoading();
        solverService.call( getLoadContentSuccessCallback(),
                            getNoSuchFileExceptionErrorCallback() ).loadContent( versionRecordManager.getCurrentPath() );
    }

    private RemoteCallback<SolverModelContent> getLoadContentSuccessCallback() {
        return new RemoteCallback<SolverModelContent>() {

            @Override
            public void callback( final SolverModelContent content ) {
                //Path is set to null when the Editor is closed (which can happen before async calls complete).
                if ( versionRecordManager.getCurrentPath() == null ) {
                    return;
                }

                resetEditorPages( content.getOverview() );

                addXMLSourcePage();

                model = content.getConfig();

                view.setTerminationConfigModel( model.getTermination() );
                view.setScoreDirectorFactoryConfig( model.getScoreDirectorFactoryConfig(),
                                                    versionRecordManager.getCurrentPath() );

                view.hideBusyIndicator();
                createOriginalHash( model );
            }

        };
    }

    private void addXMLSourcePage() {
        addPage( new PageImpl( xmlViewer,
                               SolverEditorConstants.INSTANCE.Source() ) {

            @Override
            public void onFocus() {
                solverService.call( getToSourceRemoteCallback(),
                                    new DefaultErrorCallback() ).toSource( versionRecordManager.getCurrentPath(),
                                                                           model );
            }
        } );
    }

    private RemoteCallback<String> getToSourceRemoteCallback() {
        return new RemoteCallback<String>() {
            @Override
            public void callback( String xml ) {
                xmlViewer.setContent( xml );
            }
        };
    }

    protected Command onValidate() {
        return new Command() {
            @Override
            public void execute() {
                solverService.call( getValidateRemoteCallback(),
                                    new DefaultErrorCallback() ).validate( versionRecordManager.getCurrentPath(),
                                                                           model );
            }
        };
    }

    private RemoteCallback<List<ValidationMessage>> getValidateRemoteCallback() {
        return new RemoteCallback<List<ValidationMessage>>() {
            @Override
            public void callback( final List<ValidationMessage> results ) {
                if ( results == null || results.isEmpty() ) {
                    notification.fire( new NotificationEvent( CommonConstants.INSTANCE.ItemValidatedSuccessfully(),
                                                              NotificationEvent.NotificationType.SUCCESS ) );
                } else {
                    ValidationPopup.showMessages( results );
                }
            }
        };
    }

    @Override
    protected void save( String commitMessage ) {
        solverService.call( getSaveSuccessCallback( model.hashCode() ),
                            new HasBusyIndicatorDefaultErrorCallback( view ) ).save( versionRecordManager.getCurrentPath(),
                                                                                     model,
                                                                                     metadata,
                                                                                     commitMessage );
    }

    @OnClose
    public void onClose() {
        versionRecordManager.clear();
    }

    @OnMayClose
    public boolean mayClose() {
        return super.mayClose( model );
    }

    @WorkbenchPartTitleDecoration
    public IsWidget getTitle() {
        return super.getTitle();
    }

    @WorkbenchPartTitle
    public String getTitleText() {
        return super.getTitleText();
    }

    @WorkbenchPartView
    public IsWidget getWidget() {
        return super.getWidget();
    }

    @WorkbenchMenu
    public Menus getMenus() {
        return menus;
    }

}
