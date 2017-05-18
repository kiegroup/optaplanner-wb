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
import javax.inject.Named;

import org.guvnor.common.services.shared.message.Level;
import org.guvnor.common.services.shared.validation.model.ValidationMessage;
import org.kie.workbench.common.screens.datamodeller.model.GenerationResult;
import org.kie.workbench.common.screens.datamodeller.service.DataModelerService;
import org.kie.workbench.common.services.datamodeller.core.DataObject;
import org.kie.workbench.common.services.shared.validation.SaveValidator;
import org.optaplanner.workbench.screens.domaineditor.validation.ScoreHolderGlobalToBeRemovedMessage;
import org.optaplanner.workbench.screens.domaineditor.validation.ScoreHolderGlobalTypeNotRecognizedMessage;
import org.optaplanner.workbench.screens.domaineditor.validation.ScoreHolderGlobalTypeToBeChangedMessage;
import org.uberfire.backend.server.util.Paths;
import org.uberfire.backend.vfs.Path;
import org.uberfire.io.IOService;

import static org.optaplanner.workbench.screens.domaineditor.model.PlannerDomainAnnotations.PLANNING_SOLUTION_ANNOTATION;

/**
 * Check whether data object to be saved:
 * <ol>
 * <li>Is a Planning Solution and score type has changed.</li>
 * <li>Changes from a Planning Solution to a different data object type</li>
 * </ol>
 * <p>
 * Display warning message as the type of 'scoreHolder' global variable associated with a score type defined in the Planning Solution
 * will be changed (or the score holder global will be deleted) as a consequence. This potentially breaks all the rules where
 * the 'scoreHolder' global variable is referenced.
 */
@ApplicationScoped
public class PlanningSolutionScoreHolderSaveValidator implements SaveValidator<DataObject> {

    private DataModelerService dataModelerService;

    private IOService ioService;

    private ScoreHolderUtils scoreHolderUtils;

    @Inject
    public PlanningSolutionScoreHolderSaveValidator(final DataModelerService dataModelerService,
                                                    @Named("ioStrategy") final IOService ioService,
                                                    final ScoreHolderUtils scoreHolderUtils) {
        this.dataModelerService = dataModelerService;
        this.ioService = ioService;
        this.scoreHolderUtils = scoreHolderUtils;
    }

    @Override
    public Collection<ValidationMessage> validate(final Path dataObjectPath,
                                                  final DataObject dataObject) {
        if (dataObjectPath != null) {
            String dataObjectSource = ioService.readAllString(Paths.convert(dataObjectPath));
            GenerationResult generationResult = dataModelerService.loadDataObject(dataObjectPath,
                                                                                  dataObjectSource,
                                                                                  dataObjectPath);
            if (generationResult.hasErrors()) {
                return Collections.emptyList();
            } else {
                DataObject originalDataObject = generationResult.getDataObject();

                if (originalDataObject.getAnnotation(PLANNING_SOLUTION_ANNOTATION) != null) {
                    String originalDataObjectScoreTypeFqn = scoreHolderUtils.extractScoreTypeFqn(originalDataObject);

                    String originalDataObjectScoreHolderTypeFqn = scoreHolderUtils.getScoreHolderTypeFqn(originalDataObjectScoreTypeFqn);

                    if (originalDataObjectScoreHolderTypeFqn == null) {
                        return Arrays.asList(new ScoreHolderGlobalTypeNotRecognizedMessage(Level.WARNING));
                    }

                    List<Path> scoreHolderGlobalUsages = dataModelerService.findClassUsages(dataObjectPath,
                                                                                            originalDataObjectScoreHolderTypeFqn);
                    if (scoreHolderGlobalUsages.isEmpty()) {
                        return Collections.emptyList();
                    } else {
                        // Planning Solution -> No Planning Solution
                        if (dataObject.getAnnotation(PLANNING_SOLUTION_ANNOTATION) == null) {
                            return Arrays.asList(new ScoreHolderGlobalToBeRemovedMessage(Level.WARNING));
                        } else {
                            // Planning Solution Type T1 -> Planning Solution Type T2
                            String dataObjectScoreTypeFqn = scoreHolderUtils.extractScoreTypeFqn(dataObject);

                            String dataObjectScoreHolderTypeFqn = scoreHolderUtils.getScoreHolderTypeFqn(dataObjectScoreTypeFqn);

                            if (dataObjectScoreHolderTypeFqn == null) {
                                return Arrays.asList(new ScoreHolderGlobalTypeNotRecognizedMessage(Level.WARNING));
                            }

                            // Planning solution has changed
                            if (!originalDataObjectScoreHolderTypeFqn.equals(dataObjectScoreHolderTypeFqn)) {
                                return Arrays.asList(new ScoreHolderGlobalTypeToBeChangedMessage(Level.WARNING));
                            }
                        }
                    }
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
