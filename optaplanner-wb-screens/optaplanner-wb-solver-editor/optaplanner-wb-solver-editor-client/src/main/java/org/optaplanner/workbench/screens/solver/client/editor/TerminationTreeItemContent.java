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

import java.util.HashMap;
import java.util.Map;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.TreeItem;
import org.jboss.errai.common.client.api.IsElement;
import org.jboss.errai.common.client.dom.HTMLElement;
import org.optaplanner.workbench.screens.solver.client.resources.i18n.SolverEditorConstants;
import org.optaplanner.workbench.screens.solver.model.TerminationCompositionStyleModel;
import org.optaplanner.workbench.screens.solver.model.TerminationConfigModel;
import org.optaplanner.workbench.screens.solver.model.TerminationConfigOption;

import static org.optaplanner.workbench.screens.solver.model.TerminationConfigOption.*;

@Dependent
public class TerminationTreeItemContent implements IsElement {

    public static final SolverEditorConstants I18N_CONSTANTS = GWT.create( SolverEditorConstants.class );

    public static final long MINUTES_SPENT_DEFAULT_VALUE = 5l;

    public static final long UNIMPROVED_MINUTES_SPENT_DEFAULT_VALUE = 5l;

    private TerminationConfigForm terminationConfigForm;

    private TreeItem treeItem;
    private TerminationConfigOption terminationConfigOption;


    private TerminationConfigModel model;
    private TerminationTreeItemContentView view;

    private Map<TerminationConfigOption, TerminationManager> terminationManagerMap = new HashMap<>();

    @Inject
    public TerminationTreeItemContent( final TerminationTreeItemContentView view ) {
        this.view = view;
        view.setPresenter( this );
        initTerminationManagerMap();
    }

    public TreeItem getTreeItem() {
        return treeItem;
    }

    public TerminationConfigModel getModel() {
        return model;
    }

    public TerminationConfigOption getTerminationConfigOption() {
        return terminationConfigOption;
    }

    public void removeDropDownOption( TerminationConfigOption terminationConfigOption ) {
        if ( terminationConfigOption != NESTED ) {
            view.removeDropDownOption( terminationConfigOption );
        }
    }

    public TerminationTreeItemContentView getView() {
        return view;
    }

    public void setModel( TerminationConfigModel model ) {
        this.model = model;
    }

    public void setTerminationConfigOption( TerminationConfigOption terminationConfigOption ) {
        this.terminationConfigOption = terminationConfigOption;
        view.setNestedTreeItem( terminationConfigOption == NESTED );
        if ( terminationConfigOption == NESTED ) {
            view.setDropDownHelpContent( I18N_CONSTANTS.TerminationCompositionStyleHelp() );
        }
        setLabelStrings( terminationConfigOption );
        hideViewInputs( terminationConfigOption );
    }

    private void setLabelStrings( TerminationConfigOption terminationConfigOption ) {
        getTerminationManager( terminationConfigOption ).setLabelStrings();
    }

    private TerminationManager getTerminationManager( TerminationConfigOption terminationConfigOption ) {
        TerminationManager terminationManager = terminationManagerMap.get( terminationConfigOption );
        if ( terminationManager == null ) {
            throw new IllegalStateException( "TerminationManager for terminationConfigOption" + terminationConfigOption + " is not defined." );
        }
        return terminationManager;
    }

    private void hideViewInputs( TerminationConfigOption terminationConfigOption ) {
        for ( TerminationConfigOption option : terminationManagerMap.keySet() ) {
            if ( terminationConfigOption == option ) {
                continue;
            }
            getTerminationManager( option ).hideViewInputs();
        }
    }

    public void setTreeItem( TreeItem treeItem ) {
        this.treeItem = treeItem;
        view.setRoot( treeItem.getParentItem() == null );
    }

    public void onTerminationTypeSelected( String terminationType ) {
        terminationConfigForm.addNewTerminationType( terminationType, this );
    }

    public void setTerminationConfigForm( TerminationConfigForm terminationConfigForm ) {
        this.terminationConfigForm = terminationConfigForm;
    }

    public void removeTreeItem() {
        TerminationTreeItemContent parent = ( treeItem.getParentItem() == null ? this : (TerminationTreeItemContent) treeItem.getParentItem().getUserObject() );
        getTerminationManager( terminationConfigOption ).removeModelValue();
        parent.getView().addDropDownOption( terminationConfigOption );
        treeItem.remove();
        terminationConfigForm.displayEmptyTreeLabel( terminationConfigForm.getRootTreeItem().getChildCount() == 0 );
        terminationConfigForm.destroyTerminationTreeItemContent( this );
    }

    public void onDaysSpentChange( Long value ) {
        model.setDaysSpentLimit( value );
    }

    public void onHoursSpentChange( Long value ) {
        model.setHoursSpentLimit( value );
    }

    public void onMinutesSpentChange( Long value ) {
        model.setMinutesSpentLimit( value );
    }

    public void onSecondsSpentChange( Long value ) {
        model.setSecondsSpentLimit( value );
    }

    public void onMillisecondsSpentChange( Long value ) {
        model.setMillisecondsSpentLimit( value );
    }

    public void onUnimprovedDaysSpentChange( Long value ) {
        model.setUnimprovedDaysSpentLimit( value );
    }

    public void onUnimprovedHoursSpentChange( Long value ) {
        model.setUnimprovedHoursSpentLimit( value );
    }

    public void onUnimprovedMinutesSpentChange( Long value ) {
        model.setUnimprovedMinutesSpentLimit( value );
    }

    public void onUnimprovedSecondsSpentChange( Long value ) {
        model.setUnimprovedSecondsSpentLimit( value );
    }

    public void onUnimprovedMillisecondsSpentChange( Long value ) {
        model.setUnimprovedMillisecondsSpentLimit( value );
    }

    public void onStepCountLimitChange( Integer value ) {
        model.setStepCountLimit( value );
    }

    public void onUnimprovedStepCountLimitChange( Integer value ) {
        model.setUnimprovedStepCountLimit( value );
    }

    public void onScoreCalculationLimitChange( Long value ) {
        model.setScoreCalculationCountLimit( value );
    }

    public void onFeasibilityChange( Boolean value ) {
        model.setBestScoreFeasible( value );
    }

    public void onBestScoreLimitChange( String value ) {
        model.setBestScoreLimit( value );
    }

    public void onTerminationCompositionStyleChange( TerminationCompositionStyleModel value ) {
        model.setTerminationCompositionStyle( value );
    }

    public void setExistingValue( Object value, TerminationConfigOption terminationConfigOption ) {
        TerminationManager operation = terminationManagerMap.get( terminationConfigOption );
        if ( operation != null ) {
            operation.setExistingValue( value );
        }
    }

    public void setNewValue( TerminationConfigOption terminationConfigOption ) {
        TerminationManager operation = terminationManagerMap.get( terminationConfigOption );
        if ( operation != null ) {
            operation.setNewValue();
        }
    }

    @Override
    public HTMLElement getElement() {
        return view.getElement();
    }

    private interface TerminationManager {
        void hideViewInputs();

        void removeModelValue();

        void setExistingValue( Object value );

        void setNewValue();

        void setLabelStrings();
    }

    private void initTerminationManagerMap() {
        terminationManagerMap.put( MILLISECONDS_SPENT_LIMIT, new TerminationManager() {
            @Override
            public void hideViewInputs() {
                view.hideTimeSpentInput();
            }

            @Override
            public void removeModelValue() {
                model.setMillisecondsSpentLimit( null );
                model.setSecondsSpentLimit( null );
                model.setMinutesSpentLimit( null );
                model.setHoursSpentLimit( null );
                model.setDaysSpentLimit( null );
            }

            @Override
            public void setExistingValue( Object value ) {
                value = terminationConfigForm.getTerminationValue( model, MILLISECONDS_SPENT_LIMIT );
                view.setMillisecondsSpent( value == null ? 0l : Long.valueOf( value.toString() ) );
                model.setMillisecondsSpentLimit( value == null ? 0l : Long.valueOf( value.toString() ) );
                value = terminationConfigForm.getTerminationValue( model, SECONDS_SPENT_LIMIT );
                view.setSecondsSpent( value == null ? 0l : Long.valueOf( value.toString() ) );
                model.setSecondsSpentLimit( value == null ? 0l : Long.valueOf( value.toString() ) );
                value = terminationConfigForm.getTerminationValue( model, MINUTES_SPENT_LIMIT );
                view.setMinutesSpent( value == null ? 0l : Long.valueOf( value.toString() ) );
                model.setMinutesSpentLimit( value == null ? 0l : Long.valueOf( value.toString() ) );
                value = terminationConfigForm.getTerminationValue( model, HOURS_SPENT_LIMIT );
                view.setHoursSpent( value == null ? 0l : Long.valueOf( value.toString() ) );
                model.setHoursSpentLimit( value == null ? 0l : Long.valueOf( value.toString() ) );
                value = terminationConfigForm.getTerminationValue( model, DAYS_SPENT_LIMIT );
                view.setDaysSpent( value == null ? 0l : Long.valueOf( value.toString() ) );
                model.setDaysSpentLimit( value == null ? 0l : Long.valueOf( value.toString() ) );
            }

            @Override
            public void setNewValue() {
                view.setMillisecondsSpent( 0l );
                model.setMillisecondsSpentLimit( 0l );
                view.setSecondsSpent( 0l );
                model.setSecondsSpentLimit( 0l );
                view.setMinutesSpent( MINUTES_SPENT_DEFAULT_VALUE );
                model.setMinutesSpentLimit( MINUTES_SPENT_DEFAULT_VALUE );
                view.setHoursSpent( 0l );
                model.setHoursSpentLimit( 0l );
                view.setDaysSpent( 0l );
                model.setDaysSpentLimit( 0l );
            }

            @Override
            public void setLabelStrings() {
                view.setFormLabelText( I18N_CONSTANTS.TimeSpent() );
                view.setFormLabelHelpContent( I18N_CONSTANTS.TimeSpentHelp() );
            }
        } );
        terminationManagerMap.put( UNIMPROVED_MILLISECONDS_SPENT_LIMIT, new TerminationManager() {
            @Override
            public void hideViewInputs() {
                view.hideUnimprovedTimeSpentInput();
            }

            @Override
            public void removeModelValue() {
                model.setUnimprovedMillisecondsSpentLimit( null );
                model.setUnimprovedSecondsSpentLimit( null );
                model.setUnimprovedMinutesSpentLimit( null );
                model.setUnimprovedHoursSpentLimit( null );
                model.setUnimprovedDaysSpentLimit( null );
            }

            @Override
            public void setExistingValue( Object value ) {
                value = terminationConfigForm.getTerminationValue( model, UNIMPROVED_MILLISECONDS_SPENT_LIMIT );
                view.setUnimprovedMillisecondsSpent( value == null ? 0l : Long.valueOf( value.toString() ) );
                model.setUnimprovedMinutesSpentLimit( value == null ? 0l : Long.valueOf( value.toString() ) );
                value = terminationConfigForm.getTerminationValue( model, UNIMPROVED_SECONDS_SPENT_LIMIT );
                view.setUnimprovedSecondsSpent( value == null ? 0l : Long.valueOf( value.toString() ) );
                model.setUnimprovedSecondsSpentLimit( value == null ? 0l : Long.valueOf( value.toString() ) );
                value = terminationConfigForm.getTerminationValue( model, UNIMPROVED_MINUTES_SPENT_LIMIT );
                view.setUnimprovedMinutesSpent( value == null ? 0l : Long.valueOf( value.toString() ) );
                model.setUnimprovedMinutesSpentLimit( value == null ? 0l : Long.valueOf( value.toString() ) );
                value = terminationConfigForm.getTerminationValue( model, UNIMPROVED_HOURS_SPENT_LIMIT );
                view.setUnimprovedHoursSpent( value == null ? 0l : Long.valueOf( value.toString() ) );
                model.setUnimprovedHoursSpentLimit( value == null ? 0l : Long.valueOf( value.toString() ) );
                value = terminationConfigForm.getTerminationValue( model, UNIMPROVED_DAYS_SPENT_LIMIT );
                view.setUnimprovedDaysSpent( value == null ? 0l : Long.valueOf( value.toString() ) );
                model.setUnimprovedDaysSpentLimit( value == null ? 0l : Long.valueOf( value.toString() ) );
            }

            @Override
            public void setNewValue() {
                view.setUnimprovedMillisecondsSpent( 0l );
                model.setUnimprovedMillisecondsSpentLimit( 0l );
                view.setUnimprovedSecondsSpent( 0l );
                model.setUnimprovedSecondsSpentLimit( 0l );
                view.setUnimprovedMinutesSpent( UNIMPROVED_MINUTES_SPENT_DEFAULT_VALUE );
                model.setUnimprovedMinutesSpentLimit( UNIMPROVED_MINUTES_SPENT_DEFAULT_VALUE );
                view.setUnimprovedHoursSpent( 0l );
                model.setUnimprovedHoursSpentLimit( 0l );
                view.setUnimprovedDaysSpent( 0l );
                model.setUnimprovedDaysSpentLimit( 0l );
            }

            @Override
            public void setLabelStrings() {
                view.setFormLabelText( I18N_CONSTANTS.UnimprovedTimeSpent() );
                view.setFormLabelHelpContent( I18N_CONSTANTS.UnimprovedTimeSpentHelp() );
            }
        } );
        terminationManagerMap.put( BEST_SCORE_LIMIT, new TerminationManager() {
            @Override
            public void hideViewInputs() {
                view.hideBestScoreLimitInput();
            }

            @Override
            public void removeModelValue() {
                model.setBestScoreLimit( null );
            }

            @Override
            public void setExistingValue( Object value ) {
                view.setBestScoreLimit( value == null ? null : value.toString() );
            }

            @Override
            public void setLabelStrings() {
                view.setFormLabelText( I18N_CONSTANTS.BestScoreLimit() );
                view.setFormLabelHelpContent( I18N_CONSTANTS.BestScoreLimitHelp() );
            }

            @Override
            public void setNewValue() {
            }
        } );
        terminationManagerMap.put( BEST_SCORE_FEASIBLE, new TerminationManager() {
            @Override
            public void hideViewInputs() {
                view.hideBestScoreFeasibleInput();
            }

            @Override
            public void removeModelValue() {
                model.setBestScoreFeasible( null );
            }

            @Override
            public void setExistingValue( Object value ) {
                view.setBestScoreFeasible( value == null ? null : Boolean.valueOf( value.toString() ) );
            }

            @Override
            public void setNewValue() {
                view.setBestScoreFeasible( true );
                model.setBestScoreFeasible( true );
            }

            @Override
            public void setLabelStrings() {
                view.setFormLabelText( I18N_CONSTANTS.BestScoreFeasible() );
                view.setFormLabelHelpContent( I18N_CONSTANTS.BestScoreFeasibleHelp() );
            }
        } );
        terminationManagerMap.put( STEP_COUNT_LIMIT, new TerminationManager() {
            @Override
            public void hideViewInputs() {
                view.hideStepCountLimitInput();
            }

            @Override
            public void removeModelValue() {
                model.setStepCountLimit( null );
            }

            @Override
            public void setExistingValue( Object value ) {
                view.setStepCountLimit( value == null ? 0 : Integer.valueOf( value.toString() ) );
                model.setStepCountLimit( value == null ? 0 : Integer.valueOf( value.toString() ) );
            }

            @Override
            public void setLabelStrings() {
                view.setFormLabelText( I18N_CONSTANTS.StepCountLimit() );
                view.setFormLabelHelpContent( I18N_CONSTANTS.StepCountLimitHelp() );
            }

            @Override
            public void setNewValue() {
                view.setStepCountLimit( 0 );
                model.setStepCountLimit( 0 );
            }
        } );
        terminationManagerMap.put( UNIMPROVED_STEP_COUNT_LIMIT, new TerminationManager() {
            @Override
            public void hideViewInputs() {
                view.hideUnimprovedStepCountLimitInput();
            }

            @Override
            public void removeModelValue() {
                model.setUnimprovedStepCountLimit( null );
            }

            @Override
            public void setExistingValue( Object value ) {
                view.setUnimprovedStepCountLimit( value == null ? 0 : Integer.valueOf( value.toString() ) );
                model.setUnimprovedStepCountLimit( value == null ? 0 : Integer.valueOf( value.toString() ) );
            }

            @Override
            public void setLabelStrings() {
                view.setFormLabelText( I18N_CONSTANTS.UnimprovedStepCountLimit() );
                view.setFormLabelHelpContent( I18N_CONSTANTS.UnimprovedStepCountLimitHelp() );
            }

            @Override
            public void setNewValue() {
                view.setUnimprovedStepCountLimit( 0 );
                model.setUnimprovedStepCountLimit( 0 );
            }
        } );
        terminationManagerMap.put( SCORE_CALCULATION_COUNT_LIMIT, new TerminationManager() {
            @Override
            public void hideViewInputs() {
                view.hideScoreCalculationCountLimitInput();
            }

            @Override
            public void removeModelValue() {
                model.setScoreCalculationCountLimit( null );
            }

            @Override
            public void setExistingValue( Object value ) {
                view.setScoreCalculationCountLimit( value == null ? 0 : Long.valueOf( value.toString() ) );
                model.setScoreCalculationCountLimit( value == null ? 0 : Long.valueOf( value.toString() ) );
            }

            @Override
            public void setLabelStrings() {
                view.setFormLabelText( I18N_CONSTANTS.ScoreCalculationCountLimit() );
                view.setFormLabelHelpContent( I18N_CONSTANTS.ScoreCalculationCountLimitHelp() );
            }

            @Override
            public void setNewValue() {
                view.setScoreCalculationCountLimit( 0l );
                model.setScoreCalculationCountLimit( 0l );
            }
        } );
        terminationManagerMap.put( TERMINATION_COMPOSITION_STYLE, new TerminationManager() {
            @Override
            public void hideViewInputs() {
            }

            @Override
            public void removeModelValue() {
                model.setTerminationCompositionStyle( null );
            }

            @Override
            public void setExistingValue( Object value ) {
                view.setTerminationCompositionStyle( value == null ? null : TerminationCompositionStyleModel.valueOf( value.toString() ) );
            }

            @Override
            public void setLabelStrings() {
            }

            @Override
            public void setNewValue() {
            }
        } );
        terminationManagerMap.put( NESTED, new TerminationManager() {
            @Override
            public void hideViewInputs() {
            }

            @Override
            public void removeModelValue() {
                TerminationTreeItemContent parent = ( treeItem.getParentItem() == null ? TerminationTreeItemContent.this : (TerminationTreeItemContent) treeItem.getParentItem().getUserObject() );
                parent.getModel().getTerminationConfigList().remove( model );
                if ( parent.getModel().getTerminationConfigList().isEmpty() ) {
                    parent.getModel().setTerminationConfigList( null );
                }
            }

            @Override
            public void setExistingValue( Object value ) {
            }

            @Override
            public void setLabelStrings() {
            }

            @Override
            public void setNewValue() {
            }
        } );
    }
}
