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

import org.drools.workbench.models.commons.backend.rule.exception.RuleModelDRLPersistenceException;
import org.drools.workbench.models.datamodel.rule.IAction;
import org.junit.Test;
import org.optaplanner.workbench.models.datamodel.rule.ActionMediumConstraintMatch;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class MediumConstraintMatchPersistenceExtensionTest {

    private MediumConstraintMatchPersistenceExtension extension = new MediumConstraintMatchPersistenceExtension();
    private TestUtils testUtils = new TestUtils(extension);

    @Test
    public void acceptString() {
        assertTrue(extension.accept("scoreHolder.addMediumConstraintMatch(kcontext, -1);"));

        assertFalse(extension.accept("unknownString"));
    }

    @Test
    public void unmarshalMediumConstraintMatch() throws RuleModelDRLPersistenceException {
        String actionString = "scoreHolder.addMediumConstraintMatch(kcontext, -1);";

        IAction action = extension.unmarshal(actionString);

        assertTrue(action instanceof ActionMediumConstraintMatch);

        ActionMediumConstraintMatch actionMediumConstraintMatch = (ActionMediumConstraintMatch) action;

        assertEquals("-1",
                     actionMediumConstraintMatch.getConstraintMatch());
    }

    @Test
    public void unmarshalMediumConstraintMatchNull() throws RuleModelDRLPersistenceException {
        String actionString = "scoreHolder.addMediumConstraintMatch(kcontext, null);";

        IAction action = extension.unmarshal(actionString);

        assertTrue(action instanceof ActionMediumConstraintMatch);

        ActionMediumConstraintMatch actionMediumConstraintMatch = (ActionMediumConstraintMatch) action;

        assertNull(actionMediumConstraintMatch.getConstraintMatch());
    }

    @Test
    public void unmarshalUnrecognizedString() throws RuleModelDRLPersistenceException {
        final String actionText = "unrecognizedString";
        testUtils.assertRuleModelDRLPersistenceExceptionWasThrown(actionText);
    }

    @Test
    public void unmarshalTooManyArguments() throws RuleModelDRLPersistenceException {
        final String actionText = "scoreHolder.addMediumConstraintMatch(kcontext, -1, 123);";
        testUtils.assertRuleModelDRLPersistenceExceptionWasThrown(actionText);
    }

    @Test
    public void unmarshalArgumentsInvalid() throws RuleModelDRLPersistenceException {
        final String actionText = "scoreHolder.addMediumConstraintMatch(context, 123, 321);";
        testUtils.assertRuleModelDRLPersistenceExceptionWasThrown(actionText);
    }

    @Test
    public void unmarshalNotEnoughArguments() throws RuleModelDRLPersistenceException {
        final String actionText = "scoreHolder.addMediumConstraintMatch(kcontext);";
        testUtils.assertRuleModelDRLPersistenceExceptionWasThrown(actionText);
    }

    @Test
    public void unmarshalWrongFirstArgument() throws RuleModelDRLPersistenceException {
        final String actionText = "scoreHolder.addMediumConstraintMatch(context, 1);";
        testUtils.assertRuleModelDRLPersistenceExceptionWasThrown(actionText);
    }

    @Test
    public void unmarshalEmptyArguments() throws RuleModelDRLPersistenceException {
        final String actionText = "scoreHolder.addMediumConstraintMatch( , );";
        testUtils.assertRuleModelDRLPersistenceExceptionWasThrown(actionText);
    }
}
