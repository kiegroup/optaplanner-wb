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
import java.util.Optional;

import javax.annotation.Generated;
import javax.enterprise.context.ApplicationScoped;

import org.jboss.errai.bus.server.annotations.Service;
import org.kie.workbench.common.services.datamodeller.codegen.GenerationTools;
import org.kie.workbench.common.services.datamodeller.core.Annotation;
import org.kie.workbench.common.services.datamodeller.core.DataObject;
import org.kie.workbench.common.services.datamodeller.core.JavaClass;
import org.kie.workbench.common.services.datamodeller.core.Method;
import org.kie.workbench.common.services.datamodeller.core.Parameter;
import org.kie.workbench.common.services.datamodeller.core.Type;
import org.kie.workbench.common.services.datamodeller.core.Visibility;
import org.kie.workbench.common.services.datamodeller.core.impl.AnnotationImpl;
import org.kie.workbench.common.services.datamodeller.core.impl.JavaClassImpl;
import org.kie.workbench.common.services.datamodeller.core.impl.MethodImpl;
import org.kie.workbench.common.services.datamodeller.core.impl.ParameterImpl;
import org.kie.workbench.common.services.datamodeller.core.impl.TypeImpl;
import org.kie.workbench.common.services.datamodeller.util.DriverUtils;
import org.optaplanner.workbench.screens.domaineditor.model.ComparatorDefinition;
import org.optaplanner.workbench.screens.domaineditor.service.ComparatorDefinitionService;

@Service
@ApplicationScoped
public class ComparatorDefinitionServiceImpl implements ComparatorDefinitionService {

    private GenerationTools generationTools = new GenerationTools();

    public JavaClass createComparatorObject(DataObject dataObject) {
        JavaClass comparatorObject = new JavaClassImpl("",
                                                       "DifficultyComparator");

        AnnotationImpl generatedAnnotation = new AnnotationImpl(DriverUtils.buildAnnotationDefinition(Generated.class));
        generatedAnnotation.setValue("value",
                                     Arrays.asList(ComparatorDefinitionService.class.getName()));

        comparatorObject.addAnnotation(generatedAnnotation);

        AnnotationImpl comparatorDefinitionAnnotation = new AnnotationImpl(DriverUtils.buildAnnotationDefinition(ComparatorDefinition.class));

        comparatorObject.addAnnotation(comparatorDefinitionAnnotation);
        comparatorObject.addInterface("java.util.Comparator<" + dataObject.getClassName() + ">");

        Method compareMethod = updateCompareMethod(new MethodImpl(),
                                                   comparatorObject.getAnnotation(ComparatorDefinition.class.getName()),
                                                   dataObject.getClassName());

        generatedAnnotation = new AnnotationImpl(DriverUtils.buildAnnotationDefinition(Generated.class));
        generatedAnnotation.setValue("value",
                                     Arrays.asList(ComparatorDefinitionService.class.getName()));

        compareMethod.addAnnotation(generatedAnnotation);

        comparatorObject.addMethod(compareMethod);

        return comparatorObject;
    }

    @Override
    public JavaClass updateComparatorObject(DataObject dataObject,
                                            JavaClass comparatorObject) {
        comparatorObject.getInterfaces().removeIf(i -> i.startsWith("java.util.Comparator"));
        comparatorObject.addInterface("java.util.Comparator<" + dataObject.getClassName() + ">");

        Optional<Method> compareMethod = comparatorObject.getMethods()
                .stream()
                .filter(m -> m.getReturnType().getName().equals("int") && m.getParameters().size() == 2 && m.getName().equals("compare"))
                .findFirst();

        if (!compareMethod.isPresent()) {
            throw new IllegalStateException("'compare' method not found in comparator object " + comparatorObject);
        }

        comparatorObject.addMethod(updateCompareMethod(compareMethod.get(),
                                                       comparatorObject.getAnnotation(ComparatorDefinition.class.getName()),
                                                       dataObject.getClassName()));

        return comparatorObject;
    }

    private Method updateCompareMethod(Method compareMethod,
                                       Annotation objectPropertyPaths,
                                       String type) {

        Parameter parameter1 = new ParameterImpl(new TypeImpl(type),
                                                 "o1");
        Parameter parameter2 = new ParameterImpl(new TypeImpl(type),
                                                 "o2");

        Type returnType = new TypeImpl("int");

        compareMethod.setName("compare");
        compareMethod.setParameters(Arrays.asList(parameter1,
                                                  parameter2));
        compareMethod.setBody(generateCompareBody(objectPropertyPaths,
                                                  type));
        compareMethod.setReturnType(returnType);
        compareMethod.setVisibility(Visibility.PUBLIC);

        return compareMethod;
    }

    private String generateCompareBody(Annotation comparatorDefinition,
                                       String type) {
        List<Annotation> objectPropertyPaths = (List<Annotation>) comparatorDefinition.getValue("objectPropertyPaths");

        if (objectPropertyPaths == null || objectPropertyPaths.isEmpty()) {
            return "return 0;";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("return java.util.Comparator");

        Annotation firstObjectPropertyPath = objectPropertyPaths.get(0);
        sb.append(getComparingRow(firstObjectPropertyPath,
                                  true,
                                  type));
        for (int i = 1; i < objectPropertyPaths.size(); i++) {
            sb.append(getComparingRow(objectPropertyPaths.get(i),
                                      false,
                                      type));
        }

        sb.append(".compare(o1, o2);");
        return sb.toString();
    }

    private String getComparingRow(Annotation objectPropertyPath,
                                   boolean firstElement,
                                   String type) {
        StringBuilder sb = new StringBuilder();
        String comparatorMethodName = firstElement ? "comparing" : "thenComparing";
        sb.append(".")
                .append(comparatorMethodName)
                .append("( (")
                .append(type)
                .append(" o) -> { Object tempResult = o;");
        String previousObjectPropertyClassName = type;

        List<Annotation> fieldPaths = (List<Annotation>) objectPropertyPath.getValue("objectProperties");

        if (fieldPaths != null) {
            for (Annotation pathElement : fieldPaths) {
                String typeString = (String) pathElement.getValue("type");
                if (typeString.endsWith(".class")) {
                    typeString = typeString.substring(0,
                                                      typeString.indexOf(".class"));
                }
                sb.append("tempResult = tempResult == null ? null : ")
                        .append("(( ")
                        .append(previousObjectPropertyClassName)
                        .append(") tempResult ).")
                        .append(generationTools.toJavaGetter((String) pathElement.getValue("name"),
                                                             typeString))
                        .append("();");
                previousObjectPropertyClassName = typeString;
            }
        }

        sb.append("return (").append(previousObjectPropertyClassName).append(") tempResult; }");

        Object ascending = objectPropertyPath.getValue("ascending");

        if (ascending != null && !(boolean) ascending) {
            sb.append(", java.util.Comparator.reverseOrder()");
        }
        sb.append(")");
        return sb.toString();
    }
}
