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

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwtmockito.GwtMockitoTestRunner;
import org.jboss.errai.ui.client.local.spi.TranslationService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.optaplanner.core.api.score.buildin.bendable.BendableScoreHolder;
import org.optaplanner.core.api.score.buildin.bendablebigdecimal.BendableBigDecimalScoreHolder;
import org.optaplanner.core.api.score.buildin.bendablelong.BendableLongScoreHolder;
import org.optaplanner.core.api.score.buildin.hardmediumsoft.HardMediumSoftScoreHolder;
import org.optaplanner.core.api.score.buildin.hardmediumsoftbigdecimal.HardMediumSoftBigDecimalScoreHolder;
import org.optaplanner.core.api.score.buildin.hardmediumsoftlong.HardMediumSoftLongScoreHolder;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScoreHolder;
import org.optaplanner.core.api.score.buildin.hardsoftbigdecimal.HardSoftBigDecimalScoreHolder;
import org.optaplanner.core.api.score.buildin.hardsoftdouble.HardSoftDoubleScoreHolder;
import org.optaplanner.core.api.score.buildin.hardsoftlong.HardSoftLongScoreHolder;
import org.optaplanner.core.api.score.buildin.simple.SimpleScoreHolder;
import org.optaplanner.core.api.score.buildin.simplebigdecimal.SimpleBigDecimalScoreHolder;
import org.optaplanner.core.api.score.buildin.simpledouble.SimpleDoubleScoreHolder;
import org.optaplanner.core.api.score.buildin.simplelong.SimpleLongScoreHolder;
import org.optaplanner.workbench.screens.guidedrule.client.resources.i18n.GuidedRuleEditorConstants;

import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(GwtMockitoTestRunner.class)
public class ConstraintMatchInputWidgetBlurHandlerTest {

    @Mock
    private ConstraintMatchInputWidget widget;

    @Mock
    private TranslationService translationService;

    private ConstraintMatchInputWidgetBlurHandler handler;

    @Before
    public void setUp() throws Exception {
        handler = new ConstraintMatchInputWidgetBlurHandler(widget,
                                                            translationService,
                                                            HardSoftScoreHolder.class.getName());
    }

    @Test
    public void nullConstraintMatch() throws Exception {
        when(widget.getConstraintMatchValue()).thenReturn(null);
        when(translationService.getTranslation(GuidedRuleEditorConstants.ConstraintMatchInputWidgetBlurHandler_EmptyValuesAreNotAllowedForModifyScore))
                .thenReturn("translation");

        handler.onBlur(mock(BlurEvent.class));

        verify(widget).showError("translation");
        verify(widget,
               never()).clearError();
    }

    @Test
    public void emptyConstraintMatch() throws Exception {
        when(widget.getConstraintMatchValue()).thenReturn("");
        when(translationService.getTranslation(GuidedRuleEditorConstants.ConstraintMatchInputWidgetBlurHandler_EmptyValuesAreNotAllowedForModifyScore))
                .thenReturn("translation");

        handler.onBlur(mock(BlurEvent.class));

        verify(widget).showError("translation");
        verify(widget,
               never()).clearError();
    }

    @Test
    public void whiteSpaceConstraintMatch() throws Exception {
        when(widget.getConstraintMatchValue()).thenReturn(" ");
        when(translationService.getTranslation(GuidedRuleEditorConstants.ConstraintMatchInputWidgetBlurHandler_EmptyValuesAreNotAllowedForModifyScore))
                .thenReturn("translation");

        handler.onBlur(mock(BlurEvent.class));

        verify(widget).showError("translation");
        verify(widget,
               never()).clearError();
    }

    @Test
    public void numberConstraintMatch() throws Exception {
        when(widget.getConstraintMatchValue()).thenReturn("123");

        handler.onBlur(mock(BlurEvent.class));

        verify(widget,
               never()).showError(anyString());
        verify(widget).clearError();
    }

    @Test
    public void validNumericValueConstraintMatchBendableScoreHolder() throws Exception {
        testValidNumericValueConstraintMatch(BendableScoreHolder.class.getName(),
                                             "-1");
    }

    @Test
    public void validNumericValueConstraintMatchBendableScoreHolderWhiteSpace() throws Exception {
        testValidNumericValueConstraintMatch(BendableScoreHolder.class.getName(),
                                             " 1 ");
    }

    @Test
    public void validNumericValueConstraintMatchSimpleScoreHolder() throws Exception {
        testValidNumericValueConstraintMatch(SimpleScoreHolder.class.getName(),
                                             "-1");
    }

    @Test
    public void validNumericValueConstraintMatchSimpleScoreHolderWhiteSpace() throws Exception {
        testValidNumericValueConstraintMatch(SimpleScoreHolder.class.getName(),
                                             " 1 ");
    }

    @Test
    public void validNumericValueConstraintMatchHardSoftScoreHolder() throws Exception {
        testValidNumericValueConstraintMatch(HardSoftScoreHolder.class.getName(),
                                             "-1");
    }

    @Test
    public void validNumericValueConstraintMatchHardSoftScoreHolderWhiteSpace() throws Exception {
        testValidNumericValueConstraintMatch(HardSoftScoreHolder.class.getName(),
                                             " 1 ");
    }

    @Test
    public void validNumericValueConstraintMatchHardMediumSoftScoreHolder() throws Exception {
        testValidNumericValueConstraintMatch(HardMediumSoftScoreHolder.class.getName(),
                                             "-1");
    }

    @Test
    public void validNumericValueConstraintMatchHardMediumSoftScoreHolderWhiteSpace() throws Exception {
        testValidNumericValueConstraintMatch(HardMediumSoftScoreHolder.class.getName(),
                                             " 1 ");
    }

    @Test
    public void validNumericValueConstraintMatchBendableLongScoreHolder() throws Exception {
        testValidNumericValueConstraintMatch(BendableLongScoreHolder.class.getName(),
                                             "-9999999999");
    }

    @Test
    public void validNumericValueConstraintMatchBendableLongScoreHolderWhiteSpace() throws Exception {
        testValidNumericValueConstraintMatch(BendableLongScoreHolder.class.getName(),
                                             " 9999999999 ");
    }

    @Test
    public void validNumericValueConstraintMatchSimpleLongScoreHolder() throws Exception {
        testValidNumericValueConstraintMatch(SimpleLongScoreHolder.class.getName(),
                                             "-9999999999");
    }

    @Test
    public void validNumericValueConstraintMatchSimpleLongScoreHolderWhiteSpace() throws Exception {
        testValidNumericValueConstraintMatch(SimpleLongScoreHolder.class.getName(),
                                             " 9999999999 ");
    }

    @Test
    public void validNumericValueConstraintMatchHardSoftLongScoreHolder() throws Exception {
        testValidNumericValueConstraintMatch(HardSoftLongScoreHolder.class.getName(),
                                             "-9999999999");
    }

    @Test
    public void validNumericValueConstraintMatchHardSoftLongScoreHolderWhiteSpace() throws Exception {
        testValidNumericValueConstraintMatch(HardSoftLongScoreHolder.class.getName(),
                                             " 9999999999 ");
    }

    @Test
    public void validNumericValueConstraintMatchHardMediumSoftLongScoreHolder() throws Exception {
        testValidNumericValueConstraintMatch(HardMediumSoftLongScoreHolder.class.getName(),
                                             "-9999999999");
    }

    @Test
    public void validNumericValueConstraintMatchHardMediumSoftLongScoreHolderWhiteSpace() throws Exception {
        testValidNumericValueConstraintMatch(HardMediumSoftLongScoreHolder.class.getName(),
                                             " 9999999999 ");
    }

    @Test
    public void validNumericValueConstraintMatchSimpleDoubleScoreHolder() throws Exception {
        testValidNumericValueConstraintMatch(SimpleDoubleScoreHolder.class.getName(),
                                             "-3.14");
    }

    @Test
    public void validNumericValueConstraintMatchSimpleDoubleScoreHolderWhiteSpace() throws Exception {
        testValidNumericValueConstraintMatch(SimpleDoubleScoreHolder.class.getName(),
                                             " 3.14 ");
    }

    @Test
    public void validNumericValueConstraintMatchHardSoftDoubleScoreHolder() throws Exception {
        testValidNumericValueConstraintMatch(HardSoftDoubleScoreHolder.class.getName(),
                                             "-3.14");
    }

    @Test
    public void validNumericValueConstraintMatchHardSoftDoubleScoreHolderWhiteSpace() throws Exception {
        testValidNumericValueConstraintMatch(HardSoftDoubleScoreHolder.class.getName(),
                                             " 3.14 ");
    }

    @Test
    public void validNumericValueConstraintMatchBendableBigDecimalScoreHolder() throws Exception {
        testValidNumericValueConstraintMatch(BendableBigDecimalScoreHolder.class.getName(),
                                             "-5.599");
    }

    @Test
    public void validNumericValueConstraintMatchBendableBigDecimalScoreHolderWhiteSpace() throws Exception {
        testValidNumericValueConstraintMatch(BendableBigDecimalScoreHolder.class.getName(),
                                             " 5.599 ");
    }

    @Test
    public void validNumericValueConstraintMatchSimpleBigDecimalScoreHolder() throws Exception {
        testValidNumericValueConstraintMatch(SimpleBigDecimalScoreHolder.class.getName(),
                                             "-5.599");
    }

    @Test
    public void validNumericValueConstraintMatchSimpleBigDecimalScoreHolderWhiteSpace() throws Exception {
        testValidNumericValueConstraintMatch(SimpleBigDecimalScoreHolder.class.getName(),
                                             " 5.599 ");
    }

    @Test
    public void validNumericValueConstraintMatchHardSoftBigDecimalScoreHolder() throws Exception {
        testValidNumericValueConstraintMatch(HardSoftBigDecimalScoreHolder.class.getName(),
                                             "-5.599");
    }

    @Test
    public void validNumericValueConstraintMatchHardSoftBigDecimalScoreHolderWhiteSpace() throws Exception {
        testValidNumericValueConstraintMatch(HardSoftBigDecimalScoreHolder.class.getName(),
                                             " 5.599 ");
    }

    @Test
    public void validNumericValueConstraintMatchHardMediumSoftBigDecimalScoreHolder() throws Exception {
        testValidNumericValueConstraintMatch(HardMediumSoftBigDecimalScoreHolder.class.getName(),
                                             "-5.599");
    }

    @Test
    public void validNumericValueConstraintMatchHardMediumSoftBigDecimalScoreHolderWhiteSpace() throws Exception {
        testValidNumericValueConstraintMatch(HardMediumSoftBigDecimalScoreHolder.class.getName(),
                                             " 5.599 ");
    }

    private void testValidNumericValueConstraintMatch(final String scoreHolderClass,
                                                      final String constraintMatchValue) throws Exception {
        ConstraintMatchInputWidgetBlurHandler handler = new ConstraintMatchInputWidgetBlurHandler(widget,
                                                                                                  translationService,
                                                                                                  scoreHolderClass);

        when(widget.getConstraintMatchValue()).thenReturn(constraintMatchValue);
        when(translationService.getTranslation(GuidedRuleEditorConstants.ConstraintMatchInputWidgetBlurHandler_IntegerValueParsingError))
                .thenReturn("translation");

        handler.onBlur(mock(BlurEvent.class));

        verify(widget,
               never()).showError("translation");
        verify(widget,
               times(1)).clearError();
    }

    @Test
    public void invalidValueConstraintMatchBendableScoreHolder() throws Exception {
        testInvalidValueConstraintMatch(BendableScoreHolder.class.getName());
    }

    @Test
    public void invalidValueConstraintMatchSimpleScoreHolder() throws Exception {
        testInvalidValueConstraintMatch(SimpleScoreHolder.class.getName());
    }

    @Test
    public void invalidValueConstraintMatchHardSoftScoreHolder() throws Exception {
        testInvalidValueConstraintMatch(HardSoftScoreHolder.class.getName());
    }

    @Test
    public void invalidValueConstraintMatchHardMediumSoftScoreHolder() throws Exception {
        testInvalidValueConstraintMatch(HardMediumSoftScoreHolder.class.getName());
    }

    @Test
    public void invalidValueConstraintMatchBendableLongScoreHolder() throws Exception {
        testInvalidValueConstraintMatch(BendableLongScoreHolder.class.getName());
    }

    @Test
    public void invalidValueConstraintMatchSimpleLongScoreHolder() throws Exception {
        testInvalidValueConstraintMatch(SimpleLongScoreHolder.class.getName());
    }

    @Test
    public void invalidValueConstraintMatchHardSoftLongScoreHolder() throws Exception {
        testInvalidValueConstraintMatch(HardSoftLongScoreHolder.class.getName());
    }

    @Test
    public void invalidValueConstraintMatchHardMediumSoftLongScoreHolder() throws Exception {
        testInvalidValueConstraintMatch(HardMediumSoftLongScoreHolder.class.getName());
    }

    @Test
    public void invalidValueConstraintMatchSimpleDoubleScoreHolder() throws Exception {
        testInvalidValueConstraintMatch(SimpleDoubleScoreHolder.class.getName());
    }

    @Test
    public void invalidValueConstraintMatchHardSoftDoubleScoreHolder() throws Exception {
        testInvalidValueConstraintMatch(HardSoftDoubleScoreHolder.class.getName());
    }

    @Test
    public void invalidValueConstraintMatchBendableBigDecimalScoreHolder() throws Exception {
        testInvalidValueConstraintMatch(BendableBigDecimalScoreHolder.class.getName());
    }

    @Test
    public void invalidValueConstraintMatchSimpleBigDecimalScoreHolder() throws Exception {
        testInvalidValueConstraintMatch(SimpleBigDecimalScoreHolder.class.getName());
    }

    @Test
    public void invalidValueConstraintMatchHardSoftBigDecimalScoreHolder() throws Exception {
        testInvalidValueConstraintMatch(HardSoftBigDecimalScoreHolder.class.getName());
    }

    @Test
    public void invalidValueConstraintMatchHardMediumSoftBigDecimalScoreHolder() throws Exception {
        testInvalidValueConstraintMatch(HardMediumSoftBigDecimalScoreHolder.class.getName());
    }

    private void testInvalidValueConstraintMatch(final String scoreHolderClass) {
        ConstraintMatchInputWidgetBlurHandler handler = new ConstraintMatchInputWidgetBlurHandler(widget,
                                                                                                  translationService,
                                                                                                  scoreHolderClass);
        when(widget.getConstraintMatchValue()).thenReturn("123zzz");
        when(translationService.getTranslation(anyString()))
                .thenReturn("translation");

        handler.onBlur(mock(BlurEvent.class));

        verify(widget,
               times(1)).showError("translation");
        verify(widget,
               never()).clearError();
    }

    @Test
    public void validExpressionConstraintMatchBendableScoreHolder() throws Exception {
        testValidExpressionConstraintMatch(BendableScoreHolder.class.getName());
    }

    @Test
    public void validExpressionConstraintMatchSimpleScoreHolder() throws Exception {
        testValidExpressionConstraintMatch(SimpleScoreHolder.class.getName());
    }

    @Test
    public void validExpressionConstraintMatchHardSoftScoreHolder() throws Exception {
        testValidExpressionConstraintMatch(HardSoftScoreHolder.class.getName());
    }

    @Test
    public void validExpressionConstraintMatchHardMediumSoftScoreHolder() throws Exception {
        testValidExpressionConstraintMatch(HardMediumSoftScoreHolder.class.getName());
    }

    @Test
    public void validExpressionConstraintMatchBendableLongScoreHolder() throws Exception {
        testValidExpressionConstraintMatch(BendableLongScoreHolder.class.getName());
    }

    @Test
    public void validExpressionConstraintMatchSimpleLongScoreHolder() throws Exception {
        testValidExpressionConstraintMatch(SimpleLongScoreHolder.class.getName());
    }

    @Test
    public void validExpressionConstraintMatchHardSoftLongScoreHolder() throws Exception {
        testValidExpressionConstraintMatch(HardSoftLongScoreHolder.class.getName());
    }

    @Test
    public void validExpressionConstraintMatchHardMediumSoftLongScoreHolder() throws Exception {
        testValidExpressionConstraintMatch(HardMediumSoftLongScoreHolder.class.getName());
    }

    @Test
    public void validExpressionConstraintMatchSimpleDoubleScoreHolder() throws Exception {
        testValidExpressionConstraintMatch(SimpleDoubleScoreHolder.class.getName());
    }

    @Test
    public void validExpressionConstraintMatchHardSoftDoubleScoreHolder() throws Exception {
        testValidExpressionConstraintMatch(HardSoftDoubleScoreHolder.class.getName());
    }

    @Test
    public void validExpressionConstraintMatchBendableBigDecimalScoreHolder() throws Exception {
        testValidExpressionConstraintMatch(BendableBigDecimalScoreHolder.class.getName());
    }

    @Test
    public void validExpressionConstraintMatchSimpleBigDecimalScoreHolder() throws Exception {
        testValidExpressionConstraintMatch(SimpleBigDecimalScoreHolder.class.getName());
    }

    @Test
    public void validExpressionConstraintMatchHardSoftBigDecimalScoreHolder() throws Exception {
        testValidExpressionConstraintMatch(HardSoftBigDecimalScoreHolder.class.getName());
    }

    @Test
    public void validExpressionConstraintMatchHardMediumSoftBigDecimalScoreHolder() throws Exception {
        testValidExpressionConstraintMatch(HardMediumSoftBigDecimalScoreHolder.class.getName());
    }

    private void testValidExpressionConstraintMatch(final String scoreHolderClass) throws Exception {
        ConstraintMatchInputWidgetBlurHandler handler = new ConstraintMatchInputWidgetBlurHandler(widget,
                                                                                                  translationService,
                                                                                                  scoreHolderClass);
        when(widget.getConstraintMatchValue()).thenReturn("$person.getAge()");

        handler.onBlur(mock(BlurEvent.class));

        verify(widget,
               never()).showError(anyString());
        verify(widget).clearError();
    }
}
