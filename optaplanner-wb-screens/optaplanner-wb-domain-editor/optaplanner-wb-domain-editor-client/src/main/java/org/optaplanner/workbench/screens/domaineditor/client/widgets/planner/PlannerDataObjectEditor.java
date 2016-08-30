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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.enterprise.context.Dependent;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import com.google.gwt.user.client.ui.Widget;
import org.jboss.errai.common.client.api.Caller;
import org.jboss.errai.common.client.api.ErrorCallback;
import org.jboss.errai.common.client.api.RemoteCallback;
import org.kie.workbench.common.screens.datamodeller.client.DataModelerContext;
import org.kie.workbench.common.screens.datamodeller.client.command.DataModelCommandBuilder;
import org.kie.workbench.common.screens.datamodeller.client.command.ValuePair;
import org.kie.workbench.common.screens.datamodeller.client.handlers.DomainHandlerRegistry;
import org.kie.workbench.common.screens.datamodeller.client.widgets.common.domain.ObjectEditor;
import org.kie.workbench.common.screens.datamodeller.events.ChangeType;
import org.kie.workbench.common.screens.datamodeller.events.DataModelerEvent;
import org.kie.workbench.common.screens.datamodeller.events.DataObjectChangeEvent;
import org.kie.workbench.common.services.datamodeller.core.DataObject;
import org.kie.workbench.common.services.datamodeller.core.JavaClass;
import org.kie.workbench.common.services.datamodeller.core.ObjectProperty;
import org.optaplanner.core.api.score.Score;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;
import org.optaplanner.workbench.screens.domaineditor.client.resources.i18n.DomainEditorConstants;
import org.optaplanner.workbench.screens.domaineditor.client.resources.i18n.DomainEditorConstantsWithLookup;
import org.optaplanner.workbench.screens.domaineditor.client.util.PlannerDomainTypes;
import org.optaplanner.workbench.screens.domaineditor.model.ComparatorDefinition;
import org.optaplanner.workbench.screens.domaineditor.model.ComparatorObject;
import org.optaplanner.workbench.screens.domaineditor.model.ObjectPropertyPath;
import org.optaplanner.workbench.screens.domaineditor.model.PlannerDomainAnnotations;
import org.optaplanner.workbench.screens.domaineditor.service.PlannerDataObjectEditorService;
import org.uberfire.commons.data.Pair;
import org.uberfire.ext.widgets.common.client.common.popups.errors.ErrorPopup;

@Dependent
public class PlannerDataObjectEditor
        extends ObjectEditor
        implements PlannerDataObjectEditorView.Presenter {

    private Caller<PlannerDataObjectEditorService> plannerDataObjectEditorService;

    private PlannerDataObjectEditorView view;

    private ComparatorObject comparatorObject;

    @Inject
    public PlannerDataObjectEditor( PlannerDataObjectEditorView view,
                                    DomainHandlerRegistry handlerRegistry,
                                    Event<DataModelerEvent> dataModelerEvent,
                                    DataModelCommandBuilder commandBuilder,
                                    Caller<PlannerDataObjectEditorService> plannerDataObjectEditorService) {
        super( handlerRegistry, dataModelerEvent, commandBuilder );
        this.view = view;
        this.plannerDataObjectEditorService = plannerDataObjectEditorService;

        view.init( this );
    }

    @Override
    public Widget asWidget() {
        return view.asWidget();
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
        clear();
        this.dataObject = dataObject;
        this.comparatorObject = null;
        if ( dataObject != null ) {
            boolean hasPlanningEntity = dataObject.getAnnotation( PlannerDomainAnnotations.PLANNING_ENTITY_ANNOTATION ) != null;
            boolean hasPlanningSolution = dataObject.getAnnotation( PlannerDomainAnnotations.PLANNING_SOLUTION_ANNOTATION ) != null;

            view.setPlanningEntityValue( hasPlanningEntity );
            view.setPlanningSolutionValue( hasPlanningSolution );
            view.showPlanningSolutionScoreType( hasPlanningSolution );
            view.setNotInPlanningValue( !hasPlanningEntity && !hasPlanningSolution );
            view.destroyFieldPicker();

            if ( hasPlanningEntity ) {
                if ( dataObject.getAnnotation( ComparatorDefinition.class.getName() ) != null && !dataObject.getNestedClasses().isEmpty() ) {
                    for ( JavaClass javaClass : dataObject.getNestedClasses() ) {
                        if ( javaClass instanceof ComparatorObject ) {
                            PlannerDataObjectEditor.this.comparatorObject = (ComparatorObject) javaClass;
                            break;
                        }
                    }
                }
                view.initFieldPicker( getContext().getDataModel(), getDataObject(), comparatorObject );
            }
        }
    }

    @Override
    public void clear() {
        view.clear();
        view.setNotInPlanningValue( true );
    }

    @Override
    public void objectPropertyPathChanged( List<ObjectPropertyPath> objectPropertyPaths ) {

        if ( objectPropertyPaths == null || objectPropertyPaths.isEmpty() ) {
            commandBuilder.buildDataObjectRemoveAnnotationCommand( context, getName(), dataObject, ComparatorDefinition.class.getName() ).execute();
            commandBuilder.buildDataObjectRemoveNestedClassCommand( context, getName(), dataObject, comparatorObject ).execute();
            commandBuilder.buildDataObjectAddAnnotationCommand( getContext(),
                    getName(),
                    getDataObject(),
                    PlannerDomainAnnotations.PLANNING_ENTITY_ANNOTATION ).execute();
            this.comparatorObject = null;
            view.initFieldPicker( getContext().getDataModel(), getDataObject(), null );
            return;
        }

        List<String> objectPropertyPathList = new ArrayList<>();
        for ( ObjectPropertyPath objectPropertyPath : objectPropertyPaths ) {
            StringBuilder pathBuilder = new StringBuilder();
            List<ObjectProperty> path = objectPropertyPath.getObjectPropertyPath();
            for ( int i = 0; i < path.size(); i++ ) {
                ObjectProperty objectProperty = path.get( i );
                pathBuilder.append( objectProperty.getClassName() ).append( ":" ).append( objectProperty.getName() );
                if ( i != path.size() - 1 ) {
                    pathBuilder.append( "-" );
                }
            }
            pathBuilder.append( "=" ).append( objectPropertyPath.isDescending() ? "desc" : "asc" );
            objectPropertyPathList.add( pathBuilder.toString() );
        }

        commandBuilder.buildDataObjectAddAnnotationCommand( getContext(),
                getName(),
                getDataObject(),
                ComparatorDefinition.class.getName(),
                Arrays.asList( new ValuePair( "fieldPaths", objectPropertyPathList ) ) ).execute();

        plannerDataObjectEditorService.call( new RemoteCallback<ComparatorObject>() {
            @Override
            public void callback( ComparatorObject comparatorObject ) {
                PlannerDataObjectEditor.this.comparatorObject = comparatorObject;
                dataObject.getNestedClasses().clear();
                commandBuilder.buildDataObjectAddNestedClassCommand( getContext(), getName(), getDataObject(), comparatorObject ).execute();
            }
        }, new ErrorCallback<Object>() {
            @Override
            public boolean error( Object o, Throwable throwable ) {
                view.initFieldPicker( getContext().getDataModel(), getDataObject(), comparatorObject );
                ErrorPopup.showMessage( DomainEditorConstants.INSTANCE.UnexpectedErrorComparatorUpdate() + " " + throwable.getMessage() );
                return false;
            }
        } ).updateComparatorObject( getDataObject(), comparatorObject, objectPropertyPaths );

        List<ValuePair> valuePairList = Arrays.asList( new ValuePair( "difficultyComparatorClass", getDataObject().getName() + "." + getDataObject().getName() + "Comparator.class" ) );
        commandBuilder.buildDataObjectAddAnnotationCommand( getContext(),
                getName(),
                getDataObject(),
                PlannerDomainAnnotations.PLANNING_ENTITY_ANNOTATION,
                valuePairList ).execute();
    }

    @Override
    public void removeComparatorDefinition( DataObject dataObject, boolean resetPlanningEntityAnnotation ) {
        if ( comparatorObject != null ) {
            commandBuilder.buildDataObjectRemoveNestedClassCommand( getContext(),
                    getName(),
                    dataObject,
                    comparatorObject ).execute();
            this.comparatorObject = null;
        }

        commandBuilder.buildDataObjectRemoveAnnotationCommand( getContext(),
                getName(),
                getDataObject(),
                ComparatorDefinition.class.getName() ).execute();

        if ( resetPlanningEntityAnnotation ) {
            commandBuilder.buildDataObjectAddAnnotationCommand( getContext(),
                    getName(),
                    getDataObject(),
                    PlannerDomainAnnotations.PLANNING_ENTITY_ANNOTATION ).execute();
        }
    }

    @Override
    public void onNotInPlanningChange() {
        boolean value = view.getNotInPlanningValue();
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
            removeComparatorDefinition( getDataObject(), false );
            view.destroyFieldPicker();
            view.showPlanningSolutionScoreType( false );
        }
    }

    @Override
    public void onPlanningEntityChange() {
        boolean value = view.getPlanningEntityValue();
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
                view.initFieldPicker( getContext().getDataModel(), getDataObject(), null );
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
    public void onPlanningSolutionChange() {
        boolean value = view.getPlanningSolutionValue();
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
                        buildPlanningSolutionScoreTypeSuperClass( getDefaultSolutionScoreType() ) ).execute();
                removeComparatorDefinition( getDataObject(), false );
                view.destroyFieldPicker();
                view.showPlanningSolutionScoreType( true );
                view.setPlanningSolutionScoreType( getDefaultSolutionScoreType() );
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
        List<Pair<String, String>> planningSolutionScoreTypeOptions = new ArrayList<>( PlannerDomainTypes.SCORE_TYPES.size() );
        for (Class<? extends Score> scoreClass : PlannerDomainTypes.SCORE_TYPES ) {
            planningSolutionScoreTypeOptions.add( new Pair<>( DomainEditorConstantsWithLookup.INSTANCE.getString( scoreClass.getSimpleName() ), scoreClass.getName() ) );
        }
        return planningSolutionScoreTypeOptions;
    }

    private String buildPlanningSolutionScoreTypeSuperClass( String planningSolutionScoreType ) {
        return planningSolutionScoreType != null ?
                PlannerDomainTypes.ABSTRACT_SOLUTION_CLASS_NAME + "<" + planningSolutionScoreType + ">" :
                null;
    }

    private void adjustSelectedPlanningSolutionScoreType() {
        if ( context != null && context.getEditorModelContent() != null && context.getEditorModelContent().getSource() != null ) {
            String selectedScoreType = getSelectedPlanningSolutionTypeFromSource( context.getEditorModelContent().getSource() );
            if ( selectedScoreType == null ) {
                selectedScoreType = getDefaultSolutionScoreType();
            }
            view.initPlanningSolutionScoreTypeOptions( getPlanningSolutionScoreTypeOptions(), selectedScoreType );
        }
    }

    private String getSelectedPlanningSolutionTypeFromSource( String source ) {
        //Implementation to make the planner prototype work, since data modeller by definition
        //do not manage parametrized types.
        for ( Class<? extends Score> scoreClass : PlannerDomainTypes.SCORE_TYPES ) {
            if ( source.contains( PlannerDomainTypes.ABSTRACT_SOLUTION_SIMPLE_CLASS_NAME + "<" + scoreClass.getSimpleName() + ">" ) ||
                    source.contains( PlannerDomainTypes.ABSTRACT_SOLUTION_SIMPLE_CLASS_NAME + "<" + scoreClass.getName() + ">" )) {
                return scoreClass.getName();
            }
        }
        return null;
    }

    private String getDefaultSolutionScoreType() {
        return HardSoftScore.class.getName();
    }

}