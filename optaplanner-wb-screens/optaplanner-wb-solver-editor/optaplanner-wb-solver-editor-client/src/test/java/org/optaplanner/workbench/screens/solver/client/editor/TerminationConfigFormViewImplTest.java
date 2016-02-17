/*
 * Copyright 2016 Red Hat, Inc. and/or its affiliates.
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
package org.optaplanner.workbench.screens.solver.client.editor;

import com.google.gwtmockito.GwtMockitoTestRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import static org.mockito.Mockito.*;

@RunWith( GwtMockitoTestRunner.class )
public class TerminationConfigFormViewImplTest {

    private TerminationConfigFormViewImpl terminationConfigFormView;

    @Mock
    private Spinner spentLimitDays;

    @Mock
    private Spinner spentLimitHours;

    @Mock
    private Spinner spentLimitMinutes;

    @Mock
    private Spinner spentLimitSeconds;

    @Mock
    private Spinner unimprovedSpendLimitDays;

    @Mock
    private Spinner unimprovedSpendLimitHours;

    @Mock
    private Spinner unimprovedSpendLimitMinutes;

    @Mock
    private Spinner unimprovedSpendLimitSeconds;

    @Before
    public void setUp() throws Exception {
        terminationConfigFormView = new TerminationConfigFormViewImpl();

        terminationConfigFormView.spentLimitDays = spentLimitDays;
        terminationConfigFormView.spentLimitHours = spentLimitHours;
        terminationConfigFormView.spentLimitMinutes = spentLimitMinutes;
        terminationConfigFormView.spentLimitSeconds = spentLimitSeconds;

        terminationConfigFormView.unimprovedSpendLimitDays = unimprovedSpendLimitDays;
        terminationConfigFormView.unimprovedSpendLimitHours = unimprovedSpendLimitHours;
        terminationConfigFormView.unimprovedSpendLimitMinutes = unimprovedSpendLimitMinutes;
        terminationConfigFormView.unimprovedSpendLimitSeconds = unimprovedSpendLimitSeconds;
    }

    @Test
    public void testSpentLimitTrue() throws Exception {
        terminationConfigFormView.showSpentLimit( true );

        verify( terminationConfigFormView.useSpentLimit ).setValue( true );
        verify( spentLimitDays ).setEnabled( true );
        verify( spentLimitHours ).setEnabled( true );
        verify( spentLimitMinutes ).setEnabled( true );
        verify( spentLimitSeconds ).setEnabled( true );
    }

    @Test
    public void testSpentLimitFalse() throws Exception {
        terminationConfigFormView.showSpentLimit( false );

        verify( terminationConfigFormView.useSpentLimit ).setValue( false );
        verify( spentLimitDays ).setEnabled( false );
        verify( spentLimitHours ).setEnabled( false );
        verify( spentLimitMinutes ).setEnabled( false );
        verify( spentLimitSeconds ).setEnabled( false );
    }

    @Test
    public void testUnimprovedSpentLimitTrue() throws Exception {
        terminationConfigFormView.showUnimprovedSpentLimit( true );

        verify( terminationConfigFormView.useUnimprovedSpentLimit ).setValue( true );
        verify( unimprovedSpendLimitDays ).setEnabled( true );
        verify( unimprovedSpendLimitHours ).setEnabled( true );
        verify( unimprovedSpendLimitMinutes ).setEnabled( true );
        verify( unimprovedSpendLimitSeconds ).setEnabled( true );
    }

    @Test
    public void testUnimprovedSpentLimitFalse() throws Exception {
        terminationConfigFormView.showUnimprovedSpentLimit( false );

        verify( terminationConfigFormView.useUnimprovedSpentLimit ).setValue( false );
        verify( unimprovedSpendLimitDays ).setEnabled( false );
        verify( unimprovedSpendLimitHours ).setEnabled( false );
        verify( unimprovedSpendLimitMinutes ).setEnabled( false );
        verify( unimprovedSpendLimitSeconds ).setEnabled( false );
    }

}