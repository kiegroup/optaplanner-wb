/*
 * Copyright 2017 Red Hat, Inc. and/or its affiliates.
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

package org.optaplanner.workbench.screens.domaineditor.backend.server.validation;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.guvnor.common.services.shared.message.Level;
import org.guvnor.common.services.shared.validation.model.ValidationMessage;
import org.kie.workbench.common.services.datamodeller.core.DataObject;
import org.kie.workbench.common.services.refactoring.service.AssetsUsageService;
import org.kie.workbench.common.services.refactoring.service.ResourceType;
import org.kie.workbench.common.services.shared.validation.SaveValidator;
import org.optaplanner.workbench.screens.domaineditor.validation.PlanningSolutionToBeDuplicatedMessage;
import org.uberfire.backend.vfs.Path;

import static org.optaplanner.workbench.screens.domaineditor.model.PlannerDomainAnnotations.PLANNING_SOLUTION_ANNOTATION;

/**
 * Checks only one PlanningSolution data object exists in a data model
 */
@ApplicationScoped
public class PlanningSolutionSaveValidator implements SaveValidator<DataObject> {

    private AssetsUsageService assetsUsageService;

    @Inject
    public PlanningSolutionSaveValidator(AssetsUsageService assetsUsageService) {
        this.assetsUsageService = assetsUsageService;
    }

    @Override
    public Collection<ValidationMessage> validate(final Path dataObjectPath,
                                                  final DataObject dataObject) {
        if (dataObject != null && dataObject.getAnnotation(PLANNING_SOLUTION_ANNOTATION) != null) {
            List<Path> planningSolutionUsages = assetsUsageService.getAssetUsages(PLANNING_SOLUTION_ANNOTATION,
                                                                                  ResourceType.JAVA,
                                                                                  dataObjectPath);
            // PlanningSolution already present in this object
            if (planningSolutionUsages.contains(dataObjectPath)) {
                return Collections.emptyList();
            }
            // Check other PlanningSolution usages
            if (!planningSolutionUsages.isEmpty()) {
                return Arrays.asList(new PlanningSolutionToBeDuplicatedMessage(Level.ERROR));
            }
        }
        return Collections.emptyList();
    }

    @Override
    public boolean accept(final Path path) {
        return path.getFileName().endsWith(".java");
    }
}
