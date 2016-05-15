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
package org.optaplanner.workbench.screens.solver.model;

import org.jboss.errai.common.client.api.annotations.Portable;

@Portable
public enum TerminationConfigOption {
    TERMINATION_COMPOSITION_STYLE,
    MILLISECONDS_SPENT_LIMIT,
    SECONDS_SPENT_LIMIT,
    MINUTES_SPENT_LIMIT,
    HOURS_SPENT_LIMIT,
    DAYS_SPENT_LIMIT,
    UNIMPROVED_MILLISECONDS_SPENT_LIMIT,
    UNIMPROVED_SECONDS_SPENT_LIMIT,
    UNIMPROVED_MINUTES_SPENT_LIMIT,
    UNIMPROVED_HOURS_SPENT_LIMIT,
    UNIMPROVED_DAYS_SPENT_LIMIT,
    BEST_SCORE_LIMIT,
    BEST_SCORE_FEASIBLE,
    STEP_COUNT_LIMIT,
    UNIMPROVED_STEP_COUNT_LIMIT,
    SCORE_CALCULATION_COUNT_LIMIT,
    NESTED;

}
