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

import java.util.regex.Pattern;
import javax.enterprise.context.ApplicationScoped;

import org.drools.workbench.models.commons.backend.rule.RuleModelIActionPersistenceExtension;
import org.drools.workbench.models.datamodel.rule.FreeFormLine;
import org.drools.workbench.models.datamodel.rule.IAction;
import org.optaplanner.workbench.screens.guidedrule.model.ActionBendableSoftConstraintMatch;
import org.optaplanner.workbench.screens.guidedrule.model.ActionSoftConstraintMatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class SoftConstraintMatchPersistenceExtension implements RuleModelIActionPersistenceExtension {

    private static final Logger LOGGER = LoggerFactory.getLogger(SoftConstraintMatchPersistenceExtension.class);

    private static final Pattern CONSTRAINT_MATCH_PATTERN = Pattern.compile("scoreHolder\\.addSoftConstraintMatch\\(\\s*kcontext\\s*,.+\\);");

    @Override
    public boolean accept(final IAction iAction) {
        return iAction instanceof ActionSoftConstraintMatch || iAction instanceof ActionBendableSoftConstraintMatch;
    }

    @Override
    public String marshal(final IAction iAction) {
        if (iAction instanceof ActionSoftConstraintMatch) {
            ActionSoftConstraintMatch actionConstraintMatch = (ActionSoftConstraintMatch) iAction;
            return String.format("scoreHolder.addSoftConstraintMatch(kcontext, %s);",
                                 actionConstraintMatch.getConstraintMatch());
        } else if (iAction instanceof ActionBendableSoftConstraintMatch) {
            ActionBendableSoftConstraintMatch actionConstraintMatch = (ActionBendableSoftConstraintMatch) iAction;
            return String.format("scoreHolder.addSoftConstraintMatch(kcontext, %s, %s);",
                                 actionConstraintMatch.getPosition(),
                                 actionConstraintMatch.getConstraintMatch());
        }
        throw new IllegalArgumentException("Action " + iAction + " is not supported by this extension");
    }

    @Override
    public boolean accept(final String iActionString) {
        return CONSTRAINT_MATCH_PATTERN.matcher(iActionString).matches();
    }

    @Override
    public IAction unmarshal(final String iActionString) {
        String[] parameters = PersistenceExtensionUtils.unwrapParenthesis(iActionString).split("\\s*,\\s*");

        if ("kcontext".equals(parameters[0])) {
            if (parameters.length == 2) {
                return new ActionSoftConstraintMatch(PersistenceExtensionUtils.extractConstraintMatchValue(parameters[1]));
            }
            if (parameters.length == 3) {
                try {
                    int bendableScoreLevel = Integer.parseInt(parameters[1]);

                    return new ActionBendableSoftConstraintMatch(bendableScoreLevel,
                                                                 PersistenceExtensionUtils.extractConstraintMatchValue(parameters[2]));
                } catch (NumberFormatException e) {
                    LOGGER.debug("Could not parse bendable score level parameter " + parameters[1] + " as an Integer, returning a FreeFormLine");
                }
            }
        }

        // Line can't be parsed as an ActionSoftConstraintMatch, return a FreeFormLine
        FreeFormLine freeFormLine = new FreeFormLine();
        freeFormLine.setText(iActionString);

        return freeFormLine;
    }
}
