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

import java.util.List;

import com.google.gwtmockito.GwtMockitoTestRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.optaplanner.workbench.screens.solver.model.ConstructionHeuristicPhaseConfigModel;
import org.optaplanner.workbench.screens.solver.model.ConstructionHeuristicTypeModel;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(GwtMockitoTestRunner.class)
public class ConstructionHeuristicFormTest {

    @Mock
    private ConstructionHeuristicFormView view;

    @Mock
    private ConstructionHeuristicPhaseConfigModel model;

    @Mock
    private PhaseConfigForm phaseConfigForm;

    private ConstructionHeuristicForm constructionHeuristicForm;

    @Before
    public void setUp() {
        constructionHeuristicForm = new ConstructionHeuristicForm( view );
        constructionHeuristicForm.setPhaseConfigForm( phaseConfigForm );
    }

    @Test
    public void initConstructionHeuristicForm() {
        verify( view ).setPresenter( constructionHeuristicForm );

        ArgumentCaptor<List> constructionHeuristicTypeSelectOptionsCaptor = ArgumentCaptor.forClass( List.class );
        verify( view ).initConstructionHeuristicTypeSelectOptions( constructionHeuristicTypeSelectOptionsCaptor.capture() );

        List value = constructionHeuristicTypeSelectOptionsCaptor.getValue();
        assertEquals( ConstructionHeuristicTypeModel.values().length, value.size() );
    }

    @Test
    public void onConstructionHeuristicTypeSelected() {
        when( model.getConstructionHeuristicType() ).thenReturn( ConstructionHeuristicTypeModel.FIRST_FIT );

        constructionHeuristicForm.setModel( model );

        constructionHeuristicForm.onConstructionHeuristicTypeSelected( "FIRST_FIT" );

        verify( model ).setConstructionHeuristicType( ConstructionHeuristicTypeModel.FIRST_FIT );
    }

    @Test
    public void onConstructionHeuristicRemoved() {
        constructionHeuristicForm.onConstructionHeuristicRemoved();

        verify( phaseConfigForm ).removeConstructionHeuristic( constructionHeuristicForm );
    }

    @Test
    public void setModelNullConstructionHeuristicsType() {
        when( model.getConstructionHeuristicType() ).thenReturn( null ).thenReturn( ConstructionHeuristicTypeModel.FIRST_FIT );

        constructionHeuristicForm.setModel( model );

        verify( model ).setConstructionHeuristicType( ConstructionHeuristicTypeModel.FIRST_FIT );
    }

    @Test
    public void setModelNonNullConstructionHeuristicsType() {
        when( model.getConstructionHeuristicType() ).thenReturn( ConstructionHeuristicTypeModel.STRONGEST_FIT );

        constructionHeuristicForm.setModel( model );

        verify( model, times( 0 ) ).setConstructionHeuristicType( ConstructionHeuristicTypeModel.FIRST_FIT );
    }

}
