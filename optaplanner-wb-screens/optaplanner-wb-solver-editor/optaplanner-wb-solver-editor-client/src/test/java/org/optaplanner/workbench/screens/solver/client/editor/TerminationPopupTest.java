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

import com.google.gwtmockito.GwtMockitoTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.optaplanner.workbench.screens.solver.model.TerminationConfigOption;
import org.uberfire.commons.data.Pair;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

@RunWith(GwtMockitoTestRunner.class)
public class TerminationPopupTest {

    @Mock
    private TerminationPopupView view;

    @Mock
    private TerminationPopupView.TerminationPopupHandler handler;

    @Mock
    private TerminationTreeItemContent content;

    @Test
    public void testInit() {
        TerminationPopup terminationPopup = new TerminationPopup( view );
        terminationPopup.init( content, new ArrayList<>() );
        terminationPopup.show();

        verify( view, times( 1 ) ).init( terminationPopup );
        verify( view, times( 1 ) ).show();

        verify( view, times( 1 ) ).initTerminationOptionList( anyList() );
        verify( view, times( 1 ) ).showTerminationValueLabel( false );
        verify( view, times( 1 ) ).showNumericTerminationValueInput( false );
        verify( view, times( 1 ) ).showTextTerminationValueInput( false );
        verify( view, times( 1 ) ).showListTerminationValueInput( false );
    }

    @Test
    public void testCreateNewTermination() {
        createNewTermination( false );
    }

    @Test
    public void testCreateNewTerminationContinue() {
        createNewTermination( true );
    }

    public void createNewTermination( boolean createAndContinue ) {
        TerminationPopup terminationPopup = new TerminationPopup( view );
        terminationPopup.init( content, new ArrayList<>() );

        terminationPopup.addPopupHandler( handler );


        when( view.getSelectedTermination() ).thenReturn( "terminationType" );
        Pair<String, String> terminationValue = new Pair<>( "terminationValue", "terminationValue" );
        when( view.getTerminationValue() ).thenReturn( terminationValue );

        if ( createAndContinue ) {
            terminationPopup.onCreateAndContinue();
        } else {
            terminationPopup.onCreate();
        }

        verify( view, times( 1 ) ).getSelectedTermination();
        verify( view, times( 1 ) ).getTerminationValue();

        if ( createAndContinue ) {
            verify( handler, times( 1 ) ).onCreateAndContinue( "terminationType", terminationValue, content );
        } else {
            verify( handler, times( 1 ) ).onCreate( "terminationType", terminationValue, content );
        }
    }


    private void changeSelectedTerminationType( boolean displayTerminationValueLabel,
                                                boolean displayNumericTerminationValue,
                                                boolean displayTerminationValueInput,
                                                boolean displayTerminationCompositionStyleList,
                                                boolean expectInitTerminationList,
                                                String selectedTerminationType ) {
        TerminationPopup terminationPopup = new TerminationPopup( view );

        when( view.getSelectedTermination() ).thenReturn( selectedTerminationType );

        terminationPopup.onTerminationChange();

        verify( view, times( 1 ) ).showTerminationValueLabel( displayTerminationValueLabel );
        verify( view, times( 1 ) ).showNumericTerminationValueInput( displayNumericTerminationValue );
        verify( view, times( 1 ) ).showTextTerminationValueInput( displayTerminationValueInput );
        verify( view, times( 1 ) ).showListTerminationValueInput( displayTerminationCompositionStyleList );
        verify( view, times( expectInitTerminationList ? 1 : 0 ) ).initTerminationValueList( anyList() );
    }

    @Test
    public void testChangeSelectedTerminationTypeComposite() {
        changeSelectedTerminationType( false, false, false, false, false, TerminationConfigOption.NESTED.name() );
    }

    @Test
    public void testChangeSelectedTerminationTypeNumeric() {
        changeSelectedTerminationType( true, true, false, false, false, TerminationConfigOption.MILLISECONDS_SPENT_LIMIT.name() );
    }

    @Test
    public void testChangeSelectedTerminationTypeCompositionStype() {
        changeSelectedTerminationType( true, false, false, true, true, TerminationConfigOption.TERMINATION_COMPOSITION_STYLE.name() );
    }

    @Test
    public void testChangeSelectedTerminationTypeBestScoreFeasible() {
        changeSelectedTerminationType( true, false, false, true, true, TerminationConfigOption.BEST_SCORE_FEASIBLE.name() );
    }

    @Test
    public void testChangeSelectedTerminationTypeBestScoreLimit() {
        changeSelectedTerminationType( true, false, true, false, false, TerminationConfigOption.BEST_SCORE_LIMIT.name() );
    }

    @Test
    public void testErrorMessage() {
        TerminationPopup terminationPopup = new TerminationPopup( view );
        terminationPopup.init( content, new ArrayList<>() );
        terminationPopup.setErrorMessage( "message" );

        verify( view ).setErrorMessage( "message" );
    }

}
