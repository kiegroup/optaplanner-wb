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

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import org.gwtbootstrap3.client.ui.InlineRadio;

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

    private Presenter presenter;

    public PlannerDataObjectEditorViewImpl() {
        initWidget( uiBinder.createAndBindUi( this ) );
    }

    @Override
    public void setPresenter( Presenter presenter ) {
        this.presenter = presenter;
    }

    @Override
    public void setNotInPlanningValue( boolean value ) {
        notInPlanningRadioButton.setValue( value );
    }

    @Override
    public void setPlanningEntityValue( boolean value ) {
        planningEntityRadioButton.setValue( value );
    }

    @Override
    public void setPlanningSolutionValue( boolean value ) {
        planningSolutionRadioButton.setValue( value );
    }

    @Override
    public void clear() {
        setNotInPlanningValue( false );
        setPlanningEntityValue( false );
        setPlanningSolutionValue( false );
    }

    @UiHandler("notInPlanningRadioButton")
    void onNotInPlanningChange( ClickEvent event ) {
        presenter.onNotInPlanningChange( notInPlanningRadioButton.getValue() );
    }

    @UiHandler("planningEntityRadioButton")
    void onPlanningEntityChange( ClickEvent event ) {
        presenter.onPlanningEntityChange( planningEntityRadioButton.getValue() );
    }

    @UiHandler("planningSolutionRadioButton")
    void onPlanningSolutionChange( ClickEvent event ) {
        presenter.onPlanningSolutionChange( planningSolutionRadioButton.getValue() );
    }

}
