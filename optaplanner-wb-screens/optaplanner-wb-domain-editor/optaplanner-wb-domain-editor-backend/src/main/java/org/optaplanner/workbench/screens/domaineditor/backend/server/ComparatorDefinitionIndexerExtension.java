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

import javax.enterprise.context.ApplicationScoped;

import org.jboss.forge.roaster.model.Annotation;
import org.jboss.forge.roaster.model.JavaType;
import org.kie.workbench.common.screens.datamodeller.backend.server.indexing.JavaFileIndexerExtension;
import org.kie.workbench.common.services.refactoring.backend.server.indexing.DefaultIndexBuilder;
import org.kie.workbench.common.services.refactoring.model.index.ResourceReference;
import org.kie.workbench.common.services.refactoring.service.PartType;
import org.kie.workbench.common.services.refactoring.service.ResourceType;
import org.optaplanner.workbench.screens.domaineditor.model.ComparatorDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class ComparatorDefinitionIndexerExtension implements JavaFileIndexerExtension {

    private static final Logger logger = LoggerFactory.getLogger( ComparatorDefinitionIndexerExtension.class );

    @Override
    public void process( DefaultIndexBuilder builder, JavaType javaType ) {
        try {
            Annotation comparatorDefinition = javaType.getAnnotation( ComparatorDefinition.class.getName() );

            if ( comparatorDefinition == null ) {
                return;
            }

            if ( javaType.getSyntaxErrors() == null || javaType.getSyntaxErrors().isEmpty() ) {

                String[] fieldPathDefinitions = comparatorDefinition.getStringArrayValue( "fieldPaths" );

                String previousFullyQualifiedClassname = null;

                if ( fieldPathDefinitions != null ) {
                    for ( String fieldPathDefinition : fieldPathDefinitions ) {
                        if ( !fieldPathDefinition.matches( "\\w[\\.\\w]*:\\w[\\-\\w[\\.\\w]*:\\w]*=(asc|desc)" ) ) {
                            throw new IllegalStateException( "Invalid field path string " + fieldPathDefinition );
                        }
                        String[] fieldPathDefinitionParts = fieldPathDefinition.split( "=" );
                        String[] fieldPath = fieldPathDefinitionParts[0].split( "-" );

                        previousFullyQualifiedClassname = fieldPath[0].split( ":" )[0];

                        for (int i = 1; i < fieldPath.length; i++) {
                            String[] fieldPathElementPair = fieldPath[i].split( ":" );

                            ResourceReference resourceReference = new ResourceReference( previousFullyQualifiedClassname, ResourceType.JAVA );
                            resourceReference.addPartReference( fieldPathElementPair[1], PartType.FIELD );

                            previousFullyQualifiedClassname = fieldPathElementPair[0];

                            builder.addGenerator( resourceReference );
                        }
                    }
                }
            }
        } catch ( Exception e ) {
            logger.error( "Unable to index comparator definition for " + javaType.getQualifiedName() );
        }
    }
}
