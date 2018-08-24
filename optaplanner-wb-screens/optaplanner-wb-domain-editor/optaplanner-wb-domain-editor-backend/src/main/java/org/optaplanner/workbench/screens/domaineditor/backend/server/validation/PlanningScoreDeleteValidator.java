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

import javax.annotation.Generated;
import javax.enterprise.context.ApplicationScoped;

import org.guvnor.common.services.shared.validation.model.ValidationMessage;
import org.kie.workbench.common.screens.datamodeller.validation.ObjectPropertyDeleteValidator;
import org.kie.workbench.common.services.datamodeller.core.DataObject;
import org.kie.workbench.common.services.datamodeller.core.ObjectProperty;
import org.optaplanner.core.api.domain.solution.PlanningScore;
import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.optaplanner.workbench.screens.domaineditor.validation.PlanningScoreToBeDeletedMessage;

/**
 * Checks whether a generated score field is deleted. A correctly configured Planning Solution needs to define
 * a single score field.
 */
@ApplicationScoped
public class PlanningScoreDeleteValidator implements ObjectPropertyDeleteValidator {

    @Override
    public Collection<ValidationMessage> validate(final DataObject dataObject,
                                                  final ObjectProperty objectProperty) {
        final boolean isGeneratedPlanningScoreField =
                dataObject != null
                        && dataObject.getAnnotation(PlanningSolution.class.getName()) != null
                        && objectProperty != null
                        && "score".equals(objectProperty.getName())
                        && objectProperty.getAnnotation(PlanningScore.class.getName()) != null
                        && objectProperty.getAnnotation(Generated.class.getName()) != null;
        return isGeneratedPlanningScoreField ? Arrays.asList(new PlanningScoreToBeDeletedMessage()) : Collections.emptyList();
    }
}
