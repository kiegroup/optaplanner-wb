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

package org.optaplanner.workbench.screens.domaineditor.client.validation;

import java.util.HashMap;
import java.util.Map;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.guvnor.common.services.shared.validation.model.ValidationMessage;
import org.jboss.errai.ui.client.local.spi.TranslationService;
import org.kie.workbench.common.widgets.client.popups.validation.ValidationMessageTranslator;
import org.optaplanner.workbench.screens.domaineditor.client.resources.i18n.DomainEditorConstants;
import org.optaplanner.workbench.screens.domaineditor.validation.PlanningSolutionToBeDuplicatedMessage;
import org.optaplanner.workbench.screens.domaineditor.validation.ScoreHolderGlobalFileToBeRemovedMessage;
import org.optaplanner.workbench.screens.domaineditor.validation.ScoreHolderGlobalToBeDefinedManuallyMessage;
import org.optaplanner.workbench.screens.domaineditor.validation.ScoreHolderGlobalToBeRemovedMessage;
import org.optaplanner.workbench.screens.domaineditor.validation.ScoreHolderGlobalTypeNotRecognizedMessage;
import org.optaplanner.workbench.screens.domaineditor.validation.ScoreHolderGlobalTypeToBeChangedMessage;

@ApplicationScoped
public class PlannerValidationMessageTranslator implements ValidationMessageTranslator {

    private TranslationService translationService;

    private static Map<String, String> MESSAGE_CLASS_KEY_MAPPING;

    static {
        MESSAGE_CLASS_KEY_MAPPING = new HashMap<>();
        MESSAGE_CLASS_KEY_MAPPING.put( PlanningSolutionToBeDuplicatedMessage.class.getName(),
                                       DomainEditorConstants.PlannerCheckTranslatorMultiplePlanningSolutionsToBeCreated );
        MESSAGE_CLASS_KEY_MAPPING.put( ScoreHolderGlobalToBeRemovedMessage.class.getName(),
                                       DomainEditorConstants.PlannerCheckTranslatorScoreHolderGlobalToBeDeleted );
        MESSAGE_CLASS_KEY_MAPPING.put( ScoreHolderGlobalTypeToBeChangedMessage.class.getName(),
                                       DomainEditorConstants.PlannerCheckTranslatorScoreHolderGlobalToBeChanged );
        MESSAGE_CLASS_KEY_MAPPING.put( ScoreHolderGlobalTypeNotRecognizedMessage.class.getName(),
                                       DomainEditorConstants.PlannerCheckTranslatorScoreHolderGlobalTypeNotRecognized );
        MESSAGE_CLASS_KEY_MAPPING.put( ScoreHolderGlobalFileToBeRemovedMessage.class.getName(),
                                       DomainEditorConstants.PlannerCheckTranslatorScoreHolderGlobalFileToBeRemovedMessage );
        MESSAGE_CLASS_KEY_MAPPING.put( ScoreHolderGlobalToBeDefinedManuallyMessage.class.getName(),
                                       DomainEditorConstants.PlannerCheckTranslatorScoreHolderGlobalToBeDefinedManuallyMessage );
    }

    @Inject
    public PlannerValidationMessageTranslator( final TranslationService translationService ) {
        this.translationService = translationService;
    }

    @Override
    public boolean accept( final ValidationMessage message ) {
        return MESSAGE_CLASS_KEY_MAPPING.containsKey( message.getClass().getName() );
    }

    @Override
    public ValidationMessage translate( final ValidationMessage messageToTranslate ) {
        String translationKey = MESSAGE_CLASS_KEY_MAPPING.get( messageToTranslate.getClass().getName() );

        if ( translationKey == null ) {
            throw new IllegalStateException( "No translation found for message " + messageToTranslate );
        }

        return getMessageTranslation( messageToTranslate,
                                      translationKey );
    }

    private ValidationMessage getMessageTranslation( final ValidationMessage messageToTranslate,
                                                     final String translationKey ) {
        ValidationMessage translatedMessage = new ValidationMessage( messageToTranslate );
        translatedMessage.setText( translationService.getTranslation( translationKey ) );
        return translatedMessage;
    }
}
