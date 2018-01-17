/*
 * Copyright 2017 Red Hat, Inc. and/or its affiliates.
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

package org.optaplanner.workbench.screens.domaineditor.backend.server.validation;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.kie.workbench.common.services.backend.project.ModuleClassLoaderHelper;
import org.kie.workbench.common.services.datamodeller.core.DataObject;
import org.kie.workbench.common.services.datamodeller.core.ObjectProperty;
import org.kie.workbench.common.services.shared.project.KieModuleService;
import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.optaplanner.core.api.score.buildin.bendable.BendableScore;
import org.optaplanner.core.api.score.buildin.bendable.BendableScoreHolder;
import org.optaplanner.core.api.score.buildin.bendablebigdecimal.BendableBigDecimalScore;
import org.optaplanner.core.api.score.buildin.bendablebigdecimal.BendableBigDecimalScoreHolder;
import org.optaplanner.core.api.score.buildin.bendablelong.BendableLongScore;
import org.optaplanner.core.api.score.buildin.bendablelong.BendableLongScoreHolder;
import org.optaplanner.core.api.score.buildin.hardmediumsoft.HardMediumSoftScore;
import org.optaplanner.core.api.score.buildin.hardmediumsoft.HardMediumSoftScoreHolder;
import org.optaplanner.core.api.score.buildin.hardmediumsoftbigdecimal.HardMediumSoftBigDecimalScore;
import org.optaplanner.core.api.score.buildin.hardmediumsoftbigdecimal.HardMediumSoftBigDecimalScoreHolder;
import org.optaplanner.core.api.score.buildin.hardmediumsoftlong.HardMediumSoftLongScore;
import org.optaplanner.core.api.score.buildin.hardmediumsoftlong.HardMediumSoftLongScoreHolder;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScoreHolder;
import org.optaplanner.core.api.score.buildin.hardsoftbigdecimal.HardSoftBigDecimalScore;
import org.optaplanner.core.api.score.buildin.hardsoftbigdecimal.HardSoftBigDecimalScoreHolder;
import org.optaplanner.core.api.score.buildin.hardsoftdouble.HardSoftDoubleScore;
import org.optaplanner.core.api.score.buildin.hardsoftdouble.HardSoftDoubleScoreHolder;
import org.optaplanner.core.api.score.buildin.hardsoftlong.HardSoftLongScore;
import org.optaplanner.core.api.score.buildin.hardsoftlong.HardSoftLongScoreHolder;
import org.optaplanner.core.api.score.buildin.simple.SimpleScore;
import org.optaplanner.core.api.score.buildin.simple.SimpleScoreHolder;
import org.optaplanner.core.api.score.buildin.simplebigdecimal.SimpleBigDecimalScore;
import org.optaplanner.core.api.score.buildin.simplebigdecimal.SimpleBigDecimalScoreHolder;
import org.optaplanner.core.api.score.buildin.simpledouble.SimpleDoubleScore;
import org.optaplanner.core.api.score.buildin.simpledouble.SimpleDoubleScoreHolder;
import org.optaplanner.core.api.score.buildin.simplelong.SimpleLongScore;
import org.optaplanner.core.api.score.buildin.simplelong.SimpleLongScoreHolder;

@ApplicationScoped
public class ScoreHolderUtils {

    public ScoreHolderUtils() {
    }

    public String extractScoreTypeFqn(final DataObject dataObject) {
        if (dataObject.getAnnotation(PlanningSolution.class.getName()) != null) {
            final ObjectProperty scoreObjectProperty = dataObject.getProperty("score");
            if (scoreObjectProperty != null) {
                return scoreObjectProperty.getClassName();
            }
        }
        return null;
    }

    public String getScoreHolderTypeFqn(final String scoreTypeFqn) {
        if (BendableScore.class.getName().equals(scoreTypeFqn)) {
            return BendableScoreHolder.class.getName();
        } else if (BendableBigDecimalScore.class.getName().equals(scoreTypeFqn)) {
            return BendableBigDecimalScoreHolder.class.getName();
        } else if (BendableLongScore.class.getName().equals(scoreTypeFqn)) {
            return BendableLongScoreHolder.class.getName();
        } else if (HardMediumSoftScore.class.getName().equals(scoreTypeFqn)) {
            return HardMediumSoftScoreHolder.class.getName();
        } else if (HardMediumSoftBigDecimalScore.class.getName().equals(scoreTypeFqn)) {
            return HardMediumSoftBigDecimalScoreHolder.class.getName();
        } else if (HardMediumSoftLongScore.class.getName().equals(scoreTypeFqn)) {
            return HardMediumSoftLongScoreHolder.class.getName();
        } else if (HardSoftScore.class.getName().equals(scoreTypeFqn)) {
            return HardSoftScoreHolder.class.getName();
        } else if (HardSoftBigDecimalScore.class.getName().equals(scoreTypeFqn)) {
            return HardSoftBigDecimalScoreHolder.class.getName();
        } else if (HardSoftDoubleScore.class.getName().equals(scoreTypeFqn)) {
            return HardSoftDoubleScoreHolder.class.getName();
        } else if (HardSoftLongScore.class.getName().equals(scoreTypeFqn)) {
            return HardSoftLongScoreHolder.class.getName();
        } else if (SimpleScore.class.getName().equals(scoreTypeFqn)) {
            return SimpleScoreHolder.class.getName();
        } else if (SimpleBigDecimalScore.class.getName().equals(scoreTypeFqn)) {
            return SimpleBigDecimalScoreHolder.class.getName();
        } else if (SimpleDoubleScore.class.getName().equals(scoreTypeFqn)) {
            return SimpleDoubleScoreHolder.class.getName();
        } else if (SimpleLongScore.class.getName().equals(scoreTypeFqn)) {
            return SimpleLongScoreHolder.class.getName();
        }
        return null;
    }
}
