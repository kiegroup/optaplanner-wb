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

import org.junit.Test;
import org.kie.workbench.common.services.datamodeller.core.DataObject;
import org.kie.workbench.common.services.datamodeller.core.JavaClass;
import org.kie.workbench.common.services.datamodeller.core.Method;
import org.kie.workbench.common.services.datamodeller.core.Parameter;
import org.kie.workbench.common.services.datamodeller.core.Visibility;
import org.kie.workbench.common.services.datamodeller.core.impl.AnnotationImpl;
import org.kie.workbench.common.services.datamodeller.core.impl.DataObjectImpl;
import org.kie.workbench.common.services.datamodeller.core.impl.JavaClassImpl;
import org.kie.workbench.common.services.datamodeller.core.impl.MethodImpl;
import org.kie.workbench.common.services.datamodeller.core.impl.ParameterImpl;
import org.kie.workbench.common.services.datamodeller.core.impl.TypeImpl;
import org.kie.workbench.common.services.datamodeller.util.DriverUtils;
import org.optaplanner.workbench.screens.domaineditor.model.ComparatorDefinition;
import org.optaplanner.workbench.screens.domaineditor.service.ComparatorDefinitionService;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class ComparatorDefinitionServiceTest {

    private ComparatorDefinitionService comparatorDefinitionService = new ComparatorDefinitionServiceImpl();

    @Test
    public void createComparatorObject() {
        JavaClass comparatorObject = comparatorDefinitionService.createComparatorObject(new DataObjectImpl("foo.bar",
                                                                                                           "TestDataObject"));

        assertEquals("DifficultyComparator",
                     comparatorObject.getName());
        assertTrue(comparatorObject.getInterfaces().contains("java.util.Comparator<foo.bar.TestDataObject>"));

        List<Method> comparatorObjectMethods = comparatorObject.getMethods();

        assertNotNull(comparatorObjectMethods);
        assertEquals(1,
                     comparatorObjectMethods.size());

        Method compareMethod = comparatorObjectMethods.get(0);

        assertEquals("compare",
                     compareMethod.getName());
        assertEquals("int",
                     compareMethod.getReturnType().getName());
        assertEquals("int",
                     compareMethod.getReturnType().getName());

        assertEquals(2,
                     compareMethod.getParameters().size());
        assertEquals("foo.bar.TestDataObject",
                     compareMethod.getParameters().get(0).getType().getName());
        assertEquals("o1",
                     compareMethod.getParameters().get(0).getName());
        assertEquals("foo.bar.TestDataObject",
                     compareMethod.getParameters().get(1).getType().getName());
        assertEquals("o2",
                     compareMethod.getParameters().get(1).getName());
    }

    @Test
    public void updateComparatorObject() {
        DataObject dataObject = new DataObjectImpl("foo.bar",
                                                   "TestDataObject");

        JavaClass comparatorObject = new JavaClassImpl("",
                                                       "DifficultyComparator");

        Parameter parameter1 = new ParameterImpl(new TypeImpl("TestDataObject"),
                                                 "o1");
        Parameter parameter2 = new ParameterImpl(new TypeImpl("TestDataObject"),
                                                 "o2");

        Method compareMethod = new MethodImpl("compare",
                                              Arrays.asList(parameter1,
                                                            parameter2),
                                              "foo",
                                              new TypeImpl("int"),
                                              Visibility.PUBLIC);

        comparatorObject.addMethod(compareMethod);

        comparatorObject.addInterface("java.util.Comparator<foo.bar.TestDataObject>");

        dataObject.addNestedClass(comparatorObject);

        comparatorObject.addAnnotation(new AnnotationImpl(DriverUtils.buildAnnotationDefinition(ComparatorDefinition.class)));

        // Rename data object
        dataObject.setName("TestDataObjectUpdated");

        JavaClass updatedComparatorObject = comparatorDefinitionService.updateComparatorObject(dataObject,
                                                                                               comparatorObject);

        assertTrue(updatedComparatorObject.getInterfaces().contains("java.util.Comparator<foo.bar.TestDataObjectUpdated>"));
    }
}
