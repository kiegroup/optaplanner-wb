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

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.Composite;
import org.gwtbootstrap3.extras.select.client.ui.Select;
import org.jboss.errai.common.client.dom.Div;
import org.jboss.errai.common.client.dom.Input;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.EventHandler;
import org.jboss.errai.ui.shared.api.annotations.Templated;
import org.kie.workbench.common.screens.datamodeller.client.util.UIUtil;
import org.uberfire.commons.data.Pair;

@Dependent
@Templated
public class PlannerDataObjectEditorViewImpl
        extends Composite
        implements PlannerDataObjectEditorView {

    @Inject
    @DataField("notInPlanningRadioButton")
    Input notInPlanningRadioButton;

    @Inject
    @DataField("planningEntityRadioButton")
    Input planningEntityRadioButton;

    @Inject
    @DataField("planningSolutionRadioButton")
    Input planningSolutionRadioButton;

    @Inject
    @DataField("planningSolutionScoreTypeGroup")
    Div planningSolutionScoreTypeGroup;

    @Inject
    @DataField("planningSolutionScoreTypeSelector")
    Select planningSolutionScoreTypeSelector;

    private Presenter presenter;

    public PlannerDataObjectEditorViewImpl() {
    }

    @Override
    public void init( Presenter presenter ) {
        this.presenter = presenter;
    }

    @Override
    public void setNotInPlanningValue( boolean value ) {
        notInPlanningRadioButton.setChecked( value );
    }

    @Override
    public boolean getNotInPlanningValue() {
        return notInPlanningRadioButton.getChecked();
    }

    @Override
    public void setPlanningEntityValue( boolean value ) {
        planningEntityRadioButton.setChecked( value );
    }

    @Override
    public boolean getPlanningEntityValue() {
        return planningEntityRadioButton.getChecked();
    }

    @Override
    public void setPlanningSolutionValue( boolean value ) {
        planningSolutionRadioButton.setChecked( value );
    }

    @Override
    public boolean getPlanningSolutionValue() {
        return planningSolutionRadioButton.getChecked();
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
        planningSolutionScoreTypeGroup.setHidden( !show );
    }

    @Override
    public void setPlanningSolutionScoreType( String scoreType ) {
        UIUtil.setSelectedValue( planningSolutionScoreTypeSelector, scoreType );
    }

    @EventHandler("notInPlanningRadioButton")
    void onNotInPlanningChange( ClickEvent event ) {
        presenter.onNotInPlanningChange( );
    }

    @EventHandler("planningEntityRadioButton")
    void onPlanningEntityChange( ClickEvent event ) {
        presenter.onPlanningEntityChange( );
    }

    @EventHandler("planningSolutionRadioButton")
    void onPlanningSolutionChange( ClickEvent event ) {
        presenter.onPlanningSolutionChange( );
    }

    @EventHandler("planningSolutionScoreTypeSelector")
    void setPlanningSolutionScoreTypeChange( ChangeEvent event ) {
        presenter.onPlanningSolutionScoreTypeChange();
    }

}
