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

import com.google.gwt.user.client.ui.IsWidget;
import org.kie.workbench.common.services.datamodeller.core.DataObject;
import org.kie.workbench.common.services.datamodeller.core.ObjectProperty;

public interface DataObjectFieldPickerItemView extends IsWidget {

    interface Presenter {

        void onFieldAdded( ObjectProperty field, boolean notify );

        void onFieldRemoved( ObjectProperty objectProperty );

        void onRootLabelRemoved();

        void onMoveFieldItemUp();

        void onMoveFieldItemDown();

        void onOrderSelectValueChange( boolean checked, boolean notify );

        void setFieldPickerItemIndex( int index );

    }

    void setPresenter( Presenter presenter );

    void initSelectFieldDropdownOptions( List<ObjectProperty> options );

    void addRootItem( DataObject rootDataObject );

    void addFieldItem( ObjectProperty objectProperty );

    void removeLastFieldItem();

    void displaySelectFieldButton( boolean display );

    void setOrderSelectDescendingValue( boolean descending );

    void setFieldPickerItemIndex( int index );

}
