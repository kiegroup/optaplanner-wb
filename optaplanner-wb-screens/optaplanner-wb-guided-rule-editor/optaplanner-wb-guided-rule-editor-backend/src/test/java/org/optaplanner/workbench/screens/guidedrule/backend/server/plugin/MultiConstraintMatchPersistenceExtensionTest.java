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
import java.util.Collections;
import java.util.List;

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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class MultiConstraintMatchPersistenceExtensionTest {

    private MultiConstraintHardSoftMatchPersistenceExtension extension = new MultiConstraintHardSoftMatchPersistenceExtension();
    private TestUtils testUtils = new TestUtils(extension);

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
    public void unmarshalActionMultiConstraintHardSoftMatchNull() throws RuleModelDRLPersistenceException {
        String actionString = "scoreHolder.addMultiConstraintMatch(kcontext, null, null);";

        IAction action = extension.unmarshal(actionString);

        assertTrue(action instanceof ActionMultiConstraintHardSoftMatch);

        ActionMultiConstraintHardSoftMatch actionActionMultiConstraintHardSoftMatch = (ActionMultiConstraintHardSoftMatch) action;

        assertNull(actionActionMultiConstraintHardSoftMatch.getActionHardConstraintMatch().getConstraintMatch());
        assertNull(actionActionMultiConstraintHardSoftMatch.getActionSoftConstraintMatch().getConstraintMatch());
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
    public void unmarshalActionMultiConstraintBendableMatchDifferentLength() throws RuleModelDRLPersistenceException {
        final String actionString = "scoreHolder.addMultiConstraintMatch(kcontext, new int[] {-1}, new int[] {2, -3});";

        final IAction action = extension.unmarshal(actionString);

        final List<ActionBendableHardConstraintMatch> hardConstraintList =
                Arrays.asList(new ActionBendableHardConstraintMatch(0, "-1"));
        final List<ActionBendableSoftConstraintMatch> softConstraintList =
                Arrays.asList(new ActionBendableSoftConstraintMatch(0, "2"),
                              new ActionBendableSoftConstraintMatch(1, "-3"));

        final ActionMultiConstraintBendableMatch expectedAction =
                new ActionMultiConstraintBendableMatch(hardConstraintList, softConstraintList);

        assertEquals(expectedAction, action);
    }

    @Test
    public void unmarshalActionMultiConstraintBendableMatchEmpty() throws RuleModelDRLPersistenceException {
        String actionString = "scoreHolder.addMultiConstraintMatch(kcontext, new int[] {}, new int[] {});";

        IAction action = extension.unmarshal(actionString);

        ActionMultiConstraintBendableMatch expectedAction = new ActionMultiConstraintBendableMatch(Collections.emptyList(),
                                                                                                   Collections.emptyList());

        assertEquals(expectedAction,
                     action);
    }

    @Test
    public void unmarshalActionMultiConstraintBendableMatch() throws RuleModelDRLPersistenceException {
        final String actionString = "scoreHolder.addMultiConstraintMatch(kcontext, new int[] {-1, -2}, new int[] {-3, -4});";

        final IAction action = extension.unmarshal(actionString);

        final List<ActionBendableHardConstraintMatch> hardConstraintList =
                Arrays.asList(new ActionBendableHardConstraintMatch(0, "-1"),
                              new ActionBendableHardConstraintMatch(1, "-2"));
        final List<ActionBendableSoftConstraintMatch> softConstraintList =
                Arrays.asList(new ActionBendableSoftConstraintMatch(0, "-3"),
                              new ActionBendableSoftConstraintMatch(1, "-4"));
        final ActionMultiConstraintBendableMatch expectedAction =
                new ActionMultiConstraintBendableMatch(hardConstraintList, softConstraintList);

        assertEquals(expectedAction, action);
    }

    @Test
    public void unmarshalActionMultiConstraintBendableLongMatch() throws RuleModelDRLPersistenceException {
        final String actionString = "scoreHolder.addMultiConstraintMatch(kcontext, new long[] {-1l, -2l}, new long[] {-3l, -4l});";

        final IAction action = extension.unmarshal(actionString);

        final List<ActionBendableHardConstraintMatch> hardConstraintList =
                Arrays.asList(new ActionBendableHardConstraintMatch(0, "-1l"),
                              new ActionBendableHardConstraintMatch(1, "-2l"));
        final List<ActionBendableSoftConstraintMatch> softConstraintList =
                Arrays.asList(new ActionBendableSoftConstraintMatch(0, "-3l"),
                              new ActionBendableSoftConstraintMatch(1, "-4l"));
        final ActionMultiConstraintBendableLongMatch expectedAction =
                new ActionMultiConstraintBendableLongMatch(hardConstraintList, softConstraintList);

        assertEquals(expectedAction, action);
    }

    @Test
    public void unmarshalActionMultiConstraintBendableBigDecimalMatch() throws RuleModelDRLPersistenceException {
        final String hard = "new java.math.BigDecimal[] {new java.math.BigDecimal(-1), new java.math.BigDecimal(-2)}";
        final String soft = "new java.math.BigDecimal[] {new java.math.BigDecimal(-3), new java.math.BigDecimal(-4)}";
        final String actionString = "scoreHolder.addMultiConstraintMatch(kcontext, " + hard + ", " + soft + ");";

        final IAction action = extension.unmarshal(actionString);

        final List<ActionBendableHardConstraintMatch> hardConstraintList =
                Arrays.asList(new ActionBendableHardConstraintMatch(0, "new java.math.BigDecimal(-1)"),
                              new ActionBendableHardConstraintMatch(1, "new java.math.BigDecimal(-2)"));
        final List<ActionBendableSoftConstraintMatch> softConstraintList =
                Arrays.asList(new ActionBendableSoftConstraintMatch(0, "new java.math.BigDecimal(-3)"),
                              new ActionBendableSoftConstraintMatch(1, "new java.math.BigDecimal(-4)"));
        final ActionMultiConstraintBendableBigDecimalMatch expectedAction =
                new ActionMultiConstraintBendableBigDecimalMatch(hardConstraintList, softConstraintList);

        assertEquals(expectedAction, action);
    }

    @Test
    public void unmarshalActionMultiConstraintBendableBigDecimalMatch2() throws RuleModelDRLPersistenceException {
        final String hardConstraint = "new BigDecimal[] {new BigDecimal(-1), new BigDecimal(-2)}";
        final String softConstraint = "new BigDecimal[] {new BigDecimal(-3), new BigDecimal(-4)}";
        final String actionString = "scoreHolder.addMultiConstraintMatch(kcontext, " + hardConstraint + ", " + softConstraint + ");";

        final IAction action = extension.unmarshal(actionString);

        final List<ActionBendableHardConstraintMatch> hardConstraintList =
                Arrays.asList(new ActionBendableHardConstraintMatch(0, "new BigDecimal(-1)"),
                              new ActionBendableHardConstraintMatch(1, "new BigDecimal(-2)"));
        final List<ActionBendableSoftConstraintMatch> softConstraintList =
                Arrays.asList(new ActionBendableSoftConstraintMatch(0, "new BigDecimal(-3)"),
                              new ActionBendableSoftConstraintMatch(1, "new BigDecimal(-4)"));

        final ActionMultiConstraintBendableBigDecimalMatch expectedAction =
                new ActionMultiConstraintBendableBigDecimalMatch(hardConstraintList, softConstraintList);

        assertEquals(expectedAction, action);
    }

    @Test
    public void unmarshalActionMultiConstraintHardSoftMatchArrays() throws RuleModelDRLPersistenceException {
        final String hardConstraint = "new float[] {-1}";
        final String softConstraint = "new float[] {-2}";
        final String actionString = "scoreHolder.addMultiConstraintMatch(kcontext, " + hardConstraint + ", " + softConstraint + ");";

        final IAction action = extension.unmarshal(actionString);

        assertTrue(action instanceof ActionMultiConstraintHardSoftMatch);

        final ActionMultiConstraintHardSoftMatch actionActionMultiConstraintHardSoftMatch = (ActionMultiConstraintHardSoftMatch) action;

        assertEquals(hardConstraint,
                     actionActionMultiConstraintHardSoftMatch.getActionHardConstraintMatch().getConstraintMatch());
        assertEquals(softConstraint,
                     actionActionMultiConstraintHardSoftMatch.getActionSoftConstraintMatch().getConstraintMatch());
    }

    @Test
    public void unmarshalUnrecognizedString() throws RuleModelDRLPersistenceException {
        final String actionText = "unrecognizedString";
        testUtils.assertRuleModelDRLPersistenceExceptionWasThrown(actionText);
    }

    @Test
    public void unmarshalTooManyArguments() throws RuleModelDRLPersistenceException {
        final String actionText = "scoreHolder.addMultiConstraintMatch(kcontext, -1, -2, -3, 123);";
        testUtils.assertRuleModelDRLPersistenceExceptionWasThrown(actionText);
    }

    @Test
    public void unmarshalNotEnoughArguments() throws RuleModelDRLPersistenceException {
        final String actionText = "scoreHolder.addMultiConstraintMatch(kcontext, -1);";
        testUtils.assertRuleModelDRLPersistenceExceptionWasThrown(actionText);
    }

    @Test
    public void unmarshalNotEnoughArgumentsJustOne() throws RuleModelDRLPersistenceException {
        final String actionText = "scoreHolder.addMultiConstraintMatch(kcontext);";
        testUtils.assertRuleModelDRLPersistenceExceptionWasThrown(actionText);
    }

    @Test
    public void unmarshalMissingArguments() throws RuleModelDRLPersistenceException {
        final String actionText = "scoreHolder.addMultiConstraintMatch();";
        testUtils.assertRuleModelDRLPersistenceExceptionWasThrown(actionText);
    }

    @Test
    public void unmarshalEmptyArguments() throws RuleModelDRLPersistenceException {
        final String actionText = "scoreHolder.addMultiConstraintMatch( , , );";
        testUtils.assertRuleModelDRLPersistenceExceptionWasThrown(actionText);
    }

    @Test
    public void unmarshalWrongFirstArgument() throws RuleModelDRLPersistenceException {
        final String actionText = "scoreHolder.addMultiConstraintMatch(context, 1, -1);";
        testUtils.assertRuleModelDRLPersistenceExceptionWasThrown(actionText);
    }

    @Test
    public void unmarshalWrongFirstArgumentHardMediumSoft() throws RuleModelDRLPersistenceException {
        final String actionText = "scoreHolder.addMultiConstraintMatch(context, 1, -1, 1);";
        testUtils.assertRuleModelDRLPersistenceExceptionWasThrown(actionText);
    }

    @Test
    public void unmarshalWrongFirstArgumentArrays() throws RuleModelDRLPersistenceException {
        final String actionText = "scoreHolder.addMultiConstraintMatch(context, new long[] {-1}, new long[] {-2});";
        testUtils.assertRuleModelDRLPersistenceExceptionWasThrown(actionText);
    }

    @Test
    public void unmarshalDifferentArrays() throws RuleModelDRLPersistenceException {
        final String actionText = "scoreHolder.addMultiConstraintMatch(kcontext, new int[] {-1}, new long[] {-2});";
        testUtils.assertRuleModelDRLPersistenceExceptionWasThrown(actionText);
    }

    @Test
    public void unmarshalHardIsArraySoftIsNot() throws RuleModelDRLPersistenceException {
        final String actionText = "scoreHolder.addMultiConstraintMatch(kcontext, new int[] {-1}, -2);";
        testUtils.assertRuleModelDRLPersistenceExceptionWasThrown(actionText);
    }

    @Test
    public void unmarshalHardIsNotArraySoftIs() throws RuleModelDRLPersistenceException {
        final String actionText = "scoreHolder.addMultiConstraintMatch(kcontext, -1, new long[] {-2});";
        testUtils.assertRuleModelDRLPersistenceExceptionWasThrown(actionText);
    }
}
