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
package org.optaplanner.workbench.screens.solver.client.editor;

import java.util.ArrayList;
import java.util.List;

import com.google.gwtmockito.GwtMockitoTestRunner;
import org.jboss.errai.ioc.client.api.ManagedInstance;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.optaplanner.workbench.screens.solver.model.ConstructionHeuristicPhaseConfigModel;
import org.optaplanner.workbench.screens.solver.model.LocalSearchPhaseConfigModel;
import org.optaplanner.workbench.screens.solver.model.PhaseConfigModel;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(GwtMockitoTestRunner.class)
public class PhaseConfigFormTest {

    @Mock
    private PhaseConfigFormView view;

    @Mock
    private ManagedInstance<ConstructionHeuristicForm> constructionHeuristicFormProvider;

    @Mock
    private ManagedInstance<LocalSearchForm> localSearchFormProvider;

    @Mock
    private ConstructionHeuristicForm constructionHeuristicForm;

    @Mock
    private LocalSearchForm localSearchForm;

    @Mock
    private List model;

    private PhaseConfigForm phaseConfigForm;

    @Before
    public void setUp() {
        phaseConfigForm = new PhaseConfigForm(view,
                                              constructionHeuristicFormProvider,
                                              localSearchFormProvider);
        phaseConfigForm.setModel(new ArrayList<>());
    }

    @Test
    public void setPresenter() {
        verify(view).setPresenter(phaseConfigForm);
    }

    @Test
    public void setModel() {
        List<PhaseConfigModel> phaseConfigModelList = new ArrayList<>();
        phaseConfigModelList.add(new ConstructionHeuristicPhaseConfigModel());
        phaseConfigModelList.add(new ConstructionHeuristicPhaseConfigModel());

        phaseConfigModelList.add(new LocalSearchPhaseConfigModel());
        phaseConfigModelList.add(new LocalSearchPhaseConfigModel());

        when(constructionHeuristicFormProvider.get()).thenReturn(constructionHeuristicForm);
        when(localSearchFormProvider.get()).thenReturn(localSearchForm);

        phaseConfigForm.setModel(phaseConfigModelList);

        verify(view,
               times(2)).addConstructionHeuristic(any());
        verify(view,
               times(2)).addLocalSearch(any());
    }

    @Test
    public void addConstructionHeuristic() {
        addConstructionHeuristic(true);
    }

    @Test
    public void addConstructionHeuristicExisting() {
        addConstructionHeuristic(false);
    }

    private void addConstructionHeuristic(final boolean newConstructionHeuristic) {
        when(constructionHeuristicFormProvider.get()).thenReturn(constructionHeuristicForm);

        if (newConstructionHeuristic) {
            phaseConfigForm.addConstructionHeuristic();
        } else {
            phaseConfigForm.addConstructionHeuristic(new ConstructionHeuristicPhaseConfigModel());
        }
        verify(view).addConstructionHeuristic(any());
    }

    @Test
    public void removeConstructionHeuristic() {
        phaseConfigForm.removeConstructionHeuristic(constructionHeuristicForm);
        verify(view).removeConstructionHeuristic(constructionHeuristicForm.getElement());
    }

    @Test
    public void addLocalSearch() {
        addLocalSearch(true);
    }

    @Test
    public void addLocalSearchExisting() {
        addLocalSearch(false);
    }

    private void addLocalSearch(final boolean newLocalSearch) {
        when(localSearchFormProvider.get()).thenReturn(localSearchForm);

        if (newLocalSearch) {
            phaseConfigForm.addLocalSearch();
        } else {
            phaseConfigForm.addLocalSearch(new LocalSearchPhaseConfigModel());
        }
        verify(view).addLocalSearch(any());
    }

    @Test
    public void removeLocalSearch() {
        phaseConfigForm.removeLocalSearch(localSearchForm);
        verify(view).removeLocalSearch(localSearchForm.getElement());
    }

    @Test
    public void displayEmptyPhaseConfigurationLabelConstructionHeuristic() {
        when(constructionHeuristicFormProvider.get()).thenReturn(constructionHeuristicForm);
        phaseConfigForm.addConstructionHeuristic();
        verify(view).displayEmptyPhaseConfigurationLabel(false);

        phaseConfigForm.removeConstructionHeuristic(constructionHeuristicForm);
        verify(view).displayEmptyPhaseConfigurationLabel(true);
    }

    @Test
    public void displayEmptyPhaseConfigurationLabelLocalSearch() {
        when(localSearchFormProvider.get()).thenReturn(localSearchForm);
        phaseConfigForm.addLocalSearch();
        verify(view).displayEmptyPhaseConfigurationLabel(false);

        phaseConfigForm.removeLocalSearch(localSearchForm);
        verify(view).displayEmptyPhaseConfigurationLabel(true);
    }
}
