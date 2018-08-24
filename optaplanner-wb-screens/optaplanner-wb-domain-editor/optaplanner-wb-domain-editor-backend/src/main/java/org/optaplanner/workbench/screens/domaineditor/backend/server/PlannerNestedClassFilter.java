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

import javax.annotation.Generated;
import javax.enterprise.context.ApplicationScoped;

import org.jboss.forge.roaster.model.JavaClass;
import org.jboss.forge.roaster.model.JavaType;
import org.kie.workbench.common.services.datamodeller.driver.NestedClassFilter;
import org.optaplanner.workbench.screens.domaineditor.model.ComparatorDefinition;

@ApplicationScoped
public class PlannerNestedClassFilter implements NestedClassFilter {

    @Override
    public boolean accept(JavaType<?> javaType) {
        return javaType.isClass() && (javaType.getAnnotation(Generated.class.getName()) != null)
                && (javaType.getAnnotation(ComparatorDefinition.class.getName()) != null)
                && ((JavaClass) javaType).getInterfaces().stream().anyMatch(s -> s.startsWith(Comparator.class.getName()));
    }
}
