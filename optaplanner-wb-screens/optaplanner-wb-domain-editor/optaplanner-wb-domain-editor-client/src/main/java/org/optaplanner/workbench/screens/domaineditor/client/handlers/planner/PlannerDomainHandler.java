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

package org.optaplanner.workbench.screens.domaineditor.client.handlers.planner;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;
import java.util.stream.Collectors;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.jboss.errai.common.client.api.Caller;
import org.jboss.errai.common.client.api.RemoteCallback;
import org.kie.workbench.common.screens.datamodeller.client.command.DataModelCommand;
import org.kie.workbench.common.screens.datamodeller.client.handlers.DomainHandler;
import org.kie.workbench.common.screens.datamodeller.client.widgets.common.domain.ResourceOptions;
import org.kie.workbench.common.screens.datamodeller.events.ChangeType;
import org.kie.workbench.common.screens.datamodeller.events.DataModelerEvent;
import org.kie.workbench.common.screens.datamodeller.events.DataModelerValueChangeEvent;
import org.kie.workbench.common.screens.datamodeller.events.DataObjectChangeEvent;
import org.kie.workbench.common.screens.datamodeller.events.DataObjectFieldChangeEvent;
import org.kie.workbench.common.screens.datamodeller.events.DataObjectFieldDeletedEvent;
import org.kie.workbench.common.services.datamodeller.core.Annotation;
import org.kie.workbench.common.services.datamodeller.core.DataObject;
import org.kie.workbench.common.services.datamodeller.core.JavaClass;
import org.kie.workbench.common.services.datamodeller.core.ObjectProperty;
import org.optaplanner.workbench.screens.domaineditor.model.ComparatorDefinition;
import org.optaplanner.workbench.screens.domaineditor.model.PlannerDomainAnnotations;
import org.optaplanner.workbench.screens.domaineditor.service.ComparatorDefinitionService;

@ApplicationScoped
public class PlannerDomainHandler implements DomainHandler {

    private Caller<ComparatorDefinitionService> comparatorDefinitionService;

    public PlannerDomainHandler() {
    }

    @Inject
    public PlannerDomainHandler(Caller<ComparatorDefinitionService> comparatorDefinitionService) {
        this.comparatorDefinitionService = comparatorDefinitionService;
    }

    @Override
    public String getName() {
        return "PLANNER";
    }

    @Override
    public int getPriority() {
        return 5;
    }

    @Override
    public ResourceOptions getResourceOptions(boolean newInstance) {
        //not apply for this domain.
        return null;
    }

    @Override
    public void postCommandProcessing(DataModelCommand command) {
        //not apply for this domain.
    }

    public void onDataModelerValueChangeEvent(@Observes DataModelerValueChangeEvent event) {
        handleDataModelerEvent(event);
    }

    public void onDataObjectFieldDeletedEvent(@Observes DataObjectFieldDeletedEvent event) {
        handleDataModelerEvent(event);
    }

    private void handleDataModelerEvent(DataModelerEvent event) {
        DataObject dataObject = event.getCurrentDataObject();

        if (dataObject != null && dataObject.getAnnotation(PlannerDomainAnnotations.PLANNING_ENTITY_ANNOTATION) != null) {
            if (dataObject.getNestedClasses() == null || dataObject.getNestedClasses().isEmpty()) {
                return;
            }

            List<JavaClass> comparatorObjects = getComparatorNestedClasses(dataObject);

            for (JavaClass comparatorObject : comparatorObjects) {
                Annotation comparatorDefinition = comparatorObject.getAnnotation(ComparatorDefinition.class.getName());

                if (event instanceof DataObjectFieldDeletedEvent) {
                    boolean changed = false;

                    ComparatorDefinitionAnnotationValueHandler comparatorAnnotationHandler = new ComparatorDefinitionAnnotationValueHandler(comparatorDefinition);

                    List<Annotation> objectPropertyPaths = comparatorAnnotationHandler.getObjectPropertyPaths();

                    ListIterator<Annotation> objectPropertyPathListIterator = objectPropertyPaths.listIterator();

                    while (objectPropertyPathListIterator.hasNext()) {
                        Annotation objectPropertyPath = objectPropertyPathListIterator.next();

                        List<Annotation> objectProperties = comparatorAnnotationHandler.getObjectProperties(objectPropertyPath);

                        ListIterator<Annotation> iterator = objectProperties.listIterator();

                        boolean remove = false;

                        if (iterator.hasNext()) {
                            Annotation field = iterator.next();
                            if (event.getCurrentField().getName().equals(comparatorAnnotationHandler.getName(field))) {
                                changed = true;
                                remove = true;
                                iterator.remove();
                            }
                        }

                        if (remove) {
                            while (iterator.hasNext()) {
                                iterator.next();
                                iterator.remove();
                            }

                            if (objectProperties.isEmpty()) {
                                objectPropertyPathListIterator.remove();
                            }
                        }
                    }

                    if (changed) {
                        comparatorDefinitionService.call(getRemoteCallback(dataObject,
                                                                           comparatorObject))
                                .updateComparatorObject(dataObject,
                                                        comparatorObject);
                    }
                } else if (event instanceof DataObjectChangeEvent) {
                    if (((DataObjectChangeEvent) event).getChangeType() == ChangeType.OBJECT_NAME_CHANGE
                            || ((DataObjectChangeEvent) event).getChangeType() == ChangeType.PACKAGE_NAME_CHANGE
                            || ((DataObjectChangeEvent) event).getChangeType() == ChangeType.CLASS_NAME_CHANGE) {

                        Annotation planningEntityAnnotation = dataObject.getAnnotation(PlannerDomainAnnotations.PLANNING_ENTITY_ANNOTATION);
                        planningEntityAnnotation.setValue("difficultyComparatorClass",
                                                          dataObject.getName() + ".DifficultyComparator.class");

                        comparatorDefinitionService.call(getRemoteCallback(dataObject,
                                                                           comparatorObject))
                                .updateComparatorObject(dataObject,
                                                        comparatorObject);
                    }
                } else if (event instanceof DataObjectFieldChangeEvent) {
                    if (((DataObjectFieldChangeEvent) event).getChangeType() == ChangeType.FIELD_NAME_CHANGE) {

                        ComparatorDefinitionAnnotationValueHandler comparatorAnnotationHandler = new ComparatorDefinitionAnnotationValueHandler(comparatorDefinition);

                        List<Annotation> objectPropertyPaths = comparatorAnnotationHandler.getObjectPropertyPaths();

                        ListIterator<Annotation> objectPropertyPathListIterator = objectPropertyPaths.listIterator();

                        boolean changed = false;

                        while (objectPropertyPathListIterator.hasNext()) {
                            Annotation objectPropertyPath = objectPropertyPathListIterator.next();

                            List<Annotation> objectProperties = comparatorAnnotationHandler.getObjectProperties(objectPropertyPath);

                            if (!objectProperties.isEmpty() && ((DataObjectFieldChangeEvent) event).getOldValue().equals(comparatorAnnotationHandler.getName(objectProperties.get(0)))) {
                                comparatorAnnotationHandler.setName(objectProperties.get(0),
                                                                    (String) ((DataObjectFieldChangeEvent) event).getNewValue());
                                changed = true;
                            }
                        }

                        if (changed) {
                            comparatorDefinitionService.call(getRemoteCallback(dataObject,
                                                                               comparatorObject))
                                    .updateComparatorObject(dataObject,
                                                            comparatorObject);
                        }
                    } else if (((DataObjectFieldChangeEvent) event).getChangeType() == ChangeType.FIELD_TYPE_CHANGE) {

                        boolean changed = false;

                        ComparatorDefinitionAnnotationValueHandler comparatorAnnotationHandler = new ComparatorDefinitionAnnotationValueHandler(comparatorDefinition);

                        List<Annotation> objectPropertyPaths = comparatorAnnotationHandler.getObjectPropertyPaths();

                        ListIterator<Annotation> objectPropertyPathListIterator = objectPropertyPaths.listIterator();

                        while (objectPropertyPathListIterator.hasNext()) {
                            Annotation objectPropertyPath = objectPropertyPathListIterator.next();

                            List<Annotation> objectProperties = comparatorAnnotationHandler.getObjectProperties(objectPropertyPath);

                            ListIterator<Annotation> iterator = objectProperties.listIterator();

                            boolean remove = false;

                            if (iterator.hasNext()) {
                                Annotation objectProperty = iterator.next();
                                if (event.getCurrentField().getName().equals(comparatorAnnotationHandler.getName(objectProperty))) {
                                    comparatorAnnotationHandler.setType(objectProperty,
                                                                        (String) ((DataObjectFieldChangeEvent) event).getNewValue());
                                    changed = true;
                                    remove = true;
                                }
                            }

                            if (remove) {
                                while (iterator.hasNext()) {
                                    iterator.next();
                                    iterator.remove();
                                }

                                if (objectProperties.isEmpty()) {
                                    objectPropertyPathListIterator.remove();
                                }
                            }
                        }

                        if (changed) {
                            comparatorDefinitionService.call(getRemoteCallback(dataObject,
                                                                               comparatorObject))
                                    .updateComparatorObject(dataObject,
                                                            comparatorObject);
                        }
                    }
                }
            }
        }
    }

    private RemoteCallback<JavaClass> getRemoteCallback(DataObject dataObject,
                                                        JavaClass currentComparatorObject) {
        return new RemoteCallback<JavaClass>() {
            @Override
            public void callback(JavaClass updatedComparatorObject) {
                dataObject.removeNestedClass(currentComparatorObject);
                dataObject.addNestedClass(updatedComparatorObject);
            }
        };
    }

    private List<JavaClass> getComparatorNestedClasses(DataObject dataObject) {
        Annotation planningEntityAnnotation = dataObject.getAnnotation(PlannerDomainAnnotations.PLANNING_ENTITY_ANNOTATION);
        if (dataObject.getNestedClasses() != null && planningEntityAnnotation != null) {
            String difficultyComparatorClass = (String) planningEntityAnnotation.getValue("difficultyComparatorClass");
            if (difficultyComparatorClass != null && difficultyComparatorClass.matches("\\w[\\.\\w]+\\.class")) {
                String[] difficultyComparatorTokens = difficultyComparatorClass.split("\\.");
                return dataObject.getNestedClasses().stream()
                        .filter(t -> t.getName().equals(difficultyComparatorTokens[difficultyComparatorTokens.length - 2])
                                && t.getAnnotation(ComparatorDefinition.class.getName()) != null
                                && t.getAnnotation("javax.annotation.Generated") != null
                                && t.getInterfaces().stream().anyMatch(i -> i.startsWith(Comparator.class.getName())))
                        .collect(Collectors.toList());
            }
        }
        return Collections.emptyList();
    }

    @Override
    public boolean isDomainSpecificProperty(ObjectProperty objectProperty) {
        return objectProperty != null && "score".equals(objectProperty.getName()) && objectProperty.getAnnotation(PlannerDomainAnnotations.PLANNING_SCORE_ANNOTATION) != null;
    }
}
