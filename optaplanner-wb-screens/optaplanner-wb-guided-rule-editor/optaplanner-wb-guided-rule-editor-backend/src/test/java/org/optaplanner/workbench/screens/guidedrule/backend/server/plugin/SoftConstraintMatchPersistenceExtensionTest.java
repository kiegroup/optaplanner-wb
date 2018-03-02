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
import org.optaplanner.workbench.models.datamodel.rule.ActionBendableSoftConstraintMatch;
import org.optaplanner.workbench.models.datamodel.rule.ActionSoftConstraintMatch;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class SoftConstraintMatchPersistenceExtensionTest {

    private SoftConstraintMatchPersistenceExtension extension = new SoftConstraintMatchPersistenceExtension();

    @Test
    public void acceptString() {
        assertTrue(extension.accept("scoreHolder.addSoftConstraintMatch(kcontext, -1);"));
        assertTrue(extension.accept("scoreHolder.addSoftConstraintMatch(kcontext, 1, -1);"));

        assertFalse(extension.accept("unknownString"));
    }

    @Test
    public void unmarshalSoftConstraintMatch() throws RuleModelDRLPersistenceException {
        String actionString = "scoreHolder.addSoftConstraintMatch(kcontext, -1);";

        IAction action = extension.unmarshal(actionString);

        assertTrue(action instanceof ActionSoftConstraintMatch);

        ActionSoftConstraintMatch actionSoftConstraintMatch = (ActionSoftConstraintMatch) action;

        assertEquals("-1",
                     actionSoftConstraintMatch.getConstraintMatch());
    }

    @Test
    public void unmarshalSoftConstraintMatchNull() throws RuleModelDRLPersistenceException {
        String actionString = "scoreHolder.addSoftConstraintMatch(kcontext, null);";

        IAction action = extension.unmarshal(actionString);

        assertTrue(action instanceof ActionSoftConstraintMatch);

        ActionSoftConstraintMatch actionSoftConstraintMatch = (ActionSoftConstraintMatch) action;

        assertNull(actionSoftConstraintMatch.getConstraintMatch());
    }

    @Test
    public void unmarshalActionBendableSoftConstraintMatch() throws RuleModelDRLPersistenceException {
        String actionString = "scoreHolder.addSoftConstraintMatch(kcontext, 1, -1);";

        IAction action = extension.unmarshal(actionString);

        assertTrue(action instanceof ActionBendableSoftConstraintMatch);

        ActionBendableSoftConstraintMatch actionSoftConstraintMatch = (ActionBendableSoftConstraintMatch) action;

        assertEquals(1,
                     actionSoftConstraintMatch.getPosition());
        assertEquals("-1",
                     actionSoftConstraintMatch.getConstraintMatch());
    }

    @Test
    public void unmarshalUnrecognizedString() throws RuleModelDRLPersistenceException {
        final String actionText = "unrecognizedString";
        assertThatThrownBy(() -> extension.unmarshal(actionText))
                .isInstanceOf(RuleModelDRLPersistenceException.class)
                .hasMessageContaining(PersistenceExtensionUtils.EXCEPTION_MESSAGE_BASE)
                .hasMessageEndingWith(actionText);
    }

    @Test
    public void unmarshalTooManyArguments() throws RuleModelDRLPersistenceException {
        final String actionText = "scoreHolder.addSoftConstraintMatch(kcontext, 1, -1, 123);";
        assertThatThrownBy(() -> extension.unmarshal(actionText))
                .isInstanceOf(RuleModelDRLPersistenceException.class)
                .hasMessageContaining(PersistenceExtensionUtils.EXCEPTION_MESSAGE_BASE)
                .hasMessageEndingWith(actionText);
    }

    @Test
    public void unmarshalNotEnoughArguments() throws RuleModelDRLPersistenceException {
        final String actionText = "scoreHolder.addSoftConstraintMatch(kcontext);";
        assertThatThrownBy(() -> extension.unmarshal(actionText))
                .isInstanceOf(RuleModelDRLPersistenceException.class)
                .hasMessageContaining(PersistenceExtensionUtils.EXCEPTION_MESSAGE_BASE)
                .hasMessageEndingWith(actionText);
    }

    @Test
    public void unmarshalEmptyArguments() throws RuleModelDRLPersistenceException {
        final String actionText = "scoreHolder.addSoftConstraintMatch();";
        assertThatThrownBy(() -> extension.unmarshal(actionText))
                .isInstanceOf(RuleModelDRLPersistenceException.class)
                .hasMessageContaining(PersistenceExtensionUtils.EXCEPTION_MESSAGE_BASE)
                .hasMessageEndingWith(actionText);
    }

    @Test
    public void unmarshalWrongFirstArgument() throws RuleModelDRLPersistenceException {
        final String actionText = "scoreHolder.addSoftConstraintMatch(context, 1, -1);";
        assertThatThrownBy(() -> extension.unmarshal(actionText))
                .isInstanceOf(RuleModelDRLPersistenceException.class)
                .hasMessageContaining(PersistenceExtensionUtils.EXCEPTION_MESSAGE_BASE)
                .hasMessageEndingWith(actionText);
    }
}
