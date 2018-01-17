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
package org.optaplanner.workbench.client.perspectives;

import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.guvnor.structure.client.editors.repository.clone.CloneRepositoryPresenter;
import org.jboss.errai.ui.client.local.spi.TranslationService;
import org.kie.workbench.common.widgets.client.handlers.NewResourcePresenter;
import org.optaplanner.workbench.client.resources.i18n.AppConstants;
import org.uberfire.client.annotations.Perspective;
import org.uberfire.client.annotations.WorkbenchMenu;
import org.uberfire.client.annotations.WorkbenchPerspective;
import org.uberfire.client.mvp.PlaceManager;
import org.uberfire.client.workbench.panels.impl.MultiListWorkbenchPanelPresenter;
import org.uberfire.client.workbench.panels.impl.SimpleWorkbenchPanelPresenter;
import org.uberfire.mvp.impl.DefaultPlaceRequest;
import org.uberfire.workbench.model.CompassPosition;
import org.uberfire.workbench.model.PanelDefinition;
import org.uberfire.workbench.model.PerspectiveDefinition;
import org.uberfire.workbench.model.impl.PanelDefinitionImpl;
import org.uberfire.workbench.model.impl.PartDefinitionImpl;
import org.uberfire.workbench.model.impl.PerspectiveDefinitionImpl;
import org.uberfire.workbench.model.menu.MenuFactory;
import org.uberfire.workbench.model.menu.MenuItem;
import org.uberfire.workbench.model.menu.Menus;

/**
 * A Perspective for Administrators
 */
@ApplicationScoped
@WorkbenchPerspective(identifier = "PlannerAdminPerspective")
public class AdministrationPerspective {

    @Inject
    private NewResourcePresenter newResourcePresenter;

    @Inject
    private PlaceManager placeManager;

    @Inject
    private CloneRepositoryPresenter cloneRepositoryPresenter;

    @Inject
    private TranslationService translationService;

    @Perspective
    public PerspectiveDefinition buildPerspective() {
        final PerspectiveDefinition perspective = new PerspectiveDefinitionImpl(MultiListWorkbenchPanelPresenter.class.getName());
        perspective.setName(translationService.getTranslation(AppConstants.AdministrationPerspective_AdministrationPerspective));

        perspective.getRoot().addPart(new PartDefinitionImpl(new DefaultPlaceRequest("RepositoriesEditor")));

        final PanelDefinition west = new PanelDefinitionImpl(SimpleWorkbenchPanelPresenter.class.getName());
        west.setWidth(300);
        west.setMinWidth(200);
        west.addPart(new PartDefinitionImpl(new DefaultPlaceRequest("FileExplorer")));

        perspective.getRoot().insertChild(CompassPosition.WEST,
                                          west);

        return perspective;
    }

    @WorkbenchMenu
    public Menus buildMenuBar() {
        return MenuFactory
                .newTopLevelMenu(translationService.getTranslation(AppConstants.AdministrationPerspective_MenuExplore))
                .withItems(getExploreMenuItems())
                .endMenu()
                .newTopLevelMenu(translationService.getTranslation(AppConstants.AdministrationPerspective_MenuOrganizationalUnits))
                .withItems(getOrganizationalUnitsMenuItem())
                .endMenu()
                .newTopLevelMenu(translationService.getTranslation(AppConstants.AdministrationPerspective_MenuRepositories))
                .withItems(getRepositoriesMenuItems())
                .endMenu()
                .build();
    }

    private List<? extends MenuItem> getRepositoriesMenuItems() {
        ArrayList<MenuItem> menuItems = new ArrayList<MenuItem>();
        menuItems.add(MenuFactory.newSimpleItem(translationService.getTranslation(AppConstants.AdministrationPerspective_MenuListRepositories)).respondsWith(
                () -> placeManager.goTo("RepositoriesEditor")).endMenu().build().getItems().get(0));
        menuItems.add(MenuFactory.newSimpleItem(translationService.getTranslation(AppConstants.AdministrationPerspective_MenuCloneRepository)).respondsWith(
                () -> cloneRepositoryPresenter.showForm()).endMenu().build().getItems().get(0));

        return menuItems;
    }

    private List<? extends MenuItem> getOrganizationalUnitsMenuItem() {
        ArrayList<MenuItem> menuItems = new ArrayList<MenuItem>();
        menuItems.add(MenuFactory.newSimpleItem(translationService.getTranslation(AppConstants.AdministrationPerspective_MenuManageOrganizationalUnits)).respondsWith(
                () -> placeManager.goTo("org.kie.workbench.common.screens.organizationalunit.manager.OrganizationalUnitManager")).endMenu().build().getItems().get(0));
        return menuItems;
    }

    private List<? extends MenuItem> getExploreMenuItems() {
        ArrayList<MenuItem> menuItems = new ArrayList<MenuItem>();
        menuItems.add(MenuFactory.newSimpleItem(translationService.getTranslation(AppConstants.AdministrationPerspective_MenuExploreFiles)).respondsWith(
                () -> placeManager.goTo("FileExplorer")).endMenu().build().getItems().get(0));
        return menuItems;
    }
}
