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

import java.util.Arrays;
import java.util.Collections;

import javax.annotation.Generated;

import com.google.gwtmockito.GwtMockitoTestRunner;
import org.jboss.errai.common.client.api.Caller;
import org.jboss.errai.ui.client.local.spi.TranslationService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kie.workbench.common.screens.datamodeller.client.widgets.DataModelerEditorsTestHelper;
import org.kie.workbench.common.services.datamodeller.core.Annotation;
import org.kie.workbench.common.services.datamodeller.core.AnnotationDefinition;
import org.kie.workbench.common.services.datamodeller.core.DataObject;
import org.kie.workbench.common.services.datamodeller.core.Method;
import org.kie.workbench.common.services.datamodeller.core.Parameter;
import org.kie.workbench.common.services.datamodeller.core.Type;
import org.kie.workbench.common.services.datamodeller.core.Visibility;
import org.kie.workbench.common.services.datamodeller.core.impl.MethodImpl;
import org.kie.workbench.common.services.datamodeller.core.impl.ParameterImpl;
import org.kie.workbench.common.services.datamodeller.core.impl.TypeImpl;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.solution.PlanningScore;
import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.optaplanner.core.api.score.buildin.bendable.BendableScore;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;
import org.optaplanner.core.impl.domain.solution.AbstractSolution;
import org.optaplanner.workbench.screens.domaineditor.service.ComparatorDefinitionService;
import org.optaplanner.workbench.screens.domaineditor.model.PlannerDomainAnnotations;
import org.uberfire.commons.data.Pair;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(GwtMockitoTestRunner.class)
public class PlannerDataObjectEditorTest
        extends PlannerEditorBaseTest {

    @Mock
    private PlannerDataObjectEditorView view;

    @Mock
    private TranslationService translationService;

    @Mock
    private Caller<ComparatorDefinitionService> comparatorDefinitionService;

    protected PlannerDataObjectEditor createObjectEditor() {
        PlannerDataObjectEditor objectEditor = new PlannerDataObjectEditor( view,
                handlerRegistry,
                dataModelerEvent,
                commandBuilder,
                translationService,
                comparatorDefinitionService );
        return objectEditor;
    }

    @Test
    public void loadDataObject() {
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
        verify( view, times( 1 ) ).destroyFieldPicker();
        verify( view, times( 1 ) ).showPlanningSolutionBendableScoreInput( false );
    }

    @Test
    public void loadDataObjectWithBendableScoreType() {
        PlannerDataObjectEditor objectEditor = createObjectEditor();

        DataObject dataObject = context.getDataObject();

        dataObject.addAnnotation( DataModelerEditorsTestHelper.createAnnotation( PlanningSolution.class, null, null ) );

        Type getScoreMethodReturnType = new TypeImpl( BendableScore.class.getName() );
        Method getScoreMethod = new MethodImpl( "getScore", Collections.EMPTY_LIST, "return score;", getScoreMethodReturnType, Visibility.PUBLIC );
        getScoreMethod.addAnnotation( DataModelerEditorsTestHelper.createAnnotation( PlanningScore.class, new Pair( "bendableHardLevelsSize", 5 ), new Pair( "bendableSoftLevelsSize", 10 ) ) );
        getScoreMethod.addAnnotation( DataModelerEditorsTestHelper.createAnnotation( Generated.class, null, null ) );
        dataObject.addMethod( getScoreMethod );

        Parameter setScoreParameter = new ParameterImpl( new TypeImpl( BendableScore.class.getName() ), "score" );
        Type setScoreParameterReturnType = new TypeImpl( void.class.getName() );
        Method setScoreMethod = new MethodImpl( "setScore", Arrays.asList( setScoreParameter ), "this.score = score;", setScoreParameterReturnType, Visibility.PUBLIC );
        setScoreMethod.addAnnotation( DataModelerEditorsTestHelper.createAnnotation( Generated.class, null, null ) );
        dataObject.addMethod( setScoreMethod );

        objectEditor.onContextChange( context );

        verify( view, times( 1 ) ).setPlanningSolutionBendableScoreHardLevelsSize( 5 );
        verify( view, times( 1 ) ).setPlanningSolutionBendableScoreSoftLevelsSize( 10 );
    }

    @Test
    public void changeToPlanningEntity() {

        PlannerDataObjectEditor objectEditor = createObjectEditor();

        //load the editor.
        objectEditor.onContextChange( context );

        // reset state changed by onContextChange
        Mockito.reset( view );

        //emulate user input.
        when( view.getPlanningEntityValue() ).thenReturn( true );

        //notify the presenter about the changes in the UI
        objectEditor.onPlanningEntityChange();

        DataObject dataObject = context.getDataObject();

        //the dataObject should have been now configured as a PlanningEntity
        verify( view, times( 1 ) ).getPlanningEntityValue();
        assertNotNull( dataObject.getAnnotation( PlanningEntity.class.getName() ) );

        verify( view, times( 1 ) ).initFieldPicker( context.getDataModel(), dataObject, null );

        verify( view, times( 1 ) ).showPlanningSolutionScoreType( false );
        verify( view, times( 1 ) ).showPlanningSolutionBendableScoreInput( false );
        verify( view, times( 1 ) ).setPlanningSolutionBendableScoreHardLevelsSize( 0 );
        verify( view, times( 1 ) ).setPlanningSolutionBendableScoreSoftLevelsSize( 0 );
    }

    @Test
    public void changeToPlanningSolution() {

        PlannerDataObjectEditor objectEditor = createObjectEditor();

        //load the editor.
        objectEditor.onContextChange( context );

        // reset state changed by onContextChange
        Mockito.reset( view );

        //emulate user input.
        when( view.getPlanningSolutionValue() ).thenReturn( true );

        //notify the presenter about the changes in the UI
        objectEditor.onPlanningSolutionChange();

        DataObject dataObject = context.getDataObject();

        //the dataObject should have been now configured as a PlanningEntity
        verify( view, times( 1 ) ).getPlanningSolutionValue();
        verify( view, times( 1 ) ).showPlanningSolutionScoreType( true );

        verify( view, times( 1 ) ).destroyFieldPicker();

        //the dataObject should have been now configured as a HardSoftCore PlanningSolution by default.
        assertNotNull( dataObject.getAnnotation( PlanningSolution.class.getName() ) );
        assertEquals( "org.optaplanner.core.impl.domain.solution.AbstractSolution<" + HardSoftScore.class.getName() + ">"
                , dataObject.getSuperClassName() );

    }

    @Test
    public void onPlanningSolutionScoreTypeChange() {
        PlannerDataObjectEditor objectEditor = createObjectEditor();

        objectEditor.onContextChange( context );

        Mockito.reset( view );

        when( view.getPlanningSolutionScoreType() ).thenReturn( BendableScore.class.getName() );

        context.getAnnotationDefinitions().put( PlannerDomainAnnotations.PLANNING_SCORE_ANNOTATION, mock( AnnotationDefinition.class ) );
        context.getAnnotationDefinitions().put( Generated.class.getName(), mock( AnnotationDefinition.class ) );

        context.getEditorModelContent().setSource( AbstractSolution.class.getName() + "<" + HardSoftScore.class.getName() + ">" );

        objectEditor.onPlanningSolutionScoreTypeChange();

        DataObject dataObject = context.getDataObject();

        assertEquals( AbstractSolution.class.getName() + "<" + BendableScore.class.getName() + ">", dataObject.getSuperClassName() );

        // getScore and setScore methods of BendableScore type should be present in the data object
        Method getScoreMethod = dataObject.getMethod( "getScore", Collections.EMPTY_LIST );
        assertNotNull( getScoreMethod );
        assertEquals( BendableScore.class.getName(), getScoreMethod.getReturnType().getName() );

        Method setScoreMethod = dataObject.getMethod( "setScore", Arrays.asList( BendableScore.class.getName() ) );
        assertNotNull( setScoreMethod );
    }

    @Test
    public void onPlanningSolutionBendableScoreHardLevelsSizeChange() {
        testPlanningSolutionLevelsSizeChange( true );
    }

    @Test
    public void onPlanningSolutionBendableScoreSoftLevelsSizeChange() {
        testPlanningSolutionLevelsSizeChange( false );
    }

    public void testPlanningSolutionLevelsSizeChange( boolean isHardScore ) {
        PlannerDataObjectEditor objectEditor = createObjectEditor();

        objectEditor.onContextChange( context );

        DataObject dataObject = context.getDataObject();

        dataObject.addAnnotation( DataModelerEditorsTestHelper.createAnnotation( PlanningSolution.class, null, null ) );

        AnnotationDefinition planningScoreAnnotationDefinition = mock( AnnotationDefinition.class );
        when( planningScoreAnnotationDefinition.getClassName() ).thenReturn( PlanningScore.class.getName() );

        context.getAnnotationDefinitions().put( PlannerDomainAnnotations.PLANNING_SCORE_ANNOTATION, planningScoreAnnotationDefinition );
        context.getAnnotationDefinitions().put( Generated.class.getName(), mock( AnnotationDefinition.class ) );

        Type getScoreMethodReturnType = new TypeImpl( BendableScore.class.getName() );
        Method getScoreMethod = new MethodImpl( "getScore", Collections.EMPTY_LIST, "return score;", getScoreMethodReturnType, Visibility.PUBLIC );
        getScoreMethod.addAnnotation( DataModelerEditorsTestHelper.createAnnotation( PlanningScore.class, new Pair( "bendableHardLevelsSize", 5 ), new Pair( "bendableSoftLevelsSize", 5 ) ) );
        getScoreMethod.addAnnotation( DataModelerEditorsTestHelper.createAnnotation( Generated.class, null, null ) );
        dataObject.addMethod( getScoreMethod );

        Parameter setScoreParameter = new ParameterImpl( new TypeImpl( BendableScore.class.getName() ), "score" );
        Type setScoreParameterReturnType = new TypeImpl( void.class.getName() );
        Method setScoreMethod = new MethodImpl( "setScore", Arrays.asList( setScoreParameter ), "this.score = score;", setScoreParameterReturnType, Visibility.PUBLIC );
        setScoreMethod.addAnnotation( DataModelerEditorsTestHelper.createAnnotation( Generated.class, null, null ) );
        dataObject.addMethod( setScoreMethod );

        if ( isHardScore ) {
            when( view.getPlanningSolutionBendableScoreHardLevelsSize() ).thenReturn( 1 );
        } else {
            when( view.getPlanningSolutionBendableScoreSoftLevelsSize() ).thenReturn( 1 );
        }

        when( view.getPlanningSolutionScoreType() ).thenReturn( BendableScore.class.getName() );

        objectEditor.onPlanningSolutionBendableScoreHardLevelsSizeChange();

        getScoreMethod = dataObject.getMethod( "getScore", Collections.EMPTY_LIST );

        assertNotNull( getScoreMethod );

        Annotation planningScoreAnnotation = getScoreMethod.getAnnotation( PlanningScore.class.getName() );

        assertNotNull( planningScoreAnnotation );

        // check whether bendable levels size has been updated
        if ( isHardScore ) {
            assertEquals( 1, planningScoreAnnotation.getValue( "bendableHardLevelsSize" ) );
        } else {
            assertEquals( 1, planningScoreAnnotation.getValue( "bendableSoftLevelsSize" ) );
        }
    }
}
