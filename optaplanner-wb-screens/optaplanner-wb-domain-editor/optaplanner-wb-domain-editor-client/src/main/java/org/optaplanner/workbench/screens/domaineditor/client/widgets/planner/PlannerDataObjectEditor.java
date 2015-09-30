/*
 * Copyright 2015 JBoss Inc
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

import java.util.List;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.kie.workbench.common.screens.datamodeller.client.DataModelerContext;
import org.kie.workbench.common.screens.datamodeller.client.widgets.common.domain.ObjectEditor;
import org.kie.workbench.common.screens.datamodeller.events.ChangeType;
import org.kie.workbench.common.screens.datamodeller.events.DataObjectChangeEvent;
import org.kie.workbench.common.services.datamodeller.core.DataObject;
import org.optaplanner.workbench.screens.domaineditor.client.util.PlannerDomainTypes;
import org.optaplanner.workbench.screens.domaineditor.model.PlannerDomainAnnotations;
import org.uberfire.commons.data.Pair;

public class PlannerDataObjectEditor
        extends ObjectEditor
        implements PlannerDataObjectEditorView.Presenter {

    private PlannerDataObjectEditorView view;

    @Inject
    public PlannerDataObjectEditor( PlannerDataObjectEditorView view ) {
        this.view = view;
        view.setPresenter( this );
        initWidget( view.asWidget() );
        view.setPlanningSolutionScoreTypeOptions( getPlanningSolutionScoreTypeOptions() );
    }

    @Override
    public String getName() {
        return "PLANNER_OBJECT_EDITOR";
    }

    @Override
    public String getDomainName() {
        return PlannerDomainEditor.PLANNER_DOMAIN;
    }

    @Override
    public void onContextChange( DataModelerContext context ) {
        super.onContextChange( context );
        adjustSelectedPlanningSolutionScoreType();
    }

    @Override
    protected void loadDataObject( DataObject dataObject ) {
        clean();
        this.dataObject = dataObject;
        if ( dataObject != null ) {
            boolean hasPlanningEntity = dataObject.getAnnotation( PlannerDomainAnnotations.PLANNING_ENTITY_ANNOTATION ) != null;
            boolean hasPlanningSolution = dataObject.getAnnotation( PlannerDomainAnnotations.PLANNING_SOLUTION_ANNOTATION ) != null;

            view.setPlanningEntityValue( hasPlanningEntity );
            view.setPlanningSolutionValue( hasPlanningSolution );
            view.showPlanningSolutionScoreType( hasPlanningSolution );
            view.setNotInPlanningValue( !hasPlanningEntity && !hasPlanningSolution );
        }
    }

    @Override
    public void clean() {
        view.clear();
        view.setNotInPlanningValue( true );
    }

    @Override
    public void onNotInPlanningChange( boolean value ) {
        if ( value && dataObject != null ) {
            commandBuilder.buildDataObjectRemoveAnnotationCommand( getContext(),
                    getName(),
                    getDataObject(),
                    PlannerDomainAnnotations.PLANNING_ENTITY_ANNOTATION ).execute();

            commandBuilder.buildDataObjectRemoveAnnotationCommand( getContext(),
                    getName(),
                    getDataObject(),
                    PlannerDomainAnnotations.PLANNING_SOLUTION_ANNOTATION ).execute();

            if ( isPlannerSolution( getDataObject().getSuperClassName() ) ) {
                commandBuilder.buildDataObjectSuperClassChangeCommand( getContext(),
                        getName(),
                        getDataObject(),
                        null ).execute();
            }
            view.showPlanningSolutionScoreType( false );
        }
    }

    @Override
    public void onPlanningEntityChange( boolean value ) {
        if ( dataObject != null ) {
            if ( value ) {
                commandBuilder.buildDataObjectAddAnnotationCommand(
                        getContext(),
                        getName(),
                        getDataObject(),
                        PlannerDomainAnnotations.PLANNING_ENTITY_ANNOTATION ).execute();

                commandBuilder.buildDataObjectRemoveAnnotationCommand(
                        getContext(),
                        getName(),
                        getDataObject(),
                        PlannerDomainAnnotations.PLANNING_SOLUTION_ANNOTATION ).execute();

                if ( isPlannerSolution( getDataObject().getSuperClassName() ) ) {
                    commandBuilder.buildDataObjectSuperClassChangeCommand( getContext(),
                            getName(),
                            getDataObject(),
                            null ).execute();
                }
                view.showPlanningSolutionScoreType( false );
            } else {
                commandBuilder.buildDataObjectRemoveAnnotationCommand(
                        getContext(),
                        getName(),
                        getDataObject(),
                        PlannerDomainAnnotations.PLANNING_ENTITY_ANNOTATION ).execute();
            }
        }
    }

    @Override
    public void onPlanningSolutionChange( boolean value ) {
        if ( dataObject != null ) {
            if ( value ) {
                commandBuilder.buildDataObjectAddAnnotationCommand(
                        getContext(),
                        getName(),
                        getDataObject(),
                        PlannerDomainAnnotations.PLANNING_SOLUTION_ANNOTATION ).execute();

                commandBuilder.buildDataObjectRemoveAnnotationCommand(
                        getContext(),
                        getName(),
                        getDataObject(),
                        PlannerDomainAnnotations.PLANNING_ENTITY_ANNOTATION ).execute();
                commandBuilder.buildDataObjectSuperClassChangeCommand( getContext(),
                        getName(),
                        getDataObject(),
                        buildPlanningSolutionScoreTypeSuperClass( view.getPlanningSolutionScoreType() ) ).execute();
                view.showPlanningSolutionScoreType( true );
            } else {
                commandBuilder.buildDataObjectRemoveAnnotationCommand(
                        getContext(),
                        getName(),
                        getDataObject(),
                        PlannerDomainAnnotations.PLANNING_SOLUTION_ANNOTATION ).execute();
                commandBuilder.buildDataObjectSuperClassChangeCommand( getContext(),
                        getName(),
                        getDataObject(),
                        null ).execute();
                view.showPlanningSolutionScoreType( false );
            }
        }
    }

    @Override
    public void onPlanningSolutionScoreTypeChange() {
        String planningSolutionScoreType = view.getPlanningSolutionScoreType();
        String superClass = buildPlanningSolutionScoreTypeSuperClass( planningSolutionScoreType );
        if ( superClass != null ) {
            commandBuilder.buildDataObjectSuperClassChangeCommand( getContext(),
                    getName(),
                    getDataObject(),
                    superClass ).execute();
        }
    }

    protected void onDataObjectChange( @Observes DataObjectChangeEvent event ) {
        super.onDataObjectChange( event );
        if ( event.isFromContext( getContext() != null ? getContext().getContextId() : null ) &&
            !event.isFrom( getName() ) &&
            event.getChangeType() == ChangeType.SUPER_CLASS_NAME_CHANGE &&
            getDataObject() != null &&
            getDataObject().getAnnotation( PlannerDomainAnnotations.PLANNING_SOLUTION_ANNOTATION ) != null &&
            !isPlannerSolution( getDataObject().getSuperClassName() ) &&
            isPlannerSolution( event.getOldValue() != null ? event.getOldValue().toString() : null ) ) {

            commandBuilder.buildDataObjectRemoveAnnotationCommand(
                    getContext(),
                    getName(),
                    getDataObject(),
                    PlannerDomainAnnotations.PLANNING_SOLUTION_ANNOTATION ).execute();

        }
    }

    private boolean isPlannerSolution( String superClassName ) {
        return superClassName != null &&
                ( superClassName.startsWith( PlannerDomainTypes.ABSTRACT_SOLUTION_CLASS_NAME ) ||
                        superClassName.startsWith( PlannerDomainTypes.ABSTRACT_SOLUTION_SIMPLE_CLASS_NAME ) );
    }


    private List<Pair<String, String>> getPlanningSolutionScoreTypeOptions() {
        return PlannerDomainTypes.SCORE_TYPES;
    }

    private String buildPlanningSolutionScoreTypeSuperClass( String planningSolutionScoreType ) {
        return planningSolutionScoreType != null ?
                PlannerDomainTypes.ABSTRACT_SOLUTION_CLASS_NAME + "<"+ planningSolutionScoreType + ">" :
                null;
    }

    private void adjustSelectedPlanningSolutionScoreType() {
        if ( context != null && context.getEditorModelContent() != null && context.getEditorModelContent().getSource() != null ) {
            String selectedScoreType = getSelectedPlanningSolutionTypeFromSource( context.getEditorModelContent().getSource() );
            if ( selectedScoreType != null ) {
                view.setPlanningSolutionScoreType( selectedScoreType );
            } else {
                view.setPlanningSolutionScoreType( PlannerDomainTypes.SIMPLE_SCORE_CLASS );
            }
        }
    }

    private String getSelectedPlanningSolutionTypeFromSource( String source ) {
        //TODO tmp implementation to make the planner prototype work, since data modeller by definition
        //do not manage parametrized types.
        for ( Pair<String, String> type : PlannerDomainTypes.SCORE_TYPES ) {
            if ( source.contains( PlannerDomainTypes.ABSTRACT_SOLUTION_SIMPLE_CLASS_NAME + "<" + type.getK1() + ">" ) ||
                    source.contains( PlannerDomainTypes.ABSTRACT_SOLUTION_SIMPLE_CLASS_NAME + "<" + type.getK2() + ">" )) {
                return type.getK2();
            }
        }
        return null;
    }

}
