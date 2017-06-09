/*
 * Copyright 2017 Red Hat, Inc. and/or its affiliates.
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
package org.optaplanner.workbench.screens.solver.client.editor;

import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import com.google.gwt.core.client.GWT;
import org.jboss.errai.common.client.api.IsElement;
import org.jboss.errai.common.client.dom.HTMLElement;
import org.optaplanner.core.config.localsearch.LocalSearchType;
import org.optaplanner.workbench.screens.solver.client.resources.i18n.SolverEditorLookupConstants;
import org.optaplanner.workbench.screens.solver.model.LocalSearchPhaseConfigModel;
import org.uberfire.commons.data.Pair;

@Dependent
public class LocalSearchForm implements IsElement {

    private final LocalSearchFormView view;
    private LocalSearchPhaseConfigModel model;

    private PhaseConfigForm phaseConfigForm;

    private SolverEditorLookupConstants solverEditorLookupConstants = GWT.create(SolverEditorLookupConstants.class);

    @Inject
    public LocalSearchForm(final LocalSearchFormView view) {
        this.view = view;
        view.setPresenter(this);
        initLocalSearchTypeSelectOptions();
    }

    private void initLocalSearchTypeSelectOptions() {
        List<Pair<String, String>> localSearchTypeOptions = new ArrayList<>();
        for (LocalSearchType localSearchType : LocalSearchType.values()) {
            // TODO Remove once PLANNER-780 is resolved
            if (LocalSearchType.SIMULATED_ANNEALING == localSearchType) {
                continue;
            }

            Pair<String, String> option = new Pair<>(solverEditorLookupConstants.getString(localSearchType.name()),
                                                     localSearchType.name());
            localSearchTypeOptions.add(option);
        }
        view.initLocalSearchTypeSelectOptions(localSearchTypeOptions);
    }

    public void setPhaseConfigForm(PhaseConfigForm phaseConfigForm) {
        this.phaseConfigForm = phaseConfigForm;
    }

    public void onLocalSearchTypeSelected(final String localSearchType) {
        model.setLocalSearchType(LocalSearchType.valueOf(localSearchType));
    }

    public void onLocalSearchRemoved() {
        phaseConfigForm.removeLocalSearch(this);
    }

    public LocalSearchPhaseConfigModel getModel() {
        return model;
    }

    public void setModel(LocalSearchPhaseConfigModel model) {
        this.model = model;

        if (model.getLocalSearchType() == null) {
            model.setLocalSearchType(LocalSearchType.LATE_ACCEPTANCE);
        }

        view.setSelectedLocalSearchType(model.getLocalSearchType().name());
    }

    @Override
    public HTMLElement getElement() {
        return view.getElement();
    }
}
