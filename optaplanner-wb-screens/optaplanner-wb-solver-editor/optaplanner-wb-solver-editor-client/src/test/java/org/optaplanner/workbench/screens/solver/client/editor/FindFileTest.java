/*
 * Copyright 2015 JBoss Inc
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
import com.google.gwtmockito.GwtMockitoTestRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.optaplanner.workbench.screens.solver.client.editor.FindFile;
import org.optaplanner.workbench.screens.solver.client.editor.FindFileView;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(GwtMockitoTestRunner.class)
public class FindFileTest {

    @Mock
    private FindFileView view;

    private FindFile findFile;

    private String currentValue;

    @Before
    public void setUp() throws Exception {
        findFile = new FindFile( view );

        findFile.addValueChangeHandler( new ValueChangeHandler<String>() {
            @Override
            public void onValueChange( ValueChangeEvent<String> event ) {
                currentValue = event.getValue();
            }
        } );
    }

    @Test
    public void testSetPresenter() throws Exception {
        verify( view ).setPresenter( findFile );
    }

    @Test
    public void testSetFileName() throws Exception {
        findFile.setFileName( "somefile.txt" );

        verify( view ).setFileName( "somefile.txt" );

    }

    @Test
    public void testChangeFileName() throws Exception {
        findFile.onFileNameChange( "hello" );
        assertEquals( "hello", currentValue );
    }
}