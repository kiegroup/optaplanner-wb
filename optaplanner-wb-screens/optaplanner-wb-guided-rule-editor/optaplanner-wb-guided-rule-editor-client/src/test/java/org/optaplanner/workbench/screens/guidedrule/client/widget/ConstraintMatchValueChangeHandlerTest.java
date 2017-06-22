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

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwtmockito.GwtMockitoTestRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.optaplanner.workbench.screens.guidedrule.model.AbstractActionConstraintMatch;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(GwtMockitoTestRunner.class)
public class ConstraintMatchValueChangeHandlerTest {

    ConstraintMatchValueChangeHandler handler;

    @Mock
    AbstractActionConstraintMatch actionConstraintMatch;

    @Before
    public void setUp() throws Exception {
        handler = new ConstraintMatchValueChangeHandler(actionConstraintMatch);
    }

    @Test
    public void testNewConstraintMatchValue() throws Exception {
        assertSetNewConstraintMatch("$person.getAge()");
    }

    @Test
    public void testNewNullConstraintMatchValue() throws Exception {
        assertSetNewConstraintMatch(null);
    }

    @Test
    public void testNewEmptyConstraintMatchValue() throws Exception {
        assertSetNewConstraintMatch("");
    }

    private void assertSetNewConstraintMatch(String newValue) {
        ValueChangeEvent<String> event = mock(ValueChangeEvent.class);
        when(event.getValue()).thenReturn(newValue);
        handler.onValueChange(event);

        verify(actionConstraintMatch).setConstraintMatch(newValue);
    }
}
