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
package org.optaplanner.workbench.screens.solver.client.resources.i18n;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.Messages;

/**
 * Solver Editor i18n constants
 */
public interface SolverEditorConstants
        extends
        Messages {

    public static final SolverEditorConstants INSTANCE = GWT.create( SolverEditorConstants.class );

    String solverResourceTypeDescription();

    String newSolverDescription();

    String delete();

    String Source();

    String ScoreDirectorFactory();

    String ScoreDRL();

    String Termination();

    String Cancel();

    String KnowledgeSession();

    String TerminationCompositionStyleHelp();

    String TimeSpent();

    String TimeSpentHelp();

    String UnimprovedTimeSpent();

    String UnimprovedTimeSpentHelp();

    String BestScoreLimit();

    String BestScoreLimitHelp();

    String BestScoreFeasible();

    String BestScoreFeasibleHelp();

    String StepCountLimit();

    String StepCountLimitHelp();

    String UnimprovedStepCountLimit();

    String UnimprovedStepCountLimitHelp();

    String ScoreCalculationCountLimit();

    String ScoreCalculationCountLimitHelp();

    String NestedTermination();

    String And();

    String Or();

    String True();

    String False();

    String EmptyTreeLabelText();

}
