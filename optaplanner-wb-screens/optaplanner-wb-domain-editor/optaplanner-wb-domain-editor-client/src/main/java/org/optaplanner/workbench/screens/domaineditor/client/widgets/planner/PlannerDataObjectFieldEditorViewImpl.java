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

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.Composite;
import org.gwtbootstrap3.client.ui.TextBox;
import org.jboss.errai.common.client.dom.CheckboxInput;
import org.jboss.errai.common.client.dom.Div;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.EventHandler;
import org.jboss.errai.ui.shared.api.annotations.Templated;

@Dependent
@Templated
public class PlannerDataObjectFieldEditorViewImpl
        extends Composite
        implements PlannerDataObjectFieldEditorView {

    private Presenter presenter;

    @Inject
    @DataField("planningSolutionSettingsPanelDiv")
    Div planningSolutionSettingsPanelDiv;

    @Inject
    @DataField("valueRangeProviderCheckBox")
    CheckboxInput valueRangeProviderCheckBox;

    @Inject
    @DataField("valueRangeProviderIdTextBox")
    TextBox valueRangeProviderIdTextBox;

    @Inject
    @DataField("planningEntityCollectionCheckBox")
    CheckboxInput planningEntityCollectionCheckBox;

    @Inject
    @DataField("planningEntitySettingsPanelDiv")
    Div planningEntitySettingsPanelDiv;

    @Inject
    @DataField("planningVariableCheckBox")
    CheckboxInput planningVariableCheckBox;

    @Inject
    @DataField("valueRangeProviderRefsTextBox")
    TextBox valueRangeProviderRefsTextBox;

    @Inject
    @DataField("planningFieldPropertiesNotAvailablePanelDiv")
    Div planningFieldPropertiesNotAvailablePanelDiv;

    public PlannerDataObjectFieldEditorViewImpl() {
    }

    @Override
    public void init( Presenter presenter ) {
        this.presenter = presenter;
    }

    @Override
    public void setValueRangeProviderValue( boolean value ) {
        valueRangeProviderCheckBox.setChecked( value );
    }

    @Override
    public boolean getValueRangeProviderValue() {
        return valueRangeProviderCheckBox.getChecked();
    }

    @Override
    public void enableValueRangeProviderId( boolean enabled ) {
        valueRangeProviderIdTextBox.setEnabled( enabled );
    }

    @Override
    public void setValueRangeProviderIdValue( String value ) {
        valueRangeProviderIdTextBox.setValue( value );
    }

    @Override
    public String getValueRangeProviderIdValue() {
        return valueRangeProviderIdTextBox.getValue();
    }

    @Override
    public void setPlanningEntityCollectionValue( boolean value ) {
        planningEntityCollectionCheckBox.setChecked( value );
    }

    @Override
    public boolean getPlanningEntityCollectionValue() {
        return planningEntityCollectionCheckBox.getChecked();
    }

    @Override
    public void setPlanningVariableValue( boolean value ) {
        planningVariableCheckBox.setChecked( value );
    }

    @Override
    public boolean getPlanningVariableValue() {
        return planningVariableCheckBox.getChecked();
    }

    @Override
    public void enableValueRangeProviderRefs( boolean enabled ) {
        valueRangeProviderRefsTextBox.setEnabled( enabled );
    }

    @Override
    public void setValueRangeProviderRefsValue( String value ) {
        valueRangeProviderRefsTextBox.setValue( value );
    }

    public String getValueRangeProviderRefsValue() {
        return valueRangeProviderRefsTextBox.getValue();
    }

    @Override
    public void showPlanningSolutionSettingsPanel( boolean show ) {
        planningSolutionSettingsPanelDiv.setHidden( !show );
    }

    @Override
    public void showPlanningEntitySettingsPanel( boolean show ) {
        planningEntitySettingsPanelDiv.setHidden( !show );
    }

    @Override
    public void showPlanningFieldPropertiesNotAvailable( boolean show ) {
        planningFieldPropertiesNotAvailablePanelDiv.setHidden( !show );
    }

    @Override
    public void clear() {
        showPlanningEntitySettingsPanel( false );
        showPlanningSolutionSettingsPanel( false );
        showPlanningSolutionSettingsPanel( false );
        setValueRangeProviderValue( false );
        setValueRangeProviderIdValue( null );
        enableValueRangeProviderId( false );
        setPlanningEntityCollectionValue( false );
        setPlanningVariableValue( false );
        setValueRangeProviderRefsValue( null );
        enableValueRangeProviderRefs( false );
    }

    @EventHandler("valueRangeProviderCheckBox")
    void onValueRangeProviderChange( ClickEvent event ) {
        presenter.onValueRangeProviderChange();
    }

    @EventHandler("valueRangeProviderIdTextBox")
    void onValueRangeProviderId( ChangeEvent event ) {
        presenter.onValueRangeProviderIdChange();
    }

    @EventHandler("planningEntityCollectionCheckBox")
    void onPlanningEntityCollection( ClickEvent event ) {
        presenter.onPlanningEntityCollectionChange();
    }

    @EventHandler("planningVariableCheckBox")
    void onPlanningVariableChange( ClickEvent event ) {
        presenter.onPlanningVariableChange();
    }

    @EventHandler("valueRangeProviderRefsTextBox")
    void onValueRangeProviderRefs( ChangeEvent event ) {
        presenter.onValueRangeProviderRefsChange();
    }

}
