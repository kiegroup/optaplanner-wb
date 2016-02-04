/*
 * Copyright 2016 Red Hat, Inc. and/or its affiliates.
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

import org.optaplanner.core.config.score.definition.ScoreDefinitionType;
import org.optaplanner.core.config.score.director.ScoreDirectorFactoryConfig;
import org.optaplanner.core.config.solver.SolverConfig;
import org.optaplanner.core.config.solver.termination.TerminationConfig;
import org.optaplanner.workbench.screens.solver.model.ScoreDefinitionTypeModel;
import org.optaplanner.workbench.screens.solver.model.ScoreDirectorFactoryConfigModel;
import org.optaplanner.workbench.screens.solver.model.SolverConfigModel;
import org.optaplanner.workbench.screens.solver.model.TerminationConfigModel;

class ToSolverConfigModel {

    private SolverConfig solverConfig;

    public ToSolverConfigModel( final SolverConfig solverConfig ) {
        this.solverConfig = solverConfig;
    }

    public SolverConfigModel get() {
        SolverConfigModel model = new SolverConfigModel();
        model.setTerminationConfig( create( solverConfig.getTerminationConfig() ) );
        model.setScoreDirectorFactoryConfig( create( solverConfig.getScoreDirectorFactoryConfig() ) );

        return model;
    }

    private ScoreDirectorFactoryConfigModel create( final ScoreDirectorFactoryConfig scoreDirectorFactoryConfig ) {

        if ( scoreDirectorFactoryConfig == null ) {
            return new ScoreDirectorFactoryConfigModel();
        } else {
            ScoreDirectorFactoryConfigModel model = new ScoreDirectorFactoryConfigModel();

            model.setScoreDefinitionType( create( scoreDirectorFactoryConfig.getScoreDefinitionType() ) );
            model.setKSessionName( scoreDirectorFactoryConfig.getKsessionName() );

            return model;
        }
    }

    private ScoreDefinitionTypeModel create( final ScoreDefinitionType scoreDefinitionType ) {
        if ( scoreDefinitionType != null ) {
            for ( ScoreDefinitionTypeModel model : ScoreDefinitionTypeModel.values() ) {
                if ( model.name().equals( scoreDefinitionType.name() ) ) {
                    return model;
                }
            }
        }

        return null;
    }

    private TerminationConfigModel create( final TerminationConfig terminationConfig ) {

        if ( terminationConfig == null ) {
            return new TerminationConfigModel();
        } else {
            TerminationConfigModel model = new TerminationConfigModel();

            model.setDaysSpentLimit( terminationConfig.getDaysSpentLimit() );
            model.setHoursSpentLimit( terminationConfig.getHoursSpentLimit() );
            model.setMinutesSpentLimit( terminationConfig.getMinutesSpentLimit() );
            model.setSecondsSpentLimit( terminationConfig.getSecondsSpentLimit() );
            model.setMillisecondsSpentLimit( terminationConfig.getMillisecondsSpentLimit() );

            model.setUnimprovedDaysSpentLimit( terminationConfig.getUnimprovedDaysSpentLimit() );
            model.setUnimprovedHoursSpentLimit( terminationConfig.getUnimprovedHoursSpentLimit() );
            model.setUnimprovedMinutesSpentLimit( terminationConfig.getUnimprovedMinutesSpentLimit() );
            model.setUnimprovedSecondsSpentLimit( terminationConfig.getUnimprovedSecondsSpentLimit() );
            model.setUnimprovedMillisecondsSpentLimit( terminationConfig.getUnimprovedMillisecondsSpentLimit() );

            return model;
        }
    }
}
