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
import org.optaplanner.core.config.heuristic.selector.entity.EntitySorterManner;
import org.optaplanner.core.config.localsearch.LocalSearchPhaseConfig;
import org.optaplanner.core.config.localsearch.LocalSearchType;
import org.optaplanner.core.config.solver.SolverConfig;
import org.optaplanner.core.config.solver.termination.TerminationConfig;
import org.optaplanner.workbench.screens.solver.model.ConstructionHeuristicPhaseConfigModel;
import org.optaplanner.workbench.screens.solver.model.LocalSearchPhaseConfigModel;
import org.optaplanner.workbench.screens.solver.model.ScoreDirectorFactoryConfigModel;
import org.optaplanner.workbench.screens.solver.model.SolverConfigModel;
import org.optaplanner.workbench.screens.solver.model.TerminationConfigModel;

import static org.junit.Assert.*;

public class ToSolverConfigTest {

    @Test
    public void get() {
        ToSolverConfig toSolverConfigModel = new ToSolverConfig(getSolverConfigModel());
        SolverConfig solverConfig = toSolverConfigModel.get();

        TerminationConfig terminationConfig = solverConfig.getTerminationConfig();
        assertEquals(Long.valueOf(1),
                     terminationConfig.getMillisecondsSpentLimit());
        assertEquals(1,
                     terminationConfig.getTerminationConfigList().size());
        assertEquals(Boolean.TRUE,
                     terminationConfig.getTerminationConfigList().get(0).getBestScoreFeasible());

        assertEquals("testKsession",
                     solverConfig.getScoreDirectorFactoryConfig().getKsessionName());

        assertEquals(2,
                     solverConfig.getPhaseConfigList().size());
        ConstructionHeuristicPhaseConfig constructionHeuristicPhaseConfig = (ConstructionHeuristicPhaseConfig) solverConfig.getPhaseConfigList().get(0);
        assertEquals(ConstructionHeuristicType.FIRST_FIT,
                     constructionHeuristicPhaseConfig.getConstructionHeuristicType());
        assertEquals(EntitySorterManner.DECREASING_DIFFICULTY,
                     constructionHeuristicPhaseConfig.getEntitySorterManner());
        LocalSearchPhaseConfig localSearchPhaseConfig = (LocalSearchPhaseConfig) solverConfig.getPhaseConfigList().get(1);
        assertEquals(LocalSearchType.TABU_SEARCH,
                     localSearchPhaseConfig.getLocalSearchType());
    }

    private SolverConfigModel getSolverConfigModel() {
        SolverConfigModel solverConfigModel = new SolverConfigModel();

        TerminationConfigModel terminationConfigModel = new TerminationConfigModel();
        terminationConfigModel.setMillisecondsSpentLimit(1l);
        TerminationConfigModel nestedTerminationConfig = new TerminationConfigModel();
        nestedTerminationConfig.setBestScoreFeasible(true);
        terminationConfigModel.setTerminationConfigList(Arrays.asList(nestedTerminationConfig));
        solverConfigModel.setTerminationConfig(terminationConfigModel);

        ScoreDirectorFactoryConfigModel scoreDirectorFactoryConfigModel = new ScoreDirectorFactoryConfigModel();
        scoreDirectorFactoryConfigModel.setKSessionName("testKsession");
        solverConfigModel.setScoreDirectorFactoryConfig(scoreDirectorFactoryConfigModel);

        ConstructionHeuristicPhaseConfigModel constructionHeuristicPhaseConfigModel = new ConstructionHeuristicPhaseConfigModel();
        constructionHeuristicPhaseConfigModel.setConstructionHeuristicType(ConstructionHeuristicType.FIRST_FIT);
        constructionHeuristicPhaseConfigModel.setEntitySorterManner(EntitySorterManner.DECREASING_DIFFICULTY);

        LocalSearchPhaseConfigModel localSearchPhaseConfigModel = new LocalSearchPhaseConfigModel();
        localSearchPhaseConfigModel.setLocalSearchType(LocalSearchType.TABU_SEARCH);

        solverConfigModel.setPhaseConfigList(Arrays.asList(constructionHeuristicPhaseConfigModel,
                                                           localSearchPhaseConfigModel));

        return solverConfigModel;
    }
}
