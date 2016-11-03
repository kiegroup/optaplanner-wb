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

import java.util.HashSet;
import java.util.Set;

import com.google.gwtmockito.GwtMockitoTestRunner;
import org.jboss.errai.security.shared.api.Role;
import org.jboss.errai.security.shared.api.identity.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
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

import static org.mockito.Mockito.*;

@RunWith(GwtMockitoTestRunner.class)
public class PlannerWorkbenchDocksTest {

    @Mock
    private UberfireDocks uberfireDocks;

    @Mock
    private SessionInfo sessionInfo;

    @Mock
    private AuthorizationManager authorizationManager;

    @Mock
    private User user;

    private UberfireDock plannerDock;

    @InjectMocks
    private PlannerWorkbenchDocks plannerWorkbenchDocks;

    @Before
    public void initTest() {
        plannerWorkbenchDocks.setup( "authoring");
        plannerDock = new UberfireDock( UberfireDockPosition.EAST,
                PlannerCommonImageResources.INSTANCE.optaPlannerDisabledIcon(),
                PlannerCommonImageResources.INSTANCE.optaPlannerEnabledIcon(),
                new DefaultPlaceRequest( "PlannerDomainScreen" ), "authoring" ).withSize( 450 ).withLabel( PlannerCommonConstants.INSTANCE.DocksPlannerTitle() );
    }

    @Test
    public void plannerRoleGrantedTest() {
        when( sessionInfo.getId() ).thenReturn( "logged_user" );
        when( sessionInfo.getIdentity() ).thenReturn( user );
        when( authorizationManager.authorize( WorkbenchFeatures.PLANNER_AVAILABLE, user ) ).thenReturn( true );

        UberfireDockReadyEvent event = new UberfireDockReadyEvent( "authoring" );
        plannerWorkbenchDocks.perspectiveChangeEvent( event );

        verify( uberfireDocks, times( 1 ) ).add( plannerDock );
    }

    @Test
    public void plannerRoleNotGrantedNeverVisitedTest() {
        testPlannerNotGranted( false );
    }

    @Test
    public void plannerRoleNotGrantedVisitedTest() {
        testPlannerNotGranted( true );
    }

    private void testPlannerNotGranted( boolean visited ) {

        if ( visited ) {
            //make that a user with the grants visits the authoring perspective
            plannerRoleGrantedTest();

        }
        //user hasn't the planner role in this case
        Set<Role> userRoles = new HashSet<Role>();

        when( sessionInfo.getId() ).thenReturn( "logged_user" );
        when( sessionInfo.getIdentity() ).thenReturn( user );
        when( user.getRoles() ).thenReturn( userRoles );

        UberfireDockReadyEvent event = new UberfireDockReadyEvent( "authoring" );
        plannerWorkbenchDocks.perspectiveChangeEvent( event );

        if ( visited ) {
            //if the authoring was visited at least once by a user with the planner role
            //ensure the dock is removed
            verify( uberfireDocks, times( 1 ) ).remove( plannerDock );
        }
        //if not, do nothing
    }
}
