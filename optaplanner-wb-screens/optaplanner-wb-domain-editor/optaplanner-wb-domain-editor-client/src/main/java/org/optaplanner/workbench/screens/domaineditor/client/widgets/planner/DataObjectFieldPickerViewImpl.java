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

import javax.inject.Inject;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.ListGroup;
import org.gwtbootstrap3.client.ui.ListGroupItem;
import org.jboss.errai.common.client.dom.CheckboxInput;
import org.jboss.errai.common.client.dom.Div;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.EventHandler;
import org.jboss.errai.ui.shared.api.annotations.Templated;

@Templated
public class DataObjectFieldPickerViewImpl extends Composite implements DataObjectFieldPickerView {

    @Inject
    @DataField("view")
    Div view;

    @DataField("comparatorCheckbox")
    CheckboxInput comparatorCheckbox;

    @DataField("fieldDiv")
    Div fieldDiv;

    @Inject
    @DataField("fieldList")
    ListGroup fieldList;

    @Inject
    @DataField("addFieldPickerItemButton")
    Button addFieldPickerItemButton;

    private Presenter presenter;

    public DataObjectFieldPickerViewImpl() {

    }

    @Inject
    public DataObjectFieldPickerViewImpl(final CheckboxInput comparatorCheckbox,
                                         final Div fieldDiv) {
        this.comparatorCheckbox = comparatorCheckbox;
        this.fieldDiv = fieldDiv;

        comparatorCheckbox.setHidden(false);
        fieldDiv.setHidden(true);
    }

    @Override
    public void setPresenter(Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void addFieldPickerItem(DataObjectFieldPickerItem fieldPickerItem) {
        ListGroupItem listGroupItem = new ListGroupItem();
        listGroupItem.add(fieldPickerItem);
        fieldList.add(listGroupItem);
    }

    @Override
    public void removeFieldPickerItem(int position) {
        fieldList.remove(position);
    }

    @Override
    public void displayFieldPicker(boolean display) {
        fieldDiv.setHidden(!display);
    }

    @Override
    public void displayComparatorCheckbox(boolean display) {
        view.setHidden(!display);
    }

    @Override
    public void setComparatorCheckboxValue(boolean checked) {
        comparatorCheckbox.setChecked(checked);
    }

    @Override
    public void clear() {
        fieldList.clear();
        comparatorCheckbox.setChecked(false);
    }

    @Override
    public void moveFieldItemUp(int currentPosition) {
        if (currentPosition == 0) {
            return;
        }
        Widget widget = fieldList.getWidget(currentPosition);
        fieldList.remove(widget);
        fieldList.insert(widget,
                         currentPosition - 1);
    }

    @Override
    public void moveFieldItemDown(int currentPosition) {
        if (currentPosition == fieldList.getWidgetCount() - 1) {
            return;
        }
        Widget widget = fieldList.getWidget(currentPosition);
        fieldList.remove(widget);
        fieldList.insert(widget,
                         currentPosition + 1);
    }

    @EventHandler("addFieldPickerItemButton")
    public void onAddFieldPickerItemButtonClicked(ClickEvent event) {
        presenter.addFieldPickerItem();
    }

    @EventHandler("comparatorCheckbox")
    public void onComparatorCheckboxClicked(ChangeEvent event) {
        presenter.onComparatorSpecified(comparatorCheckbox.getChecked());
    }
}
