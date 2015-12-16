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
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.optaplanner.workbench.screens.solver.backend.server.TestUtil.*;

@RunWith(MockitoJUnitRunner.class)
public class SolverValidatorTest {

    @Spy
    ConfigPersistence configPersistence = new ConfigPersistence();

    @InjectMocks
    SolverValidator solverValidator;

    @Test
    public void testMissingSolutionClass() throws Exception {
        List<ValidationMessage> result = solverValidator.validate( "<solver />" );
        assertFalse( result.isEmpty() );
        assertEquals( 1, result.size() );
        assertEquals( "The solver configuration must have a solutionClass (null), if it has no scanAnnotatedClasses (null).",
                      result.get( 0 ).getText() );
    }

    @Test
    public void testFromFile() throws Exception {
        List<ValidationMessage> result = solverValidator.validate( loadResource( "solver.xml" ) );

        assertFalse( result.isEmpty() );
        assertEquals( 1, result.size() );
        assertEquals( "The scanAnnotatedClasses (ScanAnnotatedClassesConfig()) did not find any classes with a PlanningSolution annotation.",
                      result.get( 0 ).getText() );
    }

}