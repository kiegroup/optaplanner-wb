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
import org.drools.workbench.models.commons.backend.rule.exception.RuleModelDRLPersistenceException;
import org.drools.workbench.models.datamodel.rule.PluggableIAction;
import org.optaplanner.workbench.models.datamodel.rule.ActionSimpleConstraintMatch;

@ApplicationScoped
public class SimpleConstraintMatchPersistenceExtension implements RuleModelIActionPersistenceExtension {

    private static final Pattern CONSTRAINT_MATCH_PATTERN = Pattern.compile("scoreHolder\\.addConstraintMatch\\(\\s*kcontext\\s*,.+\\);");

    @Override
    public boolean accept(final String iActionString) {
        return CONSTRAINT_MATCH_PATTERN.matcher(iActionString).matches();
    }

    @Override
    public PluggableIAction unmarshal(final String iActionString) throws RuleModelDRLPersistenceException {
        String[] parameters = PersistenceExtensionUtils.unwrapParenthesis(iActionString).split("\\s*,\\s*");

        if (parameters.length > 0 && "kcontext".equals(parameters[0])) {
            if (parameters.length == 2) {
                return new ActionSimpleConstraintMatch(PersistenceExtensionUtils.extractConstraintMatchValue(parameters[1]));
            }
        }

        throw new RuleModelDRLPersistenceException(PersistenceExtensionUtils.EXCEPTION_MESSAGE_BASE + iActionString);
    }
}
