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

import org.optaplanner.core.api.score.Score;
import org.optaplanner.core.api.score.buildin.bendable.BendableScore;
import org.optaplanner.core.api.score.buildin.bendablebigdecimal.BendableBigDecimalScore;
import org.optaplanner.core.api.score.buildin.bendablelong.BendableLongScore;
import org.optaplanner.core.api.score.buildin.hardmediumsoft.HardMediumSoftScore;
import org.optaplanner.core.api.score.buildin.hardmediumsoftbigdecimal.HardMediumSoftBigDecimalScore;
import org.optaplanner.core.api.score.buildin.hardmediumsoftlong.HardMediumSoftLongScore;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;
import org.optaplanner.core.api.score.buildin.hardsoftbigdecimal.HardSoftBigDecimalScore;
import org.optaplanner.core.api.score.buildin.hardsoftdouble.HardSoftDoubleScore;
import org.optaplanner.core.api.score.buildin.hardsoftlong.HardSoftLongScore;
import org.optaplanner.core.api.score.buildin.simple.SimpleScore;
import org.optaplanner.core.api.score.buildin.simplebigdecimal.SimpleBigDecimalScore;
import org.optaplanner.core.api.score.buildin.simpledouble.SimpleDoubleScore;
import org.optaplanner.core.api.score.buildin.simplelong.SimpleLongScore;

public class PlannerDomainTypes {

    public static final String ABSTRACT_SOLUTION_CLASS_NAME = "org.optaplanner.core.impl.domain.solution.AbstractSolution";

    public static final String ABSTRACT_SOLUTION_SIMPLE_CLASS_NAME = "AbstractSolution";

    public static final List<Class<? extends Score>> SCORE_TYPES = new ArrayList<>(  );

    static {
        SCORE_TYPES.add( BendableScore.class );
        SCORE_TYPES.add( BendableBigDecimalScore.class );
        SCORE_TYPES.add( BendableLongScore.class );
        SCORE_TYPES.add( HardMediumSoftScore.class );
        SCORE_TYPES.add( HardMediumSoftBigDecimalScore.class );
        SCORE_TYPES.add( HardMediumSoftLongScore.class );
        SCORE_TYPES.add( HardSoftScore.class );
        SCORE_TYPES.add( HardSoftBigDecimalScore.class );
        SCORE_TYPES.add( HardSoftDoubleScore.class );
        SCORE_TYPES.add( HardSoftLongScore.class );
        SCORE_TYPES.add( SimpleScore.class );
        SCORE_TYPES.add( SimpleBigDecimalScore.class );
        SCORE_TYPES.add( SimpleDoubleScore.class );
        SCORE_TYPES.add( SimpleLongScore.class );
    }
}
