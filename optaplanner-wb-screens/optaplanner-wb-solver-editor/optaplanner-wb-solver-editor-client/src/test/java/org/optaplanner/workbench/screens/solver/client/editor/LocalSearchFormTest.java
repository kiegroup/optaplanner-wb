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

import java.util.List;

import com.google.gwtmockito.GwtMockitoTestRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.optaplanner.core.config.localsearch.LocalSearchType;
import org.optaplanner.workbench.screens.solver.model.LocalSearchPhaseConfigModel;
import org.uberfire.commons.data.Pair;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(GwtMockitoTestRunner.class)
public class LocalSearchFormTest {

    @Mock
    private LocalSearchFormView view;

    @Mock
    private LocalSearchPhaseConfigModel model;

    @Mock
    private PhaseConfigForm phaseConfigForm;

    private LocalSearchForm localSearchForm;

    @Before
    public void setUp() {
        localSearchForm = new LocalSearchForm(view);
        localSearchForm.setPhaseConfigForm(phaseConfigForm);
    }

    @Test
    public void initLocalSearchForm() {
        verify(view).setPresenter(localSearchForm);

        ArgumentCaptor<List> localSearchTypeSelectOptionsCaptor = ArgumentCaptor.forClass(List.class);
        verify(view).initLocalSearchTypeSelectOptions(localSearchTypeSelectOptionsCaptor.capture());

        List value = localSearchTypeSelectOptionsCaptor.getValue();

        // TODO Value count reduced by 1 as SA is ignored until PLANNER-780 is resolved
        assertEquals(LocalSearchType.values().length - 1,
                     value.size());
    }

    @Test
    public void onLocalSearchTypeSelected() {
        when(model.getLocalSearchType()).thenReturn(LocalSearchType.LATE_ACCEPTANCE);

        localSearchForm.setModel(model);

        localSearchForm.onLocalSearchTypeSelected("LATE_ACCEPTANCE");

        verify(model).setLocalSearchType(LocalSearchType.LATE_ACCEPTANCE);
    }

    @Test
    public void onLocalSearchRemoved() {
        localSearchForm.onLocalSearchRemoved();

        verify(phaseConfigForm).removeLocalSearch(localSearchForm);
    }

    @Test
    public void setModelNullAttribute() {
        when(model.getLocalSearchType()).thenReturn(null).thenReturn(LocalSearchType.LATE_ACCEPTANCE);

        localSearchForm.setModel(model);

        verify(model).setLocalSearchType(LocalSearchType.LATE_ACCEPTANCE);
    }

    @Test
    public void setModelNonNullAttribute() {
        when(model.getLocalSearchType()).thenReturn(LocalSearchType.LATE_ACCEPTANCE);

        localSearchForm.setModel(model);

        verify(model,
               times(0)).setLocalSearchType(LocalSearchType.LATE_ACCEPTANCE);
    }

    // TODO Remove once PLANNER-780 is resolved
    @Test
    public void simulatedAnnealingIsIgnored() {
        ArgumentCaptor<List> listArgumentCaptor = ArgumentCaptor.forClass(List.class);

        verify(view,
               times(1)).initLocalSearchTypeSelectOptions(listArgumentCaptor.capture());

        List<Pair<String, String>> value = listArgumentCaptor.getValue();
        boolean simulatedAnnealingPresent = value.stream().anyMatch(p -> LocalSearchType.SIMULATED_ANNEALING.toString().equals(p.getK2()));

        assertFalse(simulatedAnnealingPresent);
    }
}
