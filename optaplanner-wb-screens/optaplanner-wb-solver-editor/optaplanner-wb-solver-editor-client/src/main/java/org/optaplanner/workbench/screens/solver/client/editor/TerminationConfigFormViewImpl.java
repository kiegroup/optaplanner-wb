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

import javax.inject.Inject;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class TerminationConfigFormViewImpl
        extends Composite
        implements TerminationConfigFormView {

    private TerminationConfigForm presenter;

    interface Binder
            extends
            UiBinder<Widget, TerminationConfigFormViewImpl> {

    }

    private static Binder uiBinder = GWT.create( Binder.class );

    @UiField(provided = true)
    Spinner spentLimitHours;

    @UiField(provided = true)
    Spinner spentLimitMinutes;

    @UiField(provided = true)
    Spinner spentLimitSeconds;

    @UiField(provided = true)
    Spinner unimprovedSpendLimitHours;

    @UiField(provided = true)
    Spinner unimprovedSpendLimitMinutes;

    @UiField(provided = true)
    Spinner unimprovedSpendLimitSeconds;

    @UiField
    CheckBox useSpentLimit;

    @UiField
    CheckBox useUnimprovedSpentLimit;

    @UiField(provided = true)
    Spinner spentLimitDays;

    @UiField(provided = true)
    Spinner unimprovedSpendLimitDays;

    @Inject
    public TerminationConfigFormViewImpl( ) {
        this.spentLimitDays = new Spinner( new SpinnerViewImpl() );
        this.spentLimitHours = new Spinner( new SpinnerViewImpl() );
        this.spentLimitMinutes = new Spinner( new SpinnerViewImpl() );
        this.spentLimitSeconds = new Spinner( new SpinnerViewImpl() );
        this.unimprovedSpendLimitDays = new Spinner( new SpinnerViewImpl() );
        this.unimprovedSpendLimitHours = new Spinner( new SpinnerViewImpl() );
        this.unimprovedSpendLimitMinutes = new Spinner( new SpinnerViewImpl() );
        this.unimprovedSpendLimitSeconds = new Spinner( new SpinnerViewImpl() );


        initWidget( uiBinder.createAndBindUi( this ) );

        addValueChangeHandlers();
    }

    private void addValueChangeHandlers() {
        this.spentLimitDays.addValueChangeHandler( new ValueChangeHandler<Long>() {
            @Override
            public void onValueChange( ValueChangeEvent<Long> event ) {
                presenter.onDaysSpentLimitChange( event.getValue() );
            }
        } );

        this.spentLimitHours.addValueChangeHandler( new ValueChangeHandler<Long>() {
            @Override
            public void onValueChange( ValueChangeEvent<Long> event ) {
                presenter.onHoursSpentLimitChange( event.getValue() );
            }
        } );
        this.spentLimitMinutes.addValueChangeHandler( new ValueChangeHandler<Long>() {
            @Override
            public void onValueChange( ValueChangeEvent<Long> event ) {
                presenter.onMinutesSpentLimitChange( event.getValue() );
            }
        } );
        this.spentLimitSeconds.addValueChangeHandler( new ValueChangeHandler<Long>() {
            @Override
            public void onValueChange( ValueChangeEvent<Long> event ) {
                presenter.onSecondsSpentLimitChange( event.getValue() );
            }
        } );

        this.unimprovedSpendLimitDays.addValueChangeHandler( new ValueChangeHandler<Long>() {
            @Override
            public void onValueChange( ValueChangeEvent<Long> event ) {
                presenter.onUnimprovedDaysSpentLimitChange( event.getValue() );
            }
        } );
        this.unimprovedSpendLimitHours.addValueChangeHandler( new ValueChangeHandler<Long>() {
            @Override
            public void onValueChange( ValueChangeEvent<Long> event ) {
                presenter.onUnimprovedHoursSpentLimit( event.getValue() );
            }
        } );
        this.unimprovedSpendLimitMinutes.addValueChangeHandler( new ValueChangeHandler<Long>() {
            @Override
            public void onValueChange( ValueChangeEvent<Long> event ) {
                presenter.onUnimprovedMinutesSpentLimit( event.getValue() );
            }
        } );
        this.unimprovedSpendLimitSeconds.addValueChangeHandler( new ValueChangeHandler<Long>() {
            @Override
            public void onValueChange( ValueChangeEvent<Long> event ) {
                presenter.onUnimprovedSecondsSpentLimit( event.getValue() );
            }
        } );
    }

    @Override
    public void setPresenter( TerminationConfigForm form ) {
        this.presenter = form;
    }

    @Override
    public void setDaysSpentLimit( Long days ) {
        spentLimitDays.setValue( days );
    }

    @Override
    public void setHoursSpentLimit( Long hours ) {
        spentLimitHours.setValue( hours );
    }

    @Override
    public void setMinutesSpentLimit( Long minutes ) {
        spentLimitMinutes.setValue( minutes );
    }

    @Override
    public void setSecondsSpentLimit( Long seconds ) {
        spentLimitSeconds.setValue( seconds );
    }

    @Override
    public void setUnimprovedDaysSpentLimit( Long days ) {
        unimprovedSpendLimitDays.setValue( days );
    }

    @Override
    public void setUnimprovedHoursSpentLimit( Long hours ) {
        unimprovedSpendLimitHours.setValue( hours );
    }

    @Override
    public void setUnimprovedMinutesSpentLimit( Long minutes ) {
        unimprovedSpendLimitMinutes.setValue( minutes );
    }

    @Override
    public void setUnimprovedSecondsSpentLimit( Long seconds ) {
        unimprovedSpendLimitSeconds.setValue( seconds );
    }

    @Override
    public void showSpentLimit( final boolean show ) {
        useSpentLimit.setValue( show );
        spentLimitDays.setEnabled( show );
        spentLimitHours.setEnabled( show );
        spentLimitMinutes.setEnabled( show );
        spentLimitSeconds.setEnabled( show );
    }

    @Override
    public void showUnimprovedSpentLimit( final boolean show ) {
        useUnimprovedSpentLimit.setValue( show );
        unimprovedSpendLimitDays.setEnabled( show );
        unimprovedSpendLimitHours.setEnabled( show );
        unimprovedSpendLimitMinutes.setEnabled( show );
        unimprovedSpendLimitSeconds.setEnabled( show );
    }

    @UiHandler({"useSpentLimit"})
    public void onUseSpentLimitChange( ClickEvent event ) {
        presenter.onUseSpentLimitChange( useSpentLimit.getValue() );
    }

    @UiHandler({"useUnimprovedSpentLimit"})
    public void onUnimprovedSpentLimitChange( ClickEvent event ) {
        presenter.onUseUnimprovedSpentLimitChange( useUnimprovedSpentLimit.getValue() );
    }
}
