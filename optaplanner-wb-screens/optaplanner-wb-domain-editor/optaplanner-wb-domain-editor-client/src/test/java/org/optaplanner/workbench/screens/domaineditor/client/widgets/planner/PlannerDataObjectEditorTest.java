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

import com.google.gwtmockito.GwtMockitoTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kie.workbench.common.services.datamodeller.core.DataObject;
import org.mockito.Mock;
import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.solution.PlanningSolution;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith( GwtMockitoTestRunner.class )
public class PlannerDataObjectEditorTest
            extends PlannerEditorBaseTest {

    @Mock
    private PlannerDataObjectEditorView view;

    protected PlannerDataObjectEditor createObjectEditor() {
        PlannerDataObjectEditor objectEditor = new PlannerDataObjectEditor( view,
                handlerRegistry,
                dataModelerEvent,
                commandBuilder );
        return objectEditor;
    }

    @Test
    public void loadDataObjectTest() {
        PlannerDataObjectEditor objectEditor = createObjectEditor();

        //The domain editors typically reacts upon DataModelerContext changes.
        //when the context changes the editor will typically be reloaded.
        objectEditor.onContextChange( context );

        //the view should be populated with the values from the dataObject, initially with no
        //planner settings.
        verify( view, times( 1 ) ).clear();
        verify( view, times( 1 ) ).setPlanningEntityValue( false );
        verify( view, times( 1 ) ).setPlanningSolutionValue( false );
        verify( view, times( 1 ) ).showPlanningSolutionScoreType( false );
        verify( view, times( 2 ) ).setNotInPlanningValue( true );
    }

    @Test
    public void changeToPlanningEntityTest() {

        PlannerDataObjectEditor objectEditor = createObjectEditor();

        //load the editor.
        objectEditor.onContextChange( context );

        //emulate user input.
        when( view.getPlanningEntityValue() ).thenReturn( true );

        //notify the presenter about the changes in the UI
        objectEditor.onPlanningEntityChange();

        DataObject dataObject = context.getDataObject();

        //the dataObject should have been now configured as a PlanningEntity
        verify( view, times( 1 ) ).getPlanningEntityValue();
        assertNotNull( dataObject.getAnnotation( PlanningEntity.class.getName() ) );

    }

    @Test
    public void changeToPlanningSolutionTest() {

        PlannerDataObjectEditor objectEditor = createObjectEditor();

        //load the editor.
        objectEditor.onContextChange( context );

        //emulate user input.
        when( view.getPlanningSolutionValue() ).thenReturn( true );

        //notify the presenter about the changes in the UI
        objectEditor.onPlanningSolutionChange();

        DataObject dataObject = context.getDataObject();

        //the dataObject should have been now configured as a PlanningEntity
        verify( view, times( 1 ) ).getPlanningSolutionValue();
        verify( view, times( 1 ) ).showPlanningSolutionScoreType( true );

        //the dataObject should have been now configured as a HardSoftCore PlanningSolution by default.
        assertNotNull( dataObject.getAnnotation( PlanningSolution.class.getName() ) );
        assertEquals( "org.optaplanner.core.impl.domain.solution.AbstractSolution<org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore>"
                , dataObject.getSuperClassName() );

    }
}
