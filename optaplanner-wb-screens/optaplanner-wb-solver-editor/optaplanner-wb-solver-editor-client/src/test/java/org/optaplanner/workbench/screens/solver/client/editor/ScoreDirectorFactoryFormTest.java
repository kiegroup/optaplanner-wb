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

import java.util.ArrayList;

import com.google.gwtmockito.GwtMockitoTestRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.optaplanner.workbench.screens.solver.model.ScoreDefinitionTypeModel;
import org.optaplanner.workbench.screens.solver.model.ScoreDirectorFactoryConfigModel;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(GwtMockitoTestRunner.class)
public class ScoreDirectorFactoryFormTest {

    @Mock
    ScoreDirectorFactoryFormView view;
    private ScoreDirectorFactoryForm form;

    @Before
    public void setUp() throws Exception {
        form = new ScoreDirectorFactoryForm( view );
    }

    @Test
    public void testSetPresenter() throws Exception {
        verify( view ).setPresenter( form );
    }

    @Test
    public void testScoreDefinitionTypesSet() throws Exception {

        for (ScoreDefinitionTypeModel type : ScoreDefinitionTypeModel.values()) {
            verify( view ).addScoreDefinitionType( type );
        }

        verify( view, atLeastOnce() ).addScoreDefinitionType( any( ScoreDefinitionTypeModel.class ) );
        verify( view, times( ScoreDefinitionTypeModel.values().length ) ).addScoreDefinitionType( any( ScoreDefinitionTypeModel.class ) );
    }

    @Test
    public void testSetEmptyModel() throws Exception {
        ScoreDirectorFactoryConfigModel model = new ScoreDirectorFactoryConfigModel();
        form.setModel( model );

        assertEquals( ScoreDefinitionTypeModel.HARD_SOFT, model.getScoreDefinitionType() );
        verify( view ).setSelectedScoreDefinitionType( ScoreDefinitionTypeModel.HARD_SOFT );
        verify( view ).setScoreDrl( "" );
    }

    @Test
    public void testSetEmptyScoreDrl() throws Exception {
        ScoreDirectorFactoryConfigModel model = new ScoreDirectorFactoryConfigModel();
        model.setScoreDefinitionType( ScoreDefinitionTypeModel.SIMPLE );
        model.setScoreDrlList( new ArrayList<String>() );
        form.setModel( model );

        verify( view ).setSelectedScoreDefinitionType( ScoreDefinitionTypeModel.SIMPLE );
        verify( view ).setScoreDrl( "" );
    }

    @Test
    public void testSetModel() throws Exception {
        ScoreDirectorFactoryConfigModel model = new ScoreDirectorFactoryConfigModel();
        model.setScoreDefinitionType( ScoreDefinitionTypeModel.SIMPLE_BIG_DECIMAL );
        ArrayList<String> scoreDrlList = new ArrayList<String>();
        scoreDrlList.add( "some.drl" );
        model.setScoreDrlList( scoreDrlList );

        form.setModel( model );

        verify( view ).setSelectedScoreDefinitionType( ScoreDefinitionTypeModel.SIMPLE_BIG_DECIMAL );
        verify( view ).setScoreDrl( "some.drl" );
    }

    @Test
    public void testOnNameChange() throws Exception {
        ScoreDirectorFactoryConfigModel model = new ScoreDirectorFactoryConfigModel();

        form.setModel( model );

        form.onFileNameChange( "hello.txt" );

        assertTrue( model.getScoreDrlList().contains( "hello.txt" ) );
    }

    @Test
    public void testSelectScoreDefinitionType() throws Exception {
        ScoreDirectorFactoryConfigModel model = new ScoreDirectorFactoryConfigModel();
        form.setModel( model );

        form.onScoreDefinitionTypeSelected( ScoreDefinitionTypeModel.HARD_SOFT.name() );

        assertEquals( ScoreDefinitionTypeModel.HARD_SOFT, model.getScoreDefinitionType() );
    }
}