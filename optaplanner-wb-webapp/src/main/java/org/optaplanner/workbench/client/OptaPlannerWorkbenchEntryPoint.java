/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.optaplanner.workbench.client;

import javax.inject.Inject;

import org.guvnor.common.services.shared.config.AppConfigService;
import org.jboss.errai.common.client.api.Caller;
import org.jboss.errai.ioc.client.api.EntryPoint;
import org.jboss.errai.security.shared.service.AuthenticationService;
import org.kie.workbench.common.services.shared.service.PlaceManagerActivityService;
import org.kie.workbench.common.workbench.client.entrypoint.DefaultWorkbenchEntryPoint;
import org.kie.workbench.common.workbench.client.menu.DefaultWorkbenchFeaturesMenusHelper;
import org.optaplanner.workbench.client.resources.i18n.AppConstants;
import org.uberfire.client.mvp.AbstractWorkbenchPerspectiveActivity;
import org.uberfire.client.mvp.ActivityBeansCache;
import org.uberfire.client.views.pfly.menu.UserMenu;
import org.uberfire.client.workbench.widgets.menu.WorkbenchMenuBarPresenter;
import org.uberfire.mvp.impl.DefaultPlaceRequest;
import org.uberfire.workbench.model.menu.MenuFactory;
import org.uberfire.workbench.model.menu.Menus;

import static org.kie.workbench.common.workbench.client.PerspectiveIds.*;
import static org.optaplanner.workbench.screens.common.client.PerspectiveIds.*;

@EntryPoint
public class OptaPlannerWorkbenchEntryPoint extends DefaultWorkbenchEntryPoint {

    protected AppConstants constants = AppConstants.INSTANCE;

    protected DefaultWorkbenchFeaturesMenusHelper menusHelper;

    protected WorkbenchMenuBarPresenter menuBar;

    protected Caller<AuthenticationService> authService;

    protected UserMenu userMenu;

    @Inject
    public OptaPlannerWorkbenchEntryPoint( final Caller<AppConfigService> appConfigService,
                                           final Caller<PlaceManagerActivityService> pmas,
                                           final ActivityBeansCache activityBeansCache,
                                           final DefaultWorkbenchFeaturesMenusHelper menusHelper,
                                           final WorkbenchMenuBarPresenter menuBar,
                                           final Caller<AuthenticationService> authService,
                                           final UserMenu userMenu ) {
        super( appConfigService, pmas, activityBeansCache );
        this.menusHelper = menusHelper;
        this.menuBar = menuBar;
        this.authService = authService;
        this.userMenu = userMenu;
    }

    @Override
    protected void setupMenu() {
        final AbstractWorkbenchPerspectiveActivity defaultPerspective = menusHelper.getDefaultPerspectiveActivity();

        final Menus menus = MenuFactory
                .newTopLevelMenu( constants.Home() ).place( new DefaultPlaceRequest( defaultPerspective.getIdentifier() ) ).endMenu()
                .newTopLevelMenu( constants.Authoring() ).perspective( AUTHORING ).endMenu()
                .newTopLevelMenu( constants.MenuRepositories() ).perspective( GUVNOR_M2REPO ).endMenu()
                .newTopLevelMenu( constants.AdministrationPerspectiveName() ).perspective( PLANNER_ADMIN ).endMenu()
                .build();

        menuBar.addMenus( menus );

        menusHelper.addUtilitiesMenuItems();
        menusHelper.addLogoutMenuItem();
    }
}
