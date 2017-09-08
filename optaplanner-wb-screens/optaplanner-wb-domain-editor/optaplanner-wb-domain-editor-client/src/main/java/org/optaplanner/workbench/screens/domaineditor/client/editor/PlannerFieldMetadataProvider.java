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

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.google.gwt.resources.client.ImageResource;
import org.jboss.errai.ui.client.local.spi.TranslationService;
import org.kie.workbench.common.screens.datamodeller.model.editor.FieldMetadata;
import org.kie.workbench.common.screens.datamodeller.model.editor.FieldMetadataProvider;
import org.kie.workbench.common.screens.datamodeller.model.editor.ImageWrapper;
import org.kie.workbench.common.services.datamodeller.core.ObjectProperty;
import org.optaplanner.workbench.screens.domaineditor.client.resources.i18n.DomainEditorConstants;
import org.optaplanner.workbench.screens.domaineditor.client.resources.images.DomainImageResources;
import org.optaplanner.workbench.screens.domaineditor.model.PlannerDomainAnnotations;

@ApplicationScoped
public class PlannerFieldMetadataProvider implements FieldMetadataProvider {

    private Map<String, String> PLANNER_ANNOTATION_DESCRIPTION_MAP = new HashMap<>(3);

    private TranslationService translationService;

    @PostConstruct
    public void init() {
        PLANNER_ANNOTATION_DESCRIPTION_MAP.put(PlannerDomainAnnotations.PLANNING_SCORE_ANNOTATION,
                                               translationService.getTranslation(DomainEditorConstants.PlannerFieldMetadataProvider_PlanningScore));
        PLANNER_ANNOTATION_DESCRIPTION_MAP.put(PlannerDomainAnnotations.PLANNING_VARIABLE_ANNOTATION,
                                               translationService.getTranslation(DomainEditorConstants.PlannerFieldMetadataProvider_PlanningVariable));
        PLANNER_ANNOTATION_DESCRIPTION_MAP.put(PlannerDomainAnnotations.VALUE_RANGE_PROVIDER_ANNOTATION,
                                               translationService.getTranslation(DomainEditorConstants.PlannerFieldMetadataProvider_ValueRangeProvider));
    }

    @Inject
    public PlannerFieldMetadataProvider(final TranslationService translationService) {
        this.translationService = translationService;
    }

    @Override
    public Optional<FieldMetadata> getFieldMetadata(ObjectProperty objectProperty) {
        if (objectProperty.getAnnotations() != null) {
            return objectProperty.getAnnotations()
                    .stream()
                    .map(p -> PLANNER_ANNOTATION_DESCRIPTION_MAP.get(p.getClassName()))
                    .filter(Objects::nonNull)
                    .findFirst()
                    .map(description -> {
                        ImageResource imageResource = DomainImageResources.INSTANCE.optaPlannerDomainEditorFieldIcon();
                        ImageWrapper imageWrapper = new ImageWrapper(imageResource.getSafeUri().asString(),
                                                                     description);
                        return new FieldMetadata(imageWrapper);
                    });
        }
        return Optional.empty();
    }
}
