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

import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

import org.guvnor.common.services.shared.message.Level;
import org.guvnor.common.services.shared.validation.model.ValidationMessage;
import org.optaplanner.core.config.solver.SolverConfig;

public class SolverValidator {

    @Inject
    private ConfigPersistence configPersistence;

    public List<ValidationMessage> validate( String xml ) {
        ArrayList<ValidationMessage> validationMessages = new ArrayList<ValidationMessage>();

        try {
            SolverConfig solverConfig = configPersistence.toSolverConfig( xml );
            solverConfig.buildSolver( null );

        } catch (Exception e) {
            validationMessages.add( make( e ) );
        }

        return validationMessages;
    }

    private ValidationMessage make( Exception e ) {
        ValidationMessage message = new ValidationMessage();
        message.setId( 0 );
        message.setLevel( Level.ERROR );
        message.setText( e.getMessage() );

        return message;
    }
}
