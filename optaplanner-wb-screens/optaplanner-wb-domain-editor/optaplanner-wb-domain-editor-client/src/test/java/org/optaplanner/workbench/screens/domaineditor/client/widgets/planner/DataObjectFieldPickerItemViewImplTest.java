/*
 * Copyright 2017 Red Hat, Inc. and/or its affiliates.
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

import com.google.gwtmockito.GwtMockitoTestRunner;
import org.jboss.errai.common.client.dom.Div;
import org.jboss.errai.ioc.client.api.ManagedInstance;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kie.workbench.common.services.datamodeller.core.DataObject;
import org.kie.workbench.common.services.datamodeller.core.ObjectProperty;
import org.mockito.Mock;

import static org.mockito.Mockito.*;

@RunWith(GwtMockitoTestRunner.class)
public class DataObjectFieldPickerItemViewImplTest {

    @Mock
    Div fieldPickerItemRow;

    @Mock
    private ManagedInstance<DataObjectFieldPickerItemLabelView> fieldPickerItemLabelViewInstance;

    private DataObjectFieldPickerItemView view;

    @Before
    public void setUp() {
        view = new DataObjectFieldPickerItemViewImpl( fieldPickerItemRow,
                                                      fieldPickerItemLabelViewInstance );
    }

    @Test
    public void addRootItem() {
        DataObject dataObject = mock( DataObject.class );
        when( dataObject.getName() ).thenReturn( "testDataObject" );

        DataObjectFieldPickerItemLabelView labelView = mock( DataObjectFieldPickerItemLabelView.class );
        when( fieldPickerItemLabelViewInstance.get() ).thenReturn( labelView );

        view.addRootItem( dataObject );

        verify( labelView ).setName( "testDataObject" );
        verify( fieldPickerItemRow ).appendChild( labelView.getElement() );
    }

    @Test
    public void addFieldItem() {
        ObjectProperty objectProperty = mock( ObjectProperty.class );
        when( objectProperty.getName() ).thenReturn( "testObjectProperty" );

        DataObjectFieldPickerItemLabelView labelView = mock( DataObjectFieldPickerItemLabelView.class );
        when( fieldPickerItemLabelViewInstance.get() ).thenReturn( labelView );

        view.addFieldItem( objectProperty );

        verify( labelView ).setName( "testObjectProperty" );
        verify( fieldPickerItemRow ).appendChild( labelView.getElement() );
    }

}
