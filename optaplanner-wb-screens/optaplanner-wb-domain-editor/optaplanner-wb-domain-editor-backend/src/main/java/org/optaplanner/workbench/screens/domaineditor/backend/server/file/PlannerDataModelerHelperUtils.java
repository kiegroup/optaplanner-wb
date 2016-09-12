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

package org.optaplanner.workbench.screens.domaineditor.backend.server.file;

import java.util.Comparator;
import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.inject.Named;

import org.kie.workbench.common.screens.datamodeller.events.DataObjectRenamedEvent;
import org.kie.workbench.common.screens.datamodeller.model.GenerationResult;
import org.kie.workbench.common.screens.datamodeller.service.DataModelerService;
import org.kie.workbench.common.services.datamodeller.core.Annotation;
import org.kie.workbench.common.services.datamodeller.core.DataObject;
import org.kie.workbench.common.services.datamodeller.core.JavaClass;
import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.workbench.screens.domaineditor.model.ComparatorDefinition;
import org.optaplanner.workbench.screens.domaineditor.model.PlannerDomainAnnotations;
import org.optaplanner.workbench.screens.domaineditor.service.ComparatorDefinitionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.uberfire.backend.server.util.Paths;
import org.uberfire.backend.vfs.Path;
import org.uberfire.io.IOService;

@ApplicationScoped
public class PlannerDataModelerHelperUtils {

    private static final Logger logger = LoggerFactory.getLogger( PlannerDataModelerHelperUtils.class );

    @Inject
    @Named( "ioStrategy" )
    private IOService ioService;

    @Inject
    private ComparatorDefinitionService comparatorDefinitionService;

    @Inject
    private DataModelerService dataModelerService;

    public void updateDataObject( Path dataObjectPath ) {
        String dataObjectString = ioService.readAllString( Paths.convert( dataObjectPath ) );

        GenerationResult generationResult = dataModelerService.loadDataObject( dataObjectPath, dataObjectString, dataObjectPath );

        DataObject dataObject = generationResult.getDataObject();

        if ( dataObject != null && generationResult.getErrors() == null || generationResult.getErrors().isEmpty() ) {
            JavaClass comparatorObject = getComparatorObject( dataObject );
            if ( comparatorObject != null ) {
                JavaClass updatedComparatorObject = comparatorDefinitionService.updateComparatorObject( dataObject, comparatorObject );

                dataObject.removeNestedClass( comparatorObject );
                dataObject.addNestedClass( updatedComparatorObject );

                Annotation planningEntityAnnotation = dataObject.getAnnotation( PlanningEntity.class.getName() );
                if ( planningEntityAnnotation != null ) {
                    planningEntityAnnotation.setValue( "difficultyComparatorClass", dataObject.getName() + ".DifficultyComparator" );
                }

                generationResult = dataModelerService.updateSource( dataObjectString, dataObjectPath, dataObject );

                if ( generationResult.getDataObject() != null && generationResult.getErrors() == null || generationResult.getErrors().isEmpty() ) {
                    ioService.write( Paths.convert( dataObjectPath ), generationResult.getSource() );
                } else {
                    logger.error( "Data object " + dataObject.getClassName() + " couldn't be updated, path: " + dataObjectPath + "." );
                }
            }
        } else {
            logger.error( "Data object couldn't be loaded, path: " + dataObjectPath + "." );
        }
    }

    // TODO introduce PlannerDatamodelerRenameHelper once DataModelerService.rename uses RenameService to rename DataObject
    private void onDataObjectRename( @Observes DataObjectRenamedEvent event ) {
        Path updatedPath = event.getPath();
        if ( updatedPath != null ) {
            try {
                updateDataObject( updatedPath );
            } catch ( Exception e ) {
                logger.error( "Data object couldn't be updated, path: " + updatedPath + "." );
            }
        }
    }

    private JavaClass getComparatorObject( DataObject dataObject ) {
        Annotation planningEntityAnnotation = dataObject.getAnnotation( PlannerDomainAnnotations.PLANNING_ENTITY_ANNOTATION );
        if ( dataObject.getNestedClasses() != null && planningEntityAnnotation != null ) {
            String difficultyComparatorClass = (String) planningEntityAnnotation.getValue( "difficultyComparatorClass" );
            if ( difficultyComparatorClass != null && difficultyComparatorClass.matches( "\\w[\\.\\w]+\\.class" ) ) {
                String[] difficultyComparatorTokens = difficultyComparatorClass.split( "\\." );
                Optional<JavaClass> comparatorNestedClass = dataObject.getNestedClasses().stream()
                        .filter( t -> t.getName().equals( difficultyComparatorTokens[difficultyComparatorTokens.length - 2] )
                                && t.getAnnotation( ComparatorDefinition.class.getName() ) != null
                                && t.getAnnotation( "javax.annotation.Generated" ) != null
                                && t.getInterfaces().stream().anyMatch( i -> i.startsWith( Comparator.class.getName() ) ) )
                        .findFirst();
                if ( comparatorNestedClass.isPresent() ) {
                    return comparatorNestedClass.get();
                }
            }
        }
        return null;
    }

}
