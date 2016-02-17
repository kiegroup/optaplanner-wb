/*
 * Copyright 2015 Red Hat, Inc. and/or its affiliates.
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
import org.optaplanner.workbench.screens.solver.model.TerminationConfigModel;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(GwtMockitoTestRunner.class)
public class TerminationConfigFormTest {

    @Mock
    private TerminationConfigFormView view;

    private TerminationConfigForm form;

    @Before
    public void setUp() throws Exception {
        form = new TerminationConfigForm( view );
    }

    @Test
    public void testSetPresenter() throws Exception {
        verify( view ).setPresenter( form );
    }

    @Test
    public void testSetEmptyModel() throws Exception {
        form.setModel( new TerminationConfigModel() );

        verify( view ).showSpentLimit( false );

        verify( view, never() ).setDaysSpentLimit( anyLong() );
        verify( view, never() ).setHoursSpentLimit( anyLong() );
        verify( view, never() ).setMinutesSpentLimit( anyLong() );
        verify( view, never() ).setSecondsSpentLimit( anyLong() );

        verify( view ).showUnimprovedSpentLimit( false );

        verify( view, never() ).setUnimprovedDaysSpentLimit( anyLong() );
        verify( view, never() ).setUnimprovedHoursSpentLimit( anyLong() );
        verify( view, never() ).setUnimprovedMinutesSpentLimit( anyLong() );
        verify( view, never() ).setUnimprovedSecondsSpentLimit( anyLong() );

    }

    @Test
    public void testSetSpentLimitModel() throws Exception {
        form.setModel( getSpentLimitModel() );

        verify( view ).showSpentLimit( true );

        verify( view ).setDaysSpentLimit( Long.valueOf( 14 ) );
        verify( view ).setHoursSpentLimit( Long.valueOf( 4 ) );
        verify( view ).setMinutesSpentLimit( Long.valueOf( 25 ) );
        verify( view ).setSecondsSpentLimit( Long.valueOf( 59 ) );

        verify( view ).showUnimprovedSpentLimit( false );

        verify( view, never() ).setUnimprovedDaysSpentLimit( anyLong() );
        verify( view, never() ).setUnimprovedHoursSpentLimit( anyLong() );
        verify( view, never() ).setUnimprovedMinutesSpentLimit( anyLong() );
        verify( view, never() ).setUnimprovedSecondsSpentLimit( anyLong() );
    }

    @Test
    public void testSetUnimprovedSpentLimitModel() throws Exception {
        form.setModel( getUnimprovedSpentLimitModel() );

        verify( view ).showSpentLimit( false );

        verify( view, never() ).setDaysSpentLimit( anyLong() );
        verify( view, never() ).setHoursSpentLimit( anyLong() );
        verify( view, never() ).setMinutesSpentLimit( anyLong() );
        verify( view, never() ).setSecondsSpentLimit( anyLong() );

        verify( view ).showUnimprovedSpentLimit( true );

        verify( view ).setUnimprovedDaysSpentLimit( Long.valueOf( 112 ) );
        verify( view ).setUnimprovedHoursSpentLimit( Long.valueOf( 12 ) );
        verify( view ).setUnimprovedMinutesSpentLimit( Long.valueOf( 10 ) );
        verify( view ).setUnimprovedSecondsSpentLimit( Long.valueOf( 44 ) );
    }

    @Test
    public void testEditModel() throws Exception {
        TerminationConfigModel model = getModel();
        form.setModel( model );

        form.onDaysSpentLimitChange( Long.valueOf( 11 ) );
        form.onHoursSpentLimitChange( Long.valueOf( 1 ) );
        form.onMinutesSpentLimitChange( Long.valueOf( 2 ) );
        form.onSecondsSpentLimitChange( Long.valueOf( 3 ) );

        form.onUnimprovedDaysSpentLimitChange( Long.valueOf( 13 ) );
        form.onUnimprovedHoursSpentLimit( Long.valueOf( 3 ) );
        form.onUnimprovedMinutesSpentLimit( Long.valueOf( 2 ) );
        form.onUnimprovedSecondsSpentLimit( Long.valueOf( 1 ) );

        assertEquals( Long.valueOf( 11 ), model.getDaysSpentLimit() );
        assertEquals( Long.valueOf( 1 ), model.getHoursSpentLimit() );
        assertEquals( Long.valueOf( 2 ), model.getMinutesSpentLimit() );
        assertEquals( Long.valueOf( 3 ), model.getSecondsSpentLimit() );

        assertEquals( Long.valueOf( 13 ), model.getUnimprovedDaysSpentLimit() );
        assertEquals( Long.valueOf( 3 ), model.getUnimprovedHoursSpentLimit() );
        assertEquals( Long.valueOf( 2 ), model.getUnimprovedMinutesSpentLimit() );
        assertEquals( Long.valueOf( 1 ), model.getUnimprovedSecondsSpentLimit() );

    }

    @Test
    public void testUseSpentLimit() throws Exception {
        TerminationConfigModel model = new TerminationConfigModel();

        form.setModel( model );
        form.onUseSpentLimitChange( true );

        assertEquals( Long.valueOf( 0 ), model.getDaysSpentLimit() );
        verify( view ).setDaysSpentLimit( Long.valueOf( 0 ) );
        assertEquals( Long.valueOf( 0 ), model.getHoursSpentLimit() );
        verify( view ).setHoursSpentLimit( Long.valueOf( 0 ) );
        assertEquals( Long.valueOf( 0 ), model.getMinutesSpentLimit() );
        verify( view ).setMinutesSpentLimit( Long.valueOf( 0 ) );
        assertEquals( Long.valueOf( 0 ), model.getSecondsSpentLimit() );
        verify( view ).setSecondsSpentLimit( Long.valueOf( 0 ) );

        verify( view ).showSpentLimit( true );

        reset( view );

        form.onUseSpentLimitChange( false );

        verify( view ).showSpentLimit( false );

        assertNull( model.getDaysSpentLimit() );
        assertNull( model.getHoursSpentLimit() );
        assertNull( model.getMinutesSpentLimit() );
        assertNull( model.getSecondsSpentLimit() );
    }

    @Test
    public void testUseUnimprovedSpentLimitChange() throws Exception {
        TerminationConfigModel model = new TerminationConfigModel();

        form.setModel( model );
        form.onUseUnimprovedSpentLimitChange( true );

        assertEquals( Long.valueOf( 0 ), model.getUnimprovedDaysSpentLimit() );
        verify( view ).setUnimprovedDaysSpentLimit( Long.valueOf( 0 ) );
        assertEquals( Long.valueOf( 0 ), model.getUnimprovedHoursSpentLimit() );
        verify( view ).setUnimprovedHoursSpentLimit( Long.valueOf( 0 ) );
        assertEquals( Long.valueOf( 0 ), model.getUnimprovedMinutesSpentLimit() );
        verify( view ).setUnimprovedMinutesSpentLimit( Long.valueOf( 0 ) );
        assertEquals( Long.valueOf( 0 ), model.getUnimprovedSecondsSpentLimit() );
        verify( view ).setUnimprovedSecondsSpentLimit( Long.valueOf( 0 ) );

        verify( view ).showUnimprovedSpentLimit( true );

        reset( view );

        form.onUseUnimprovedSpentLimitChange( false );

        verify( view ).showUnimprovedSpentLimit( false );

        assertNull( model.getUnimprovedHoursSpentLimit() );
        assertNull( model.getUnimprovedMinutesSpentLimit() );
        assertNull( model.getUnimprovedSecondsSpentLimit() );
    }

    @Test
    public void testEnableSpentLimit() throws Exception {
        form.setModel( new TerminationConfigModel() );
        verify( view ).showSpentLimit( false );
        reset( view );

        TerminationConfigModel terminationConfigModel = new TerminationConfigModel();
        terminationConfigModel.setDaysSpentLimit( 1L );
        form.setModel( terminationConfigModel );
        verify( view ).showSpentLimit( true );
        reset( view );

        terminationConfigModel = new TerminationConfigModel();
        terminationConfigModel.setHoursSpentLimit( 1L );
        form.setModel( terminationConfigModel );
        verify( view ).showSpentLimit( true );
        reset( view );

        terminationConfigModel = new TerminationConfigModel();
        terminationConfigModel.setMinutesSpentLimit( 1L );
        form.setModel( terminationConfigModel );
        verify( view ).showSpentLimit( true );
        reset( view );

        terminationConfigModel = new TerminationConfigModel();
        terminationConfigModel.setSecondsSpentLimit( 1L );
        form.setModel( terminationConfigModel );
        verify( view ).showSpentLimit( true );
    }

    @Test
    public void testEnableUnimprovedSpentLimit() throws Exception {
        form.setModel( new TerminationConfigModel() );
        verify( view ).showUnimprovedSpentLimit( false );
        reset( view );

        TerminationConfigModel terminationConfigModel = new TerminationConfigModel();
        terminationConfigModel.setUnimprovedDaysSpentLimit( 1L );
        form.setModel( terminationConfigModel );
        verify( view ).showUnimprovedSpentLimit( true );
        reset( view );

        terminationConfigModel = new TerminationConfigModel();
        terminationConfigModel.setUnimprovedHoursSpentLimit( 1L );
        form.setModel( terminationConfigModel );
        verify( view ).showUnimprovedSpentLimit( true );
        reset( view );

        terminationConfigModel = new TerminationConfigModel();
        terminationConfigModel.setUnimprovedMinutesSpentLimit( 1L );
        form.setModel( terminationConfigModel );
        verify( view ).showUnimprovedSpentLimit( true );
        reset( view );

        terminationConfigModel = new TerminationConfigModel();
        terminationConfigModel.setUnimprovedSecondsSpentLimit( 1L );
        form.setModel( terminationConfigModel );
        verify( view ).showUnimprovedSpentLimit( true );
    }

    private TerminationConfigModel getModel() {
        TerminationConfigModel terminationConfigModel = new TerminationConfigModel();

        terminationConfigModel.setDaysSpentLimit( Long.valueOf( 14 ) );
        terminationConfigModel.setHoursSpentLimit( Long.valueOf( 4 ) );
        terminationConfigModel.setMinutesSpentLimit( Long.valueOf( 25 ) );
        terminationConfigModel.setSecondsSpentLimit( Long.valueOf( 59 ) );

        terminationConfigModel.setUnimprovedDaysSpentLimit( Long.valueOf( 112 ) );
        terminationConfigModel.setUnimprovedHoursSpentLimit( Long.valueOf( 12 ) );
        terminationConfigModel.setUnimprovedMinutesSpentLimit( Long.valueOf( 10 ) );
        terminationConfigModel.setUnimprovedSecondsSpentLimit( Long.valueOf( 44 ) );
        return terminationConfigModel;
    }

    private TerminationConfigModel getUnimprovedSpentLimitModel() {
        TerminationConfigModel terminationConfigModel = new TerminationConfigModel();

        terminationConfigModel.setUnimprovedDaysSpentLimit( Long.valueOf( 112 ) );
        terminationConfigModel.setUnimprovedHoursSpentLimit( Long.valueOf( 12 ) );
        terminationConfigModel.setUnimprovedMinutesSpentLimit( Long.valueOf( 10 ) );
        terminationConfigModel.setUnimprovedSecondsSpentLimit( Long.valueOf( 44 ) );
        return terminationConfigModel;
    }

    private TerminationConfigModel getSpentLimitModel() {
        TerminationConfigModel terminationConfigModel = new TerminationConfigModel();

        terminationConfigModel.setDaysSpentLimit( Long.valueOf( 14 ) );
        terminationConfigModel.setHoursSpentLimit( Long.valueOf( 4 ) );
        terminationConfigModel.setMinutesSpentLimit( Long.valueOf( 25 ) );
        terminationConfigModel.setSecondsSpentLimit( Long.valueOf( 59 ) );

        return terminationConfigModel;
    }

}