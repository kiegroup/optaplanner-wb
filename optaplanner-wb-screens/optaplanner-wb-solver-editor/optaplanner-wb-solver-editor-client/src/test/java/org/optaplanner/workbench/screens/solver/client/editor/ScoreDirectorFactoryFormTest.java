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
import org.optaplanner.workbench.screens.solver.model.ScoreDirectorFactoryConfigModel;
import org.uberfire.backend.vfs.Path;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(GwtMockitoTestRunner.class)
public class ScoreDirectorFactoryFormTest {

    @Mock
    ScoreDirectorFactoryFormView view;

    @Mock
    private Path path;

    private ScoreDirectorFactoryForm form;

    @Before
    public void setUp() throws Exception {
        form = new ScoreDirectorFactoryForm( view );
    }

    @Test
    public void setPresenter() throws Exception {
        verify( view ).setPresenter( form );
    }

    @Test
    public void setEmptyModel() throws Exception {
        ScoreDirectorFactoryConfigModel model = new ScoreDirectorFactoryConfigModel();
        form.setModel( model, path );

        verify( view ).setKSession( null, path );
    }

    @Test
    public void setEmptyKSession() throws Exception {
        ScoreDirectorFactoryConfigModel model = new ScoreDirectorFactoryConfigModel();
        form.setModel( model, path );

        verify( view ).setKSession( null, path );
    }

    @Test
    public void setModel() throws Exception {
        ScoreDirectorFactoryConfigModel model = new ScoreDirectorFactoryConfigModel();
        model.setKSessionName( "someSession" );


        form.setModel( model, path );

        verify( view ).setKSession( "someSession", path );
    }

    @Test
    public void onNameChange() throws Exception {
        ScoreDirectorFactoryConfigModel model = new ScoreDirectorFactoryConfigModel();

        form.setModel( model, path );

        form.onKSessionNameChange( "mySession" );

        assertEquals( "mySession", model.getKSessionName() );
    }

    @Test
    public void onNameChangeToDefault() throws Exception {
        ScoreDirectorFactoryConfigModel model = new ScoreDirectorFactoryConfigModel();

        form.setModel( model, path );

        form.onKSessionNameChange( "defaultKieSession" );

        assertNull( model.getKSessionName() );
    }

    @Test
    public void onNameChangeNull() throws Exception {
        ScoreDirectorFactoryConfigModel model = new ScoreDirectorFactoryConfigModel();

        form.setModel( model, path );

        form.onKSessionNameChange( null );

        assertEquals( null, model.getKSessionName() );
    }

}