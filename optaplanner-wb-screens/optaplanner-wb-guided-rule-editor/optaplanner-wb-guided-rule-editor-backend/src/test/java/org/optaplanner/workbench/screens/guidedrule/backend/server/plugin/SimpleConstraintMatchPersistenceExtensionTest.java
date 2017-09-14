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
import org.optaplanner.workbench.screens.guidedrule.model.ActionSimpleConstraintMatch;

import static org.junit.Assert.*;

public class SimpleConstraintMatchPersistenceExtensionTest {

    private SimpleConstraintMatchPersistenceExtension extension = new SimpleConstraintMatchPersistenceExtension();

    @Test
    public void acceptString() {
        assertTrue(extension.accept("scoreHolder.addConstraintMatch(kcontext, -1);"));

        assertFalse(extension.accept("unknownString"));
    }

    @Test
    public void unmarshalSimpleConstraintMatch() throws RuleModelDRLPersistenceException {
        String actionString = "scoreHolder.addConstraintMatch(kcontext, -1);";

        IAction action = extension.unmarshal(actionString);

        assertTrue(action instanceof ActionSimpleConstraintMatch);

        ActionSimpleConstraintMatch actionSimpleConstraintMatch = (ActionSimpleConstraintMatch) action;

        assertEquals("-1",
                     actionSimpleConstraintMatch.getConstraintMatch());
    }

    @Test(expected = RuleModelDRLPersistenceException.class)
    public void unmarshalUnrecognizedString() throws RuleModelDRLPersistenceException {
        extension.unmarshal("unrecognizedString");
    }
}
