package org.optaplanner.workbench.screens.guidedrule.client.validation;

import java.util.Collection;

import org.guvnor.common.services.shared.validation.model.ValidationMessage;
import org.jboss.errai.ui.client.local.spi.TranslationService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.optaplanner.workbench.screens.guidedrule.client.resources.i18n.GuidedRuleEditorConstants;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ScoreHolderPatternBindingValidatorTest {

    @Mock
    private TranslationService translationService;

    private ScoreHolderPatternBindingValidator validator;

    @Before
    public void setUp() {
        this.validator = new ScoreHolderPatternBindingValidator(translationService);
    }

    @Test
    public void validateVariableNameMatches() {
        when(translationService.getTranslation(GuidedRuleEditorConstants.ScoreHolderPatternBindingValidator_ScoreHolderVariableReserved))
                .thenReturn("translation");
        Collection<ValidationMessage> validationMessages = validator.validate("scoreHolder");
        assertEquals(1, validationMessages.size());
    }

    @Test
    public void validateVariableNameDoesntMatch() {
        when(translationService.getTranslation(GuidedRuleEditorConstants.ScoreHolderPatternBindingValidator_ScoreHolderVariableReserved))
                .thenReturn("translation");
        Collection<ValidationMessage> validationMessages = validator.validate("differentHolder");
        assertTrue(validationMessages.isEmpty());
    }
}
