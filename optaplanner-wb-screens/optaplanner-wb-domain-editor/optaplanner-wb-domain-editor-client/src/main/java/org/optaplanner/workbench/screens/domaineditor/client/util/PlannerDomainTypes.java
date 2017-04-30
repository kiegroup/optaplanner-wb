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

import java.util.HashMap;
import java.util.Map;

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

    public static final Map<Class<? extends Score>, ScoreConfigurationHolder> SCORE_CONFIGURATION_MAP = new HashMap<>();

    static {
        SCORE_CONFIGURATION_MAP.put(BendableScore.class,
                                    ScoreConfigurationHolder.create("org.optaplanner.persistence.jaxb.api.score.buildin.bendable.BendableScoreJaxbXmlAdapter"));
        SCORE_CONFIGURATION_MAP.put(BendableBigDecimalScore.class,
                                    ScoreConfigurationHolder.create("org.optaplanner.persistence.jaxb.api.score.buildin.bendablebigdecimal.BendableBigDecimalScoreJaxbXmlAdapter"));
        SCORE_CONFIGURATION_MAP.put(BendableLongScore.class,
                                    ScoreConfigurationHolder.create("org.optaplanner.persistence.jaxb.api.score.buildin.bendablelong.BendableLongScoreJaxbXmlAdapter"));
        SCORE_CONFIGURATION_MAP.put(HardMediumSoftScore.class,
                                    ScoreConfigurationHolder.create("org.optaplanner.persistence.jaxb.api.score.buildin.hardmediumsoft.HardMediumSoftScoreJaxbXmlAdapter"));
        SCORE_CONFIGURATION_MAP.put(HardMediumSoftBigDecimalScore.class,
                                    ScoreConfigurationHolder.create("org.optaplanner.persistence.jaxb.api.score.buildin.hardmediumsoftbigdecimal.HardMediumSoftBigDecimalScoreJaxbXmlAdapter"));
        SCORE_CONFIGURATION_MAP.put(HardMediumSoftLongScore.class,
                                    ScoreConfigurationHolder.create("org.optaplanner.persistence.jaxb.api.score.buildin.hardmediumsoftlong.HardMediumSoftLongScoreJaxbXmlAdapter"));
        SCORE_CONFIGURATION_MAP.put(HardSoftScore.class,
                                    ScoreConfigurationHolder.create("org.optaplanner.persistence.jaxb.api.score.buildin.hardsoft.HardSoftScoreJaxbXmlAdapter"));
        SCORE_CONFIGURATION_MAP.put(HardSoftBigDecimalScore.class,
                                    ScoreConfigurationHolder.create("org.optaplanner.persistence.jaxb.api.score.buildin.hardsoftbigdecimal.HardSoftBigDecimalScoreJaxbXmlAdapter"));
        SCORE_CONFIGURATION_MAP.put(HardSoftDoubleScore.class,
                                    ScoreConfigurationHolder.create("org.optaplanner.persistence.jaxb.api.score.buildin.hardsoftdouble.HardSoftDoubleScoreJaxbXmlAdapter"));
        SCORE_CONFIGURATION_MAP.put(HardSoftLongScore.class,
                                    ScoreConfigurationHolder.create("org.optaplanner.persistence.jaxb.api.score.buildin.hardsoftlong.HardSoftLongScoreJaxbXmlAdapter"));
        SCORE_CONFIGURATION_MAP.put(SimpleScore.class,
                                    ScoreConfigurationHolder.create("org.optaplanner.persistence.jaxb.api.score.buildin.simple.SimpleScoreJaxbXmlAdapter"));
        SCORE_CONFIGURATION_MAP.put(SimpleBigDecimalScore.class,
                                    ScoreConfigurationHolder.create("org.optaplanner.persistence.jaxb.api.score.buildin.simplebigdecimal.SimpleBigDecimalScoreJaxbXmlAdapter"));
        SCORE_CONFIGURATION_MAP.put(SimpleDoubleScore.class,
                                    ScoreConfigurationHolder.create("org.optaplanner.persistence.jaxb.api.score.buildin.simpledouble.SimpleDoubleScoreJaxbXmlAdapter"));
        SCORE_CONFIGURATION_MAP.put(SimpleLongScore.class,
                                    ScoreConfigurationHolder.create("org.optaplanner.persistence.jaxb.api.score.buildin.simplelong.SimpleLongScoreJaxbXmlAdapter"));
    }

    public static class ScoreConfigurationHolder {

        private String jaxbXmlAdapterClass;

        private ScoreConfigurationHolder(final String jaxbXmlAdapterClass) {
            this.jaxbXmlAdapterClass = jaxbXmlAdapterClass;
        }

        public static ScoreConfigurationHolder create(final String jaxbXmlAdapterClass) {
            return new ScoreConfigurationHolder(jaxbXmlAdapterClass);
        }

        public String getJaxbXmlAdapterClass() {
            return jaxbXmlAdapterClass;
        }
    }
}
