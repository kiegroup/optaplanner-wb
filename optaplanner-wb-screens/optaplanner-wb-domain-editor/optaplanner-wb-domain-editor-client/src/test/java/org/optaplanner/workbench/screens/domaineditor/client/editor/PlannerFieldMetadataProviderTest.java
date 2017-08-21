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

package org.optaplanner.workbench.screens.domaineditor.client.editor;

import java.util.Optional;
import javax.annotation.Generated;

import com.google.gwtmockito.GwtMockitoTestRunner;
import org.jboss.errai.ui.client.local.spi.TranslationService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kie.workbench.common.screens.datamodeller.model.editor.FieldMetadata;
import org.kie.workbench.common.services.datamodeller.core.Annotation;
import org.kie.workbench.common.services.datamodeller.core.ObjectProperty;
import org.kie.workbench.common.services.datamodeller.core.impl.AnnotationImpl;
import org.kie.workbench.common.services.datamodeller.core.impl.ObjectPropertyImpl;
import org.kie.workbench.common.services.datamodeller.util.DriverUtils;
import org.mockito.Mock;
import org.optaplanner.core.api.domain.solution.PlanningScore;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(GwtMockitoTestRunner.class)
public class PlannerFieldMetadataProviderTest {

    @Mock
    private TranslationService translationService;

    private PlannerFieldMetadataProvider fieldMetadataProvider;

    @Before
    public void setUp() {
        when(translationService.getTranslation(anyString())).thenReturn("translation");
        fieldMetadataProvider = new PlannerFieldMetadataProvider(translationService);
        // emulate @PostConstruct call
        fieldMetadataProvider.init();
    }

    @Test
    public void getFieldMetadataMatchingField() {
        Annotation planningScoreAnnotation = new AnnotationImpl(DriverUtils.buildAnnotationDefinition(PlanningScore.class));
        ObjectProperty matchingField = new ObjectPropertyImpl("matchingField",
                                                              HardSoftScore.class.getName(),
                                                              false);
        matchingField.addAnnotation(planningScoreAnnotation);

        Optional<FieldMetadata> fieldMetadata = fieldMetadataProvider.getFieldMetadata(matchingField);

        assertTrue(fieldMetadata.isPresent());
    }

    @Test
    public void getFieldMetadataNonMatchingField() {
        Annotation generatedAnnotation = new AnnotationImpl(DriverUtils.buildAnnotationDefinition(Generated.class));
        ObjectProperty nonMatchingField = new ObjectPropertyImpl("nonMatchingField",
                                                                 Integer.class.getName(),
                                                                 false);
        nonMatchingField.addAnnotation(generatedAnnotation);

        Optional<FieldMetadata> fieldMetadata = fieldMetadataProvider.getFieldMetadata(nonMatchingField);

        assertFalse(fieldMetadata.isPresent());
    }
}
