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

import org.optaplanner.core.config.domain.ScanAnnotatedClassesConfig;
import org.optaplanner.core.config.score.definition.ScoreDefinitionType;
import org.optaplanner.core.config.score.director.ScoreDirectorFactoryConfig;
import org.optaplanner.core.config.solver.SolverConfig;
import org.optaplanner.core.config.solver.termination.TerminationConfig;
import org.optaplanner.workbench.screens.solver.model.ScoreDefinitionTypeModel;
import org.optaplanner.workbench.screens.solver.model.ScoreDirectorFactoryConfigModel;
import org.optaplanner.workbench.screens.solver.model.SolverConfigModel;
import org.optaplanner.workbench.screens.solver.model.TerminationConfigModel;

class ToSolverConfig {

    private SolverConfigModel config;

    public ToSolverConfig( final SolverConfigModel config ) {
        this.config = config;
    }

    public SolverConfig get() {

        SolverConfig solverConfig = new SolverConfig();

        solverConfig.setScanAnnotatedClassesConfig( new ScanAnnotatedClassesConfig() );

        solverConfig.setTerminationConfig( create( config.getTermination() ) );
        solverConfig.setScoreDirectorFactoryConfig( create( config.getScoreDirectorFactoryConfig() ) );

        return solverConfig;
    }

    private ScoreDirectorFactoryConfig create( final ScoreDirectorFactoryConfigModel scoreDirectorFactoryConfig ) {

        if ( scoreDirectorFactoryConfig == null ) {
            return new ScoreDirectorFactoryConfig();
        } else {
            ScoreDirectorFactoryConfig config = new ScoreDirectorFactoryConfig();

            config.setScoreDefinitionType( create( scoreDirectorFactoryConfig.getScoreDefinitionType() ) );
            config.setKsessionName( scoreDirectorFactoryConfig.getKSessionName() );

            return config;
        }
    }

    private ScoreDefinitionType create( final ScoreDefinitionTypeModel scoreDefinitionType ) {
        if ( scoreDefinitionType != null ) {
            for ( ScoreDefinitionType model : ScoreDefinitionType.values() ) {
                if ( model.name().equals( scoreDefinitionType.name() ) ) {
                    return model;
                }
            }
        }
        return null;
    }

    private TerminationConfig create( final TerminationConfigModel termination ) {

        if ( termination == null ) {
            return new TerminationConfig();
        } else {
            TerminationConfig terminationConfig = new TerminationConfig();

            terminationConfig.setDaysSpentLimit( termination.getDaysSpentLimit() );
            terminationConfig.setHoursSpentLimit( termination.getHoursSpentLimit() );
            terminationConfig.setMinutesSpentLimit( termination.getMinutesSpentLimit() );
            terminationConfig.setSecondsSpentLimit( termination.getSecondsSpentLimit() );
            terminationConfig.setMillisecondsSpentLimit( termination.getMillisecondsSpentLimit() );

            terminationConfig.setUnimprovedDaysSpentLimit( termination.getUnimprovedDaysSpentLimit() );
            terminationConfig.setUnimprovedHoursSpentLimit( termination.getUnimprovedHoursSpentLimit() );
            terminationConfig.setUnimprovedMinutesSpentLimit( termination.getUnimprovedMinutesSpentLimit() );
            terminationConfig.setUnimprovedSecondsSpentLimit( termination.getUnimprovedSecondsSpentLimit() );
            terminationConfig.setUnimprovedMillisecondsSpentLimit( termination.getUnimprovedMillisecondsSpentLimit() );

            return terminationConfig;
        }
    }
}
