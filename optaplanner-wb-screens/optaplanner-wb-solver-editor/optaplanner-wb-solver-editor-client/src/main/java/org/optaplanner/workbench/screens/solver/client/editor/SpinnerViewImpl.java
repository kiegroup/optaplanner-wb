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

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import org.gwtbootstrap3.client.ui.FormLabel;

@Dependent
public class SpinnerViewImpl
        extends Composite
        implements SpinnerView {

    private Spinner presenter;

    interface Binder
            extends
            UiBinder<Widget, SpinnerViewImpl> {

    }

    private static Binder uiBinder = GWT.create( Binder.class );

    @UiField TextBox valueTextBox;

    @UiField FocusPanel up;
    @UiField FocusPanel down;
    @UiField FormLabel label;

    public SpinnerViewImpl() {
        initWidget( uiBinder.createAndBindUi( this ) );
    }

    @Override
    public void setPresenter( Spinner presenter ) {
        this.presenter = presenter;
    }

    @Override
    public void setLabel( String text ) {
        label.setText( text );
    }

    @Override
    public void setValue( long value ) {
        valueTextBox.setValue( Long.toString( value ) );
    }

    @Override
    public void enable() {
        valueTextBox.setEnabled( true );
        up.setVisible( true );
        down.setVisible( true );
    }

    @Override
    public void disable() {
        valueTextBox.setEnabled( false );
        up.setVisible( false );
        down.setVisible( false );
    }

    @UiHandler("valueTextBox") public void handleKeyUp( KeyUpEvent event ) {
        presenter.onValueChange( valueTextBox.getText() );
    }

    @UiHandler("valueTextBox") public void handleChange( ChangeEvent event ) {
        presenter.onValueChange( valueTextBox.getText() );
    }

    @UiHandler("up") public void onUp( ClickEvent event ) {
        presenter.onUp();
    }

    @UiHandler("down") public void onDown( ClickEvent event ) {
        presenter.onDown();
    }
}
