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

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.errai.common.client.api.Caller;
import org.jboss.errai.common.client.api.RemoteCallback;
import org.kie.workbench.common.screens.datamodeller.client.command.DataModelCommand;
import org.kie.workbench.common.screens.datamodeller.client.handlers.DomainHandler;
import org.kie.workbench.common.screens.datamodeller.client.widgets.common.domain.ResourceOptions;
import org.kie.workbench.common.screens.datamodeller.events.ChangeType;
import org.kie.workbench.common.screens.datamodeller.events.DataModelerEvent;
import org.kie.workbench.common.screens.datamodeller.events.DataObjectChangeEvent;
import org.kie.workbench.common.screens.datamodeller.events.DataObjectFieldChangeEvent;
import org.kie.workbench.common.screens.datamodeller.events.DataObjectFieldDeletedEvent;
import org.kie.workbench.common.services.datamodeller.core.Annotation;
import org.kie.workbench.common.services.datamodeller.core.DataObject;
import org.kie.workbench.common.services.datamodeller.core.JavaClass;
import org.kie.workbench.common.services.datamodeller.core.ObjectProperty;
import org.optaplanner.workbench.screens.domaineditor.model.ComparatorDefinition;
import org.optaplanner.workbench.screens.domaineditor.model.ComparatorObject;
import org.optaplanner.workbench.screens.domaineditor.model.ObjectPropertyPath;
import org.optaplanner.workbench.screens.domaineditor.model.PlannerDomainAnnotations;
import org.optaplanner.workbench.screens.domaineditor.service.PlannerDataObjectEditorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class PlannerDomainHandler implements DomainHandler {

    private static final Logger logger = LoggerFactory.getLogger( PlannerDomainHandler.class );

    @Inject
    private Caller<PlannerDataObjectEditorService> plannerDataObjectEditorService;

    public PlannerDomainHandler() {
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
    public ResourceOptions getResourceOptions( boolean newInstance ) {
        //not apply for this domain.
        return null;
    }

    @Override
    public void postCommandProcessing( DataModelCommand command ) {
        //not apply for this domain.
    }

    @Override
    public void postEventProcessing( DataModelerEvent event ) {
        try {
            DataObject dataObject = event.getCurrentDataObject();
            if ( dataObject != null && dataObject.getAnnotation( PlannerDomainAnnotations.PLANNING_ENTITY_ANNOTATION ) != null ) {
                Annotation comparatorDefinition = dataObject.getAnnotation( ComparatorDefinition.class.getName() );
                if ( comparatorDefinition == null ) {
                    return;
                }
                if ( dataObject.getNestedClasses() == null || dataObject.getNestedClasses().isEmpty() ) {
                    return;
                }

                ComparatorObject comparatorObject = getComparatorNestedClass( dataObject );

                if ( event instanceof DataObjectFieldDeletedEvent ) {
                    boolean changed = false;
                    ListIterator<ObjectPropertyPath> objectPropertyPathListIterator = comparatorObject.getObjectPropertyPathList().listIterator();
                    while ( objectPropertyPathListIterator.hasNext() ) {
                        ObjectPropertyPath objectPropertyPath = objectPropertyPathListIterator.next();
                        ListIterator<ObjectProperty> iterator = objectPropertyPath.getObjectPropertyPath().listIterator();
                        boolean remove = false;
                        while ( iterator.hasNext() ) {
                            ObjectProperty objectProperty = iterator.next();
                            if ( remove ) {
                                iterator.remove();
                                continue;
                            }
                            if ( objectProperty.equals( event.getCurrentField() ) ) {
                                iterator.remove();
                                remove = true;
                                changed = true;
                            }
                        }
                        if ( objectPropertyPath.getObjectPropertyPath().isEmpty() ) {
                            objectPropertyPathListIterator.remove();
                        }
                    }
                    if ( changed ) {
                        plannerDataObjectEditorService.call( new RemoteCallback<ComparatorObject>() {
                            @Override
                            public void callback( ComparatorObject updatedComparatorObject ) {
                                dataObject.getNestedClasses().clear();
                                if ( updatedComparatorObject != null ) {
                                    dataObject.addNestedClass( updatedComparatorObject );

                                    comparatorDefinition.setValue( "fieldPaths", getAnnotationDef( updatedComparatorObject.getObjectPropertyPathList() ) );
                                } else {
                                    dataObject.removeAnnotation( comparatorDefinition.getClassName() );
                                    Annotation planningEntityAnnotation = dataObject.getAnnotation( PlannerDomainAnnotations.PLANNING_ENTITY_ANNOTATION );
                                    if ( planningEntityAnnotation != null ) {
                                        planningEntityAnnotation.removeValue( "difficultyComparatorClass" );
                                    }
                                }
                            }
                        } ).updateComparatorObject( dataObject, comparatorObject, comparatorObject.getObjectPropertyPathList() );
                    }
                } else if ( event instanceof DataObjectChangeEvent ) {
                    if ( ( (DataObjectChangeEvent) event ).getChangeType() == ChangeType.OBJECT_NAME_CHANGE
                            || ( (DataObjectChangeEvent) event ).getChangeType() == ChangeType.PACKAGE_NAME_CHANGE
                            || ( (DataObjectChangeEvent) event ).getChangeType() == ChangeType.CLASS_NAME_CHANGE ) {

                        Annotation planningEntityAnnotation = dataObject.getAnnotation( PlannerDomainAnnotations.PLANNING_ENTITY_ANNOTATION );
                        planningEntityAnnotation.setValue( "difficultyComparatorClass", dataObject.getName() + "." + dataObject.getName() + "Comparator.class" );

                        plannerDataObjectEditorService.call( new RemoteCallback<ComparatorObject>() {
                            @Override
                            public void callback( ComparatorObject updatedComparatorObject ) {
                                dataObject.getNestedClasses().clear();
                                if ( updatedComparatorObject != null ) {
                                    dataObject.addNestedClass( updatedComparatorObject );

                                    comparatorDefinition.setValue( "fieldPaths", getAnnotationDef( updatedComparatorObject.getObjectPropertyPathList() ) );
                                } else {
                                    dataObject.removeAnnotation( comparatorDefinition.getClassName() );
                                    Annotation planningEntityAnnotation = dataObject.getAnnotation( PlannerDomainAnnotations.PLANNING_ENTITY_ANNOTATION );
                                    if ( planningEntityAnnotation != null ) {
                                        planningEntityAnnotation.removeValue( "difficultyComparatorClass" );
                                    }
                                }
                            }
                        } ).updateComparatorObject( dataObject, comparatorObject, comparatorObject.getObjectPropertyPathList() );
                    }
                } else if ( event instanceof DataObjectFieldChangeEvent ) {
                    if ( ( (DataObjectFieldChangeEvent) event ).getChangeType() == ChangeType.FIELD_NAME_CHANGE ) {
                        for ( ObjectPropertyPath objectPropertyPath : comparatorObject.getObjectPropertyPathList() ) {
                            List<ObjectProperty> objectPropertyList = objectPropertyPath.getObjectPropertyPath();
                            if ( objectPropertyList != null && !objectPropertyList.isEmpty() ) {
                                if ( objectPropertyList.get( 0 ).getName().equals( ( (DataObjectFieldChangeEvent) event ).getOldValue() ) ) {
                                    objectPropertyList.get( 0 ).setName( ( (DataObjectFieldChangeEvent) event ).getNewValue().toString() );
                                    break;
                                }
                            }
                        }

                        plannerDataObjectEditorService.call( new RemoteCallback<ComparatorObject>() {
                            @Override
                            public void callback( ComparatorObject updatedComparatorObject ) {
                                dataObject.getNestedClasses().clear();
                                if ( updatedComparatorObject != null ) {
                                    dataObject.addNestedClass( updatedComparatorObject );

                                    comparatorDefinition.setValue( "fieldPaths", getAnnotationDef( updatedComparatorObject.getObjectPropertyPathList() ) );
                                } else {
                                    dataObject.removeAnnotation( comparatorDefinition.getClassName() );
                                    Annotation planningEntityAnnotation = dataObject.getAnnotation( PlannerDomainAnnotations.PLANNING_ENTITY_ANNOTATION );
                                    if ( planningEntityAnnotation != null ) {
                                        planningEntityAnnotation.removeValue( "difficultyComparatorClass" );
                                    }
                                }
                            }
                        } ).updateComparatorObject( dataObject, comparatorObject, comparatorObject.getObjectPropertyPathList() );
                    } else if ( ( (DataObjectFieldChangeEvent) event ).getChangeType() == ChangeType.FIELD_TYPE_CHANGE ) {
                        boolean changed = false;
                        ListIterator<ObjectPropertyPath> objectPropertyPathListIterator = comparatorObject.getObjectPropertyPathList().listIterator();
                        while ( objectPropertyPathListIterator.hasNext() ) {
                            ObjectPropertyPath objectPropertyPath = objectPropertyPathListIterator.next();
                            ListIterator<ObjectProperty> iterator = objectPropertyPath.getObjectPropertyPath().listIterator();
                            boolean remove = false;
                            while ( iterator.hasNext() ) {
                                ObjectProperty objectProperty = iterator.next();
                                if ( remove ) {
                                    iterator.remove();
                                    continue;
                                }
                                if ( objectProperty.equals( event.getCurrentField() ) ) {
                                    // remove just all subsequent items in the path
                                    remove = true;
                                    changed = true;
                                }
                            }
                            if ( objectPropertyPath.getObjectPropertyPath().isEmpty() ) {
                                objectPropertyPathListIterator.remove();
                            }
                        }
                        if ( changed ) {
                            plannerDataObjectEditorService.call( new RemoteCallback<ComparatorObject>() {
                                @Override
                                public void callback( ComparatorObject updatedComparatorObject ) {
                                    dataObject.getNestedClasses().clear();
                                    if ( updatedComparatorObject != null ) {
                                        dataObject.addNestedClass( updatedComparatorObject );

                                        comparatorDefinition.setValue( "fieldPaths", getAnnotationDef( updatedComparatorObject.getObjectPropertyPathList() ) );
                                    } else {
                                        dataObject.removeAnnotation( comparatorDefinition.getClassName() );
                                        Annotation planningEntityAnnotation = dataObject.getAnnotation( PlannerDomainAnnotations.PLANNING_ENTITY_ANNOTATION );
                                        if ( planningEntityAnnotation != null ) {
                                            planningEntityAnnotation.removeValue( "difficultyComparatorClass" );
                                        }
                                    }
                                }
                            } ).updateComparatorObject( dataObject, comparatorObject, comparatorObject.getObjectPropertyPathList() );
                        }
                    }
                }
            }
        } catch ( Exception e ) {
            logger.error( "Unexpected error while processing data modeller event " + event, e );
        }
    }

    private ComparatorObject getComparatorNestedClass( DataObject dataObject ) {
        for ( JavaClass javaClass : dataObject.getNestedClasses() ) {
            if ( javaClass instanceof ComparatorObject ) {
                return (ComparatorObject) javaClass;
            }
        }
        throw new IllegalStateException( "Data object " + dataObject.getClassName() + " contains " + ComparatorDefinition.class.getName() + " annotation, " +
                "however no nested comparator object is present." );
    }

    private List<String> getAnnotationDef( List<ObjectPropertyPath> objectPropertyPaths ) {
        List<String> objectPropertyPathList = new ArrayList<>();
        for ( ObjectPropertyPath objectPropertyPath : objectPropertyPaths ) {
            StringBuilder pathBuilder = new StringBuilder();
            List<ObjectProperty> path = objectPropertyPath.getObjectPropertyPath();
            for ( int i = 0; i < path.size(); i++ ) {
                ObjectProperty objectProperty = path.get( i );

                pathBuilder.append( objectProperty.getClassName() ).append( ":" ).append( objectProperty.getName() );
                if ( i != path.size() - 1 ) {
                    pathBuilder.append( "-" );
                }
            }
            pathBuilder.append( "=" ).append( objectPropertyPath.isDescending() ? "desc" : "asc" );

            objectPropertyPathList.add( pathBuilder.toString() );
        }
        return objectPropertyPathList;
    }
}
