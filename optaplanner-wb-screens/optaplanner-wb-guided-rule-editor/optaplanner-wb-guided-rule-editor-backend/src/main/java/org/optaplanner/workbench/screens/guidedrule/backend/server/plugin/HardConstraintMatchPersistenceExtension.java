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

import java.util.List;
import java.util.regex.Pattern;
import javax.enterprise.context.ApplicationScoped;

import org.drools.core.util.StringUtils;
import org.drools.workbench.models.commons.backend.rule.RuleModelIActionPersistenceExtension;
import org.drools.workbench.models.datamodel.rule.FreeFormLine;
import org.drools.workbench.models.datamodel.rule.IAction;
import org.optaplanner.workbench.screens.guidedrule.model.ActionBendableHardConstraintMatch;
import org.optaplanner.workbench.screens.guidedrule.model.ActionHardConstraintMatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class HardConstraintMatchPersistenceExtension implements RuleModelIActionPersistenceExtension {

    private static final Logger LOGGER = LoggerFactory.getLogger(HardConstraintMatchPersistenceExtension.class);

    private static final Pattern CONSTRAINT_MATCH_PATTERN = Pattern.compile("scoreHolder\\.addHardConstraintMatch\\(\\s*kcontext\\s*,.+\\);");

    @Override
    public boolean accept(final IAction iAction) {
        return iAction instanceof ActionHardConstraintMatch || iAction instanceof ActionBendableHardConstraintMatch;
    }

    @Override
    public String marshal(final IAction iAction) {
        if (iAction instanceof ActionHardConstraintMatch) {
            ActionHardConstraintMatch actionConstraintMatch = (ActionHardConstraintMatch) iAction;
            return String.format("scoreHolder.addHardConstraintMatch(kcontext, %s);",
                                 actionConstraintMatch.getConstraintMatch());
        } else if (iAction instanceof ActionBendableHardConstraintMatch) {
            ActionBendableHardConstraintMatch actionConstraintMatch = (ActionBendableHardConstraintMatch) iAction;
            return String.format("scoreHolder.addHardConstraintMatch(kcontext, %s, %s);",
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
        List<String> parameters = StringUtils.splitArgumentsList(PersistenceExtensionUtils.unwrapParenthesis(iActionString));

        if (!parameters.isEmpty() && "kcontext".equals(parameters.get(0))) {
            if (parameters.size() == 2) {
                return new ActionHardConstraintMatch(PersistenceExtensionUtils.extractConstraintMatchValue(parameters.get(1)));
            }
            if (parameters.size() == 3) {
                try {
                    int bendableScoreLevel = Integer.parseInt(parameters.get(1));

                    return new ActionBendableHardConstraintMatch(bendableScoreLevel,
                                                                 PersistenceExtensionUtils.extractConstraintMatchValue(parameters.get(2)));
                } catch (NumberFormatException e) {
                    LOGGER.debug("Could not parse bendable score level parameter " + parameters.get(1) + " as an Integer, returning a FreeFormLine");
                }
            }
        }

        // Line can't be parsed as an ActionHardConstraintMatch, return a FreeFormLine
        FreeFormLine freeFormLine = new FreeFormLine();
        freeFormLine.setText(iActionString);

        return freeFormLine;
    }
}
