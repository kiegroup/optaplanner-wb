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

package org.optaplanner.workbench.client;

import java.util.ArrayList;

import com.google.gwtmockito.GwtMockitoTestRunner;
import org.guvnor.common.services.shared.config.AppConfigService;
import org.guvnor.common.services.shared.security.KieWorkbenchACL;
import org.jboss.errai.common.client.api.Caller;
import org.jboss.errai.security.shared.service.AuthenticationService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kie.workbench.common.services.shared.security.KieWorkbenchSecurityService;
import org.kie.workbench.common.services.shared.service.PlaceManagerActivityService;
import org.kie.workbench.common.workbench.client.menu.DefaultWorkbenchFeaturesMenusHelper;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.optaplanner.workbench.client.resources.i18n.AppConstants;
import org.uberfire.client.mvp.AbstractWorkbenchPerspectiveActivity;
import org.uberfire.client.mvp.ActivityBeansCache;
import org.uberfire.client.views.pfly.menu.UserMenu;
import org.uberfire.client.workbench.widgets.menu.WorkbenchMenuBarPresenter;
import org.uberfire.mocks.CallerMock;
import org.uberfire.mocks.ConstantsAnswerMock;
import org.uberfire.workbench.model.menu.MenuItem;
import org.uberfire.workbench.model.menu.Menus;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(GwtMockitoTestRunner.class)
public class OptaPlannerWorkbenchEntryPointTest {

    @Mock
    private AppConfigService appConfigService;
    private CallerMock<AppConfigService> appConfigServiceCallerMock;

    @Mock
    private KieWorkbenchSecurityService kieSecurityService;
    private CallerMock<KieWorkbenchSecurityService> kieSecurityServiceCallerMock;

    @Mock
    private PlaceManagerActivityService pmas;
    private CallerMock<PlaceManagerActivityService> pmasCallerMock;

    @Mock
    private KieWorkbenchACL kieACL;

    @Mock
    private ActivityBeansCache activityBeansCache;

    @Mock
    private DefaultWorkbenchFeaturesMenusHelper menusHelper;

    @Mock
    private WorkbenchMenuBarPresenter menuBar;

    @Mock
    private Caller<AuthenticationService> authService;

    @Mock
    private UserMenu userMenu;

    private OptaPlannerWorkbenchEntryPoint optaPlannerWorkbenchEntryPoint;

    @Before
    public void setup() {
        appConfigServiceCallerMock = new CallerMock<>( appConfigService );
        kieSecurityServiceCallerMock = new CallerMock<>( kieSecurityService );
        pmasCallerMock = new CallerMock<>( pmas );

        optaPlannerWorkbenchEntryPoint = spy( new OptaPlannerWorkbenchEntryPoint( appConfigServiceCallerMock,
                                                                                  kieSecurityServiceCallerMock,
                                                                                  pmasCallerMock,
                                                                                  kieACL,
                                                                                  activityBeansCache,
                                                                                  menusHelper,
                                                                                  menuBar,
                                                                                  authService,
                                                                                  userMenu ) );
        mockMenuHelper();
        mockConstants();
    }

    @Test
    public void setupMenuTest() {
        optaPlannerWorkbenchEntryPoint.setupMenu();

        ArgumentCaptor<Menus> menusCaptor = ArgumentCaptor.forClass( Menus.class );
        verify( menuBar ).addMenus( menusCaptor.capture() );

        Menus menus = menusCaptor.getValue();

        assertEquals( 4, menus.getItems().size() );

        assertEquals( optaPlannerWorkbenchEntryPoint.constants.Home(), menus.getItems().get( 0 ).getCaption() );
        assertEquals( optaPlannerWorkbenchEntryPoint.constants.Authoring(), menus.getItems().get( 1 ).getCaption() );
        assertEquals( optaPlannerWorkbenchEntryPoint.constants.MenuRepositories(), menus.getItems().get( 2 ).getCaption() );
        assertEquals( optaPlannerWorkbenchEntryPoint.constants.AdministrationPerspectiveName(), menus.getItems().get( 3 ).getCaption() );

        verify( menusHelper ).addUtilitiesMenuItems();
        verify( menusHelper ).addLogoutMenuItem();
    }

    private void mockMenuHelper() {
        final ArrayList<MenuItem> menuItems = new ArrayList<>();
        menuItems.add( mock( MenuItem.class ) );
        doReturn( menuItems ).when( menusHelper ).getPerspectivesMenuItems();

        doReturn( mock( AbstractWorkbenchPerspectiveActivity.class ) ).when( menusHelper ).getDefaultPerspectiveActivity();
    }

    private void mockConstants() {
        optaPlannerWorkbenchEntryPoint.constants = mock( AppConstants.class, new ConstantsAnswerMock() );
    }

}
