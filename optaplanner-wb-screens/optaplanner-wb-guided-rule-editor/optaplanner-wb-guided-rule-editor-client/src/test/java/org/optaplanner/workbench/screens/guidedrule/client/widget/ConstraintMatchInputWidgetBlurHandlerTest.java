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
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(GwtMockitoTestRunner.class)
public class ConstraintMatchInputWidgetBlurHandlerTest {

    ConstraintMatchInputWidgetBlurHandler handler;

    @Mock
    ConstraintMatchInputWidget widget;

    @Before
    public void setUp() throws Exception {
        handler = new ConstraintMatchInputWidgetBlurHandler(widget);

    }

    @Test
    public void testNullConstraintMatch() throws Exception {
        when(widget.getConstraintMatchValue()).thenReturn(null);

        handler.onBlur(mock(BlurEvent.class));

        verify(widget).showEmptyValuesNotAllowedError();
        verify(widget, never()).clearError();
    }

    @Test
    public void testEmptyConstraintMatch() throws Exception {
        when(widget.getConstraintMatchValue()).thenReturn("");

        handler.onBlur(mock(BlurEvent.class));

        verify(widget).showEmptyValuesNotAllowedError();
        verify(widget, never()).clearError();
    }

    @Test
    public void testJustWhiteSpaceConstraintMatch() throws Exception {
        when(widget.getConstraintMatchValue()).thenReturn(" ");

        handler.onBlur(mock(BlurEvent.class));

        verify(widget).showEmptyValuesNotAllowedError();
        verify(widget, never()).clearError();
    }

    @Test
    public void testNumberConstraintMatch() throws Exception {
        when(widget.getConstraintMatchValue()).thenReturn("123");

        handler.onBlur(mock(BlurEvent.class));

        verify(widget, never()).showEmptyValuesNotAllowedError();
        verify(widget).clearError();
    }

    @Test
    public void testExpressionConstraintMatch() throws Exception {
        when(widget.getConstraintMatchValue()).thenReturn("$person.getAge()");

        handler.onBlur(mock(BlurEvent.class));

        verify(widget, never()).showEmptyValuesNotAllowedError();
        verify(widget).clearError();
    }


}
