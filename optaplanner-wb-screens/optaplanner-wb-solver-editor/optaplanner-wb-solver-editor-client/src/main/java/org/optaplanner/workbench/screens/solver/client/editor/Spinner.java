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

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

@Dependent
public class Spinner
        extends HasValueChangeHandlersImpl<Long>
        implements IsWidget {

    private long value = 0;
    private long max = 0;

    private SpinnerView view;

    public Spinner() {
    }

    @Inject
    public Spinner( SpinnerView view ) {
        this.view = view;
        view.setPresenter( this );
    }

    public void setValue( Long value ) {
        if ( value == null ) {
            value = Long.valueOf( 0 );
        }

        this.value = value;
        view.setValue( value );
        ValueChangeEvent.fire( this, value );
    }

    public void setMax( int max ) {
        this.max = max;
    }

    public void onUp() {
        if ( max != 0 && value >= max ) {
            setValue( Long.valueOf( 0 ) );
        } else {
            setValue( value + 1 );
        }
    }

    public void onDown() {
        if ( max != 0 && value > 0 ) {
            setValue( value - 1 );
        } else {
            setValue( max );
        }
    }

    public void onValueChange( String newValue ) {
        try {
            long newLongValue = Long.parseLong( newValue );
            if ( max == 0 || (newLongValue > 0 && newLongValue <= max) ) {
                setValue( newLongValue );
            } else {
                view.setValue( value );
            }
        } catch (NumberFormatException e) {
            view.setValue( value );
        }
    }

    public void setEnabled( boolean enabled ) {
        if ( enabled ) {
            view.enable();
        } else {
            view.disable();
            value = 0;
            view.setValue( value );
        }
    }

    @Override
    public Widget asWidget() {
        return view.asWidget();
    }

    public void setText( String text ) {
        view.setLabel( text );
    }
}
