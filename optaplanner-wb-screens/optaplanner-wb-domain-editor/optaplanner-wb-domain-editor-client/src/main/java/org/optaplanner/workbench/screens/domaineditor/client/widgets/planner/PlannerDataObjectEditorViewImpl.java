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

import java.util.List;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import org.gwtbootstrap3.client.ui.FormGroup;
import org.gwtbootstrap3.client.ui.InlineRadio;
import org.gwtbootstrap3.extras.select.client.ui.Select;
import org.kie.workbench.common.screens.datamodeller.client.util.UIUtil;
import org.uberfire.commons.data.Pair;

@Dependent
public class PlannerDataObjectEditorViewImpl
        extends Composite
        implements PlannerDataObjectEditorView {

    interface Binder
            extends UiBinder<Widget, PlannerDataObjectEditorViewImpl> {

    }

    private static Binder uiBinder = GWT.create( Binder.class );

    @UiField
    InlineRadio notInPlanningRadioButton;

    @UiField
    InlineRadio planningEntityRadioButton;

    @UiField
    InlineRadio planningSolutionRadioButton;

    @UiField
    FormGroup planningSolutionScoreTypeGroup;

    @UiField
    Select planningSolutionScoreTypeSelector;

    private Presenter presenter;

    @Inject
    public PlannerDataObjectEditorViewImpl() {
        initWidget( uiBinder.createAndBindUi( this ) );
    }

    @Override
    public void init( Presenter presenter ) {
        this.presenter = presenter;
    }

    @Override
    public void setNotInPlanningValue( boolean value ) {
        notInPlanningRadioButton.setValue( value );
    }

    @Override
    public boolean getNotInPlanningValue() {
        return notInPlanningRadioButton.getValue();
    }

    @Override
    public void setPlanningEntityValue( boolean value ) {
        planningEntityRadioButton.setValue( value );
    }

    @Override
    public boolean getPlanningEntityValue() {
        return planningEntityRadioButton.getValue();
    }

    @Override
    public void setPlanningSolutionValue( boolean value ) {
        planningSolutionRadioButton.setValue( value );
    }

    @Override
    public boolean getPlanningSolutionValue() {
        return planningSolutionRadioButton.getValue();
    }

    @Override
    public void clear() {
        setNotInPlanningValue( false );
        setPlanningEntityValue( false );
        setPlanningSolutionValue( false );
    }

    @Override
    public void initPlanningSolutionScoreTypeOptions( List<Pair<String, String>> options, String selectedScoreType ) {
        UIUtil.initList( planningSolutionScoreTypeSelector, options, selectedScoreType, false );
    }

    @Override
    public String getPlanningSolutionScoreType() {
        return planningSolutionScoreTypeSelector.getValue();
    }

    @Override
    public void showPlanningSolutionScoreType( boolean show ) {
        planningSolutionScoreTypeGroup.setVisible( show );
    }

    @Override
    public void setPlanningSolutionScoreType( String scoreType ) {
        UIUtil.setSelectedValue( planningSolutionScoreTypeSelector, scoreType );
    }

    @UiHandler("notInPlanningRadioButton")
    void onNotInPlanningChange( ClickEvent event ) {
        presenter.onNotInPlanningChange( );
    }

    @UiHandler("planningEntityRadioButton")
    void onPlanningEntityChange( ClickEvent event ) {
        presenter.onPlanningEntityChange( );
    }

    @UiHandler("planningSolutionRadioButton")
    void onPlanningSolutionChange( ClickEvent event ) {
        presenter.onPlanningSolutionChange( );
    }

    @UiHandler("planningSolutionScoreTypeSelector")
    void setPlanningSolutionScoreTypeChange( ChangeEvent event ) {
        presenter.onPlanningSolutionScoreTypeChange();
    }

}
