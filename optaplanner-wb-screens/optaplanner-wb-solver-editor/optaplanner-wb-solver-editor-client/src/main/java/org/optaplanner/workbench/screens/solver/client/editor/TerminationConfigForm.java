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

package org.optaplanner.workbench.screens.solver.client.editor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.Widget;
import org.optaplanner.workbench.screens.solver.client.resources.i18n.SolverEditorConstants;
import org.optaplanner.workbench.screens.solver.model.TerminationCompositionStyleModel;
import org.optaplanner.workbench.screens.solver.model.TerminationConfigModel;
import org.optaplanner.workbench.screens.solver.model.TerminationConfigOption;
import org.uberfire.commons.data.Pair;

@Dependent
public class TerminationConfigForm
        implements IsWidget {

    private TerminationConfigModel model;

    private TerminationConfigFormView view;

    private TerminationPopup terminationPopup;

    private Map<TerminationConfigOption, TerminationModelManager> modelManagerMap = new HashMap<>();

    @Inject
    public TerminationConfigForm( TerminationConfigFormView view, TerminationPopup terminationPopup ) {
        this.view = view;
        this.terminationPopup = terminationPopup;
        initModelManagerMap();
    }

    @PostConstruct
    public void init() {
        terminationPopup.addPopupHandler( new TerminationPopupView.TerminationPopupHandler() {
            @Override
            public void onCreate( String terminationType, Pair<String, String> value, TerminationTreeItemContent treeItemContent ) {
                handleNewTermination( terminationType, value, treeItemContent, true );
            }

            @Override
            public void onCreateAndContinue( String terminationType, Pair<String, String> value, TerminationTreeItemContent treeItemContent ) {
                handleNewTermination( terminationType, value, treeItemContent, false );
            }

            @Override
            public void onCancel() {
                terminationPopup.resetSelectedTermination();
                terminationPopup.setErrorMessage( null );
                terminationPopup.hide();
            }
        } );
    }

    private void handleNewTermination( String terminationType, Pair<String, String> value, TerminationTreeItemContent treeItemContent, boolean creationFinished ) {
        TerminationConfigOption terminationConfigOption = TerminationConfigOption.valueOf( terminationType );
        if ( !TerminationConfigOption.NESTED.equals( terminationConfigOption ) && value.getK2().isEmpty() ) {
            terminationPopup.setErrorMessage( SolverEditorConstants.INSTANCE.terminationValueEmpty() );
        } else {
            TreeItem nestedTreeItem = new TreeItem();
            nestedTreeItem.setState( true );
            treeItemContent.getTreeItem().addItem( nestedTreeItem );
            treeItemContent.getTreeItem().setState( true );

            TerminationConfigModel terminationConfigModel = getModelManager( terminationConfigOption ).setTerminationValue( treeItemContent.getModel(), null, value.getK2() );
            TerminationTreeItemContent terminationTreeItemContent = new TerminationTreeItemContent( TerminationConfigForm.this, nestedTreeItem, terminationConfigOption, terminationConfigModel, value.getK1() );
            nestedTreeItem.setUserObject( terminationTreeItemContent );
            nestedTreeItem.setWidget( terminationTreeItemContent );

            terminationPopup.resetSelectedTermination();
            terminationPopup.setErrorMessage( null );
            if ( creationFinished ) {
                terminationPopup.hide();
            } else {
                terminationPopup.init( treeItemContent, getAssignableTerminationConfigOptions( treeItemContent.getModel() ) );
            }
        }
    }

    public List<Pair<String, String>> getAssignableTerminationConfigOptions( TerminationConfigModel terminationConfigModel ) {
        List<Pair<String, String>> uninitializedTerminationConfigOptions = new ArrayList<>();
        for ( TerminationConfigOption terminationConfigOption : TerminationConfigOption.values() ) {
            Pair<String, Object> terminationValue = getModelManager( terminationConfigOption ).getTerminationValue( terminationConfigModel );
            if ( terminationValue.getK2() == null || terminationConfigOption.equals( TerminationConfigOption.NESTED ) ) {
                uninitializedTerminationConfigOptions.add( new Pair<>( getModelManager( terminationConfigOption ).getLocalizedTerminationConfigOption(), terminationConfigOption.name() ) );
            }
        }
        return uninitializedTerminationConfigOptions;
    }

    public void setModel( TerminationConfigModel terminationConfigModel ) {
        model = terminationConfigModel;
        initTerminationTree( model );
    }

    private void initTerminationTree( TerminationConfigModel terminationConfigModel ) {
        TreeItem rootTreeItem = new TreeItem();
        initTerminationTreeItemHierarchy( terminationConfigModel, rootTreeItem );
        view.initTree( rootTreeItem );
        rootTreeItem.setState( true );
    }

    private void initTerminationTreeItemHierarchy( TerminationConfigModel terminationConfigModel, TreeItem treeItem ) {
        TerminationTreeItemContent terminationTreeItemContent = new TerminationTreeItemContent( this, treeItem, TerminationConfigOption.NESTED, terminationConfigModel, null );
        treeItem.setUserObject( terminationTreeItemContent );
        treeItem.setWidget( terminationTreeItemContent );
        for ( TerminationConfigOption terminationConfigOption : TerminationConfigOption.values() ) {
            Pair<String, Object> terminationValue = getModelManager( terminationConfigOption ).getTerminationValue( terminationConfigModel );
            if ( terminationValue.getK2() != null && !TerminationConfigOption.NESTED.equals( terminationConfigOption )) {
                TreeItem nestedTreeItem = new TreeItem();
                treeItem.addItem( nestedTreeItem );
                TerminationTreeItemContent nestedTerminationTreeItemContent = new TerminationTreeItemContent( this, nestedTreeItem, terminationConfigOption, terminationConfigModel, terminationValue.getK1() );
                nestedTreeItem.setUserObject( nestedTerminationTreeItemContent );
                nestedTreeItem.setWidget( nestedTerminationTreeItemContent );
                nestedTreeItem.setState( true );
            }
        }
        if ( terminationConfigModel.getTerminationConfigList() != null ) {
            for ( TerminationConfigModel nestedTerminationConfigModel : terminationConfigModel.getTerminationConfigList() ) {
                TreeItem nestedTreeItem = new TreeItem();
                treeItem.addItem( nestedTreeItem );
                initTerminationTreeItemHierarchy( nestedTerminationConfigModel, nestedTreeItem );
                nestedTreeItem.setState( true );
            }
        }
    }

    public interface TerminationModelManager {

        TerminationConfigModel setTerminationValue( TerminationConfigModel terminationConfigModel, TerminationConfigModel parentTerminationConfigModel, String value );

        Pair<String, Object> getTerminationValue( TerminationConfigModel terminationConfigModel );

        String getLocalizedTerminationConfigOption();
    }

    private void initModelManagerMap() {
        for ( TerminationConfigOption terminationConfigOption : TerminationConfigOption.values() ) {
            modelManagerMap.put( terminationConfigOption, createModelManager( terminationConfigOption ) );
        }
    }

    public TerminationModelManager getModelManager( TerminationConfigOption terminationConfigOption ) {
        return modelManagerMap.get( terminationConfigOption );
    }

    private TerminationModelManager createModelManager( TerminationConfigOption terminationConfigOption ) {
        switch ( terminationConfigOption ) {
            case NESTED: {
                return new TerminationModelManager() {
                    @Override
                    public TerminationConfigModel setTerminationValue( TerminationConfigModel terminationConfigModel, TerminationConfigModel parentTerminationConfigModel, String value ) {
                        if ( value == null ) {
                            parentTerminationConfigModel.getTerminationConfigList().remove( terminationConfigModel );
                            if ( parentTerminationConfigModel.getTerminationConfigList().isEmpty() ) {
                                parentTerminationConfigModel.setTerminationConfigList( null );
                            }
                            return parentTerminationConfigModel;
                        } else {
                            TerminationConfigModel childTerminationConfigModel = new TerminationConfigModel();
                            if ( terminationConfigModel.getTerminationConfigList() == null ) {
                                terminationConfigModel.setTerminationConfigList( new ArrayList<>( 1 ) );
                            }
                            terminationConfigModel.getTerminationConfigList().add( childTerminationConfigModel );
                            return childTerminationConfigModel;
                        }
                    }

                    @Override
                    public Pair<String, Object> getTerminationValue( TerminationConfigModel terminationConfigModel ) {
                        return new Pair<>( null, terminationConfigModel.getTerminationConfigList() );
                    }

                    @Override
                    public String getLocalizedTerminationConfigOption() {
                        return SolverEditorConstants.INSTANCE.NestedTermination();
                    }
                };
            }
            case TERMINATION_COMPOSITION_STYLE: {
                return new TerminationModelManager() {
                    @Override
                    public TerminationConfigModel setTerminationValue( TerminationConfigModel terminationConfigModel, TerminationConfigModel parentTerminationConfigModel, String value ) {
                        terminationConfigModel.setTerminationCompositionStyle( value == null ? null : TerminationCompositionStyleModel.valueOf( value ) );
                        return terminationConfigModel;
                    }

                    @Override
                    public Pair<String, Object> getTerminationValue( TerminationConfigModel terminationConfigModel ) {
                        TerminationCompositionStyleModel terminationCompositionStyle = terminationConfigModel.getTerminationCompositionStyle();
                        String label = null;
                        if ( TerminationCompositionStyleModel.AND.equals( terminationCompositionStyle ) ) {
                            label = SolverEditorConstants.INSTANCE.And();
                        } else if ( TerminationCompositionStyleModel.OR.equals( terminationCompositionStyle ) ) {
                            label = SolverEditorConstants.INSTANCE.Or();
                        }
                        return new Pair<>( label, terminationCompositionStyle );
                    }

                    @Override
                    public String getLocalizedTerminationConfigOption() {
                        return SolverEditorConstants.INSTANCE.TerminationCompositionStyle();
                    }
                };
            }
            case MILLISECONDS_SPENT_LIMIT: {
                return new TerminationModelManager() {
                    @Override
                    public TerminationConfigModel setTerminationValue( TerminationConfigModel terminationConfigModel, TerminationConfigModel parentTerminationConfigModel, String value ) {
                        terminationConfigModel.setMillisecondsSpentLimit( value == null ? null : Long.valueOf( value ) );
                        return terminationConfigModel;
                    }

                    @Override
                    public Pair<String, Object> getTerminationValue( TerminationConfigModel terminationConfigModel ) {
                        return new Pair<>( safeToString( terminationConfigModel.getMillisecondsSpentLimit() ), terminationConfigModel.getMillisecondsSpentLimit() );
                    }

                    @Override
                    public String getLocalizedTerminationConfigOption() {
                        return SolverEditorConstants.INSTANCE.MillisecondsSpentLimit();
                    }
                };
            }
            case SECONDS_SPENT_LIMIT: {
                return new TerminationModelManager() {
                    @Override
                    public TerminationConfigModel setTerminationValue( TerminationConfigModel terminationConfigModel, TerminationConfigModel parentTerminationConfigModel, String value ) {
                        terminationConfigModel.setSecondsSpentLimit( value == null ? null : Long.valueOf( value ) );
                        return terminationConfigModel;
                    }

                    @Override
                    public Pair<String, Object> getTerminationValue( TerminationConfigModel terminationConfigModel ) {
                        return new Pair<>( safeToString( terminationConfigModel.getSecondsSpentLimit() ), terminationConfigModel.getSecondsSpentLimit() );
                    }

                    @Override
                    public String getLocalizedTerminationConfigOption() {
                        return SolverEditorConstants.INSTANCE.SecondsSpentLimit();
                    }
                };
            }
            case MINUTES_SPENT_LIMIT: {
                return new TerminationModelManager() {
                    @Override
                    public TerminationConfigModel setTerminationValue( TerminationConfigModel terminationConfigModel, TerminationConfigModel parentTerminationConfigModel, String value ) {
                        terminationConfigModel.setMinutesSpentLimit( value == null ? null : Long.valueOf( value ) );
                        return terminationConfigModel;
                    }

                    @Override
                    public Pair<String, Object> getTerminationValue( TerminationConfigModel terminationConfigModel ) {
                        return new Pair<>( safeToString( terminationConfigModel.getMinutesSpentLimit() ), terminationConfigModel.getMinutesSpentLimit() );
                    }

                    @Override
                    public String getLocalizedTerminationConfigOption() {
                        return SolverEditorConstants.INSTANCE.MinutesSpentLimit();
                    }
                };
            }
            case HOURS_SPENT_LIMIT: {
                return new TerminationModelManager() {
                    @Override
                    public TerminationConfigModel setTerminationValue( TerminationConfigModel terminationConfigModel, TerminationConfigModel parentTerminationConfigModel, String value ) {
                        terminationConfigModel.setHoursSpentLimit( value == null ? null : Long.valueOf( value ) );
                        return terminationConfigModel;
                    }

                    @Override
                    public Pair<String, Object> getTerminationValue( TerminationConfigModel terminationConfigModel ) {
                        return new Pair<>( safeToString( terminationConfigModel.getHoursSpentLimit() ), terminationConfigModel.getHoursSpentLimit() );
                    }

                    @Override
                    public String getLocalizedTerminationConfigOption() {
                        return SolverEditorConstants.INSTANCE.HoursSpentLimit();
                    }
                };
            }
            case DAYS_SPENT_LIMIT: {
                return new TerminationModelManager() {
                    @Override
                    public TerminationConfigModel setTerminationValue( TerminationConfigModel terminationConfigModel, TerminationConfigModel parentTerminationConfigModel, String value ) {
                        terminationConfigModel.setDaysSpentLimit( value == null ? null : Long.valueOf( value ) );
                        return terminationConfigModel;
                    }

                    @Override
                    public Pair<String, Object> getTerminationValue( TerminationConfigModel terminationConfigModel ) {
                        return new Pair<>( safeToString( terminationConfigModel.getDaysSpentLimit() ), terminationConfigModel.getDaysSpentLimit() );
                    }

                    @Override
                    public String getLocalizedTerminationConfigOption() {
                        return SolverEditorConstants.INSTANCE.DaysSpentLimit();
                    }
                };
            }
            case UNIMPROVED_MILLISECONDS_SPENT_LIMIT: {
                return new TerminationModelManager() {
                    @Override
                    public TerminationConfigModel setTerminationValue( TerminationConfigModel terminationConfigModel, TerminationConfigModel parentTerminationConfigModel, String value ) {
                        terminationConfigModel.setUnimprovedMillisecondsSpentLimit( value == null ? null : Long.valueOf( value ) );
                        return terminationConfigModel;
                    }

                    @Override
                    public Pair<String, Object> getTerminationValue( TerminationConfigModel terminationConfigModel ) {
                        return new Pair<>( safeToString( terminationConfigModel.getUnimprovedMillisecondsSpentLimit() ), terminationConfigModel.getUnimprovedMillisecondsSpentLimit() );
                    }

                    @Override
                    public String getLocalizedTerminationConfigOption() {
                        return SolverEditorConstants.INSTANCE.UnimprovedMillisecondsSpentLimit();
                    }
                };
            }
            case UNIMPROVED_SECONDS_SPENT_LIMIT: {
                return new TerminationModelManager() {
                    @Override
                    public TerminationConfigModel setTerminationValue( TerminationConfigModel terminationConfigModel, TerminationConfigModel parentTerminationConfigModel, String value ) {
                        terminationConfigModel.setUnimprovedSecondsSpentLimit( value == null ? null : Long.valueOf( value ) );
                        return terminationConfigModel;
                    }

                    @Override
                    public Pair<String, Object> getTerminationValue( TerminationConfigModel terminationConfigModel ) {
                        return new Pair<>( safeToString( terminationConfigModel.getUnimprovedSecondsSpentLimit() ), terminationConfigModel.getUnimprovedSecondsSpentLimit() );
                    }

                    @Override
                    public String getLocalizedTerminationConfigOption() {
                        return SolverEditorConstants.INSTANCE.UnimprovedSecondsSpentLimit();
                    }
                };
            }
            case UNIMPROVED_MINUTES_SPENT_LIMIT: {
                return new TerminationModelManager() {
                    @Override
                    public TerminationConfigModel setTerminationValue( TerminationConfigModel terminationConfigModel, TerminationConfigModel parentTerminationConfigModel, String value ) {
                        terminationConfigModel.setUnimprovedMinutesSpentLimit( value == null ? null : Long.valueOf( value ) );
                        return terminationConfigModel;
                    }

                    @Override
                    public Pair<String, Object> getTerminationValue( TerminationConfigModel terminationConfigModel ) {
                        return new Pair<>( safeToString( terminationConfigModel.getUnimprovedMinutesSpentLimit() ), terminationConfigModel.getUnimprovedMinutesSpentLimit() );
                    }


                    @Override
                    public String getLocalizedTerminationConfigOption() {
                        return SolverEditorConstants.INSTANCE.UnimprovedMinutesSpentLimit();
                    }
                };
            }
            case UNIMPROVED_HOURS_SPENT_LIMIT: {
                return new TerminationModelManager() {
                    @Override
                    public TerminationConfigModel setTerminationValue( TerminationConfigModel terminationConfigModel, TerminationConfigModel parentTerminationConfigModel, String value ) {
                        terminationConfigModel.setUnimprovedHoursSpentLimit( value == null ? null : Long.valueOf( value ) );
                        return terminationConfigModel;
                    }

                    @Override
                    public Pair<String, Object> getTerminationValue( TerminationConfigModel terminationConfigModel ) {
                        return new Pair<>( safeToString( terminationConfigModel.getUnimprovedHoursSpentLimit() ), terminationConfigModel.getUnimprovedHoursSpentLimit() );
                    }


                    @Override
                    public String getLocalizedTerminationConfigOption() {
                        return SolverEditorConstants.INSTANCE.UnimprovedHoursSpentLimit();
                    }
                };
            }
            case UNIMPROVED_DAYS_SPENT_LIMIT: {
                return new TerminationModelManager() {
                    @Override
                    public TerminationConfigModel setTerminationValue( TerminationConfigModel terminationConfigModel, TerminationConfigModel parentTerminationConfigModel, String value ) {
                        terminationConfigModel.setUnimprovedDaysSpentLimit( value == null ? null : Long.valueOf( value ) );
                        return terminationConfigModel;
                    }

                    @Override
                    public Pair<String, Object> getTerminationValue( TerminationConfigModel terminationConfigModel ) {
                        return new Pair<>( safeToString( terminationConfigModel.getUnimprovedDaysSpentLimit() ), terminationConfigModel.getUnimprovedDaysSpentLimit() );
                    }


                    @Override
                    public String getLocalizedTerminationConfigOption() {
                        return SolverEditorConstants.INSTANCE.UnimprovedDaysSpentLimit();
                    }
                };
            }
            case BEST_SCORE_LIMIT: {
                return new TerminationModelManager() {
                    @Override
                    public TerminationConfigModel setTerminationValue( TerminationConfigModel terminationConfigModel, TerminationConfigModel parentTerminationConfigModel, String value ) {
                        terminationConfigModel.setBestScoreLimit( value );
                        return terminationConfigModel;
                    }

                    @Override
                    public Pair<String, Object> getTerminationValue( TerminationConfigModel terminationConfigModel ) {
                        return new Pair<>( terminationConfigModel.getBestScoreLimit(), terminationConfigModel.getBestScoreLimit() );
                    }


                    @Override
                    public String getLocalizedTerminationConfigOption() {
                        return SolverEditorConstants.INSTANCE.BestScoreLimit();
                    }
                };
            }
            case BEST_SCORE_FEASIBLE: {
                return new TerminationModelManager() {
                    @Override
                    public TerminationConfigModel setTerminationValue( TerminationConfigModel terminationConfigModel, TerminationConfigModel parentTerminationConfigModel, String value ) {
                        terminationConfigModel.setBestScoreFeasible( value == null ? null : Boolean.valueOf( value ) );
                        return terminationConfigModel;
                    }

                    @Override
                    public Pair<String, Object> getTerminationValue( TerminationConfigModel terminationConfigModel ) {
                        Boolean bestScoreFeasible = terminationConfigModel.getBestScoreFeasible();
                        String label = null;
                        if ( Boolean.TRUE.equals( bestScoreFeasible ) ) {
                            label = SolverEditorConstants.INSTANCE.True();
                        } else if ( Boolean.FALSE.equals( bestScoreFeasible ) ) {
                            label = SolverEditorConstants.INSTANCE.False();
                        }
                        return new Pair<>( label, bestScoreFeasible );
                    }

                    @Override
                    public String getLocalizedTerminationConfigOption() {
                        return SolverEditorConstants.INSTANCE.BestScoreFeasible();
                    }
                };
            }
            case STEP_COUNT_LIMIT: {
                return new TerminationModelManager() {
                    @Override
                    public TerminationConfigModel setTerminationValue( TerminationConfigModel terminationConfigModel, TerminationConfigModel parentTerminationConfigModel, String value ) {
                        terminationConfigModel.setStepCountLimit( value == null ? null : Integer.valueOf( value ) );
                        return terminationConfigModel;
                    }

                    @Override
                    public Pair<String, Object> getTerminationValue( TerminationConfigModel terminationConfigModel ) {
                        return new Pair<>( safeToString( terminationConfigModel.getStepCountLimit() ), terminationConfigModel.getStepCountLimit() );
                    }

                    @Override
                    public String getLocalizedTerminationConfigOption() {
                        return SolverEditorConstants.INSTANCE.StepCountLimit();
                    }
                };
            }
            case UNIMPROVED_STEP_COUNT_LIMIT: {
                return new TerminationModelManager() {
                    @Override
                    public TerminationConfigModel setTerminationValue( TerminationConfigModel terminationConfigModel, TerminationConfigModel parentTerminationConfigModel, String value ) {
                        terminationConfigModel.setUnimprovedStepCountLimit( value == null ? null : Integer.valueOf( value ) );
                        return terminationConfigModel;
                    }

                    @Override
                    public Pair<String, Object> getTerminationValue( TerminationConfigModel terminationConfigModel ) {
                        return new Pair<>( safeToString( terminationConfigModel.getUnimprovedStepCountLimit() ), terminationConfigModel.getUnimprovedStepCountLimit() );
                    }

                    @Override
                    public String getLocalizedTerminationConfigOption() {
                        return SolverEditorConstants.INSTANCE.UnimprovedStepCountLimit();
                    }
                };
            }
            case SCORE_CALCULATION_COUNT_LIMIT: {
                return new TerminationModelManager() {
                    @Override
                    public TerminationConfigModel setTerminationValue( TerminationConfigModel terminationConfigModel, TerminationConfigModel parentTerminationConfigModel, String value ) {
                        terminationConfigModel.setScoreCalculationCountLimit( value == null ? null : Long.valueOf( value ) );
                        return terminationConfigModel;
                    }

                    @Override
                    public Pair<String, Object> getTerminationValue( TerminationConfigModel terminationConfigModel ) {
                        return new Pair<>( safeToString( terminationConfigModel.getScoreCalculationCountLimit() ), terminationConfigModel.getScoreCalculationCountLimit() );
                    }

                    @Override
                    public String getLocalizedTerminationConfigOption() {
                        return SolverEditorConstants.INSTANCE.ScoreCalculationCountLimit();
                    }
                };
            }
            default:
                throw new IllegalStateException( "Option " + terminationConfigOption + " is undefined" );
        }

    }

    private String safeToString( Object object ) {
        if ( object == null ) {
            return null;
        }
        return object.toString();
    }

    public TerminationPopup getTerminationPopup() {
        return terminationPopup;
    }

    @Override
    public Widget asWidget() {
        return view.asWidget();
    }
}
