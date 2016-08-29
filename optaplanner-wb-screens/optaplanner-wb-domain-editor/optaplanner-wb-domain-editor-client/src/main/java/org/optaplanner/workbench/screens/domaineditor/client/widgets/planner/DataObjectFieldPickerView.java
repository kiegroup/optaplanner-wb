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

import com.google.gwt.user.client.ui.IsWidget;

public interface DataObjectFieldPickerView extends IsWidget {

    interface Presenter {

        DataObjectFieldPickerItem addFieldPickerItem();

        void onFieldPickerItemRemoved( DataObjectFieldPickerItem fieldPickerItem );

        void onMoveFieldPickerItemUp( DataObjectFieldPickerItem fieldPickerItem );

        void onMoveFieldPickerItemDown( DataObjectFieldPickerItem fieldPickerItem );

        void onComparatorSpecified( boolean specified );
    }

    void setPresenter( Presenter presenter );

    void addFieldPickerItem( DataObjectFieldPickerItem fieldPickerItem );

    void removeFieldPickerItem( int position );

    void displayFieldPicker( boolean display );

    void displayComparatorCheckbox( boolean display );

    void setComparatorCheckboxValue( boolean checked );

    void clear();

    void moveFieldItemUp( int currentPosition );

    void moveFieldItemDown( int currentPosition );

}
