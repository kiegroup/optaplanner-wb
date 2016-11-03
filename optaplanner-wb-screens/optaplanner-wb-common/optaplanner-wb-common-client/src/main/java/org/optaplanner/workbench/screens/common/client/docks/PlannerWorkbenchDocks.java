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

package org.optaplanner.workbench.screens.common.client.docks;

import javax.enterprise.context.Dependent;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.optaplanner.workbench.screens.common.client.resources.i18n.PlannerCommonConstants;
import org.optaplanner.workbench.screens.common.client.resources.images.PlannerCommonImageResources;
import org.optaplanner.workbench.screens.common.client.security.WorkbenchFeatures;
import org.uberfire.client.workbench.docks.UberfireDock;
import org.uberfire.client.workbench.docks.UberfireDockPosition;
import org.uberfire.client.workbench.docks.UberfireDockReadyEvent;
import org.uberfire.client.workbench.docks.UberfireDocks;
import org.uberfire.mvp.impl.DefaultPlaceRequest;
import org.uberfire.rpc.SessionInfo;
import org.uberfire.security.authz.AuthorizationManager;

@Dependent
public class PlannerWorkbenchDocks {

    private final UberfireDocks uberfireDocks;

    private final AuthorizationManager authorizationManager;

    private final SessionInfo sessionInfo;

    private String perspectiveIdentifier;

    private UberfireDock plannerDock;

    @Inject
    public PlannerWorkbenchDocks( final UberfireDocks uberfireDocks,
                                  final AuthorizationManager authorizationManager,
                                  final SessionInfo sessionInfo ) {
        this.uberfireDocks = uberfireDocks;
        this.authorizationManager = authorizationManager;
        this.sessionInfo = sessionInfo;
    }

    public void setup( String perspectiveIdentifier ) {
        this.perspectiveIdentifier = perspectiveIdentifier;
    }

    public void perspectiveChangeEvent( @Observes UberfireDockReadyEvent dockReadyEvent ) {
        String currentPerspectiveId = dockReadyEvent.getCurrentPerspective();
        if ( currentPerspectiveId != null && currentPerspectiveId.equals( perspectiveIdentifier ) ) {
            if ( authorizationManager.authorize( WorkbenchFeatures.PLANNER_AVAILABLE, sessionInfo.getIdentity() ) ) {
                if ( plannerDock == null ) {
                    plannerDock = new UberfireDock( UberfireDockPosition.EAST, PlannerCommonImageResources.INSTANCE.optaPlannerDisabledIcon(), PlannerCommonImageResources.INSTANCE.optaPlannerEnabledIcon(), new DefaultPlaceRequest( "PlannerDomainScreen" ), perspectiveIdentifier ).withSize( 450 ).withLabel( PlannerCommonConstants.INSTANCE.DocksPlannerTitle() );
                } else {
                    //avoid duplications
                    uberfireDocks.remove( plannerDock );
                }
                uberfireDocks.add( plannerDock );
            } else if ( plannerDock != null ) {
                uberfireDocks.remove( plannerDock );
            }
            uberfireDocks.enable( UberfireDockPosition.EAST, perspectiveIdentifier );
        } else {
            uberfireDocks.disable( UberfireDockPosition.EAST, perspectiveIdentifier );
        }
    }

}
