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

import javax.inject.Inject;

import org.kie.workbench.common.screens.datamodeller.client.widgets.common.domain.ObjectEditor;
import org.kie.workbench.common.services.datamodeller.core.DataObject;
import org.optaplanner.workbench.screens.domaineditor.model.PlannerDomainAnnotations;

public class PlannerDataObjectEditor
        extends ObjectEditor
        implements PlannerDataObjectEditorView.Presenter {

    private PlannerDataObjectEditorView view;

    @Inject
    public PlannerDataObjectEditor( PlannerDataObjectEditorView view ) {
        this.view = view;
        view.setPresenter( this );
        initWidget( view.asWidget() );
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
    protected void loadDataObject( DataObject dataObject ) {
        clean();
        this.dataObject = dataObject;
        if ( dataObject != null ) {
            boolean hasPlanningEntity = dataObject.getAnnotation( PlannerDomainAnnotations.PLANNING_ENTITY_ANNOTATION ) != null;
            boolean hasPlanningSolution = dataObject.getAnnotation( PlannerDomainAnnotations.PLANNING_SOLUTION_ANNOTATION ) != null;

            view.setPlanningEntityValue( hasPlanningEntity );
            view.setPlanningSolutionValue( hasPlanningSolution );
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

            } else {
                commandBuilder.buildDataObjectRemoveAnnotationCommand(
                        getContext(),
                        getName(),
                        getDataObject(),
                        PlannerDomainAnnotations.PLANNING_SOLUTION_ANNOTATION ).execute();

            }
        }
    }
}
