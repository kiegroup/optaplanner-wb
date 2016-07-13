/*
 * Copyright 2016 Red Hat, Inc. and/or its affiliates.
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

import java.util.Arrays;

import org.junit.Test;
import org.optaplanner.core.config.constructionheuristic.ConstructionHeuristicPhaseConfig;
import org.optaplanner.core.config.constructionheuristic.ConstructionHeuristicType;
import org.optaplanner.core.config.score.director.ScoreDirectorFactoryConfig;
import org.optaplanner.core.config.solver.SolverConfig;
import org.optaplanner.core.config.solver.termination.TerminationConfig;
import org.optaplanner.workbench.screens.solver.model.ConstructionHeuristicPhaseConfigModel;
import org.optaplanner.workbench.screens.solver.model.ConstructionHeuristicTypeModel;
import org.optaplanner.workbench.screens.solver.model.SolverConfigModel;
import org.optaplanner.workbench.screens.solver.model.TerminationConfigModel;

import static org.junit.Assert.assertEquals;

public class ToSolverConfigModelTest {

    @Test
    public void get() {
        ToSolverConfigModel toSolverConfigModel = new ToSolverConfigModel( getSolverConfig() );
        SolverConfigModel solverConfigModel = toSolverConfigModel.get();

        TerminationConfigModel terminationConfigModel = solverConfigModel.getTermination();
        assertEquals( Long.valueOf( 1 ), terminationConfigModel.getMillisecondsSpentLimit() );
        assertEquals( 1, terminationConfigModel.getTerminationConfigList().size() );
        assertEquals( Boolean.TRUE, terminationConfigModel.getTerminationConfigList().get( 0 ).getBestScoreFeasible() );

        assertEquals( "testKsession", solverConfigModel.getScoreDirectorFactoryConfig().getKSessionName() );

        assertEquals( 1, solverConfigModel.getPhaseConfigList().size() );
        ConstructionHeuristicPhaseConfigModel constructionHeuristicPhaseConfigModel = (ConstructionHeuristicPhaseConfigModel) solverConfigModel.getPhaseConfigList().get( 0 );
        assertEquals( ConstructionHeuristicTypeModel.FIRST_FIT, constructionHeuristicPhaseConfigModel.getConstructionHeuristicType() );

    }

    private SolverConfig getSolverConfig() {
        SolverConfig solverConfig = new SolverConfig();

        TerminationConfig terminationConfig = new TerminationConfig();
        terminationConfig.setMillisecondsSpentLimit( 1l );
        TerminationConfig nestedTerminationConfig = new TerminationConfig();
        nestedTerminationConfig.setBestScoreFeasible( true );
        terminationConfig.setTerminationConfigList( Arrays.asList( nestedTerminationConfig ) );
        solverConfig.setTerminationConfig( terminationConfig );

        ScoreDirectorFactoryConfig scoreDirectorFactoryConfig = new ScoreDirectorFactoryConfig();
        scoreDirectorFactoryConfig.setKsessionName( "testKsession" );
        solverConfig.setScoreDirectorFactoryConfig( scoreDirectorFactoryConfig );

        ConstructionHeuristicPhaseConfig constructionHeuristicPhaseConfig = new ConstructionHeuristicPhaseConfig();
        constructionHeuristicPhaseConfig.setConstructionHeuristicType( ConstructionHeuristicType.FIRST_FIT );
        solverConfig.setPhaseConfigList( Arrays.asList( constructionHeuristicPhaseConfig ) );

        return solverConfig;
    }
}
