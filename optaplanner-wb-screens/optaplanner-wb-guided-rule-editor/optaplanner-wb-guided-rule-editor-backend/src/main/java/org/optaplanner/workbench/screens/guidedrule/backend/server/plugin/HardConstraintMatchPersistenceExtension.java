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
import org.drools.workbench.models.commons.backend.rule.exception.RuleModelDRLPersistenceException;
import org.drools.workbench.models.datamodel.rule.PluggableIAction;
import org.optaplanner.workbench.models.datamodel.rule.ActionBendableHardConstraintMatch;
import org.optaplanner.workbench.models.datamodel.rule.ActionHardConstraintMatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class HardConstraintMatchPersistenceExtension implements RuleModelIActionPersistenceExtension {

    private static final Logger LOGGER = LoggerFactory.getLogger(HardConstraintMatchPersistenceExtension.class);

    private static final Pattern CONSTRAINT_MATCH_PATTERN = Pattern.compile("scoreHolder\\.addHardConstraintMatch\\(\\s*kcontext\\s*,.+\\);");

    @Override
    public boolean accept(final String iActionString) {
        return CONSTRAINT_MATCH_PATTERN.matcher(iActionString).matches();
    }

    @Override
    public PluggableIAction unmarshal(final String iActionString) throws RuleModelDRLPersistenceException {
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
                    LOGGER.error("Could not parse bendable score level parameter " + parameters.get(1) + " as an Integer");
                }
            }
        }

        throw new RuleModelDRLPersistenceException(PersistenceExtensionUtils.EXCEPTION_MESSAGE_BASE + iActionString);
    }
}
