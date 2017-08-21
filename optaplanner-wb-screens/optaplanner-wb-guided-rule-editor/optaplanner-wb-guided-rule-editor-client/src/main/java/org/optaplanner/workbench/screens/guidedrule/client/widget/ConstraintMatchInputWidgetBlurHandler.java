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

package org.optaplanner.workbench.screens.guidedrule.client.widget;

import java.math.BigDecimal;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import org.jboss.errai.ui.client.local.spi.TranslationService;

import static org.optaplanner.workbench.screens.guidedrule.client.resources.i18n.GuidedRuleEditorConstants.ConstraintMatchInputWidgetBlurHandler_BigDecimalValueParsingError;
import static org.optaplanner.workbench.screens.guidedrule.client.resources.i18n.GuidedRuleEditorConstants.ConstraintMatchInputWidgetBlurHandler_DoubleValueParsingError;
import static org.optaplanner.workbench.screens.guidedrule.client.resources.i18n.GuidedRuleEditorConstants.ConstraintMatchInputWidgetBlurHandler_EmptyValuesAreNotAllowedForModifyScore;
import static org.optaplanner.workbench.screens.guidedrule.client.resources.i18n.GuidedRuleEditorConstants.ConstraintMatchInputWidgetBlurHandler_IntegerValueParsingError;
import static org.optaplanner.workbench.screens.guidedrule.client.resources.i18n.GuidedRuleEditorConstants.ConstraintMatchInputWidgetBlurHandler_LongValueParsingError;

public class ConstraintMatchInputWidgetBlurHandler implements BlurHandler {

    private ConstraintMatchInputWidget widget;
    private TranslationService translationService;
    private String scoreHolderType;

    public ConstraintMatchInputWidgetBlurHandler(final ConstraintMatchInputWidget widget,
                                                 final TranslationService translationService,
                                                 final String scoreHolderType) {
        this.widget = widget;
        this.translationService = translationService;
        this.scoreHolderType = scoreHolderType;
    }

    @Override
    public void onBlur(BlurEvent event) {
        String inputValue = widget.getConstraintMatchValue();
        if (inputValue == null || inputValue.trim().isEmpty()) {
            widget.showError(translationService.getTranslation(ConstraintMatchInputWidgetBlurHandler_EmptyValuesAreNotAllowedForModifyScore));
        } else {
            inputValue = inputValue.trim();
            if (inputValue.matches("-?\\s*\\d+(\\.\\d+)?.*")) {
                switch (scoreHolderType) {
                    // int
                    case "org.optaplanner.core.api.score.buildin.bendable.BendableScoreHolder":
                    case "org.optaplanner.core.api.score.buildin.simple.SimpleScoreHolder":
                    case "org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScoreHolder":
                    case "org.optaplanner.core.api.score.buildin.hardmediumsoft.HardMediumSoftScoreHolder": {
                        try {
                            Integer.parseInt(inputValue);
                        } catch (NumberFormatException e) {
                            widget.showError(translationService.getTranslation(ConstraintMatchInputWidgetBlurHandler_IntegerValueParsingError));
                            return;
                        }
                        break;
                    }
                    // long
                    case "org.optaplanner.core.api.score.buildin.bendablelong.BendableLongScoreHolder":
                    case "org.optaplanner.core.api.score.buildin.simplelong.SimpleLongScoreHolder":
                    case "org.optaplanner.core.api.score.buildin.hardsoftlong.HardSoftLongScoreHolder":
                    case "org.optaplanner.core.api.score.buildin.hardmediumsoftlong.HardMediumSoftLongScoreHolder": {
                        try {
                            Long.parseLong(inputValue);
                        } catch (NumberFormatException e) {
                            widget.showError(translationService.getTranslation(ConstraintMatchInputWidgetBlurHandler_LongValueParsingError));
                            return;
                        }
                        break;
                    }
                    // double
                    case "org.optaplanner.core.api.score.buildin.simpledouble.SimpleDoubleScoreHolder":
                    case "org.optaplanner.core.api.score.buildin.hardsoftdouble.HardSoftDoubleScoreHolder": {
                        try {
                            Double.parseDouble(inputValue);
                        } catch (NumberFormatException e) {
                            widget.showError(translationService.getTranslation(ConstraintMatchInputWidgetBlurHandler_DoubleValueParsingError));
                            return;
                        }
                        break;
                    }
                    // java.math.BigDecimal
                    case "org.optaplanner.core.api.score.buildin.bendablebigdecimal.BendableBigDecimalScoreHolder":
                    case "org.optaplanner.core.api.score.buildin.simplebigdecimal.SimpleBigDecimalScoreHolder":
                    case "org.optaplanner.core.api.score.buildin.hardsoftbigdecimal.HardSoftBigDecimalScoreHolder":
                    case "org.optaplanner.core.api.score.buildin.hardmediumsoftbigdecimal.HardMediumSoftBigDecimalScoreHolder": {
                        try {
                            new BigDecimal(inputValue);
                        } catch (NumberFormatException e) {
                            widget.showError(translationService.getTranslation(ConstraintMatchInputWidgetBlurHandler_BigDecimalValueParsingError));
                            return;
                        }
                        break;
                    }

                }
            }
            widget.clearError();
        }
    }
}
