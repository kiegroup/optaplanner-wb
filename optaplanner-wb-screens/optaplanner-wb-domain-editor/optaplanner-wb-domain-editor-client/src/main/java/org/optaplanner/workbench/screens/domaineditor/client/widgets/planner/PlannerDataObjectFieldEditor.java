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
import java.util.List;
import javax.enterprise.context.Dependent;
import javax.enterprise.event.Event;
import javax.inject.Inject;

import com.google.gwt.user.client.ui.Widget;
import org.kie.workbench.common.screens.datamodeller.client.command.DataModelCommandBuilder;
import org.kie.workbench.common.screens.datamodeller.client.command.ValuePair;
import org.kie.workbench.common.screens.datamodeller.client.handlers.DomainHandlerRegistry;
import org.kie.workbench.common.screens.datamodeller.client.widgets.common.domain.FieldEditor;
import org.kie.workbench.common.screens.datamodeller.events.DataModelerEvent;
import org.kie.workbench.common.services.datamodeller.core.Annotation;
import org.kie.workbench.common.services.datamodeller.core.DataObject;
import org.kie.workbench.common.services.datamodeller.core.ObjectProperty;
import org.optaplanner.workbench.screens.domaineditor.model.PlannerDomainAnnotations;

@Dependent
public class PlannerDataObjectFieldEditor
        extends FieldEditor
        implements PlannerDataObjectFieldEditorView.Presenter {

    private PlannerDataObjectFieldEditorView view;

    @Inject
    public PlannerDataObjectFieldEditor( PlannerDataObjectFieldEditorView view,
            DomainHandlerRegistry handlerRegistry,
            Event<DataModelerEvent> dataModelerEvent,
            DataModelCommandBuilder commandBuilder ) {
        super( handlerRegistry, dataModelerEvent, commandBuilder );
        this.view = view;
        view.init( this );
    }

    @Override
    public Widget asWidget() {
        return view.asWidget();
    }

    @Override
    public String getName() {
        return "PLANNER_FIELD_EDITOR";
    }

    @Override
    public String getDomainName() {
        return PlannerDomainEditor.PLANNER_DOMAIN;
    }

    @Override
    protected void loadDataObjectField( DataObject dataObject, ObjectProperty objectField ) {
        view.clear();
        view.showPlanningFieldPropertiesNotAvailable( true );
        this.dataObject = dataObject;
        this.objectField = objectField;

        if ( dataObject != null && objectField != null ) {
            loadPlanningSolution( );
            loadPlanningEntitySettings( );
        }
    }

    @Override
    public void onValueRangeProviderChange() {
        boolean isSet = view.getValueRangeProviderValue();
        if ( isSet ) {
            List<ValuePair> initialValue = new ArrayList<ValuePair>(  );
            initialValue.add( new ValuePair( "id", "" ) );
            commandBuilder.buildFieldAnnotationAddCommand(
                    getContext(),
                    getName(),
                    getDataObject(),
                    getObjectField(),
                    PlannerDomainAnnotations.VALUE_RANGE_PROVIDER_ANNOTATION,
                    initialValue ).execute();
        } else {
            commandBuilder.buildFieldAnnotationRemoveCommand(
                    getContext(),
                    getName(),
                    getDataObject(),
                    getObjectField(),
                    PlannerDomainAnnotations.VALUE_RANGE_PROVIDER_ANNOTATION ).execute();
            view.setValueRangeProviderIdValue( null );
        }
        view.enableValueRangeProviderId( isSet );
    }

    @Override
    public void onValueRangeProviderIdChange() {
        String value = view.getValueRangeProviderIdValue();
        value = value == null ? "" : value;

        if ( getObjectField().getAnnotation( PlannerDomainAnnotations.VALUE_RANGE_PROVIDER_ANNOTATION ) != null ) {
            commandBuilder.buildFieldAnnotationValueChangeCommand(
                    getContext(),
                    getName(),
                    getDataObject(),
                    getObjectField(),
                    PlannerDomainAnnotations.VALUE_RANGE_PROVIDER_ANNOTATION,
                    "id",
                    value,
                    false ).execute();
        }
    }

    @Override
    public void onPlanningEntityCollectionChange() {
        boolean isSet = view.getPlanningEntityCollectionValue();
        if ( isSet ) {
            commandBuilder.buildFieldAnnotationAddCommand(
                    getContext(),
                    getName(),
                    getDataObject(),
                    getObjectField(),
                    PlannerDomainAnnotations.PLANNING_ENTITY_COLLECTION_PROPERTY_ANNOTATION ).execute();
        } else {
            commandBuilder.buildFieldAnnotationRemoveCommand(
                    getContext(),
                    getName(),
                    getDataObject(),
                    getObjectField(),
                    PlannerDomainAnnotations.PLANNING_ENTITY_COLLECTION_PROPERTY_ANNOTATION ).execute();
        }
    }

    @Override
    public void onPlanningVariableChange() {
        boolean isSet = view.getPlanningVariableValue();
        if ( isSet ) {
            commandBuilder.buildFieldAnnotationAddCommand(
                    getContext(),
                    getName(),
                    getDataObject(),
                    getObjectField(),
                    PlannerDomainAnnotations.PLANNING_VARIABLE_ANNOTATION ).execute();
        } else {
            commandBuilder.buildFieldAnnotationRemoveCommand(
                    getContext(),
                    getName(),
                    getDataObject(),
                    getObjectField(),
                    PlannerDomainAnnotations.PLANNING_VARIABLE_ANNOTATION ).execute();
            view.setValueRangeProviderRefsValue( null );
        }
        view.enableValueRangeProviderRefs( isSet );
    }

    @Override
    public void onValueRangeProviderRefsChange() {
        String textValue = view.getValueRangeProviderRefsValue();
        List<String> listValue = null;

        if ( textValue != null && !"".equals( textValue ) ) {
            listValue = new ArrayList<String>(  );
            listValue.add( textValue );
        }

        if ( getObjectField().getAnnotation( PlannerDomainAnnotations.PLANNING_VARIABLE_ANNOTATION ) != null ) {
            commandBuilder.buildFieldAnnotationValueChangeCommand(
                    getContext(),
                    getName(),
                    getDataObject(),
                    getObjectField(),
                    PlannerDomainAnnotations.PLANNING_VARIABLE_ANNOTATION,
                    "valueRangeProviderRefs",
                    listValue,
                    false ).execute();
        }
    }

    @Override
    public void clear() {
        view.clear();
    }

    private void loadPlanningEntitySettings( ) {
        if ( dataObject.getAnnotation( PlannerDomainAnnotations.PLANNING_ENTITY_ANNOTATION ) != null ) {
            view.showPlanningEntitySettingsPanel( true );
            view.showPlanningFieldPropertiesNotAvailable( false );
            Annotation annotation = objectField.getAnnotation( PlannerDomainAnnotations.PLANNING_VARIABLE_ANNOTATION );
            if ( annotation != null ) {
                view.setPlanningVariableValue( true );
                view.enableValueRangeProviderRefs( true );
                List valueRangeProviderRefs = (List) annotation.getValue( "valueRangeProviderRefs" );
                if ( valueRangeProviderRefs != null && valueRangeProviderRefs.size() > 0 ) {
                    String value = valueRangeProviderRefs.get( 0 ) != null ? valueRangeProviderRefs.get( 0 ).toString() : null;
                    view.setValueRangeProviderRefsValue( value );
                }
            } else {
                view.enableValueRangeProviderRefs( false );
            }
        }
    }

    private void loadPlanningSolution( ) {
        if ( dataObject.getAnnotation( PlannerDomainAnnotations.PLANNING_SOLUTION_ANNOTATION  ) != null ) {
            view.showPlanningSolutionSettingsPanel( true );
            view.showPlanningFieldPropertiesNotAvailable( false );

            //set the ValueRangeProvider
            Annotation annotation = objectField.getAnnotation( PlannerDomainAnnotations.VALUE_RANGE_PROVIDER_ANNOTATION );
            if ( annotation != null ) {
                view.setValueRangeProviderValue( true );
                view.enableValueRangeProviderId( true );
                String id = (String) annotation.getValue( "id" );
                view.setValueRangeProviderIdValue( id );
            } else {
                view.enableValueRangeProviderId( false );
            }

            //set the PlanningEntityCollection
            annotation = objectField.getAnnotation( PlannerDomainAnnotations.PLANNING_ENTITY_COLLECTION_PROPERTY_ANNOTATION );
            view.setPlanningEntityCollectionValue( annotation != null );
        }

    }

}
