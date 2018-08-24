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

import java.util.Collection;

import javax.annotation.Generated;

import org.guvnor.common.services.shared.validation.model.ValidationMessage;
import org.junit.Before;
import org.junit.Test;
import org.kie.workbench.common.services.datamodeller.core.DataObject;
import org.kie.workbench.common.services.datamodeller.core.ObjectProperty;
import org.kie.workbench.common.services.datamodeller.core.impl.AnnotationImpl;
import org.kie.workbench.common.services.datamodeller.core.impl.DataObjectImpl;
import org.kie.workbench.common.services.datamodeller.core.impl.ObjectPropertyImpl;
import org.kie.workbench.common.services.datamodeller.util.DriverUtils;
import org.optaplanner.core.api.domain.solution.PlanningScore;
import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;
import org.optaplanner.workbench.screens.domaineditor.validation.PlanningScoreToBeDeletedMessage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class PlanningScoreDeleteValidatorTest {

    private PlanningScoreDeleteValidator deleteValidator;

    @Before
    public void setUp() {
        this.deleteValidator = new PlanningScoreDeleteValidator();
    }

    @Test
    public void validateIsAPlanningScore() {
        DataObject dataObject = new DataObjectImpl("test",
                                                   "PlanningSolution");
        dataObject.addAnnotation(new AnnotationImpl(DriverUtils.buildAnnotationDefinition(PlanningSolution.class)));

        ObjectProperty objectProperty = new ObjectPropertyImpl("score",
                                                               HardSoftScore.class.getName(),
                                                               false);
        objectProperty.addAnnotation(new AnnotationImpl(DriverUtils.buildAnnotationDefinition(PlanningScore.class)));
        objectProperty.addAnnotation(new AnnotationImpl(DriverUtils.buildAnnotationDefinition(Generated.class)));

        Collection<ValidationMessage> result = deleteValidator.validate(dataObject,
                                                                        objectProperty);
        assertEquals(1,
                     result.size());
        assertTrue(result.iterator().next() instanceof PlanningScoreToBeDeletedMessage);
    }

    @Test
    public void validateIsNotAPlanningScore() {
        DataObject dataObject = new DataObjectImpl("test",
                                                   "PlainDataObject");
        ObjectProperty objectProperty = new ObjectPropertyImpl("notAPlanningScore",
                                                               Integer.class.getName(),
                                                               false);

        Collection<ValidationMessage> result = deleteValidator.validate(dataObject,
                                                                        objectProperty);

        assertEquals(0,
                     result.size());
    }

    @Test
    public void validateNullObject() {
        assertEquals(0, deleteValidator.validate(null, null).size());
    }

    @Test
    public void validateNullProperty() {
        DataObject dataObject = new DataObjectImpl("test", "PlanningSolution");
        dataObject.addAnnotation(new AnnotationImpl(DriverUtils.buildAnnotationDefinition(PlanningSolution.class)));
        assertEquals(0, deleteValidator.validate(dataObject, null).size());
    }
}
