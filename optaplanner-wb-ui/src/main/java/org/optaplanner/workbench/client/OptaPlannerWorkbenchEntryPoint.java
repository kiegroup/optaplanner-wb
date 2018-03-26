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

import java.util.Arrays;
import java.util.List;
import javax.inject.Inject;

import org.guvnor.common.services.shared.config.AppConfigService;
import org.jboss.errai.common.client.api.Caller;
import org.jboss.errai.ioc.client.api.EntryPoint;
import org.jboss.errai.ui.client.local.spi.TranslationService;
import org.jboss.errai.ui.shared.api.annotations.Bundle;
import org.kie.workbench.common.workbench.client.PerspectiveIds;
import org.kie.workbench.common.workbench.client.entrypoint.DefaultWorkbenchEntryPoint;
import org.kie.workbench.common.workbench.client.menu.DefaultWorkbenchFeaturesMenusHelper;
import org.optaplanner.workbench.client.resources.i18n.AppConstants;
import org.uberfire.client.mvp.ActivityBeansCache;
import org.uberfire.client.views.pfly.menu.UserMenu;
import org.uberfire.client.workbench.widgets.menu.megamenu.WorkbenchMegaMenuPresenter;
import org.uberfire.ext.preferences.client.admin.page.AdminPage;
import org.uberfire.workbench.model.menu.MenuFactory;
import org.uberfire.workbench.model.menu.MenuItem;
import org.uberfire.workbench.model.menu.Menus;

@EntryPoint
@Bundle("resources/i18n/AppConstants.properties")
public class OptaPlannerWorkbenchEntryPoint extends DefaultWorkbenchEntryPoint {

    protected DefaultWorkbenchFeaturesMenusHelper menusHelper;

    protected WorkbenchMegaMenuPresenter menuBar;

    protected UserMenu userMenu;

    protected AdminPage adminPage;

    protected TranslationService translationService;

    @Inject
    public OptaPlannerWorkbenchEntryPoint(final Caller<AppConfigService> appConfigService,
                                          final ActivityBeansCache activityBeansCache,
                                          final DefaultWorkbenchFeaturesMenusHelper menusHelper,
                                          final WorkbenchMegaMenuPresenter menuBar,
                                          final UserMenu userMenu,
                                          final AdminPage adminPage,
                                          final TranslationService translationService) {
        super(appConfigService,
              activityBeansCache);
        this.menusHelper = menusHelper;
        this.menuBar = menuBar;
        this.userMenu = userMenu;
        this.adminPage = adminPage;
        this.translationService = translationService;
    }

    @Override
    protected void setupMenu() {
        adminPage.addScreen("root",
                            translationService.getTranslation(AppConstants.OptaPlannerWorkbenchEntryPoint_Settings));
        adminPage.setDefaultScreen("root");

        adminPage.addPreference("root",
                                "LibraryPreferences",
                                translationService.getTranslation(AppConstants.OptaPlannerWorkbenchEntryPoint_Library),
                                "fa-cubes",
                                "preferences");

        adminPage.addPreference("root",
                                "ArtifactRepositoryPreference",
                                translationService.getTranslation(AppConstants.OptaPlannerWorkbenchEntryPoint_ArtifactRepository),
                                "fa-archive",
                                "preferences");

        menusHelper.addRolesMenuItems();
        menusHelper.addUtilitiesMenuItems();

        final Menus menus = MenuFactory
                .newTopLevelMenu(translationService.getTranslation(AppConstants.OptaPlannerWorkbenchEntryPoint_Home))
                .withItems(getHomePerspectives())
                .endMenu()
                .newTopLevelMenu(translationService.getTranslation(AppConstants.OptaPlannerWorkbenchEntryPoint_Authoring))
                .withItems(getAuthoringPerspectives())
                .endMenu()
                .newTopLevelMenu(translationService.getTranslation(AppConstants.OptaPlannerWorkbenchEntryPoint_Deploy))
                .withItems(getDeployPerspectives())
                .endMenu()
                .build();

        menuBar.addMenus(menus);
    }

    private List<MenuItem> getHomePerspectives() {
        return Arrays.asList(
                MenuFactory.newSimpleItem(translationService.getTranslation(AppConstants.OptaPlannerWorkbenchEntryPoint_Home)).perspective(PerspectiveIds.HOME).endMenu().build().getItems().get(0),
                MenuFactory.newSimpleItem(translationService.getTranslation(AppConstants.OptaPlannerWorkbenchEntryPoint_Admin)).perspective(PerspectiveIds.ADMIN).endMenu().build().getItems().get(0)
        );
    }

    private List<MenuItem> getAuthoringPerspectives() {
        return Arrays.asList(
                MenuFactory.newSimpleItem(translationService.getTranslation(AppConstants.OptaPlannerWorkbenchEntryPoint_ProjectAuthoring)).perspective(PerspectiveIds.LIBRARY).endMenu().build().getItems().get(0),
                MenuFactory.newSimpleItem(translationService.getTranslation(AppConstants.OptaPlannerWorkbenchEntryPoint_ArtifactRepository)).perspective(PerspectiveIds.GUVNOR_M2REPO).endMenu().build().getItems().get(0),
                MenuFactory.newSimpleItem(translationService.getTranslation(AppConstants.OptaPlannerWorkbenchEntryPoint_Administration)).perspective(PerspectiveIds.PLANNER_ADMIN).endMenu().build().getItems().get(0)
        );
    }

    private List<MenuItem> getDeployPerspectives() {
        return Arrays.asList(
                MenuFactory.newSimpleItem(translationService.getTranslation(AppConstants.OptaPlannerWorkbenchEntryPoint_ExecutionServers)).perspective(PerspectiveIds.SERVER_MANAGEMENT).endMenu().build().getItems().get(0)
        );
    }
}
