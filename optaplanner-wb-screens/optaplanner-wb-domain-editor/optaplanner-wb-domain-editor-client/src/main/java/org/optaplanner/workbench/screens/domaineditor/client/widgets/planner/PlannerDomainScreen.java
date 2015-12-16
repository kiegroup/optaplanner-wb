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

package org.optaplanner.workbench.screens.domaineditor.client.widgets.planner;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.google.gwt.user.client.ui.IsWidget;
import org.optaplanner.workbench.screens.domaineditor.client.resources.i18n.DomainEditorConstants;
import org.uberfire.client.annotations.WorkbenchPartTitle;
import org.uberfire.client.annotations.WorkbenchPartView;
import org.uberfire.client.annotations.WorkbenchScreen;

@ApplicationScoped
@WorkbenchScreen( identifier = "PlannerDomainScreen")
public class PlannerDomainScreen {

    PlannerDomainScreenView view;

    public PlannerDomainScreen() {
    }

    @Inject
    public PlannerDomainScreen( PlannerDomainScreenView view ) {
        this.view = view;
    }

    @WorkbenchPartTitle
    public String getTitle() {
        return DomainEditorConstants.INSTANCE.planner_domain_screen_name();
    }

    @WorkbenchPartView
    public IsWidget getView() {
        return view;
    }

}
