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
    public TerminationConfigFormViewImpl( final Spinner spentLimitDays,
                                          final Spinner spentLimitHours,
                                          final Spinner spentLimitMinutes,
                                          final Spinner spentLimitSeconds,
                                          final Spinner unimprovedSpentLimitHours,
                                          final Spinner unimprovedSpendLimitHours,
                                          final Spinner unimprovedSpendLimitMinutes,
                                          final Spinner unimprovedSpendLimitSeconds ) {
        this.spentLimitDays = spentLimitDays;
        this.spentLimitDays.addValueChangeHandler( new ValueChangeHandler<Long>() {
            @Override
            public void onValueChange( ValueChangeEvent<Long> event ) {
                presenter.onDaysSpentLimitChange( event.getValue() );
            }
        } );

        this.spentLimitHours = spentLimitHours;
        this.spentLimitHours.addValueChangeHandler( new ValueChangeHandler<Long>() {
            @Override
            public void onValueChange( ValueChangeEvent<Long> event ) {
                presenter.onHoursSpentLimitChange( event.getValue() );
            }
        } );
        this.spentLimitMinutes = spentLimitMinutes;
        this.spentLimitMinutes.addValueChangeHandler( new ValueChangeHandler<Long>() {
            @Override
            public void onValueChange( ValueChangeEvent<Long> event ) {
                presenter.onMinutesSpentLimitChange( event.getValue() );
            }
        } );
        this.spentLimitSeconds = spentLimitSeconds;
        this.spentLimitSeconds.addValueChangeHandler( new ValueChangeHandler<Long>() {
            @Override
            public void onValueChange( ValueChangeEvent<Long> event ) {
                presenter.onSecondsSpentLimitChange( event.getValue() );
            }
        } );

        this.unimprovedSpendLimitDays = unimprovedSpentLimitHours;
        this.unimprovedSpendLimitDays.addValueChangeHandler( new ValueChangeHandler<Long>() {
            @Override
            public void onValueChange( ValueChangeEvent<Long> event ) {
                presenter.onUnimprovedDaysSpentLimitChange( event.getValue() );
            }
        } );
        this.unimprovedSpendLimitHours = unimprovedSpendLimitHours;
        this.unimprovedSpendLimitHours.addValueChangeHandler( new ValueChangeHandler<Long>() {
            @Override
            public void onValueChange( ValueChangeEvent<Long> event ) {
                presenter.onUnimprovedHoursSpentLimit( event.getValue() );
            }
        } );
        this.unimprovedSpendLimitMinutes = unimprovedSpendLimitMinutes;
        this.unimprovedSpendLimitMinutes.addValueChangeHandler( new ValueChangeHandler<Long>() {
            @Override
            public void onValueChange( ValueChangeEvent<Long> event ) {
                presenter.onUnimprovedMinutesSpentLimit( event.getValue() );
            }
        } );
        this.unimprovedSpendLimitSeconds = unimprovedSpendLimitSeconds;
        this.unimprovedSpendLimitSeconds.addValueChangeHandler( new ValueChangeHandler<Long>() {
            @Override
            public void onValueChange( ValueChangeEvent<Long> event ) {
                presenter.onUnimprovedSecondsSpentLimit( event.getValue() );
            }
        } );

        initWidget( uiBinder.createAndBindUi( this ) );
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
        spentLimitDays.setEnabled( show );
        spentLimitHours.setEnabled( show );
        spentLimitMinutes.setEnabled( show );
        spentLimitSeconds.setEnabled( show );
    }

    @Override
    public void showUnimprovedSpentLimit( final boolean show ) {
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
