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

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import org.optaplanner.workbench.screens.solver.model.TerminationConfigModel;

@Dependent
public class TerminationConfigForm
        implements IsWidget {

    private TerminationConfigModel model;

    private TerminationConfigFormView view;

    @Inject
    public TerminationConfigForm( TerminationConfigFormView view ) {
        this.view = view;
        view.setPresenter( this );
    }

    @Override
    public Widget asWidget() {
        return view.asWidget();
    }

    public void setModel( TerminationConfigModel terminationConfigModel ) {
        model = terminationConfigModel;
        setValuesIntoView();
    }

    private void setValuesIntoView() {
        if ( isSpentLimitSet() ) {
            view.showSpentLimit( true );
            view.setDaysSpentLimit( model.getDaysSpentLimit() );
            view.setHoursSpentLimit( model.getHoursSpentLimit() );
            view.setMinutesSpentLimit( model.getMinutesSpentLimit() );
            view.setSecondsSpentLimit( model.getSecondsSpentLimit() );
        } else {
            view.showSpentLimit( false );
        }

        if ( isUnimprovedSpentLimitSet() ) {
            view.showUnimprovedSpentLimit( true );
            view.setUnimprovedDaysSpentLimit( model.getUnimprovedDaysSpentLimit() );
            view.setUnimprovedHoursSpentLimit( model.getUnimprovedHoursSpentLimit() );
            view.setUnimprovedMinutesSpentLimit( model.getUnimprovedMinutesSpentLimit() );
            view.setUnimprovedSecondsSpentLimit( model.getUnimprovedSecondsSpentLimit() );
        } else {
            view.showUnimprovedSpentLimit( false );
        }
    }

    private boolean isSpentLimitSet() {
        return model.getDaysSpentLimit() != null
                ||model.getHoursSpentLimit() != null
                || model.getMinutesSpentLimit() != null
                || model.getSecondsSpentLimit() != null;
    }

    private boolean isUnimprovedSpentLimitSet() {
        return model.getUnimprovedDaysSpentLimit() != null
                ||model.getUnimprovedHoursSpentLimit() != null
                || model.getUnimprovedMinutesSpentLimit() != null
                || model.getUnimprovedSecondsSpentLimit() != null;
    }

    public void onHoursSpentLimitChange( Long hours ) {
        model.setHoursSpentLimit( hours );
    }

    public void onMinutesSpentLimitChange( Long minutes ) {
        model.setMinutesSpentLimit( minutes );
    }

    public void onSecondsSpentLimitChange( Long seconds ) {
        model.setSecondsSpentLimit( seconds );
    }

    public void onUnimprovedHoursSpentLimit( Long hours ) {
        model.setUnimprovedHoursSpentLimit( hours );
    }

    public void onDaysSpentLimitChange( Long value ) {
        model.setDaysSpentLimit( value );
    }

    public void onUnimprovedDaysSpentLimitChange( Long value ) {
        model.setUnimprovedDaysSpentLimit( value );
    }

    public void onUnimprovedMinutesSpentLimit( Long minutes ) {
        model.setUnimprovedMinutesSpentLimit( minutes );
    }

    public void onUnimprovedSecondsSpentLimit( Long seconds ) {
        model.setUnimprovedSecondsSpentLimit( seconds );
    }

    public void onUseSpentLimitChange( Boolean value ) {

        if ( value ) {
            model.setDaysSpentLimit( Long.valueOf( 0 ) );
            model.setHoursSpentLimit( Long.valueOf( 0 ) );
            model.setMinutesSpentLimit( Long.valueOf( 0 ) );
            model.setSecondsSpentLimit( Long.valueOf( 0 ) );
        } else {
            model.setDaysSpentLimit( null );
            model.setHoursSpentLimit( null );
            model.setMinutesSpentLimit( null );
            model.setSecondsSpentLimit( null );
        }

        setValuesIntoView();
    }

    public void onUseUnimprovedSpentLimitChange( Boolean value ) {
        if ( value ) {
            model.setUnimprovedDaysSpentLimit( Long.valueOf( 0 ) );
            model.setUnimprovedHoursSpentLimit( Long.valueOf( 0 ) );
            model.setUnimprovedMinutesSpentLimit( Long.valueOf( 0 ) );
            model.setUnimprovedSecondsSpentLimit( Long.valueOf( 0 ) );
        } else {
            model.setUnimprovedDaysSpentLimit( null );
            model.setUnimprovedHoursSpentLimit( null );
            model.setUnimprovedMinutesSpentLimit( null );
            model.setUnimprovedSecondsSpentLimit( null );
        }

        setValuesIntoView();
    }

}
