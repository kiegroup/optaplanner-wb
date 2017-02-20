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

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.guvnor.common.services.shared.validation.model.ValidationMessage;
import org.jboss.errai.ui.client.local.spi.TranslationService;
import org.kie.workbench.common.widgets.client.popups.validation.ValidationMessageTranslator;
import org.optaplanner.workbench.screens.domaineditor.client.resources.i18n.DomainEditorConstants;
import org.optaplanner.workbench.screens.domaineditor.validation.PlanningSolutionToBeDuplicatedMessage;
import org.optaplanner.workbench.screens.domaineditor.validation.ScoreHolderGlobalToBeRemovedMessage;
import org.optaplanner.workbench.screens.domaineditor.validation.ScoreHolderGlobalTypeNotRecognizedMessage;
import org.optaplanner.workbench.screens.domaineditor.validation.ScoreHolderGlobalTypeToBeChangedMessage;

@ApplicationScoped
public class PlannerValidationMessageTranslator implements ValidationMessageTranslator {

    private TranslationService translationService;

    @Inject
    public PlannerValidationMessageTranslator( final TranslationService translationService ) {
        this.translationService = translationService;
    }

    @Override
    public boolean accept( final ValidationMessage message ) {
        return message instanceof PlanningSolutionToBeDuplicatedMessage
                || message instanceof ScoreHolderGlobalToBeRemovedMessage
                || message instanceof ScoreHolderGlobalTypeToBeChangedMessage
                || message instanceof ScoreHolderGlobalTypeNotRecognizedMessage;
    }

    @Override
    public ValidationMessage translate( final ValidationMessage messageToTranslate ) {
        if ( messageToTranslate instanceof PlanningSolutionToBeDuplicatedMessage ) {
            return getMessageTranslation( messageToTranslate,
                                          DomainEditorConstants.PlannerCheckTranslatorMultiplePlanningSolutionsToBeCreated );
        } else if ( messageToTranslate instanceof ScoreHolderGlobalToBeRemovedMessage ) {
            return getMessageTranslation( messageToTranslate,
                                          DomainEditorConstants.PlannerCheckTranslatorScoreHolderGlobalToBeDeleted );
        } else if ( messageToTranslate instanceof ScoreHolderGlobalTypeToBeChangedMessage ) {
            return getMessageTranslation( messageToTranslate,
                                          DomainEditorConstants.PlannerCheckTranslatorScoreHolderGlobalToBeChanged );
        } else if ( messageToTranslate instanceof ScoreHolderGlobalTypeNotRecognizedMessage ) {
            return getMessageTranslation( messageToTranslate,
                                          DomainEditorConstants.PlannerCheckTranslatorScoreHolderGlobalTypeNotRecognized );
        }
        throw new IllegalStateException( "No translation found for message " + messageToTranslate );
    }

    private ValidationMessage getMessageTranslation( final ValidationMessage messageToTranslate,
                                                     final String translationKey ) {
        ValidationMessage translatedMessage = new ValidationMessage( messageToTranslate );
        translatedMessage.setText( translationService.getTranslation( translationKey ) );
        return translatedMessage;
    }
}
