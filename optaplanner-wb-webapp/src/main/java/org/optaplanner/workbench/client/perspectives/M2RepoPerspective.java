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

import javax.enterprise.event.Event;
import javax.inject.Inject;

import org.guvnor.m2repo.client.event.M2RepoRefreshEvent;
import org.guvnor.m2repo.client.upload.UploadFormPresenter;
import org.jboss.errai.common.client.dom.Div;
import org.jboss.errai.ioc.client.api.ManagedInstance;
import org.jboss.errai.ui.client.local.api.IsElement;
import org.jboss.errai.ui.client.local.spi.TranslationService;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.Templated;
import org.kie.workbench.common.workbench.client.PerspectiveIds;
import org.optaplanner.workbench.client.resources.i18n.AppConstants;
import org.uberfire.client.annotations.WorkbenchMenu;
import org.uberfire.client.annotations.WorkbenchPanel;
import org.uberfire.client.annotations.WorkbenchPerspective;
import org.uberfire.workbench.model.menu.MenuFactory;
import org.uberfire.workbench.model.menu.Menus;

/**
 * A Perspective to show M2_REPO related screen
 */
@Templated
@WorkbenchPerspective(identifier = PerspectiveIds.GUVNOR_M2REPO)
public class M2RepoPerspective implements IsElement {

    @Inject
    private Event<M2RepoRefreshEvent> refreshEvents;

    @Inject
    private ManagedInstance<UploadFormPresenter> uploadFormPresenterProvider;

    @Inject
    private TranslationService translationService;

    @Inject
    @DataField
    @WorkbenchPanel(parts = "M2RepoEditor")
    Div m2RepoEditor;

    @WorkbenchMenu
    public Menus getMenus() {
        return MenuFactory.newTopLevelMenu(translationService.getTranslation(AppConstants.M2RepoPerspective_Upload))
                .respondsWith(() -> {
                    UploadFormPresenter uploadFormPresenter = uploadFormPresenterProvider.get();
                    uploadFormPresenter.showView();
                })
                .endMenu()
                .newTopLevelMenu(translationService.getTranslation(AppConstants.M2RepoPerspective_Refresh))
                .respondsWith(() -> refreshEvents.fire(new M2RepoRefreshEvent()))
                .endMenu()
                .build();
    }
}
