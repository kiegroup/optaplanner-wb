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

package org.optaplanner.workbench.screens.domaineditor.client.widgets.planner;

import java.util.List;
import javax.inject.Inject;

import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import org.gwtbootstrap3.client.ui.AnchorListItem;
import org.gwtbootstrap3.client.ui.DropDownMenu;
import org.gwtbootstrap3.client.ui.Label;
import org.gwtbootstrap3.client.ui.constants.IconType;
import org.jboss.errai.common.client.dom.Button;
import org.jboss.errai.common.client.dom.Div;
import org.jboss.errai.common.client.dom.Select;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.EventHandler;
import org.jboss.errai.ui.shared.api.annotations.Templated;
import org.kie.workbench.common.services.datamodeller.core.ObjectProperty;

@Templated
public class DataObjectFieldPickerItemViewImpl extends Composite implements DataObjectFieldPickerItemView {

    @Inject
    @DataField("view")
    Div view;

    @DataField("fieldPickerItemRow")
    HorizontalPanel fieldPickerItemRow;

    @Inject
    @DataField("selectFieldButton")
    Button selectFieldButton;

    @Inject
    @DataField("selectFieldDropdown")
    DropDownMenu selectFieldDropdown;

    @DataField("moveUpButton")
    org.gwtbootstrap3.client.ui.Button moveUpButton;

    @DataField("moveDownButton")
    org.gwtbootstrap3.client.ui.Button moveDownButton;

    @Inject
    @DataField("orderSelect")
    Select orderSelect;

    private com.google.gwt.user.client.ui.Label sortIndexLabel;

    private Presenter presenter;

    @Inject
    public DataObjectFieldPickerItemViewImpl( final HorizontalPanel fieldPickerItemRow,
                                              final com.google.gwt.user.client.ui.Label sortIndexLabel,
                                              final org.gwtbootstrap3.client.ui.Button moveUpButton,
                                              final org.gwtbootstrap3.client.ui.Button moveDownButton ) {
        this.fieldPickerItemRow = fieldPickerItemRow;
        this.sortIndexLabel = sortIndexLabel;
        this.moveUpButton = moveUpButton;
        this.moveDownButton = moveDownButton;

        sortIndexLabel.getElement().getStyle().setPaddingRight( 5, Style.Unit.PX );
        sortIndexLabel.getElement().getStyle().setFontWeight( Style.FontWeight.BOLD );
        fieldPickerItemRow.add( sortIndexLabel );

        moveUpButton.setIcon( IconType.ARROW_UP );
        moveDownButton.setIcon( IconType.ARROW_DOWN );
    }


    @Override
    public void setPresenter( Presenter presenter ) {
        this.presenter = presenter;
    }

    @Override
    public void initSelectFieldDropdownOptions( List<String> options ) {
        selectFieldDropdown.clear();
        for ( String option : options ) {
            AnchorListItem listItem = new AnchorListItem( option );
            listItem.addClickHandler( c -> presenter.onFieldAdded( option, true ) );
            selectFieldDropdown.add( listItem );
        }
    }

    @Override
    public void addFieldItem( String field, ObjectProperty objectProperty, boolean rootItem ) {
        if ( rootItem ) {
            Label label = new Label( field );
            label.addClickHandler( c -> presenter.onRootLabelRemoved() );
            label.setMarginRight( 5 );
            fieldPickerItemRow.add( label );
        } else {
            Label label = new Label( field );
            label.addClickHandler( c -> presenter.onFieldRemoved( objectProperty ) );
            label.setMarginRight( 5 );
            fieldPickerItemRow.add( label );
        }
    }

    @Override
    public void removeLastFieldItem() {
        Label label = (Label) fieldPickerItemRow.getWidget( fieldPickerItemRow.getWidgetCount() - 1 );
        fieldPickerItemRow.remove( label );
    }

    @Override
    public void displaySelectFieldButton( boolean display ) {
        selectFieldButton.getStyle().setProperty( "display", display ? "inline" : "none" );
    }

    @Override
    public void setOrderSelectDescendingValue( boolean descending ) {
        orderSelect.setValue( descending ? "desc" : "asc" );
    }

    @Override
    public void setFieldPickerItemIndex( int index ) {
        sortIndexLabel.setText( index + "." );
    }

    @EventHandler("moveUpButton")
    public void onMoveUpButtonClicked( ClickEvent event ) {
        presenter.onMoveFieldItemUp();
    }

    @EventHandler("moveDownButton")
    public void onMoveDownButtonClicked( ClickEvent event ) {
        presenter.onMoveFieldItemDown();
    }

    @EventHandler("orderSelect")
    public void onOrderSelectValueChange( ChangeEvent event ) {
        presenter.onOrderSelectValueChange( "desc".equals( orderSelect.getValue() ), true );
    }

}
