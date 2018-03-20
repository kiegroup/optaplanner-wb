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

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.Generated;
import javax.enterprise.context.ApplicationScoped;

import org.jboss.forge.roaster.model.Annotation;
import org.jboss.forge.roaster.model.JavaType;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.kie.workbench.common.screens.datamodeller.backend.server.indexing.JavaFileIndexerExtension;
import org.kie.workbench.common.services.refactoring.ResourceReference;
import org.kie.workbench.common.services.refactoring.backend.server.indexing.IndexBuilder;
import org.kie.workbench.common.services.refactoring.service.PartType;
import org.kie.workbench.common.services.refactoring.service.ResourceType;
import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.workbench.screens.domaineditor.model.ComparatorDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class ComparatorDefinitionIndexerExtension implements JavaFileIndexerExtension {

    private static final Logger logger = LoggerFactory.getLogger(ComparatorDefinitionIndexerExtension.class);

    @Override
    public void process(IndexBuilder builder,
                        JavaType javaType) {
        try {
            final List<Annotation> comparatorDefinitions;

            Annotation planningEntityAnnotation = javaType.getAnnotation(PlanningEntity.class.getName());

            if (planningEntityAnnotation == null) {
                return;
            }

            String difficultyComparatorClass = planningEntityAnnotation.getStringValue("difficultyComparatorClass");

            if (difficultyComparatorClass != null && javaType instanceof JavaClassSource && difficultyComparatorClass.matches("\\w[\\.\\w]+\\.class")) {
                String[] difficultyComparatorTokens = difficultyComparatorClass.split("\\.");
                comparatorDefinitions = ((JavaClassSource) javaType).getNestedTypes().stream()
                        .filter(t -> t instanceof JavaClassSource
                                && t.getName().equals(difficultyComparatorTokens[difficultyComparatorTokens.length - 2])
                                && t.getAnnotation(ComparatorDefinition.class.getName()) != null
                                && t.getAnnotation(Generated.class.getName()) != null
                                && ((JavaClassSource) t).getInterfaces().stream().anyMatch(i -> i.startsWith(Comparator.class.getName())))
                        .map(t -> t.getAnnotation(ComparatorDefinition.class.getName()))
                        .collect(Collectors.toList());
            } else {
                return;
            }

            if (javaType.getSyntaxErrors() == null || javaType.getSyntaxErrors().isEmpty()) {
                for (Annotation comparatorDefinition : comparatorDefinitions) {
                    Annotation[] fieldPathDefinitions = comparatorDefinition.getAnnotationArrayValue("objectPropertyPaths");

                    String previousFullyQualifiedClassname = null;

                    if (fieldPathDefinitions != null) {
                        for (Annotation fieldPathDefinition : fieldPathDefinitions) {
                            Annotation[] fieldDefinitions = fieldPathDefinition.getAnnotationArrayValue("objectProperties");

                            if (fieldDefinitions != null && fieldDefinitions.length > 0) {

                                previousFullyQualifiedClassname = fieldDefinitions[0].getStringValue("type");

                                if (previousFullyQualifiedClassname != null && previousFullyQualifiedClassname.matches("\\w[\\.\\w]+\\.class")) {
                                    for (int i = 1; i < fieldDefinitions.length; i++) {
                                        ResourceReference resourceReference = new ResourceReference(previousFullyQualifiedClassname.substring(0,
                                                                                                                                              previousFullyQualifiedClassname.indexOf(".class")),
                                                                                                    ResourceType.JAVA);
                                        resourceReference.addPartReference(fieldDefinitions[i].getStringValue("name"),
                                                                           PartType.FIELD);

                                        previousFullyQualifiedClassname = fieldDefinitions[i].getStringValue("type");

                                        builder.addGenerator(resourceReference);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Unable to index comparator definition for " + javaType.getQualifiedName());
        }
    }
}
