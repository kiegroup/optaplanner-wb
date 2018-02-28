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

package org.optaplanner.workbench.screens.guidedrule.backend.server.plugin;

import java.util.Arrays;

import org.drools.workbench.models.commons.backend.rule.exception.RuleModelDRLPersistenceException;
import org.drools.workbench.models.datamodel.rule.IAction;
import org.junit.Test;
import org.optaplanner.workbench.models.datamodel.rule.ActionBendableHardConstraintMatch;
import org.optaplanner.workbench.models.datamodel.rule.ActionBendableSoftConstraintMatch;
import org.optaplanner.workbench.models.datamodel.rule.ActionMultiConstraintBendableBigDecimalMatch;
import org.optaplanner.workbench.models.datamodel.rule.ActionMultiConstraintBendableLongMatch;
import org.optaplanner.workbench.models.datamodel.rule.ActionMultiConstraintBendableMatch;
import org.optaplanner.workbench.models.datamodel.rule.ActionMultiConstraintHardMediumSoftMatch;
import org.optaplanner.workbench.models.datamodel.rule.ActionMultiConstraintHardSoftMatch;

import static org.junit.Assert.*;

public class MultiConstraintMatchPersistenceExtensionTest {

    private MultiConstraintHardSoftMatchPersistenceExtension extension = new MultiConstraintHardSoftMatchPersistenceExtension();

    @Test
    public void acceptString() {
        assertTrue(extension.accept("scoreHolder.addMultiConstraintMatch(kcontext, -1, -2);"));
        assertTrue(extension.accept("scoreHolder.addMultiConstraintMatch(kcontext, -1, -2, -3);"));
        assertTrue(extension.accept("scoreHolder.addMultiConstraintMatch(kcontext, new int[] {-1, -2}, new int[] {-3, -4});"));
        assertTrue(extension.accept("scoreHolder.addMultiConstraintMatch(kcontext, new long[] {-1l, -2l}, new long[] {-3l, -4l});"));
        assertTrue(extension.accept("scoreHolder.addMultiConstraintMatch(kcontext, new java.math.BigDecimal[] {new java.math.BigDecimal(-1), new java.math.BigDecimal(-2)}, new java.math.BigDecimal[] {new java.math.BigDecimal(-3), new java.math.BigDecimal(-4)});"));

        assertFalse(extension.accept("unknownString"));
    }

    @Test
    public void unmarshalActionMultiConstraintHardSoftMatch() throws RuleModelDRLPersistenceException {
        String actionString = "scoreHolder.addMultiConstraintMatch(kcontext, -1, -2);";

        IAction action = extension.unmarshal(actionString);

        assertTrue(action instanceof ActionMultiConstraintHardSoftMatch);

        ActionMultiConstraintHardSoftMatch actionActionMultiConstraintHardSoftMatch = (ActionMultiConstraintHardSoftMatch) action;

        assertEquals("-1",
                     actionActionMultiConstraintHardSoftMatch.getActionHardConstraintMatch().getConstraintMatch());
        assertEquals("-2",
                     actionActionMultiConstraintHardSoftMatch.getActionSoftConstraintMatch().getConstraintMatch());
    }

    @Test
    public void unmarshalActionMultiConstraintHardMediumSoftMatch() throws RuleModelDRLPersistenceException {
        String actionString = "scoreHolder.addMultiConstraintMatch(kcontext, -1, -2, -3);";

        IAction action = extension.unmarshal(actionString);

        assertTrue(action instanceof ActionMultiConstraintHardMediumSoftMatch);

        ActionMultiConstraintHardMediumSoftMatch actionActionMultiConstraintHardMediumSoftMatch = (ActionMultiConstraintHardMediumSoftMatch) action;

        assertEquals("-1",
                     actionActionMultiConstraintHardMediumSoftMatch.getActionHardConstraintMatch().getConstraintMatch());
        assertEquals("-2",
                     actionActionMultiConstraintHardMediumSoftMatch.getActionMediumConstraintMatch().getConstraintMatch());
        assertEquals("-3",
                     actionActionMultiConstraintHardMediumSoftMatch.getActionSoftConstraintMatch().getConstraintMatch());
    }

    @Test
    public void unmarshalActionMultiConstraintBendableMatch() throws RuleModelDRLPersistenceException {
        String actionString = "scoreHolder.addMultiConstraintMatch(kcontext, new int[] {-1, -2}, new int[] {-3, -4});";

        IAction action = extension.unmarshal(actionString);

        ActionMultiConstraintBendableMatch expectedAction = new ActionMultiConstraintBendableMatch(Arrays.asList(new ActionBendableHardConstraintMatch(0,
                                                                                                                                                       "-1"),
                                                                                                                 new ActionBendableHardConstraintMatch(1,
                                                                                                                                                       "-2")),
                                                                                                   Arrays.asList(new ActionBendableSoftConstraintMatch(0,
                                                                                                                                                       "-3"),
                                                                                                                 new ActionBendableSoftConstraintMatch(1,
                                                                                                                                                       "-4")));

        assertEquals(expectedAction,
                     action);
    }

    @Test
    public void unmarshalActionMultiConstraintBendableLongMatch() throws RuleModelDRLPersistenceException {
        String actionString = "scoreHolder.addMultiConstraintMatch(kcontext, new long[] {-1l, -2l}, new long[] {-3l, -4l});";

        IAction action = extension.unmarshal(actionString);

        ActionMultiConstraintBendableLongMatch expectedAction = new ActionMultiConstraintBendableLongMatch(Arrays.asList(new ActionBendableHardConstraintMatch(0,
                                                                                                                                                               "-1l"),
                                                                                                                         new ActionBendableHardConstraintMatch(1,
                                                                                                                                                               "-2l")),
                                                                                                           Arrays.asList(new ActionBendableSoftConstraintMatch(0,
                                                                                                                                                               "-3l"),
                                                                                                                         new ActionBendableSoftConstraintMatch(1,
                                                                                                                                                               "-4l")));

        assertEquals(expectedAction,
                     action);
    }

    @Test
    public void unmarshalActionMultiConstraintBendableBigDecimalMatch() throws RuleModelDRLPersistenceException {
        String actionString = "scoreHolder.addMultiConstraintMatch(kcontext, new java.math.BigDecimal[] {new java.math.BigDecimal(-1), new java.math.BigDecimal(-2)}, new java.math.BigDecimal[] {new java.math.BigDecimal(-3), new java.math.BigDecimal(-4)});";

        IAction action = extension.unmarshal(actionString);

        ActionMultiConstraintBendableBigDecimalMatch expectedAction = new ActionMultiConstraintBendableBigDecimalMatch(Arrays.asList(new ActionBendableHardConstraintMatch(0,
                                                                                                                                                                           "new java.math.BigDecimal(-1)"),
                                                                                                                                     new ActionBendableHardConstraintMatch(1,
                                                                                                                                                                           "new java.math.BigDecimal(-2)")),
                                                                                                                       Arrays.asList(new ActionBendableSoftConstraintMatch(0,
                                                                                                                                                                           "new java.math.BigDecimal(-3)"),
                                                                                                                                     new ActionBendableSoftConstraintMatch(1,
                                                                                                                                                                           "new java.math.BigDecimal(-4)")));

        assertEquals(expectedAction,
                     action);
    }

    @Test(expected = RuleModelDRLPersistenceException.class)
    public void unmarshalUnrecognizedString() throws RuleModelDRLPersistenceException {
        extension.unmarshal("unrecognizedString");
    }

    @Test(expected = RuleModelDRLPersistenceException.class)
    public void unmarshalTooManyArguments() throws RuleModelDRLPersistenceException {
        extension.unmarshal("scoreHolder.addMultiConstraintMatch(kcontext, -1, -2, -3, 123);");
    }

    @Test(expected = RuleModelDRLPersistenceException.class)
    public void unmarshalNotEnoughArguments() throws RuleModelDRLPersistenceException {
        extension.unmarshal("scoreHolder.addMultiConstraintMatch(kcontext, -1);");
    }
}
