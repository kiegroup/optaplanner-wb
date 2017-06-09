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
import java.util.List;
import java.util.Map;

import org.kie.workbench.common.screens.datamodeller.client.util.AnnotationValueHandler;
import org.kie.workbench.common.services.datamodeller.core.Annotation;
import org.kie.workbench.common.services.datamodeller.core.AnnotationDefinition;
import org.kie.workbench.common.services.datamodeller.core.ObjectProperty;
import org.kie.workbench.common.services.datamodeller.core.impl.AnnotationImpl;
import org.optaplanner.workbench.screens.domaineditor.model.ComparatorDefinition;
import org.optaplanner.workbench.screens.domaineditor.model.ComparatorObjectProperty;
import org.optaplanner.workbench.screens.domaineditor.model.ComparatorObjectPropertyPath;
import org.optaplanner.workbench.screens.domaineditor.model.ObjectPropertyPath;

public class ComparatorDefinitionAnnotationValueHandler extends AnnotationValueHandler {

    private static final String OBJECT_PROPERTY_PATHS = "objectPropertyPaths";

    private static final String OBJECT_PROPERTIES = "objectProperties";

    private static final String ASCENDING = "ascending";

    private static final String NAME = "name";

    private static final String TYPE = "type";

    public ComparatorDefinitionAnnotationValueHandler(Annotation annotation) {
        super(annotation);
    }

    public List<Annotation> getObjectPropertyPaths() {
        Object objectPropertyPaths = annotation.getValue(OBJECT_PROPERTY_PATHS);
        return objectPropertyPaths == null ? new ArrayList<>(0) : (List<Annotation>) objectPropertyPaths;
    }

    public List<Annotation> getObjectProperties(Annotation objectPropertyPath) {
        Object objectProperties = objectPropertyPath.getValue(OBJECT_PROPERTIES);
        return objectProperties == null ? new ArrayList<>(0) : (List<Annotation>) objectProperties;
    }

    public boolean isAscending(Annotation objectPropertyPath) {
        return (boolean) objectPropertyPath.getValue(ASCENDING);
    }

    public String getName(Annotation comparatorObjectProperty) {
        return (String) comparatorObjectProperty.getValue(NAME);
    }

    public void setName(Annotation comparatorObjectProperty,
                        String name) {
        if (name == null) {
            comparatorObjectProperty.removeValue(NAME);
        } else {
            comparatorObjectProperty.setValue(NAME,
                                              name);
        }
    }

    public String getType(Annotation comparatorObjectProperty) {
        return (String) comparatorObjectProperty.getValue(TYPE);
    }

    public void setType(Annotation comparatorObjectProperty,
                        String type) {
        if (type == null) {
            comparatorObjectProperty.removeValue(TYPE);
        } else {
            comparatorObjectProperty.setValue(TYPE,
                                              type);
        }
    }

    public static Annotation createAnnotation(List<ObjectPropertyPath> objectPropertyPathList,
                                              Map<String, AnnotationDefinition> annotationDefinitions) {

        Annotation annotation = new AnnotationImpl(annotationDefinitions.get(ComparatorDefinition.class.getName()));

        List<Annotation> objectPropertyPathAnnotations = objectPropertyPathList == null ? new ArrayList<>(0) : new ArrayList<>(objectPropertyPathList.size());

        if (objectPropertyPathList != null) {
            for (ObjectPropertyPath objectPropertyPath : objectPropertyPathList) {
                AnnotationImpl comparatorFieldPath = new AnnotationImpl(annotationDefinitions.get(ComparatorObjectPropertyPath.class.getName()));
                List<ObjectProperty> path = objectPropertyPath.getObjectPropertyPath();
                List<Annotation> comparatorFieldPaths = new ArrayList<>();
                for (int i = 0; i < path.size(); i++) {
                    ObjectProperty objectProperty = path.get(i);
                    AnnotationImpl comparatorField = new AnnotationImpl(annotationDefinitions.get(ComparatorObjectProperty.class.getName()));
                    comparatorField.setValue(NAME,
                                             objectProperty.getName());
                    comparatorField.setValue(TYPE,
                                             objectProperty.getClassName());
                    comparatorFieldPaths.add(comparatorField);
                }
                comparatorFieldPath.setValue(OBJECT_PROPERTIES,
                                             comparatorFieldPaths);
                comparatorFieldPath.setValue(ASCENDING,
                                             !objectPropertyPath.isDescending());

                objectPropertyPathAnnotations.add(comparatorFieldPath);
            }
        }

        annotation.setValue(OBJECT_PROPERTY_PATHS,
                            objectPropertyPathAnnotations);

        return annotation;
    }
}
