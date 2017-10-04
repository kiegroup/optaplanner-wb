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

package org.optaplanner.workbench.screens.guidedrule.client.resources.i18n;

import org.jboss.errai.ui.shared.api.annotations.TranslationKey;

public interface GuidedRuleEditorConstants {

    @TranslationKey(defaultValue = "")
    String RuleModellerActionPlugin_ModifyHardScore = "RuleModellerActionPlugin.ModifyHardScore";

    @TranslationKey(defaultValue = "")
    String RuleModellerActionPlugin_ModifyMediumScore = "RuleModellerActionPlugin.ModifyMediumScore";

    @TranslationKey(defaultValue = "")
    String RuleModellerActionPlugin_ModifyMultipleScoreLevels = "RuleModellerActionPlugin.ModifyMultipleScoreLevels";

    @TranslationKey(defaultValue = "")
    String RuleModellerActionPlugin_ModifySimpleScore = "RuleModellerActionPlugin.ModifySimpleScore";

    @TranslationKey(defaultValue = "")
    String RuleModellerActionPlugin_ModifySoftScore = "RuleModellerActionPlugin.ModifySoftScore";

    @TranslationKey(defaultValue = "")
    String RuleModellerActionPlugin_HardScore = "RuleModellerActionPlugin.HardScore";

    @TranslationKey(defaultValue = "")
    String RuleModellerActionPlugin_MediumScore = "RuleModellerActionPlugin.MediumScore";

    @TranslationKey(defaultValue = "")
    String RuleModellerActionPlugin_SimpleScore = "RuleModellerActionPlugin.SimpleScore";

    @TranslationKey(defaultValue = "")
    String RuleModellerActionPlugin_SoftScore = "RuleModellerActionPlugin.SoftScore";

    @TranslationKey(defaultValue = "")
    String RuleModellerActionPlugin_MultiConstraintMatch = "RuleModellerActionPlugin.MultiConstraintMatch";

    @TranslationKey(defaultValue = "")
    String RuleModellerActionPlugin_HardScoreLevelSizeIsZero = "RuleModellerActionPlugin.HardScoreLevelSizeIsZero";

    @TranslationKey(defaultValue = "")
    String RuleModellerActionPlugin_SoftScoreLevelSizeIsZero = "RuleModellerActionPlugin.SoftScoreLevelSizeIsZero";

    @TranslationKey(defaultValue = "")
    String RuleModellerActionPlugin_ScoreLevelExceeded = "RuleModellerActionPlugin.ScoreLevelExceeded";

    @TranslationKey(defaultValue = "")
    String RuleModellerActionPlugin_AmbigiousConstraintMatchesDetected = "RuleModellerActionPlugin.AmbigiousConstraintMatchesDetected";

    @TranslationKey(defaultValue = "")
    String ConstraintMatchInputWidgetBlurHandler_EmptyValuesAreNotAllowedForModifyScore = "ConstraintMatchInputWidgetBlurHandler.EmptyValuesAreNotAllowedForModifyScore";

    @TranslationKey(defaultValue = "")
    String ConstraintMatchInputWidgetBlurHandler_IntegerValueParsingError = "ConstraintMatchInputWidgetBlurHandler.IntegerValueParsingError";

    @TranslationKey(defaultValue = "")
    String ConstraintMatchInputWidgetBlurHandler_LongValueParsingError = "ConstraintMatchInputWidgetBlurHandler.LongValueParsingError";

    @TranslationKey(defaultValue = "")
    String ConstraintMatchInputWidgetBlurHandler_DoubleValueParsingError = "ConstraintMatchInputWidgetBlurHandler.DoubleValueParsingError";

    @TranslationKey(defaultValue = "")
    String ConstraintMatchInputWidgetBlurHandler_BigDecimalValueParsingError = "ConstraintMatchInputWidgetBlurHandler.BigDecimalValueParsingError";

    @TranslationKey(defaultValue = "")
    String ActionPluginClientService_ScoreHolderGlobalNotFound = "ActionPluginClientService.ScoreHolderGlobalNotFound";

    @TranslationKey(defaultValue = "")
    String ActionPluginClientService_MultipleScoreHolderGlobals = "ActionPluginClientService.MultipleScoreHolderGlobals";

    @TranslationKey(defaultValue = "")
    String ActionPluginClientService_ScoreTypeNotSupported = "ActionPluginClientService.ScoreTypeNotSupported";

    @TranslationKey(defaultValue = "")
    String ScoreHolderPatternBindingValidator_ScoreHolderVariableReserved = "ScoreHolderPatternBindingValidator.ScoreHolderVariableReserved";
}
