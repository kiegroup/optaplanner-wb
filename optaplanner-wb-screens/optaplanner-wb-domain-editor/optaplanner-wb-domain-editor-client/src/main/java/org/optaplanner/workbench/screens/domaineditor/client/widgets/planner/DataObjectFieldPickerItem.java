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

import java.util.stream.Collectors;
import javax.inject.Inject;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import org.kie.workbench.common.screens.datamodeller.client.util.DataModelerUtils;
import org.kie.workbench.common.services.datamodeller.core.DataModel;
import org.kie.workbench.common.services.datamodeller.core.DataObject;
import org.kie.workbench.common.services.datamodeller.core.ObjectProperty;
import org.optaplanner.workbench.screens.domaineditor.model.ObjectPropertyPath;
import org.optaplanner.workbench.screens.domaineditor.model.ObjectPropertyPathImpl;

public class DataObjectFieldPickerItem implements DataObjectFieldPickerItemView.Presenter,
                                                  IsWidget {

    private DataObjectFieldPickerItemView view;

    private DataModel dataModel;

    private ObjectPropertyPath objectPropertyPath = new ObjectPropertyPathImpl();

    private DataObject rootDataObject;
    private DataObjectFieldPicker picker;

    @Inject
    public DataObjectFieldPickerItem(DataObjectFieldPickerItemView view) {
        this.view = view;
        view.setPresenter(this);
    }

    public void init(DataModel dataModel,
                     DataObject rootDataObject,
                     DataObjectFieldPicker picker) {
        this.dataModel = dataModel;
        this.rootDataObject = rootDataObject;
        this.picker = picker;
        initSelectFieldDropdownOptions(rootDataObject);
        view.addRootItem(rootDataObject);
        view.setOrderSelectDescendingValue(false);
    }

    private void initSelectFieldDropdownOptions(DataObject dataObject) {
        view.initSelectFieldDropdownOptions(dataObject.getProperties().stream().filter(p -> DataModelerUtils.isManagedProperty(p)).collect(Collectors.toList()));
    }

    @Override
    public void onFieldAdded(ObjectProperty objectProperty,
                             boolean notify) {
        objectPropertyPath.appendObjectProperty(objectProperty);
        view.addFieldItem(objectProperty);
        if (objectProperty.isBaseType() || objectProperty.isPrimitiveType()) {
            view.displaySelectFieldButton(false);
        } else {
            DataObject dataObject = dataModel.getDataObject(objectProperty.getClassName());
            initSelectFieldDropdownOptions(dataObject);
            view.displaySelectFieldButton(true);
        }
        if (notify) {
            picker.objectPropertyPathChanged(false);
        }
    }

    @Override
    public void onFieldRemoved(ObjectProperty objectProperty) {
        for (int i = objectPropertyPath.getObjectPropertyPath().size() - 1; i >= 0; i--) {
            ObjectProperty currentObjectProperty = objectPropertyPath.getObjectPropertyPath().get(i);
            objectPropertyPath.getObjectPropertyPath().remove(i);
            view.removeLastFieldItem();
            if (currentObjectProperty.equals(objectProperty)) {
                break;
            }
        }
        DataObject dataObject;
        if (objectPropertyPath.getObjectPropertyPath().isEmpty()) {
            dataObject = rootDataObject;
        } else {
            ObjectProperty parentObjectProperty = objectPropertyPath.getObjectPropertyPath().get(objectPropertyPath.getObjectPropertyPath().size() - 1);
            dataObject = dataModel.getDataObject(parentObjectProperty.getClassName());
        }
        initSelectFieldDropdownOptions(dataObject);
        view.displaySelectFieldButton(true);
        picker.objectPropertyPathChanged(true);
    }

    @Override
    public void onRootLabelRemoved() {
        picker.onFieldPickerItemRemoved(this);
    }

    @Override
    public void onMoveFieldItemUp() {
        picker.onMoveFieldPickerItemUp(this);
    }

    @Override
    public void onMoveFieldItemDown() {
        picker.onMoveFieldPickerItemDown(this);
    }

    @Override
    public void onOrderSelectValueChange(boolean descending,
                                         boolean notify) {
        view.setOrderSelectDescendingValue(descending);
        objectPropertyPath.setDescending(descending);
        if (notify) {
            picker.objectPropertyPathChanged(false);
        }
    }

    @Override
    public void setFieldPickerItemIndex(int index) {
        view.setFieldPickerItemIndex(index);
    }

    public ObjectPropertyPath getObjectPropertyPath() {
        return objectPropertyPath;
    }

    @Override
    public Widget asWidget() {
        return view.asWidget();
    }
}
