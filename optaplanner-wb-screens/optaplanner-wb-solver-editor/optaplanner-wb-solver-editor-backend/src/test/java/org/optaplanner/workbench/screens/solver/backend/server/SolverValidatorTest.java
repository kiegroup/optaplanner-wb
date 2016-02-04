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

package org.optaplanner.workbench.screens.solver.backend.server;

import java.util.List;

import org.guvnor.common.services.shared.validation.model.ValidationMessage;
import org.guvnor.test.TestFileSystem;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.uberfire.backend.server.util.Paths;
import org.uberfire.backend.vfs.Path;

import static org.junit.Assert.*;
import static org.optaplanner.workbench.screens.solver.backend.server.TestUtil.*;

@RunWith(MockitoJUnitRunner.class)
public class SolverValidatorTest {

    @Mock
    Path path;

    private SolverValidator   solverValidator;

    private TestFileSystem testFileSystem;

    @Before
    public void setUp() throws Exception {
        testFileSystem = new TestFileSystem();
        solverValidator = testFileSystem.getReference( SolverValidator.class );
    }

    @After
    public void tearDown() throws Exception {
        testFileSystem.tearDown();
    }

    @Test
    public void testProjectWorks() throws Exception {

        final String url = "/ProjectWorks/src/main/resources/cb/my.solver.xml";

        org.uberfire.java.nio.file.Path path = testFileSystem.fileSystemProvider.getPath( this.getClass().getResource( url ).toURI() );

        final List<ValidationMessage> messages = solverValidator.validate( Paths.convert( path ),
                                                                           loadResource( url ) );

        for ( ValidationMessage message : messages ) {
            System.out.println( message.getText() );
        }

        assertTrue( messages.isEmpty() );
    }

    @Test
    public void testProjectBuildError() throws Exception {

        final String url = "/ProjectBuildError/src/main/resources/cb/my.solver.xml";

        org.uberfire.java.nio.file.Path path = testFileSystem.fileSystemProvider.getPath( this.getClass().getResource( url ).toURI() );

        final List<ValidationMessage> messages = solverValidator.validate( Paths.convert( path ),
                                                                           loadResource( url ) );

        for ( ValidationMessage message : messages ) {
            System.out.println( message.getText() );
        }

        assertFalse( messages.isEmpty() );
    }

    @Test
    public void testProjectPlannerError() throws Exception {

        final String url = "/ProjectPlannerError/src/main/resources/cb/my.solver.xml";

        org.uberfire.java.nio.file.Path path = testFileSystem.fileSystemProvider.getPath( this.getClass().getResource( url ).toURI() );

        final List<ValidationMessage> messages = solverValidator.validate( Paths.convert( path ),
                                                                           loadResource( url ) );

        for ( ValidationMessage message : messages ) {
            System.out.println( message.getText() );
        }

        assertFalse( messages.isEmpty() );
    }

}