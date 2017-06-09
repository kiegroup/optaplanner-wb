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
import javax.annotation.PostConstruct;
import javax.inject.Inject;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.Composite;
import org.gwtbootstrap3.client.ui.AnchorListItem;
import org.gwtbootstrap3.client.ui.DropDownMenu;
import org.gwtbootstrap3.client.ui.constants.IconType;
import org.jboss.errai.common.client.dom.Button;
import org.jboss.errai.common.client.dom.Div;
import org.jboss.errai.common.client.dom.Label;
import org.jboss.errai.ioc.client.api.ManagedInstance;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.EventHandler;
import org.jboss.errai.ui.shared.api.annotations.Templated;
import org.kie.workbench.common.services.datamodeller.core.DataObject;
import org.kie.workbench.common.services.datamodeller.core.ObjectProperty;
import org.uberfire.mvp.Command;

@Templated
public class DataObjectFieldPickerItemViewImpl extends Composite implements DataObjectFieldPickerItemView {

    @Inject
    @DataField("view")
    Div view;

    @Inject
    @DataField("fieldPickerItemRow")
    Div fieldPickerItemRow;

    @Inject
    @DataField("selectFieldButton")
    Button selectFieldButton;

    @Inject
    @DataField("selectFieldDropdown")
    DropDownMenu selectFieldDropdown;

    @Inject
    @DataField("moveUpButton")
    org.gwtbootstrap3.client.ui.Button moveUpButton;

    @Inject
    @DataField("moveDownButton")
    org.gwtbootstrap3.client.ui.Button moveDownButton;

    @Inject
    @DataField("ascSortButton")
    org.gwtbootstrap3.client.ui.Button ascSortButton;

    @Inject
    @DataField("descSortButton")
    org.gwtbootstrap3.client.ui.Button descSortButton;

    @Inject
    @DataField("sortIndexLabel")
    Label sortIndexLabel;

    private ManagedInstance<DataObjectFieldPickerItemLabelView> fieldPickerItemLabelViewInstance;

    private Presenter presenter;

    @Inject
    public DataObjectFieldPickerItemViewImpl(final Div fieldPickerItemRow,
                                             final ManagedInstance<DataObjectFieldPickerItemLabelView> fieldPickerItemLabelViewInstance) {
        this.fieldPickerItemRow = fieldPickerItemRow;
        this.fieldPickerItemLabelViewInstance = fieldPickerItemLabelViewInstance;
    }

    @PostConstruct
    public void init() {
        moveUpButton.setIcon(IconType.ARROW_UP);
        moveDownButton.setIcon(IconType.ARROW_DOWN);
        ascSortButton.setIcon(IconType.SORT_ALPHA_ASC);
        descSortButton.setIcon(IconType.SORT_ALPHA_DESC);
    }

    @Override
    public void setPresenter(Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void initSelectFieldDropdownOptions(List<ObjectProperty> options) {
        selectFieldDropdown.clear();
        for (ObjectProperty option : options) {
            AnchorListItem listItem = new AnchorListItem(option.getName());
            listItem.addClickHandler(c -> presenter.onFieldAdded(option,
                                                                 true));
            selectFieldDropdown.add(listItem);
        }
    }

    @Override
    public void addRootItem(DataObject rootDataObject) {
        createFieldPickerItemLabel(rootDataObject.getName(),
                                   () -> presenter.onRootLabelRemoved());
    }

    @Override
    public void addFieldItem(ObjectProperty objectProperty) {
        createFieldPickerItemLabel(objectProperty.getName(),
                                   () -> presenter.onFieldRemoved(objectProperty));
    }

    private void createFieldPickerItemLabel(String name,
                                            Command removeLabelCommand) {
        DataObjectFieldPickerItemLabelView labelView = fieldPickerItemLabelViewInstance.get();
        labelView.setName(name);
        labelView.setRemoveLabelCommand(removeLabelCommand);
        fieldPickerItemRow.appendChild(labelView.getElement());
    }

    @Override
    public void removeLastFieldItem() {
        fieldPickerItemRow.removeChild(fieldPickerItemRow.getLastChild());
    }

    @Override
    public void displaySelectFieldButton(boolean display) {
        selectFieldButton.setDisabled(!display);
    }

    @Override
    public void setOrderSelectDescendingValue(boolean descending) {
        ascSortButton.setVisible(!descending);
        descSortButton.setVisible(descending);
    }

    @Override
    public void setFieldPickerItemIndex(int index) {
        sortIndexLabel.setTextContent(index + ".");
    }

    @EventHandler("moveUpButton")
    public void onMoveUpButtonClicked(ClickEvent event) {
        presenter.onMoveFieldItemUp();
    }

    @EventHandler("moveDownButton")
    public void onMoveDownButtonClicked(ClickEvent event) {
        presenter.onMoveFieldItemDown();
    }

    @EventHandler("ascSortButton")
    public void onAscSortButtonClicked(ClickEvent event) {
        presenter.onOrderSelectValueChange(true,
                                           true);
    }

    @EventHandler("descSortButton")
    public void onDescSortButtonClicked(ClickEvent event) {
        presenter.onOrderSelectValueChange(false,
                                           true);
    }
}
