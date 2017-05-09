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

package org.optaplanner.workbench.screens.guidedrule.client.plugin;

import org.junit.Test;
import org.optaplanner.core.api.score.buildin.bendable.BendableScoreHolder;
import org.optaplanner.core.api.score.buildin.bendablebigdecimal.BendableBigDecimalScoreHolder;
import org.optaplanner.core.api.score.buildin.bendablelong.BendableLongScoreHolder;
import org.optaplanner.core.api.score.buildin.hardmediumsoft.HardMediumSoftScoreHolder;
import org.optaplanner.core.api.score.buildin.hardmediumsoftbigdecimal.HardMediumSoftBigDecimalScoreHolder;
import org.optaplanner.core.api.score.buildin.hardmediumsoftlong.HardMediumSoftLongScoreHolder;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScoreHolder;
import org.optaplanner.core.api.score.buildin.hardsoftbigdecimal.HardSoftBigDecimalScoreHolder;
import org.optaplanner.core.api.score.buildin.hardsoftdouble.HardSoftDoubleScoreHolder;
import org.optaplanner.core.api.score.buildin.hardsoftlong.HardSoftLongScoreHolder;
import org.optaplanner.core.api.score.buildin.simple.SimpleScoreHolder;
import org.optaplanner.core.api.score.buildin.simplebigdecimal.SimpleBigDecimalScoreHolder;
import org.optaplanner.core.api.score.buildin.simpledouble.SimpleDoubleScoreHolder;
import org.optaplanner.core.api.score.buildin.simplelong.SimpleLongScoreHolder;

import static org.junit.Assert.*;

public class ConstraintMatchRuleModellerActionPluginTest {

    @Test
    public void bendableHardConstraintMatchPluginScoreHolderTypes() {
        BendableHardConstraintMatchRuleModellerActionPlugin plugin = new BendableHardConstraintMatchRuleModellerActionPlugin();
        assertTrue(plugin.SUPPORTED_SCORE_HOLDER_TYPES.contains(BendableScoreHolder.class.getName()));
        assertTrue(plugin.SUPPORTED_SCORE_HOLDER_TYPES.contains(BendableLongScoreHolder.class.getName()));
        assertTrue(plugin.SUPPORTED_SCORE_HOLDER_TYPES.contains(BendableBigDecimalScoreHolder.class.getName()));
    }

    @Test
    public void bendableSoftConstraintMatchPluginScoreHolderTypes() {
        BendableSoftConstraintMatchRuleModellerActionPlugin plugin = new BendableSoftConstraintMatchRuleModellerActionPlugin();
        assertTrue(plugin.SUPPORTED_SCORE_HOLDER_TYPES.contains(BendableScoreHolder.class.getName()));
        assertTrue(plugin.SUPPORTED_SCORE_HOLDER_TYPES.contains(BendableLongScoreHolder.class.getName()));
        assertTrue(plugin.SUPPORTED_SCORE_HOLDER_TYPES.contains(BendableBigDecimalScoreHolder.class.getName()));
    }

    @Test
    public void hardConstraintMatchPluginScoreHolderTypes() {
        HardConstraintMatchRuleModellerActionPlugin plugin = new HardConstraintMatchRuleModellerActionPlugin();
        assertTrue(plugin.SUPPORTED_SCORE_HOLDER_TYPES.contains(HardMediumSoftScoreHolder.class.getName()));
        assertTrue(plugin.SUPPORTED_SCORE_HOLDER_TYPES.contains(HardMediumSoftBigDecimalScoreHolder.class.getName()));
        assertTrue(plugin.SUPPORTED_SCORE_HOLDER_TYPES.contains(HardMediumSoftLongScoreHolder.class.getName()));
        assertTrue(plugin.SUPPORTED_SCORE_HOLDER_TYPES.contains(HardSoftScoreHolder.class.getName()));
        assertTrue(plugin.SUPPORTED_SCORE_HOLDER_TYPES.contains(HardSoftBigDecimalScoreHolder.class.getName()));
        assertTrue(plugin.SUPPORTED_SCORE_HOLDER_TYPES.contains(HardSoftDoubleScoreHolder.class.getName()));
        assertTrue(plugin.SUPPORTED_SCORE_HOLDER_TYPES.contains(HardSoftLongScoreHolder.class.getName()));
    }

    @Test
    public void mediumConstraintMatchPluginScoreHolderTypes() {
        MediumConstraintMatchRuleModellerActionPlugin plugin = new MediumConstraintMatchRuleModellerActionPlugin();
        assertTrue(plugin.SUPPORTED_SCORE_HOLDER_TYPES.contains(HardMediumSoftScoreHolder.class.getName()));
        assertTrue(plugin.SUPPORTED_SCORE_HOLDER_TYPES.contains(HardMediumSoftBigDecimalScoreHolder.class.getName()));
        assertTrue(plugin.SUPPORTED_SCORE_HOLDER_TYPES.contains(HardMediumSoftLongScoreHolder.class.getName()));
    }

    @Test
    public void softConstraintMatchPluginScoreHolderTypes() {
        SoftConstraintMatchRuleModellerActionPlugin plugin = new SoftConstraintMatchRuleModellerActionPlugin();
        assertTrue(plugin.SUPPORTED_SCORE_HOLDER_TYPES.contains(HardMediumSoftScoreHolder.class.getName()));
        assertTrue(plugin.SUPPORTED_SCORE_HOLDER_TYPES.contains(HardMediumSoftBigDecimalScoreHolder.class.getName()));
        assertTrue(plugin.SUPPORTED_SCORE_HOLDER_TYPES.contains(HardMediumSoftLongScoreHolder.class.getName()));
        assertTrue(plugin.SUPPORTED_SCORE_HOLDER_TYPES.contains(HardSoftScoreHolder.class.getName()));
        assertTrue(plugin.SUPPORTED_SCORE_HOLDER_TYPES.contains(HardSoftBigDecimalScoreHolder.class.getName()));
        assertTrue(plugin.SUPPORTED_SCORE_HOLDER_TYPES.contains(HardSoftDoubleScoreHolder.class.getName()));
        assertTrue(plugin.SUPPORTED_SCORE_HOLDER_TYPES.contains(HardSoftLongScoreHolder.class.getName()));
    }

    @Test
    public void simpleConstraintMatchPluginScoreHolderTypes() {
        SimpleConstraintMatchRuleModellerActionPlugin plugin = new SimpleConstraintMatchRuleModellerActionPlugin();
        assertTrue(plugin.SUPPORTED_SCORE_HOLDER_TYPES.contains(SimpleScoreHolder.class.getName()));
        assertTrue(plugin.SUPPORTED_SCORE_HOLDER_TYPES.contains(SimpleBigDecimalScoreHolder.class.getName()));
        assertTrue(plugin.SUPPORTED_SCORE_HOLDER_TYPES.contains(SimpleDoubleScoreHolder.class.getName()));
        assertTrue(plugin.SUPPORTED_SCORE_HOLDER_TYPES.contains(SimpleLongScoreHolder.class.getName()));
    }

    @Test
    public void multiConstraintBendableBigDecimalMatchScoreHolderTypes() {
        MultiConstraintBendableBigDecimalMatchRuleModellerActionPlugin plugin = new MultiConstraintBendableBigDecimalMatchRuleModellerActionPlugin();
        assertTrue(plugin.SUPPORTED_SCORE_HOLDER_TYPES.contains(BendableBigDecimalScoreHolder.class.getName()));
    }

    @Test
    public void multiConstraintBendableLongMatchScoreHolderTypes() {
        MultiConstraintBendableLongMatchRuleModellerActionPlugin plugin = new MultiConstraintBendableLongMatchRuleModellerActionPlugin();
        assertTrue(plugin.SUPPORTED_SCORE_HOLDER_TYPES.contains(BendableLongScoreHolder.class.getName()));
    }

    @Test
    public void multiConstraintBendableMatchScoreHolderTypes() {
        MultiConstraintBendableMatchRuleModellerActionPlugin plugin = new MultiConstraintBendableMatchRuleModellerActionPlugin();
        assertTrue(plugin.SUPPORTED_SCORE_HOLDER_TYPES.contains(BendableScoreHolder.class.getName()));
    }

    @Test
    public void multiConstraintHardMediumSoftMatchScoreHolderTypes() {
        MultiConstraintHardMediumSoftMatchRuleModellerActionPlugin plugin = new MultiConstraintHardMediumSoftMatchRuleModellerActionPlugin();
        assertTrue(plugin.SUPPORTED_SCORE_HOLDER_TYPES.contains(HardMediumSoftScoreHolder.class.getName()));
        assertTrue(plugin.SUPPORTED_SCORE_HOLDER_TYPES.contains(HardMediumSoftBigDecimalScoreHolder.class.getName()));
        assertTrue(plugin.SUPPORTED_SCORE_HOLDER_TYPES.contains(HardMediumSoftLongScoreHolder.class.getName()));
    }

    @Test
    public void multiConstraintHardSoftMatchScoreHolderTypes() {
        MultiConstraintHardSoftMatchRuleModellerActionPlugin plugin = new MultiConstraintHardSoftMatchRuleModellerActionPlugin();
        assertTrue(plugin.SUPPORTED_SCORE_HOLDER_TYPES.contains(HardSoftScoreHolder.class.getName()));
        assertTrue(plugin.SUPPORTED_SCORE_HOLDER_TYPES.contains(HardSoftBigDecimalScoreHolder.class.getName()));
        assertTrue(plugin.SUPPORTED_SCORE_HOLDER_TYPES.contains(HardSoftDoubleScoreHolder.class.getName()));
        assertTrue(plugin.SUPPORTED_SCORE_HOLDER_TYPES.contains(HardSoftLongScoreHolder.class.getName()));
    }
}
