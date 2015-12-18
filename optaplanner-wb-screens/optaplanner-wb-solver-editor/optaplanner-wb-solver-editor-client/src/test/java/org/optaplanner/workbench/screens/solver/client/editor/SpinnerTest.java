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

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class SpinnerTest {

    private SpinnerView view;
    private Spinner spinner;

    private Long currentValue = null;

    @Before
    public void setUp() throws Exception {
        view = mock( SpinnerView.class );
        spinner = new Spinner( view );
        spinner.addValueChangeHandler( new ValueChangeHandler<Long>() {
            @Override
            public void onValueChange( ValueChangeEvent<Long> event ) {
                currentValue = event.getValue();
            }
        } );
    }

    @Test
    public void testPresenter() throws Exception {
        verify( view ).setPresenter( spinner );
    }

    @Test
    public void testSetValue() throws Exception {
        spinner.setValue( Long.valueOf( 99 ) );

        verify( view ).setValue( 99 );
    }

    @Test
    public void testSetNullValue() throws Exception {
        spinner.setValue( null );

        verify( view ).setValue( 0 );
    }

    @Test
    public void testEnable() throws Exception {
        spinner.setEnabled( true );

        verify( view ).enable();
    }

    @Test
    public void testDisable() throws Exception {
        spinner.setEnabled( false );

        verify( view ).disable();
    }

    @Test
    public void testNoMaxSet() throws Exception {

        spinner.setValue( Long.valueOf( 99 ) );
        assertEquals( 99, currentValue.intValue() );

        spinner.onUp();
        assertEquals( 100, currentValue.intValue() );

        spinner.onValueChange( "1" );
        assertEquals( 1, currentValue.intValue() );

        spinner.onDown();
        assertEquals( 0, currentValue.intValue() );

        spinner.onValueChange( "100" );
        assertEquals( 100, currentValue.intValue() );

        spinner.onUp();
        assertEquals( 101, currentValue.intValue() );
    }

    @Test
    public void testUp() throws Exception {

        spinner.setMax( 100 );
        spinner.setValue( Long.valueOf( 99 ) );

        assertEquals( 99, currentValue.intValue() );

        spinner.onUp();
        assertEquals( 100, currentValue.intValue() );

        spinner.onUp();
        assertEquals( 0, currentValue.intValue() );

        spinner.onUp();
        assertEquals( 1, currentValue.intValue() );
    }

    @Test
    public void testDown() throws Exception {
        spinner.setMax( 100 );
        spinner.setValue( Long.valueOf( 1 ) );

        assertEquals( 1, currentValue.intValue() );

        spinner.onDown();
        assertEquals( 0, currentValue.intValue() );

        spinner.onDown();
        assertEquals( 100, currentValue.intValue() );

        spinner.onDown();
        assertEquals( 99, currentValue.intValue() );
    }

    @Test
    public void testUserTypesInTheValue() throws Exception {
        spinner.setMax( 100 );
        spinner.setValue( Long.valueOf( 1 ) );

        spinner.onValueChange( "50" );

        assertEquals( 50, currentValue.intValue() );
    }

    @Test
    public void testUserTypesInInvalidValue() throws Exception {
        spinner.setMax( 100 );
        spinner.setValue( Long.valueOf( 1 ) );

        spinner.onValueChange( "HAHA I WILL BREAK YOU!!!!" );

        verify( view, times( 2 ) ).setValue( 1 );

        assertEquals( 1, currentValue.intValue() );
    }

    @Test
    public void testUserTypesInTooBigValue() throws Exception {
        spinner.setMax( 100 );
        spinner.setValue( Long.valueOf( 1 ) );

        spinner.onValueChange( "200" );

        verify( view, times( 2 ) ).setValue( 1 );

        assertEquals( 1, currentValue.intValue() );
    }

    @Test
    public void testUserTypesInTooSmallValue() throws Exception {
        spinner.setMax( 100 );
        spinner.setValue( Long.valueOf( 1 ) );

        spinner.onValueChange( "-1" );

        verify( view, times( 2 ) ).setValue( 1 );

        assertEquals( 1, currentValue.intValue() );
    }
}