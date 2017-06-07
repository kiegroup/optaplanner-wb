/*
 * Copyright 2015 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
*/

package org.optaplanner.workbench.client.home;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import com.google.gwt.core.client.GWT;
import org.jboss.errai.ui.client.local.spi.TranslationService;
import org.kie.workbench.common.screens.home.model.HomeModel;
import org.kie.workbench.common.screens.home.model.ModelUtils;
import org.kie.workbench.common.screens.home.model.SectionEntry;
import org.optaplanner.workbench.client.resources.i18n.AppConstants;
import org.uberfire.client.mvp.PlaceManager;

import static org.kie.workbench.common.workbench.client.PerspectiveIds.GUVNOR_M2REPO;
import static org.kie.workbench.common.workbench.client.PerspectiveIds.LIBRARY;
import static org.kie.workbench.common.workbench.client.PerspectiveIds.PLANNER_ADMIN;
import static org.kie.workbench.common.workbench.client.PerspectiveIds.SERVER_MANAGEMENT;
import static org.uberfire.workbench.model.ActivityResourceType.PERSPECTIVE;

/**
 * Producer method for the Home Page content
 */
@ApplicationScoped
public class HomeProducer {

    private HomeModel model;

    @Inject
    private PlaceManager placeManager;

    @Inject
    private TranslationService translationService;

    @PostConstruct
    public void init() {
        final String url = GWT.getModuleBaseURL();
        model = new HomeModel(translationService.getTranslation(AppConstants.HomeProducer_KieKnowledgeDevelopmentCycle));
        model.addCarouselEntry(ModelUtils.makeCarouselEntry(translationService.getTranslation(AppConstants.HomeProducer_Author),
                                                            translationService.getTranslation(AppConstants.HomeProducer_FormalizeYourBusinessKnowledge),
                                                            url + "/images/HandHome.jpg"));
        model.addCarouselEntry(ModelUtils.makeCarouselEntry(translationService.getTranslation(AppConstants.HomeProducer_Deploy),
                                                            translationService.getTranslation(AppConstants.HomeProducer_ConfigureYourEnvironment),
                                                            url + "/images/HandHome.jpg"));
        final SectionEntry s1 = ModelUtils.makeSectionEntry(translationService.getTranslation(AppConstants.HomeProducer_Author) + ":");

        s1.addChild(ModelUtils.makeSectionEntry(translationService.getTranslation(AppConstants.HomeProducer_ProjectAuthoring),
                                                () -> placeManager.goTo(LIBRARY),
                                                LIBRARY,
                                                PERSPECTIVE));
        s1.addChild(ModelUtils.makeSectionEntry(translationService.getTranslation(AppConstants.HomeProducer_ArtifactRepository),
                                                () -> placeManager.goTo(GUVNOR_M2REPO),
                                                GUVNOR_M2REPO,
                                                PERSPECTIVE));
        s1.addChild(ModelUtils.makeSectionEntry(translationService.getTranslation(AppConstants.HomeProducer_Administration),
                                                () -> placeManager.goTo(PLANNER_ADMIN),
                                                PLANNER_ADMIN,
                                                PERSPECTIVE));
        model.addSection(s1);

        final SectionEntry s2 = ModelUtils.makeSectionEntry(translationService.getTranslation(AppConstants.HomeProducer_Deploy) + ":");

        s2.addChild(ModelUtils.makeSectionEntry(translationService.getTranslation(AppConstants.HomeProducer_DeployYourArtifacts),
                                                () -> placeManager.goTo(SERVER_MANAGEMENT),
                                                SERVER_MANAGEMENT,
                                                PERSPECTIVE));
        model.addSection(s2);
    }

    @Produces
    public HomeModel getModel() {
        return model;
    }
}
