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

import org.guvnor.common.services.shared.message.Level;
import org.guvnor.common.services.shared.validation.model.ValidationMessage;
import org.jboss.errai.ui.client.local.spi.TranslationService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.optaplanner.workbench.screens.domaineditor.validation.PlanningSolutionToBeDuplicatedMessage;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class PlannerValidationMessageTranslatorTest {

    @Mock
    private TranslationService translationService;

    private PlannerValidationMessageTranslator translator;

    @Before
    public void setUp() {
        translator = new PlannerValidationMessageTranslator( translationService );
    }

    @Test
    public void acceptSupportedClass() {
        assertTrue( translator.accept( new PlanningSolutionToBeDuplicatedMessage( Level.ERROR ) ) );
    }

    @Test
    public void acceptUnsupportedClass() {
        assertFalse( translator.accept( new UnsupportedMessage() ) );
    }

    @Test
    public void translateSupported() {
        when( translationService.getTranslation( anyString() ) ).thenReturn( "Test translation" );

        ValidationMessage translatedMessage = translator.translate( new PlanningSolutionToBeDuplicatedMessage( Level.ERROR ) );
        assertEquals( "Test translation", translatedMessage.getText() );
    }

    @Test(expected = IllegalStateException.class)
    public void translateUnsupported() {
        assertNotNull( translator.translate( new UnsupportedMessage() ) );
    }

    private class UnsupportedMessage extends ValidationMessage {
    }
}
