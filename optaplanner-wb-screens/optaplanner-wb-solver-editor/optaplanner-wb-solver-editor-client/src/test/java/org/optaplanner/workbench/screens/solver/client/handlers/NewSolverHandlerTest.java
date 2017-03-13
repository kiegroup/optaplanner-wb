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
package org.optaplanner.workbench.screens.solver.client.handlers;

import com.google.gwt.core.client.GWT;
import com.google.gwtmockito.GwtMockitoTestRunner;
import org.jboss.errai.security.shared.api.identity.User;
import org.jboss.errai.ui.client.local.spi.TranslationService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.optaplanner.workbench.screens.solver.client.type.SolverResourceType;
import org.optaplanner.workbench.screens.solver.service.SolverEditorService;
import org.uberfire.ext.widgets.common.client.common.BusyIndicatorView;
import org.uberfire.mocks.CallerMock;
import org.uberfire.rpc.SessionInfo;
import org.uberfire.security.authz.AuthorizationManager;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(GwtMockitoTestRunner.class )
public class NewSolverHandlerTest {

    @Mock
    private SolverEditorService solverService;

    @Mock
    private BusyIndicatorView busyIndicatorView;

    @Mock
    private User user;

    @Mock
    private AuthorizationManager authorizationManager;

    @Mock
    private SessionInfo sessionInfo;

    @Mock
    private TranslationService translationService;

    private NewSolverHandler newSolverHandler;

    private SolverResourceType resourceType;

    @Before
    public void setUp() throws Exception {
        newSolverHandler = new NewSolverHandler( new CallerMock<>( solverService ),
                                                 resourceType,
                                                 busyIndicatorView,
                                                 authorizationManager,
                                                 sessionInfo,
                                                 translationService );
        resourceType = GWT.create( SolverResourceType.class );
    }

    @Test
    public void noPermissionToCreate() throws Exception {
        testPerimissionToCreate( false );
    }

    @Test
    public void hasPermissionToCreate() throws Exception {
        testPerimissionToCreate( true );
    }

    private void testPerimissionToCreate( boolean hasPermission ) {
        when( authorizationManager.authorize( NewSolverHandler.PLANNER_AVAILABLE, sessionInfo.getIdentity() ) ).thenReturn( hasPermission );

        assertEquals( hasPermission, newSolverHandler.canCreate() );
    }

}