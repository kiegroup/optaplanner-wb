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

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import org.gwtbootstrap3.client.ui.CheckBox;
import org.gwtbootstrap3.client.ui.Form;
import org.gwtbootstrap3.client.ui.TextBox;

@Dependent
public class PlannerDataObjectFieldEditorViewImpl
        extends Composite
        implements PlannerDataObjectFieldEditorView {

    interface Binder
            extends UiBinder<Widget, PlannerDataObjectFieldEditorViewImpl> {

    }

    private static Binder uiBinder = GWT.create( Binder.class );

    private Presenter presenter;

    @UiField
    Form planningSolutionSettingsPanel;

    @UiField
    CheckBox valueRangeProviderCheckBox;

    @UiField
    TextBox valueRangeProviderIdTextBox;

    @UiField
    CheckBox planningEntityCollectionCheckBox;

    @UiField
    Form planningEntitySettingsPanel;

    @UiField
    CheckBox planningVariableCheckBox;

    @UiField
    TextBox valueRangeProviderRefsTextBox;

    @UiField
    Form planningFieldPropertiesNotAvailablePanel;

    @Inject
    public PlannerDataObjectFieldEditorViewImpl() {
        initWidget( uiBinder.createAndBindUi( this ) );
    }

    @Override
    public void init( Presenter presenter ) {
        this.presenter = presenter;
    }

    @Override
    public void setValueRangeProviderValue( boolean value ) {
        valueRangeProviderCheckBox.setValue( value );
    }

    @Override
    public boolean getValueRangeProviderValue() {
        return valueRangeProviderCheckBox.getValue();
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
        planningEntityCollectionCheckBox.setValue( value );
    }

    @Override
    public boolean getPlanningEntityCollectionValue() {
        return planningEntityCollectionCheckBox.getValue();
    }

    @Override
    public void setPlanningVariableValue( boolean value ) {
        planningVariableCheckBox.setValue( value );
    }

    @Override
    public boolean getPlanningVariableValue() {
        return planningVariableCheckBox.getValue();
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
        planningSolutionSettingsPanel.setVisible( show );

    }

    @Override
    public void showPlanningEntitySettingsPanel( boolean show ) {
        planningEntitySettingsPanel.setVisible( show );
    }

    @Override
    public void showPlanningFieldPropertiesNotAvailable( boolean show ) {
        planningFieldPropertiesNotAvailablePanel.setVisible( show );
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

    @UiHandler("valueRangeProviderCheckBox")
    void onValueRangeProviderChange( ClickEvent event ) {
        presenter.onValueRangeProviderChange();
    }

    @UiHandler("valueRangeProviderIdTextBox")
    void onValueRangeProviderId( ValueChangeEvent<String> event ) {
        presenter.onValueRangeProviderIdChange();
    }

    @UiHandler("planningEntityCollectionCheckBox")
    void onPlanningEntityCollection( ClickEvent event ) {
        presenter.onPlanningEntityCollectionChange();
    }

    @UiHandler("planningVariableCheckBox")
    void onPlanningVariableChange( ClickEvent event ) {
        presenter.onPlanningVariableChange();
    }

    @UiHandler("valueRangeProviderRefsTextBox")
    void onValueRangeProviderRefs( ValueChangeEvent<String> event ) {
        presenter.onValueRangeProviderRefsChange();
    }

}
