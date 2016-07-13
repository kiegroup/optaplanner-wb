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

import java.util.ArrayList;
import java.util.List;

import org.optaplanner.core.config.constructionheuristic.ConstructionHeuristicPhaseConfig;
import org.optaplanner.core.config.phase.PhaseConfig;
import org.optaplanner.core.config.score.director.ScoreDirectorFactoryConfig;
import org.optaplanner.core.config.solver.SolverConfig;
import org.optaplanner.core.config.solver.termination.TerminationConfig;
import org.optaplanner.workbench.screens.solver.model.ConstructionHeuristicPhaseConfigModel;
import org.optaplanner.workbench.screens.solver.model.ConstructionHeuristicTypeModel;
import org.optaplanner.workbench.screens.solver.model.PhaseConfigModel;
import org.optaplanner.workbench.screens.solver.model.ScoreDirectorFactoryConfigModel;
import org.optaplanner.workbench.screens.solver.model.SolverConfigModel;
import org.optaplanner.workbench.screens.solver.model.TerminationCompositionStyleModel;
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
        model.setPhaseConfigList( create( solverConfig.getPhaseConfigList() ) );

        return model;
    }

    private ScoreDirectorFactoryConfigModel create( final ScoreDirectorFactoryConfig scoreDirectorFactoryConfig ) {

        if ( scoreDirectorFactoryConfig == null ) {
            return new ScoreDirectorFactoryConfigModel();
        } else {
            ScoreDirectorFactoryConfigModel model = new ScoreDirectorFactoryConfigModel();

            model.setKSessionName( scoreDirectorFactoryConfig.getKsessionName() );

            return model;
        }
    }

    private TerminationConfigModel create( final TerminationConfig terminationConfig ) {

        if ( terminationConfig == null ) {
            return new TerminationConfigModel();
        } else {
            TerminationConfigModel model = new TerminationConfigModel();

            if ( terminationConfig.getTerminationCompositionStyle() != null ) {
                model.setTerminationCompositionStyle( TerminationCompositionStyleModel.valueOf( terminationConfig.getTerminationCompositionStyle().name() ) );
            }

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

            model.setBestScoreLimit( terminationConfig.getBestScoreLimit() );
            model.setBestScoreFeasible( terminationConfig.getBestScoreFeasible() );

            model.setStepCountLimit( terminationConfig.getStepCountLimit() );
            model.setUnimprovedStepCountLimit( terminationConfig.getUnimprovedStepCountLimit() );

            model.setScoreCalculationCountLimit( terminationConfig.getScoreCalculationCountLimit() );

            if ( terminationConfig.getTerminationConfigList() != null ) {
                List<TerminationConfigModel> nestedTerminationList = new ArrayList<>();
                for ( TerminationConfig termination : terminationConfig.getTerminationConfigList() ) {
                    nestedTerminationList.add( create( termination ) );
                }
                model.setTerminationConfigList( nestedTerminationList );
            }
            return model;
        }
    }

    private List<PhaseConfigModel> create( final List<PhaseConfig> phaseConfigList ) {
        if ( phaseConfigList == null ) {
            return new ArrayList<>();
        } else {
            List<PhaseConfigModel> result = new ArrayList<>( phaseConfigList.size() );
            for ( PhaseConfig phaseConfig : phaseConfigList ) {
                if ( phaseConfig instanceof ConstructionHeuristicPhaseConfig ) {
                    ConstructionHeuristicPhaseConfigModel phaseConfigModel = new ConstructionHeuristicPhaseConfigModel();
                    ConstructionHeuristicPhaseConfig constructionHeuristicPhaseConfig = (ConstructionHeuristicPhaseConfig) phaseConfig;
                    if ( constructionHeuristicPhaseConfig.getConstructionHeuristicType() != null ) {
                        phaseConfigModel.setConstructionHeuristicType( ConstructionHeuristicTypeModel.valueOf( constructionHeuristicPhaseConfig.getConstructionHeuristicType().name() ) );
                    }
                    result.add( phaseConfigModel );
                }
            }
            return result;
        }
    }
}
