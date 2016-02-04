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
package org.optaplanner.workbench.screens.solver.client.handlers;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.IsWidget;
import org.guvnor.common.services.project.model.Package;
import org.jboss.errai.common.client.api.Caller;
import org.jboss.errai.security.shared.api.RoleImpl;
import org.jboss.errai.security.shared.api.identity.User;
import org.kie.workbench.common.widgets.client.handlers.DefaultNewResourceHandler;
import org.kie.workbench.common.widgets.client.handlers.NewResourcePresenter;
import org.kie.workbench.common.widgets.client.resources.i18n.CommonConstants;
import org.optaplanner.workbench.screens.solver.client.resources.SolverEditorResources;
import org.optaplanner.workbench.screens.solver.client.resources.i18n.SolverEditorConstants;
import org.optaplanner.workbench.screens.solver.client.type.SolverResourceType;
import org.optaplanner.workbench.screens.solver.model.SolverConfigModel;
import org.optaplanner.workbench.screens.solver.service.SolverEditorService;
import org.uberfire.ext.widgets.common.client.callbacks.HasBusyIndicatorDefaultErrorCallback;
import org.uberfire.ext.widgets.common.client.common.BusyIndicatorView;
import org.uberfire.workbench.type.ResourceTypeDefinition;

/**
 * Handler for the creation of new Solver Configurations
 */
@ApplicationScoped
public class NewSolverHandler
        extends DefaultNewResourceHandler {

    private Caller<SolverEditorService> solverService;

    private SolverResourceType resourceType;

    private BusyIndicatorView busyIndicatorView;

    private User user;

    public NewSolverHandler() {
    }

    @Inject
    public NewSolverHandler( final Caller<SolverEditorService> solverService,
                             final SolverResourceType resourceType,
                             final BusyIndicatorView busyIndicatorView,
                             final User user ) {
        this.solverService = solverService;
        this.resourceType = resourceType;
        this.busyIndicatorView = busyIndicatorView;
        this.user = user;
    }

    @Override
    public String getDescription() {
        return SolverEditorConstants.INSTANCE.newSolverDescription();
    }

    @Override
    public IsWidget getIcon() {
        return new Image( SolverEditorResources.INSTANCE.images().typeSolver() );
    }

    @Override
    public ResourceTypeDefinition getResourceType() {
        return resourceType;
    }

    @Override
    public void create( final Package pkg,
                        final String baseFileName,
                        final NewResourcePresenter presenter ) {
        busyIndicatorView.showBusyIndicator( CommonConstants.INSTANCE.Saving() );
        solverService.call( getSuccessCallback( presenter ),
                            new HasBusyIndicatorDefaultErrorCallback( busyIndicatorView ) ).create( pkg.getPackageMainResourcesPath(),
                                                                                                    buildFileName( baseFileName,
                                                                                                                   resourceType ),
                                                                                                    new SolverConfigModel(),
                                                                                                    "" );
    }

    @Override
    public boolean canCreate() {
        return user.getRoles().contains( new RoleImpl( "plannermgmt" ) );
    }
}
