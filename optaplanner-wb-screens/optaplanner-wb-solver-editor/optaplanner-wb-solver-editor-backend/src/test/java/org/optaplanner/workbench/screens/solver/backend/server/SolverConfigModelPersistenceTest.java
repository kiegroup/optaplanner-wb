/*
 * Copyright 2015 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.optaplanner.workbench.screens.solver.backend.server;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.optaplanner.workbench.screens.solver.model.ScoreDirectorFactoryConfigModel;
import org.optaplanner.workbench.screens.solver.model.SolverConfigModel;
import org.optaplanner.workbench.screens.solver.model.TerminationConfigModel;
import org.uberfire.io.IOService;

import static org.junit.Assert.*;
import static org.optaplanner.workbench.screens.solver.backend.server.TestUtil.*;

@RunWith(MockitoJUnitRunner.class)
public class SolverConfigModelPersistenceTest {

    @Mock
    IOService ioService;

    @InjectMocks
    ConfigPersistence configPersistence;

    @Test
    public void testNew() throws Exception {
        SolverConfigModel config = new SolverConfigModel();
        config.setTerminationConfig( new TerminationConfigModel() );
        final ScoreDirectorFactoryConfigModel scoreDirectorFactoryConfig = new ScoreDirectorFactoryConfigModel();
        scoreDirectorFactoryConfig.setKSessionName( "hello" );
        config.setScoreDirectorFactoryConfig( scoreDirectorFactoryConfig );
        String xml = configPersistence.toXML( config );
        assertNotNull( xml );
        assertTrue( xml.startsWith( "<solver" ) );
        assertTrue( xml.contains( "<scanAnnotatedClasses" ) );
        assertTrue( xml.contains( "hello" ) );
        assertTrue( xml.endsWith( ">" ) );
    }

    @Test
    public void testTerminationIsNotEmpty() throws Exception {

        SolverConfigModel solverConfigModel = configPersistence.toConfig( "<solver  />" );

        assertNotNull( solverConfigModel.getTermination() );
    }

    @Test
    public void testScoreDirectorFactoryConfigIsNotEmpty() throws Exception {

        SolverConfigModel solverConfigModel = configPersistence.toConfig( "<solver />" );

        assertNotNull( solverConfigModel.getScoreDirectorFactoryConfig() );
    }

    @Test
    public void testFromFile() throws Exception {
        SolverConfigModel config = configPersistence.toConfig( loadResource( "solver.xml" ) );

        assertNotNull( config );

        assertEquals( "testdataKsession",
                      config.getScoreDirectorFactoryConfig().getKSessionName() );

        assertEquals( Long.valueOf( 30 ),
                      config.getTermination().getSecondsSpentLimit() );
    }

    @Test
    public void testFromFileNoKSessionName() throws Exception {
        SolverConfigModel config = configPersistence.toConfig( loadResource( "ksessionNameNull.solver.xml" ) );

        assertNotNull( config );

        assertEquals( null,
                      config.getScoreDirectorFactoryConfig().getKSessionName() );

        assertEquals( Long.valueOf( 30 ),
                      config.getTermination().getSecondsSpentLimit() );
    }


}