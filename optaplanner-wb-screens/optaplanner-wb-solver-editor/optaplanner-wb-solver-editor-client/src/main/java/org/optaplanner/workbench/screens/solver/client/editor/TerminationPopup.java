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
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import org.optaplanner.workbench.screens.solver.client.resources.i18n.SolverEditorConstants;
import org.optaplanner.workbench.screens.solver.model.TerminationCompositionStyleModel;
import org.optaplanner.workbench.screens.solver.model.TerminationConfigOption;
import org.uberfire.commons.data.Pair;

@Dependent
public class TerminationPopup implements TerminationPopupView.Presenter {

    private TerminationPopupView view;

    private TerminationPopupView.TerminationPopupHandler popupHandler;
    private TerminationTreeItemContent terminationTreeItemContent;


    public TerminationPopup() {
    }

    public void init( TerminationTreeItemContent terminationTreeItemContent, List<Pair<String, String>> terminationValueList ) {
        view.initTerminationOptionList( terminationValueList );
        view.showTerminationValueLabel( false );
        view.showNumericTerminationValueInput( false );
        view.showTextTerminationValueInput( false );
        view.showListTerminationValueInput( false );
        view.setSelectedTerminationOption( TerminationConfigOption.NESTED );
        this.terminationTreeItemContent = terminationTreeItemContent;
    }

    @Inject
    public TerminationPopup( TerminationPopupView view ) {
        this.view = view;
        view.init( this );
    }

    public void addPopupHandler( TerminationPopupView.TerminationPopupHandler popupHandler ) {
        this.popupHandler = popupHandler;
    }

    @Override
    public void onCreate() {
        if ( popupHandler != null ) {
            popupHandler.onCreate( view.getSelectedTermination(), view.getTerminationValue(), terminationTreeItemContent );
        }
    }

    @Override
    public void onCreateAndContinue() {
        if ( popupHandler != null ) {
            popupHandler.onCreateAndContinue( view.getSelectedTermination(), view.getTerminationValue(), terminationTreeItemContent );
        }
    }

    @Override
    public void onCancel() {
        if ( popupHandler != null ) {
            popupHandler.onCancel();
        }
    }

    @Override
    public void onTerminationChange() {
        switch ( TerminationConfigOption.valueOf( view.getSelectedTermination() ) ) {
            case NESTED: {
                view.showTerminationValueLabel( false );
                view.showNumericTerminationValueInput( false );
                view.showTextTerminationValueInput( false );
                view.showListTerminationValueInput( false );
                break;
            }
            case MILLISECONDS_SPENT_LIMIT:
            case SECONDS_SPENT_LIMIT:
            case MINUTES_SPENT_LIMIT:
            case HOURS_SPENT_LIMIT:
            case DAYS_SPENT_LIMIT:
            case UNIMPROVED_MILLISECONDS_SPENT_LIMIT:
            case UNIMPROVED_SECONDS_SPENT_LIMIT:
            case UNIMPROVED_MINUTES_SPENT_LIMIT:
            case UNIMPROVED_HOURS_SPENT_LIMIT:
            case UNIMPROVED_DAYS_SPENT_LIMIT: {
                view.showTerminationValueLabel( true );
                view.showNumericTerminationValueInput( true );
                view.showTextTerminationValueInput( false );
                view.showListTerminationValueInput( false );
                view.setNumericTerminationValueMaximum( Long.MAX_VALUE );
                break;
            }
            case SCORE_CALCULATION_COUNT_LIMIT:
            case STEP_COUNT_LIMIT:
            case UNIMPROVED_STEP_COUNT_LIMIT: {
                view.showTerminationValueLabel( true );
                view.showNumericTerminationValueInput( true );
                view.showTextTerminationValueInput( false );
                view.showListTerminationValueInput( false );
                view.setNumericTerminationValueMaximum( Integer.MAX_VALUE );
                break;
            }
            case TERMINATION_COMPOSITION_STYLE: {
                List<Pair<String, String>> terminationValueList = new ArrayList<>();
                terminationValueList.add( new Pair<>( SolverEditorConstants.INSTANCE.And(), TerminationCompositionStyleModel.AND.name() ) );
                terminationValueList.add( new Pair<>( SolverEditorConstants.INSTANCE.Or(), TerminationCompositionStyleModel.OR.name() ) );
                view.initTerminationValueList( terminationValueList );
                view.showTerminationValueLabel( true );
                view.showNumericTerminationValueInput( false );
                view.showTextTerminationValueInput( false );
                view.showListTerminationValueInput( true );
                break;
            }
            case BEST_SCORE_FEASIBLE: {
                List<Pair<String, String>> terminationValueList = new ArrayList<>();
                terminationValueList.add( new Pair<>( SolverEditorConstants.INSTANCE.True(), Boolean.TRUE.toString() ) );
                terminationValueList.add( new Pair<>( SolverEditorConstants.INSTANCE.False(), Boolean.FALSE.toString() ) );
                view.initTerminationValueList( terminationValueList );
                view.showTerminationValueLabel( true );
                view.showNumericTerminationValueInput( false );
                view.showTextTerminationValueInput( false );
                view.showListTerminationValueInput( true );
                break;
            }
            case BEST_SCORE_LIMIT: {
                view.showTerminationValueLabel( true );
                view.showNumericTerminationValueInput( false );
                view.showTextTerminationValueInput( true );
                view.showListTerminationValueInput( false );
                break;
            }
        }
        view.clearInput();
        view.setErrorMessage( null );
    }

    @Override
    public void resetSelectedTermination() {
        view.clearInput();
    }

    public void show() {
        view.show();
    }

    public void hide() {
        view.hide();
    }

    public void setErrorMessage( String errorMessage ) {
        view.setErrorMessage( errorMessage );
    }
}
