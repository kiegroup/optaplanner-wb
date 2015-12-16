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

package org.optaplanner.workbench.screens.domaineditor.client.util;

import java.util.ArrayList;
import java.util.List;

import org.uberfire.commons.data.Pair;

public class PlannerDomainTypes {

    public static final String ABSTRACT_SOLUTION_CLASS_NAME = "org.optaplanner.core.impl.domain.solution.AbstractSolution";

    public static final String ABSTRACT_SOLUTION_SIMPLE_CLASS_NAME = "AbstractSolution";

    public static final String SIMPLE_SCORE = "SimpleScore";

    public static final String SIMPLE_SCORE_CLASS = "org.optaplanner.core.api.score.buildin.simple.SimpleScore";

    public static final String SIMPLE_LONG_SCORE = "SimpleLongScore";

    public static final String SIMPLE_LONG_SCORE_CLASS = "org.optaplanner.core.api.score.buildin.simplelong.SimpleLongScore";

    public static final String SIMPLE_DOUBLE_SCORE = "SimpleDoubleScore";

    public static final String SIMPLE_DOUBLE_SCORE_CLASS = "org.optaplanner.core.api.score.buildin.simpledouble.SimpleDoubleScore";

    public static final String SIMPLE_BIG_DECIMAL_SCORE = "SimpleBigDecimalScore";

    public static final String SIMPLE_BIG_DECIMAL_SCORE_CLASS = "org.optaplanner.core.api.score.buildin.simplebigdecimal.SimpleBigDecimalScore";

    public static final String HARD_SOFT_SCORE = "HardSoftScore";

    public static final String HARD_SOFT_SCORE_CLASS = "org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore";

    public static final String HARD_SOFT_LONG_SCORE = "HardSoftLongScore";

    public static final String HARD_SOFT_LONG_SCORE_CLASS = "org.optaplanner.core.api.score.buildin.hardsoftlong.HardSoftLongScore";

    public static final String HARD_SOFT_DOUBLE_SCORE = "HardSoftDoubleScore";

    public static final String HARD_SOFT_DOUBLE_SCORE_CLASS = "org.optaplanner.core.api.score.buildin.hardsoftdouble.HardSoftDoubleScore";

    public static final String HARD_SOFT_BIG_DECIMAL_SCORE = "HardSoftBigDecimalScore";

    public static final String HARD_SOFT_BIG_DECIMAL_CLASS = "org.optaplanner.core.api.score.buildin.hardsoftbigdecimal.HardSoftBigDecimalScore";

    public static final String HARD_MEDIUM_SOFT_SCORE = "HardMediumSoftScore";

    public static final String HARD_MEDIUM_SOFT_SCORE_CLASS = "org.optaplanner.core.api.score.buildin.hardmediumsoft.HardMediumSoftScore";

    public static final String HARD_MEDIUM_SOFT_LONG_SCORE = "HardMediumSoftLongScore";

    public static final String HARD_MEDIUM_SOFT_LONG_SCORE_CLASS = "org.optaplanner.core.api.score.buildin.hardmediumsoftlong.HardMediumSoftLongScore";

    public static final List<Pair<String, String>> SCORE_TYPES = new ArrayList<Pair<String, String>>(  );

    static {
        SCORE_TYPES.add( new Pair<String, String>( SIMPLE_SCORE, SIMPLE_SCORE_CLASS ) );
        SCORE_TYPES.add( new Pair<String, String>( SIMPLE_LONG_SCORE, SIMPLE_LONG_SCORE_CLASS ) );
        SCORE_TYPES.add( new Pair<String, String>( SIMPLE_DOUBLE_SCORE, SIMPLE_DOUBLE_SCORE_CLASS ) );
        SCORE_TYPES.add( new Pair<String, String>( SIMPLE_BIG_DECIMAL_SCORE, SIMPLE_BIG_DECIMAL_SCORE_CLASS ) );

        SCORE_TYPES.add( new Pair<String, String>( HARD_SOFT_SCORE, HARD_SOFT_SCORE_CLASS ) );
        SCORE_TYPES.add( new Pair<String, String>( HARD_SOFT_LONG_SCORE, HARD_SOFT_LONG_SCORE_CLASS ) );
        SCORE_TYPES.add( new Pair<String, String>( HARD_SOFT_DOUBLE_SCORE, HARD_SOFT_DOUBLE_SCORE_CLASS ) );
        SCORE_TYPES.add( new Pair<String, String>( HARD_SOFT_BIG_DECIMAL_SCORE, HARD_SOFT_BIG_DECIMAL_CLASS ) );

        SCORE_TYPES.add( new Pair<String, String>( HARD_MEDIUM_SOFT_SCORE, HARD_MEDIUM_SOFT_SCORE_CLASS ) );
        SCORE_TYPES.add( new Pair<String, String>( HARD_MEDIUM_SOFT_LONG_SCORE, HARD_MEDIUM_SOFT_LONG_SCORE_CLASS ) );

    }
}
