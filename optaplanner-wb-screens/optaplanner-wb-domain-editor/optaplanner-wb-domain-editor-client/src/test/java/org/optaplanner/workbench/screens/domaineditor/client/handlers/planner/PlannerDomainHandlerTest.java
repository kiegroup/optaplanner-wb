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

package org.optaplanner.workbench.screens.domaineditor.client.handlers.planner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import javax.annotation.Generated;

import org.jboss.errai.common.client.api.Caller;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kie.workbench.common.screens.datamodeller.events.ChangeType;
import org.kie.workbench.common.screens.datamodeller.events.DataObjectChangeEvent;
import org.kie.workbench.common.screens.datamodeller.events.DataObjectFieldChangeEvent;
import org.kie.workbench.common.screens.datamodeller.events.DataObjectFieldDeletedEvent;
import org.kie.workbench.common.services.datamodeller.core.Annotation;
import org.kie.workbench.common.services.datamodeller.core.AnnotationDefinition;
import org.kie.workbench.common.services.datamodeller.core.DataObject;
import org.kie.workbench.common.services.datamodeller.core.Method;
import org.kie.workbench.common.services.datamodeller.core.ObjectProperty;
import org.kie.workbench.common.services.datamodeller.core.Visibility;
import org.kie.workbench.common.services.datamodeller.core.impl.AnnotationImpl;
import org.kie.workbench.common.services.datamodeller.core.impl.DataObjectImpl;
import org.kie.workbench.common.services.datamodeller.core.impl.JavaClassImpl;
import org.kie.workbench.common.services.datamodeller.core.impl.MethodImpl;
import org.kie.workbench.common.services.datamodeller.core.impl.ObjectPropertyImpl;
import org.kie.workbench.common.services.datamodeller.core.impl.ParameterImpl;
import org.kie.workbench.common.services.datamodeller.core.impl.TypeImpl;
import org.kie.workbench.common.services.datamodeller.util.DriverUtils;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.workbench.screens.domaineditor.service.ComparatorDefinitionService;
import org.optaplanner.workbench.screens.domaineditor.client.widgets.planner.PlannerTestUtil;
import org.optaplanner.workbench.screens.domaineditor.model.ComparatorDefinition;
import org.optaplanner.workbench.screens.domaineditor.model.ObjectPropertyPath;
import org.optaplanner.workbench.screens.domaineditor.model.ObjectPropertyPathImpl;
import org.optaplanner.workbench.screens.domaineditor.model.PlannerDomainAnnotations;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class PlannerDomainHandlerTest {

    @Mock
    private Caller<ComparatorDefinitionService> comparatorDefinitionService;

    private DataObject dataObject;

    private JavaClassImpl comparatorObject;

    private PlannerDomainHandler plannerDomainHandler;

    @Before
    public void setUp() {
        when( comparatorDefinitionService.call( any() ) ).thenReturn( mock( ComparatorDefinitionService.class ) );
        this.plannerDomainHandler = new PlannerDomainHandler( comparatorDefinitionService );
        initDataObjects();
    }

    @Test
    public void postEventProcessingDataObjectFieldDeleted() {
        DataObjectFieldDeletedEvent event = (DataObjectFieldDeletedEvent) new DataObjectFieldDeletedEvent()
                .withCurrentDataObject( dataObject )
                .withCurrentField( dataObject.getProperty( "dataObject1Property2" ) );

        Annotation comparatorDefinitionAnnotation = comparatorObject.getAnnotation( ComparatorDefinition.class.getName() );

        ComparatorDefinitionAnnotationValueHandler comparatorAnnotationHandler = new ComparatorDefinitionAnnotationValueHandler( comparatorDefinitionAnnotation );

        assertFalse( comparatorAnnotationHandler.getObjectPropertyPaths().isEmpty() );

        plannerDomainHandler.onDataObjectFieldDeletedEvent( event );

        assertTrue( comparatorAnnotationHandler.getObjectPropertyPaths().isEmpty() );
    }

    @Test
    public void postEventProcessingDataObjectNameChanged() {
        DataObjectChangeEvent event = new DataObjectChangeEvent( ChangeType.OBJECT_NAME_CHANGE,
                "testContextId",
                "testSource",
                dataObject,
                null,
                dataObject.getName(),
                "DataObject1NewName" );

        Annotation planningEntityAnnotation = dataObject.getAnnotation( PlannerDomainAnnotations.PLANNING_ENTITY_ANNOTATION );
        assertNotNull( planningEntityAnnotation );
        Object difficultyComparatorClass = planningEntityAnnotation.getValue( "difficultyComparatorClass" );

        assertEquals( dataObject.getName() + ".DifficultyComparator.class", difficultyComparatorClass );

        dataObject.setName( "DataObject1NewName" );

        plannerDomainHandler.onDataModelerValueChangeEvent( event );

        planningEntityAnnotation = dataObject.getAnnotation( PlannerDomainAnnotations.PLANNING_ENTITY_ANNOTATION );
        assertNotNull( planningEntityAnnotation );
        difficultyComparatorClass = planningEntityAnnotation.getValue( "difficultyComparatorClass" );

        assertEquals( "DataObject1NewName.DifficultyComparator.class", difficultyComparatorClass );
    }

    @Test
    public void postEventProcessingFieldNameChanged() {
        ObjectProperty objectProperty = dataObject.getProperty( "dataObject1Property2" );

        DataObjectFieldChangeEvent event = new DataObjectFieldChangeEvent( ChangeType.FIELD_NAME_CHANGE,
                "testContextId",
                "testSource",
                dataObject,
                objectProperty,
                null,
                "dataObject1Property2",
                "dataObject1Property2NewName" );

        Annotation comparatorDefinitionAnnotation = comparatorObject.getAnnotation( ComparatorDefinition.class.getName() );

        ComparatorDefinitionAnnotationValueHandler comparatorAnnotationHandler = new ComparatorDefinitionAnnotationValueHandler( comparatorDefinitionAnnotation );

        assertEquals( 1, comparatorAnnotationHandler.getObjectPropertyPaths().size() );
        Annotation objectPropertyPath = comparatorAnnotationHandler.getObjectPropertyPaths().get( 0 );

        assertEquals( 2, comparatorAnnotationHandler.getObjectProperties( objectPropertyPath ).size() );
        Annotation comparatorObjectProperty = comparatorAnnotationHandler.getObjectProperties( objectPropertyPath ).get( 0 );

        assertEquals( "dataObject1Property2", comparatorAnnotationHandler.getName( comparatorObjectProperty ) );

        objectProperty.setName( "dataObject1Property2NewName" );

        plannerDomainHandler.onDataModelerValueChangeEvent( event );

        assertEquals( "dataObject1Property2NewName", comparatorAnnotationHandler.getName( comparatorObjectProperty ) );

        assertEquals( 1, comparatorAnnotationHandler.getObjectPropertyPaths().size() );
        assertEquals( 2, comparatorAnnotationHandler.getObjectProperties( objectPropertyPath ).size() );
    }

    @Test
    public void postEventProcessingFieldTypeChanged() {
        ObjectProperty objectProperty = dataObject.getProperty( "dataObject1Property2" );

        String oldType = objectProperty.getClassName();

        DataObjectFieldChangeEvent event = new DataObjectFieldChangeEvent( ChangeType.FIELD_TYPE_CHANGE,
                "testContextId",
                "testSource",
                dataObject,
                objectProperty,
                null,
                oldType,
                oldType + "NewType" );

        Annotation comparatorDefinitionAnnotation = comparatorObject.getAnnotation( ComparatorDefinition.class.getName() );

        ComparatorDefinitionAnnotationValueHandler comparatorAnnotationHandler = new ComparatorDefinitionAnnotationValueHandler( comparatorDefinitionAnnotation );

        assertEquals( 1, comparatorAnnotationHandler.getObjectPropertyPaths().size() );
        Annotation objectPropertyPath = comparatorAnnotationHandler.getObjectPropertyPaths().get( 0 );

        assertEquals( 2, comparatorAnnotationHandler.getObjectProperties( objectPropertyPath ).size() );
        Annotation comparatorObjectProperty = comparatorAnnotationHandler.getObjectProperties( objectPropertyPath ).get( 0 );

        assertEquals( oldType, comparatorAnnotationHandler.getType( comparatorObjectProperty ) );

        objectProperty.setClassName( oldType + "NewType" );

        plannerDomainHandler.onDataModelerValueChangeEvent( event );

        assertEquals( oldType + "NewType", comparatorAnnotationHandler.getType( comparatorObjectProperty ) );

        assertEquals( 1, comparatorAnnotationHandler.getObjectPropertyPaths().size() );
        assertEquals( 1, comparatorAnnotationHandler.getObjectProperties( objectPropertyPath ).size() );
    }

    private void initDataObjects() {
        Map<String, AnnotationDefinition> annotationDefinitionMap = PlannerTestUtil.getComparatorObjectAnnotations();

        DataObject dataObject1 = new DataObjectImpl( "bar.foo", "DataObject1" );

        ObjectProperty dataObject1Property1 = new ObjectPropertyImpl( "dataObject1Property1", "java.lang.Integer", false );
        ObjectProperty dataObject1Property2 = new ObjectPropertyImpl( "dataObject1Property2", "bar.foo.DataObject2", false );

        dataObject1.addProperty( dataObject1Property1 );
        dataObject1.addProperty( dataObject1Property2 );

        JavaClassImpl comparatorObject = new JavaClassImpl( "", "DifficultyComparator" );
        comparatorObject.addInterface( Comparator.class.getName() );
        comparatorObject.addAnnotation( new AnnotationImpl( annotationDefinitionMap.get( Generated.class.getName() ) ) );

        Method compareMethod = new MethodImpl( "compare", Arrays.asList( new ParameterImpl( new TypeImpl( dataObject1.getClassName() ), "o1" ), new ParameterImpl( new TypeImpl( dataObject1.getClassName() ), "o2" ) ), "foo", new TypeImpl( "int" ), Visibility.PUBLIC );
        comparatorObject.addMethod( compareMethod );

        dataObject1.addNestedClass( comparatorObject );

        AnnotationImpl planningEntityAnnotation = new AnnotationImpl( DriverUtils.buildAnnotationDefinition( PlanningEntity.class ) );
        planningEntityAnnotation.setValue( "difficultyComparatorClass", dataObject1.getName() + "." + "DifficultyComparator.class" );
        dataObject1.addAnnotation( planningEntityAnnotation );

        ObjectPropertyPath objectPropertyPath = new ObjectPropertyPathImpl();
        objectPropertyPath.appendObjectProperty( dataObject1Property2 );
        objectPropertyPath.appendObjectProperty( dataObject1Property1 );

        List<ObjectPropertyPath> objectPropertyPathList = new ArrayList<>( 1 );
        objectPropertyPathList.add( objectPropertyPath );

        Annotation comparatorDefinitionAnnotation = ComparatorDefinitionAnnotationValueHandler.createAnnotation( objectPropertyPathList, annotationDefinitionMap );

        comparatorObject.addAnnotation( comparatorDefinitionAnnotation );

        DataObject dataObject2 = new DataObjectImpl( "bar.foo", "DataObject2" );
        ObjectProperty dataObject2Property1 = new ObjectPropertyImpl( "dataObject2Property1", "java.lang.Double", false );
        dataObject2.addProperty( dataObject2Property1 );

        this.dataObject = dataObject1;
        this.comparatorObject = comparatorObject;
    }

}
