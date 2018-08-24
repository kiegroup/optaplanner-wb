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

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.guvnor.common.services.shared.message.Level;
import org.guvnor.common.services.shared.validation.model.ValidationMessage;
import org.kie.workbench.common.screens.datamodeller.model.GenerationResult;
import org.kie.workbench.common.screens.datamodeller.service.DataModelerService;
import org.kie.workbench.common.services.datamodeller.core.DataObject;
import org.kie.workbench.common.services.shared.validation.CopyValidator;
import org.optaplanner.workbench.screens.domaineditor.validation.PlanningSolutionToBeDuplicatedMessage;
import org.uberfire.backend.server.util.Paths;
import org.uberfire.backend.vfs.Path;
import org.uberfire.io.IOService;

import static org.optaplanner.workbench.screens.domaineditor.model.PlannerDomainAnnotations.PLANNING_SOLUTION_ANNOTATION;

/**
 * Checks whether data object contains a PlanningSolution annotation. This would result in multiple planning solutions
 * within a single project after copying the file.
 */
@ApplicationScoped
public class PlanningSolutionCopyValidator implements CopyValidator<DataObject> {

    private DataModelerService dataModelerService;

    private IOService ioService;

    @Inject
    public PlanningSolutionCopyValidator(final DataModelerService dataModelerService,
                                         @Named("ioStrategy") final IOService ioService) {
        this.dataModelerService = dataModelerService;
        this.ioService = ioService;
    }

    @Override
    public Collection<ValidationMessage> validate(final Path dataObjectPath,
                                                  final DataObject dataObject) {
        if (dataObject != null && dataObject.getAnnotation(PLANNING_SOLUTION_ANNOTATION) != null) {
            return Arrays.asList(new PlanningSolutionToBeDuplicatedMessage(Level.ERROR));
        }
        return Collections.emptyList();
    }

    @Override
    public Collection<ValidationMessage> validate(final Path path) {
        if (path != null) {
            String dataObjectSource = ioService.readAllString(Paths.convert(path));
            GenerationResult generationResult = dataModelerService.loadDataObject(path,
                                                                                  dataObjectSource,
                                                                                  path);

            if (generationResult.hasErrors()) {
                return Collections.emptyList();
            } else {
                DataObject dataObject = generationResult.getDataObject();
                if (dataObject != null && dataObject.getAnnotation(PLANNING_SOLUTION_ANNOTATION) != null) {
                    return Arrays.asList(new PlanningSolutionToBeDuplicatedMessage(Level.ERROR));
                }
            }
        }
        return Collections.emptyList();
    }

    @Override
    public boolean accept(final Path path) {
        return path.getFileName().endsWith(".java");
    }
}
