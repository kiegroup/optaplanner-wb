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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.inject.Inject;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import org.jboss.errai.ioc.client.api.ManagedInstance;
import org.kie.workbench.common.services.datamodeller.core.DataModel;
import org.kie.workbench.common.services.datamodeller.core.DataObject;
import org.kie.workbench.common.services.datamodeller.core.ObjectProperty;
import org.optaplanner.workbench.screens.domaineditor.model.ObjectPropertyPath;

/**
 * Allows user to select object properties to compare multiple planning entities.
 */
public class DataObjectFieldPicker implements DataObjectFieldPickerView.Presenter,
                                              IsWidget {

    private List<DataObjectFieldPickerItem> fieldPickerItemList = new ArrayList<>();

    ManagedInstance<DataObjectFieldPickerItem> fieldPickerItemProducer;

    private DataModel dataModel;
    private DataObject rootDataObject;

    private DataObjectFieldPickerView view;
    private PlannerDataObjectEditorView.Presenter presenter;

    @Inject
    public DataObjectFieldPicker(final DataObjectFieldPickerView view,
                                 final ManagedInstance<DataObjectFieldPickerItem> fieldPickerItemProducer) {
        this.view = view;
        this.fieldPickerItemProducer = fieldPickerItemProducer;
        view.setPresenter(this);
    }

    public void init(DataModel dataModel,
                     DataObject rootDataObject,
                     List<ObjectPropertyPath> objectPropertyPaths,
                     PlannerDataObjectEditorView.Presenter presenter) {
        this.dataModel = dataModel;
        this.rootDataObject = rootDataObject;
        this.presenter = presenter;

        view.displayComparatorCheckbox(true);
        view.clear();
        for (DataObjectFieldPickerItem item : fieldPickerItemList) {
            fieldPickerItemProducer.destroy(item);
        }
        fieldPickerItemList.clear();

        if (objectPropertyPaths != null) {
            view.setComparatorCheckboxValue(true);
            view.displayFieldPicker(true);
            for (ObjectPropertyPath path : objectPropertyPaths) {
                DataObjectFieldPickerItem fieldPickerItem = addFieldPickerItem();
                for (ObjectProperty field : path.getObjectPropertyPath()) {
                    fieldPickerItem.onFieldAdded(field,
                                                 false);
                    fieldPickerItem.onOrderSelectValueChange(path.isDescending(),
                                                             false);
                }
            }
        }
    }

    public void destroy() {
        view.displayComparatorCheckbox(false);
        view.displayFieldPicker(false);
        view.clear();
        for (DataObjectFieldPickerItem item : fieldPickerItemList) {
            fieldPickerItemProducer.destroy(item);
        }
        fieldPickerItemList.clear();
    }

    @Override
    public void onFieldPickerItemRemoved(DataObjectFieldPickerItem fieldPickerItem) {
        int removeIndex = fieldPickerItemList.indexOf(fieldPickerItem);
        view.removeFieldPickerItem(removeIndex);
        fieldPickerItemList.remove(fieldPickerItem);
        for (int i = removeIndex; i < fieldPickerItemList.size(); i++) {
            fieldPickerItemList.get(i).setFieldPickerItemIndex(i + 1);
        }
        fieldPickerItemProducer.destroy(fieldPickerItem);
        objectPropertyPathChanged(true);
    }

    @Override
    public void onComparatorSpecified(boolean specified) {
        view.displayFieldPicker(specified);
        if (specified) {
            objectPropertyPathChanged(false);
        } else {
            presenter.removeComparatorDefinition(rootDataObject,
                                                 true);
            view.clear();
            for (DataObjectFieldPickerItem item : fieldPickerItemList) {
                fieldPickerItemProducer.destroy(item);
            }
            fieldPickerItemList.clear();
        }
    }

    @Override
    public void onMoveFieldPickerItemUp(DataObjectFieldPickerItem fieldPickerItem) {
        int currentIndex = fieldPickerItemList.indexOf(fieldPickerItem);
        view.moveFieldItemUp(currentIndex);
        if (currentIndex > 0) {
            DataObjectFieldPickerItem swapWithItem = fieldPickerItemList.get(currentIndex - 1);
            swapWithItem.setFieldPickerItemIndex(currentIndex + 1);
            fieldPickerItem.setFieldPickerItemIndex(currentIndex);
            Collections.swap(fieldPickerItemList,
                             currentIndex,
                             currentIndex - 1);
        }
        objectPropertyPathChanged(false);
    }

    @Override
    public void onMoveFieldPickerItemDown(DataObjectFieldPickerItem fieldPickerItem) {
        int currentIndex = fieldPickerItemList.indexOf(fieldPickerItem);
        view.moveFieldItemDown(currentIndex);
        if (currentIndex < fieldPickerItemList.size() - 1) {
            DataObjectFieldPickerItem swapWithItem = fieldPickerItemList.get(currentIndex + 1);
            swapWithItem.setFieldPickerItemIndex(currentIndex + 1);
            fieldPickerItem.setFieldPickerItemIndex(currentIndex + 2);
            Collections.swap(fieldPickerItemList,
                             currentIndex,
                             currentIndex + 1);
        }
        objectPropertyPathChanged(false);
    }

    @Override
    public DataObjectFieldPickerItem addFieldPickerItem() {
        DataObjectFieldPickerItem fieldPickerItem = fieldPickerItemProducer.get();
        fieldPickerItem.init(dataModel,
                             rootDataObject,
                             this);
        fieldPickerItemList.add(fieldPickerItem);
        view.addFieldPickerItem(fieldPickerItem);
        fieldPickerItem.setFieldPickerItemIndex(fieldPickerItemList.size());
        return fieldPickerItem;
    }

    @Override
    public Widget asWidget() {
        return view.asWidget();
    }

    public void objectPropertyPathChanged(boolean itemsRemoved) {
        List<ObjectPropertyPath> objectPropertyPathList = new ArrayList<>();
        for (DataObjectFieldPickerItem item : fieldPickerItemList) {
            if (!item.getObjectPropertyPath().getObjectPropertyPath().isEmpty()) {
                objectPropertyPathList.add(item.getObjectPropertyPath());
            }
        }
        presenter.objectPropertyPathChanged(objectPropertyPathList,
                                            itemsRemoved);
    }

    public boolean isEmpty() {
        return fieldPickerItemList.isEmpty();
    }
}
