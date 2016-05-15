/*
 * Copyright 2016 Red Hat, Inc. and/or its affiliates.
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

import java.util.List;
import javax.annotation.PostConstruct;
import javax.inject.Inject;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;
import org.gwtbootstrap3.client.ui.Alert;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.FormLabel;
import org.gwtbootstrap3.client.ui.ListBox;
import org.gwtbootstrap3.client.ui.TextBox;
import org.gwtbootstrap3.extras.select.client.ui.Option;
import org.gwtbootstrap3.extras.select.client.ui.Select;
import org.optaplanner.workbench.screens.solver.client.resources.i18n.SolverEditorConstants;
import org.optaplanner.workbench.screens.solver.model.TerminationConfigOption;
import org.uberfire.commons.data.Pair;
import org.uberfire.ext.widgets.common.client.common.popups.BaseModal;

public class TerminationPopupViewImpl extends BaseModal implements TerminationPopupView {

    interface Binder extends
            UiBinder<Widget, TerminationPopupViewImpl> {
    }

    private static Binder uiBinder = GWT.create( Binder.class );

    @UiField
    FormLabel terminationTypeLabel;

    @UiField
    Select terminationList;

    @UiField
    FormLabel terminationValueLabel;

    @UiField
    TextBox textTerminationValueInput;

    @UiField(provided = true)
    Spinner numericTerminationValueInput;

    @UiField
    ListBox listTerminationValueInput;

    @UiField
    Button createButton;

    @UiField
    Button createAndContinueButton;

    @UiField
    Button cancelButton;

    @UiField
    Alert alert;

    private Presenter presenter;

    public TerminationPopupViewImpl() {
    }

    @Inject
    public TerminationPopupViewImpl( Spinner numericTerminationValueInput ) {
        this.numericTerminationValueInput = numericTerminationValueInput;
    }

    @PostConstruct
    private void init() {
        setTitle( SolverEditorConstants.INSTANCE.TerminationPopupTitle() );

        setBody( uiBinder.createAndBindUi( this ) );

        terminationList.addChangeHandler( new ChangeHandler() {
            @Override
            public void onChange( ChangeEvent event ) {
                presenter.onTerminationChange();
            }
        } );

        createButton.addClickHandler( new ClickHandler() {
            @Override
            public void onClick( ClickEvent event ) {
                presenter.onCreate();
            }
        } );

        createAndContinueButton.addClickHandler( new ClickHandler() {
            @Override
            public void onClick( ClickEvent event ) {
                presenter.onCreateAndContinue();
            }
        } );

        cancelButton.addClickHandler( new ClickHandler() {
            @Override
            public void onClick( ClickEvent event ) {
                presenter.onCancel();
            }
        } );
    }

    @Override
    public void initTerminationOptionList( List<Pair<String, String>> terminationOptionList ) {
        terminationList.clear();
        for ( Pair<String, String> terminationOption : terminationOptionList ) {
            Option option = new Option();
            option.setText( terminationOption.getK1() );
            option.setValue( terminationOption.getK2() );
            terminationList.add( option );
            terminationList.refresh();
        }
    }

    @Override
    public void initTerminationValueList( List<Pair<String, String>> terminationValueList ) {
        listTerminationValueInput.clear();
        for ( Pair<String, String> pair : terminationValueList ) {
            listTerminationValueInput.addItem( pair.getK1(), pair.getK2() );
        }
    }

    @Override
    public void setNumericTerminationValueMaximum( long maximum ) {
        numericTerminationValueInput.setMax( maximum );
    }

    @Override
    public String getSelectedTermination() {
        return terminationList.getValue();
    }

    @Override
    public Pair<String, String> getTerminationValue() {
        if ( textTerminationValueInput.isVisible() ) {
            return new Pair<>( textTerminationValueInput.getText(), textTerminationValueInput.getText() );
        } else if ( numericTerminationValueInput.isEnabled() ) {
            return new Pair<>( String.valueOf( numericTerminationValueInput.getValue() ), String.valueOf( numericTerminationValueInput.getValue() ) );
        } else if ( listTerminationValueInput.isEnabled() ) {
            return new Pair<>( listTerminationValueInput.getSelectedItemText(), listTerminationValueInput.getSelectedValue() );
        }
        return new Pair<>( "", "" );
    }

    @Override
    public void showTerminationValueLabel( boolean visible ) {
        terminationValueLabel.setVisible( visible );
    }

    @Override
    public void setErrorMessage( String errorMessage ) {
        alert.setText( errorMessage );
        alert.setVisible( errorMessage != null );
    }

    @Override
    public void showTextTerminationValueInput( boolean visible ) {
        textTerminationValueInput.setVisible( visible );
    }

    @Override
    public void showNumericTerminationValueInput( boolean visible ) {
        numericTerminationValueInput.setEnabled( visible );
    }

    @Override
    public void showListTerminationValueInput( boolean visible ) {
        listTerminationValueInput.setVisible( visible );
        listTerminationValueInput.setEnabled( visible );
    }

    @Override
    public void clearInput() {
        textTerminationValueInput.setText( null );
        numericTerminationValueInput.setValue( Long.valueOf( 0 ) );
        listTerminationValueInput.setSelectedIndex( 0 );
    }

    @Override
    public void init( Presenter presenter ) {
        this.presenter = presenter;
    }

    @Override
    public void setSelectedTerminationOption( TerminationConfigOption terminationOption ) {
        terminationList.setValue( terminationOption.name() );
    }
}
