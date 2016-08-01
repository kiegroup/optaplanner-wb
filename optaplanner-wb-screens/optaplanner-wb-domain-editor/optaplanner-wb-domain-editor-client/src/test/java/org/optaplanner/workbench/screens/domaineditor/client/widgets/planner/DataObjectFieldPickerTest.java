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

import com.google.gwtmockito.GwtMockitoTestRunner;
import org.jboss.errai.ioc.client.api.ManagedInstance;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kie.workbench.common.services.datamodeller.core.DataModel;
import org.kie.workbench.common.services.datamodeller.core.DataObject;
import org.mockito.Mock;
import org.optaplanner.workbench.screens.domaineditor.model.ComparatorObject;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

@RunWith(GwtMockitoTestRunner.class)
public class DataObjectFieldPickerTest {

    @Mock
    private DataObjectFieldPickerView view;

    @Mock
    private ManagedInstance<DataObjectFieldPickerItem> fieldPickerItemProducer;

    @Mock
    private PlannerDataObjectEditor editor;

    private DataObjectFieldPicker fieldPicker;

    @Before
    public void setUp() {
        fieldPicker = new DataObjectFieldPicker( view, fieldPickerItemProducer );
    }

    @Test
    public void setPresenter() {
        verify( view, times( 1 ) ).setPresenter( fieldPicker );
    }

    @Test
    public void initWhenComparatorObjectSpecified() {
        initFieldPicker();
        verify( view, times( 1 ) ).displayComparatorCheckbox( true );
        verify( view, times( 1 ) ).displayFieldPicker( true );
        verify( view, times( 1 ) ).setComparatorCheckboxValue( true );
        verify( view, times( 1 ) ).clear();
    }

    @Test
    public void initWhenComparatorObjectNotSpecified() {
        fieldPicker.init( mock( DataModel.class ), mock( DataObject.class ), null, editor );
        verify( view, times( 1 ) ).displayComparatorCheckbox( true );
        verify( view, times( 0 ) ).displayFieldPicker( anyBoolean() );
        verify( view, times( 0 ) ).setComparatorCheckboxValue( anyBoolean() );
        verify( view, times( 1 ) ).clear();
    }

    @Test
    public void addFieldPickerItem() {
        initFieldPicker();
        when( fieldPickerItemProducer.get() ).thenReturn( mock( DataObjectFieldPickerItem.class ) );
        fieldPicker.addFieldPickerItem();
        verify( view, times( 1 ) ).addFieldPickerItem( any( DataObjectFieldPickerItem.class ) );
    }

    @Test
    public void onFieldPickerItemRemoved() {
        initFieldPicker();
        DataObjectFieldPickerItem item = new DataObjectFieldPickerItem( mock( DataObjectFieldPickerItemView.class ) );
        when( fieldPickerItemProducer.get() ).thenReturn( item );
        fieldPicker.addFieldPickerItem();

        fieldPicker.onFieldPickerItemRemoved( item );
        verify( view, times( 1 ) ).removeFieldPickerItem( anyInt() );
        verify( editor, times( 1 ) ).objectPropertyPathChanged( anyList() );
    }

    @Test
    public void onMoveFieldPickerItemUp() {
        initFieldPicker();
        fieldPicker.onMoveFieldPickerItemUp( any( DataObjectFieldPickerItem.class ) );
        verify( view, times( 1 ) ).moveFieldItemUp( anyInt() );
    }

    @Test
    public void onMoveFieldPickerItemDown() {
        initFieldPicker();
        fieldPicker.onMoveFieldPickerItemDown( any( DataObjectFieldPickerItem.class ) );
        verify( view, times( 1 ) ).moveFieldItemDown( anyInt() );
    }

    @Test
    public void destroy() {
        fieldPicker.destroy();
        verify( view, times( 1 ) ).displayFieldPicker( false );
        verify( view, times( 1 ) ).displayComparatorCheckbox( false );
        verify( view, times( 1 ) ).clear();
    }

    private void initFieldPicker() {
        fieldPicker.init( mock( DataModel.class ), mock( DataObject.class ), mock( ComparatorObject.class ), editor );
    }
}
