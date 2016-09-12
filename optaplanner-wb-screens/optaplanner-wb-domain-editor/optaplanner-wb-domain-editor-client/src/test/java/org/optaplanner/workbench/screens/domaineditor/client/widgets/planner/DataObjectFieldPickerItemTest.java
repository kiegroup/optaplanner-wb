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

import java.util.Arrays;

import com.google.gwtmockito.GwtMockitoTestRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kie.workbench.common.services.datamodeller.core.DataModel;
import org.kie.workbench.common.services.datamodeller.core.DataObject;
import org.kie.workbench.common.services.datamodeller.core.ObjectProperty;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

@RunWith(GwtMockitoTestRunner.class)
public class DataObjectFieldPickerItemTest {

    @Mock
    private DataObjectFieldPickerItemView view;

    @Mock
    private DataModel dataModel;

    @Mock
    private DataObject dataObject;

    @Mock
    private DataObjectFieldPicker fieldPicker;

    private DataObjectFieldPickerItem fieldPickerItem;

    @Before
    public void setUp() {
        fieldPickerItem = new DataObjectFieldPickerItem( view );
    }

    @Test
    public void setPresenter() {
        verify( view, times( 1 ) ).setPresenter( fieldPickerItem );
    }

    @Test
    public void init() {
        fieldPickerItem.init( dataModel, dataObject, fieldPicker );
        verify( view, times( 1 ) ).initSelectFieldDropdownOptions( anyList() );
        verify( view, times( 1 ) ).addRootItem( any( DataObject.class ) );
    }

    @Test
    public void onFieldAddedObjectType() {
        fieldPickerItem.init( dataModel, dataObject, fieldPicker );

        ObjectProperty objectProperty = mock( ObjectProperty.class );
        when( objectProperty.getName() ).thenReturn( "testProperty" );
        when( objectProperty.getClassName() ).thenReturn( "bar.foo.TestProperty" );
        when( objectProperty.isBaseType() ).thenReturn( false );
        when( objectProperty.isPrimitiveType() ).thenReturn( false );

        DataObject nestedDataObject = mock( DataObject.class );
        when( nestedDataObject.getProperties() ).thenReturn( Arrays.asList( mock( ObjectProperty.class ) ) );


        when( dataObject.getProperty( "testProperty" ) ).thenReturn( objectProperty );
        when( dataModel.getDataObject( "bar.foo.TestProperty" ) ).thenReturn( nestedDataObject );

        Mockito.reset( view );

        fieldPickerItem.onFieldAdded( objectProperty, true );

        verify( view, times( 1 ) ).addFieldItem( objectProperty );
        verify( view, times( 1 ) ).initSelectFieldDropdownOptions( anyList() );
        verify( view, times( 1 ) ).displaySelectFieldButton( true );
        verify( fieldPicker, times( 1 ) ).objectPropertyPathChanged( false );
    }

    @Test
    public void onFieldAddedPrimitive() {
        fieldPickerItem.init( dataModel, dataObject, fieldPicker );

        ObjectProperty objectProperty = mock( ObjectProperty.class );
        when( objectProperty.getName() ).thenReturn( "testProperty" );
        when( objectProperty.getClassName() ).thenReturn( "java.lang.Integer" );
        when( objectProperty.isBaseType() ).thenReturn( true );
        when( objectProperty.isPrimitiveType() ).thenReturn( false );

        when( dataObject.getProperty( "testProperty" ) ).thenReturn( objectProperty );

        Mockito.reset( view );

        fieldPickerItem.onFieldAdded( objectProperty, true );

        verify( view, times( 1 ) ).addFieldItem( objectProperty );
        verify( view, times( 0 ) ).initSelectFieldDropdownOptions( anyList() );
        verify( view, times( 1 ) ).displaySelectFieldButton( false );
        verify( fieldPicker, times( 1 ) ).objectPropertyPathChanged( false );
    }

    @Test
    public void onFieldRemoved() {
        fieldPickerItem.init( dataModel, dataObject, fieldPicker );

        ObjectProperty objectProperty = mock( ObjectProperty.class );
        when( objectProperty.getName() ).thenReturn( "testProperty" );
        when( objectProperty.getClassName() ).thenReturn( "java.lang.Integer" );
        when( objectProperty.isBaseType() ).thenReturn( true );
        when( objectProperty.isPrimitiveType() ).thenReturn( false );

        when( dataObject.getProperty( "testProperty" ) ).thenReturn( objectProperty );

        fieldPickerItem.onFieldAdded( objectProperty, true );

        Mockito.reset( view, fieldPicker );

        fieldPickerItem.onFieldRemoved( objectProperty );

        verify( view, times( 1 ) ).removeLastFieldItem();
        verify( view, times( 1 ) ).displaySelectFieldButton( true );
        verify( view, times( 1 ) ).initSelectFieldDropdownOptions( anyList() );
        verify( fieldPicker, times( 1 ) ).objectPropertyPathChanged( true );
    }
}
