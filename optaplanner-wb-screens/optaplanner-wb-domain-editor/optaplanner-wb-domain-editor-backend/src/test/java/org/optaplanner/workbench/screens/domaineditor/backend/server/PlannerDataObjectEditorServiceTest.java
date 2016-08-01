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

package org.optaplanner.workbench.screens.domaineditor.backend.server;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kie.workbench.common.services.datamodeller.core.Annotation;
import org.kie.workbench.common.services.datamodeller.core.DataModel;
import org.kie.workbench.common.services.datamodeller.core.DataObject;
import org.kie.workbench.common.services.datamodeller.core.ObjectProperty;
import org.kie.workbench.common.services.datamodeller.core.impl.AnnotationImpl;
import org.kie.workbench.common.services.datamodeller.core.impl.DataModelImpl;
import org.kie.workbench.common.services.datamodeller.core.impl.DataObjectImpl;
import org.kie.workbench.common.services.datamodeller.core.impl.ObjectPropertyImpl;
import org.kie.workbench.common.services.datamodeller.util.DriverUtils;
import org.mockito.runners.MockitoJUnitRunner;
import org.optaplanner.workbench.screens.domaineditor.model.ComparatorDefinition;
import org.optaplanner.workbench.screens.domaineditor.model.ComparatorObject;
import org.optaplanner.workbench.screens.domaineditor.model.ObjectPropertyPath;
import org.optaplanner.workbench.screens.domaineditor.service.PlannerDataObjectEditorService;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PlannerDataObjectEditorServiceTest {

    private DataModel dataModel;

    private DataObject dataObject1;

    private DataObject dataObject2;

    private PlannerDataObjectEditorService service ;

    @Before
    public void setUp() {
        dataModel = getDataModel();
        service = new PlannerDataObjectEditorServiceImpl();
    }

    @Test(expected = IllegalStateException.class)
    public void invalidAnnotationFieldPathNoOrder() {
        DataObject dataObject = mock(DataObject.class);
        when( dataObject.getAnnotation( ComparatorDefinition.class.getName() ) ).thenReturn( getAnnotation( Arrays.asList("abc.def=") ) );
        service.extractComparatorObject( dataObject, dataModel );

    }

    @Test(expected = IllegalStateException.class)
    public void invalidAnnotationFieldPathWrongOrder() {
        DataObject dataObject = mock(DataObject.class);
        when( dataObject.getAnnotation( ComparatorDefinition.class.getName() ) ).thenReturn( getAnnotation( Arrays.asList("abc.def=foo") ) );
        service.extractComparatorObject( dataObject, dataModel );
    }

    @Test(expected = IllegalStateException.class)
    public void invalidAnnotationFieldPathNoTypesIncluded() {
        DataObject dataObject = mock(DataObject.class);
        when( dataObject.getAnnotation( ComparatorDefinition.class.getName() ) ).thenReturn( getAnnotation( Arrays.asList("abc,def=desc") ) );
        service.extractComparatorObject( dataObject, dataModel );
    }

    @Test
    public void validFieldPathBaseProperty() {
        dataObject1.addAnnotation( getAnnotation( Arrays.asList("foo.bar.DataObject1:dataObject1Property1=desc") ) );
        ComparatorObject result = service.extractComparatorObject( dataObject1, dataModel );

        List<ObjectPropertyPath> objectPropertyPathList = result.getObjectPropertyPathList();
        Assert.assertEquals( 1, objectPropertyPathList.size());

        ObjectPropertyPath objectPropertyPath = objectPropertyPathList.get( 0 );
        Assert.assertEquals( Arrays.asList( dataObject1.getProperty( "dataObject1Property1" ) ), objectPropertyPath.getObjectPropertyPath() );
        Assert.assertEquals( true, objectPropertyPath.isDescending() );
    }

    @Test
    public void validFieldPathComplexProperty() {
        dataObject1.addAnnotation( getAnnotation( Arrays.asList("foo.bar.DataObject1:dataObject1Property2=asc") ) );
        ComparatorObject result = service.extractComparatorObject( dataObject1, dataModel );

        List<ObjectPropertyPath> objectPropertyPathList = result.getObjectPropertyPathList();
        Assert.assertEquals( 1, objectPropertyPathList.size());

        ObjectPropertyPath objectPropertyPath = objectPropertyPathList.get( 0 );
        Assert.assertEquals( Arrays.asList( dataObject1.getProperty( "dataObject1Property2" ) ), objectPropertyPath.getObjectPropertyPath() );
        Assert.assertEquals( false, objectPropertyPath.isDescending() );
    }

    @Test
    public void validFieldPathNestedProperties() {
        dataObject1.addAnnotation( getAnnotation( Arrays.asList("foo.bar.DataObject1:dataObject1Property2-foo.bar.DataObject2:dataObjext2Property1=desc") ) );
        ComparatorObject result = service.extractComparatorObject( dataObject1, dataModel );

        List<ObjectPropertyPath> objectPropertyPathList = result.getObjectPropertyPathList();
        Assert.assertEquals( 1, objectPropertyPathList.size());

        ObjectPropertyPath objectPropertyPath = objectPropertyPathList.get( 0 );
        Assert.assertEquals( Arrays.asList( dataObject1.getProperty( "dataObject1Property2"), dataObject2.getProperty( "dataObjext2Property1" ) ), objectPropertyPath.getObjectPropertyPath() );
        Assert.assertEquals( true, objectPropertyPath.isDescending() );
    }

    @Test(expected = IllegalStateException.class)
    public void invalidFieldPathNonExistentProperty() {
        dataObject1.addAnnotation( getAnnotation( Arrays.asList("foo.bar.DataObject1:nonExistentProperty=desc") ) );
        service.extractComparatorObject( dataObject1, dataModel );
    }

    @Test(expected = IllegalStateException.class)
    public void invalidFieldPathNonExistentNestedProperty() {
        dataObject1.addAnnotation( getAnnotation( Arrays.asList("foo.bar.DataObject1:dataObject1Property2-java.lang.Integer:nonExistentProperty=desc") ) );
        service.extractComparatorObject( dataObject1, dataModel );
    }

    private Annotation getAnnotation(List<String> fieldPaths) {
        Annotation annotation = new AnnotationImpl( DriverUtils.buildAnnotationDefinition( ComparatorDefinition.class ) );
        annotation.setValue( "fieldPaths", fieldPaths );
        return annotation;
    }

    private DataModel getDataModel() {
        DataModel dataModel = new DataModelImpl();

        DataObject dataObject1 = new DataObjectImpl( "bar.foo", "DataObject1" );
        ObjectProperty dataObject1Property1 = new ObjectPropertyImpl( "dataObject1Property1", "java.lang.Integer", false );
        ObjectProperty dataObject1Property2 = new ObjectPropertyImpl( "dataObject1Property2", "bar.foo.DataObject2", false );
        dataObject1.addProperty( dataObject1Property1 );
        dataObject1.addProperty( dataObject1Property2 );
        dataModel.addDataObject( dataObject1 );

        DataObject dataObject2 = new DataObjectImpl( "bar.foo", "DataObject2" );
        ObjectProperty dataObject2Property1 = new ObjectPropertyImpl( "dataObjext2Property1", "java.lang.Double", false );
        dataObject2.addProperty( dataObject2Property1 );
        dataModel.addDataObject( dataObject2 );

        this.dataObject1 = dataObject1;
        this.dataObject2 = dataObject2;

        return dataModel;
    }

}
