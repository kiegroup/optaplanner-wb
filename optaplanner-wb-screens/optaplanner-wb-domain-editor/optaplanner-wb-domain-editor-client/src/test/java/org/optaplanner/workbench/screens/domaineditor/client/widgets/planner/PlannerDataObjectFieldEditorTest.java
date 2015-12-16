/*
 * Copyright 2015 Red Hat, Inc. and/or its affiliates.
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
import java.util.List;

import com.google.gwtmockito.GwtMockitoTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kie.workbench.common.screens.datamodeller.client.util.AnnotationValueHandler;
import org.kie.workbench.common.services.datamodeller.core.DataObject;
import org.kie.workbench.common.services.datamodeller.core.ObjectProperty;
import org.kie.workbench.common.services.datamodeller.core.impl.AnnotationImpl;
import org.mockito.Mock;
import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.solution.PlanningEntityCollectionProperty;
import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.optaplanner.core.api.domain.valuerange.ValueRangeProvider;
import org.optaplanner.core.api.domain.variable.PlanningVariable;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;
import org.optaplanner.core.impl.domain.solution.AbstractSolution;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith( GwtMockitoTestRunner.class )
public class PlannerDataObjectFieldEditorTest
                extends PlannerEditorBaseTest {

    @Mock
    private PlannerDataObjectFieldEditorView view;

    protected PlannerDataObjectFieldEditor createFieldEditor() {
        PlannerDataObjectFieldEditor fieldEditor = new PlannerDataObjectFieldEditor( view,
                handlerRegistry,
                dataModelerEvent,
                commandBuilder );
        return fieldEditor;
    }

    @Test
    public void loadDataObjectFieldTest() {

        PlannerDataObjectFieldEditor fieldEditor = createFieldEditor();

        DataObject dataObject = context.getDataObject();
        ObjectProperty field1 = dataObject.getProperty( "field1" );
        //emulates the selection of field1
        context.setObjectProperty( field1 );

        //The domain editors typically reacts upon DataModelerContext changes.
        //when the context changes the editor will typically be reloaded.
        fieldEditor.onContextChange( context );

        //the view should be populated with the values from the field.
        verify( view, times( 1 ) ).clear();
        verify( view, times( 1 ) ).showPlanningFieldPropertiesNotAvailable( true );
    }

    @Test
    public void fieldPlanningEntitySettingsTest() {

        PlannerDataObjectFieldEditor fieldEditor = createFieldEditor();

        //first configure the DataObject as a PlanningEntity
        DataObject dataObject = context.getDataObject();
        dataObject.addAnnotation( new AnnotationImpl( context.getAnnotationDefinition( PlanningEntity.class.getName() ) ) );

        ObjectProperty field1 = dataObject.getProperty( "field1" );
        //emulates the selection of field1
        context.setObjectProperty( field1 );

        //The domain editors typically reacts upon DataModelerContext changes.
        //when the context changes the editor will typically be reloaded.
        fieldEditor.onContextChange( context );

        //emulate the user input
        when( view.getPlanningVariableValue() ).thenReturn( true );
        when( view.getValueRangeProviderRefsValue() ).thenReturn( "valueRangeProviderRefsValue" );

        fieldEditor.onPlanningVariableChange();
        fieldEditor.onValueRangeProviderRefsChange();

        List<String> valueRangeProviderRefs = new ArrayList<String>(  );
        valueRangeProviderRefs.add( "valueRangeProviderRefsValue" );
        assertNotNull( field1.getAnnotation( PlanningVariable.class.getName() ) );
        assertEquals( valueRangeProviderRefs,
                field1.getAnnotation( PlanningVariable.class.getName() ).getValue( "valueRangeProviderRefs" ) );

    }

    @Test
    public void planningSolutionSettingsTest() {

        PlannerDataObjectFieldEditor fieldEditor = createFieldEditor();

        //first configure the DataObject as a PlanningSolution
        DataObject dataObject = context.getDataObject();
        dataObject.addAnnotation( new AnnotationImpl( context.getAnnotationDefinition( PlanningSolution.class.getName() ) ) );
        dataObject.setSuperClassName( AbstractSolution.class.getName()+ "<" + HardSoftScore.class.getName() + ">" );

        ObjectProperty field1 = dataObject.getProperty( "field1" );
        //emulates the selection of field1
        context.setObjectProperty( field1 );

        //The domain editors typically reacts upon DataModelerContext changes.
        //when the context changes the editor will typically be reloaded.
        fieldEditor.onContextChange( context );

        when( view.getPlanningEntityCollectionValue() ).thenReturn( true );
        when( view.getValueRangeProviderValue() ).thenReturn( true );
        when( view.getValueRangeProviderIdValue() ).thenReturn( "valueRangeProviderIdValue" );

        //emulate the user input
        fieldEditor.onPlanningEntityCollectionChange();
        fieldEditor.onValueRangeProviderChange();
        fieldEditor.onValueRangeProviderIdChange();

        assertNotNull( field1.getAnnotation( PlanningEntityCollectionProperty.class.getName() ) );
        assertNotNull( field1.getAnnotation( ValueRangeProvider.class.getName() ) );
        assertEquals( "valueRangeProviderIdValue",
                AnnotationValueHandler.getStringValue( field1, ValueRangeProvider.class.getName(), "id" ) );



    }

}
