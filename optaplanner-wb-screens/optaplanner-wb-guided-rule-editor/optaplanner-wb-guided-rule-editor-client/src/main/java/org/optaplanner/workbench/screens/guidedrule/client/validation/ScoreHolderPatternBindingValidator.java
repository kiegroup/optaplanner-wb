package org.optaplanner.workbench.screens.guidedrule.client.validation;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import javax.inject.Inject;

import org.drools.workbench.screens.guided.rule.client.editor.validator.PatternBindingValidator;
import org.guvnor.common.services.shared.message.Level;
import org.guvnor.common.services.shared.validation.model.ValidationMessage;
import org.jboss.errai.ui.client.local.spi.TranslationService;
import org.optaplanner.workbench.screens.guidedrule.client.resources.i18n.GuidedRuleEditorConstants;

/**
 * Check whether a variable name equals to 'scoreHolder', which is a reserved name in the GRE.
 */
public class ScoreHolderPatternBindingValidator implements PatternBindingValidator {

    private static final String SCORE_HOLDER = "scoreHolder";

    private TranslationService translationService;

    @Inject
    public ScoreHolderPatternBindingValidator(final TranslationService translationService) {
        this.translationService = translationService;
    }

    @Override
    public Collection<ValidationMessage> validate(String variableName) {
        if (SCORE_HOLDER.equals(variableName)) {
            return Arrays.asList(new ValidationMessage(Level.WARNING, translationService.getTranslation(GuidedRuleEditorConstants.ScoreHolderPatternBindingValidator_ScoreHolderVariableReserved)));
        }
        return Collections.emptyList();
    }
}
