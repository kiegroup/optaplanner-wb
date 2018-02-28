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

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.enterprise.context.ApplicationScoped;

import org.drools.core.util.StringUtils;
import org.drools.workbench.models.commons.backend.rule.RuleModelIActionPersistenceExtension;
import org.drools.workbench.models.commons.backend.rule.exception.RuleModelDRLPersistenceException;
import org.drools.workbench.models.datamodel.rule.PluggableIAction;
import org.optaplanner.workbench.models.datamodel.rule.ActionBendableHardConstraintMatch;
import org.optaplanner.workbench.models.datamodel.rule.ActionBendableSoftConstraintMatch;
import org.optaplanner.workbench.models.datamodel.rule.ActionHardConstraintMatch;
import org.optaplanner.workbench.models.datamodel.rule.ActionMediumConstraintMatch;
import org.optaplanner.workbench.models.datamodel.rule.ActionMultiConstraintBendableBigDecimalMatch;
import org.optaplanner.workbench.models.datamodel.rule.ActionMultiConstraintBendableLongMatch;
import org.optaplanner.workbench.models.datamodel.rule.ActionMultiConstraintBendableMatch;
import org.optaplanner.workbench.models.datamodel.rule.ActionMultiConstraintHardMediumSoftMatch;
import org.optaplanner.workbench.models.datamodel.rule.ActionMultiConstraintHardSoftMatch;
import org.optaplanner.workbench.models.datamodel.rule.ActionSoftConstraintMatch;

import static org.optaplanner.workbench.screens.guidedrule.backend.server.plugin.PersistenceExtensionUtils.unwrapCurlyBrackets;

@ApplicationScoped
public class MultiConstraintHardSoftMatchPersistenceExtension implements RuleModelIActionPersistenceExtension {

    private static final Pattern CONSTRAINT_MATCH_PATTERN = Pattern.compile("scoreHolder\\.addMultiConstraintMatch\\(\\s*kcontext\\s*,.+\\);");

    private static final Pattern ARRAY_PATTERN = Pattern.compile("new\\s+\\b(int|long|BigDecimal|java\\.math\\.BigDecimal)\\b\\s*\\[\\s*\\]\\s*\\{.*\\}");

    @Override
    public boolean accept(final String iActionString) {
        return CONSTRAINT_MATCH_PATTERN.matcher(iActionString).matches();
    }

    @Override
    public PluggableIAction unmarshal(final String iActionString) throws RuleModelDRLPersistenceException {
        List<String> parameters = StringUtils.splitArgumentsList(PersistenceExtensionUtils.unwrapParenthesis(iActionString));

        if (!parameters.isEmpty() && "kcontext".equals(parameters.get(0))) {
            if (parameters.size() == 3) {
                boolean hardConstraintIsArray = false;
                String hardConstraintType = null;
                String hardConstraint = parameters.get(1);
                Matcher hardConstraintMatcher = ARRAY_PATTERN.matcher(hardConstraint);
                if (hardConstraintMatcher.matches()) {
                    hardConstraintIsArray = true;
                    hardConstraintType = hardConstraintMatcher.group(1);
                }

                boolean softConstraintIsArray = false;
                String softConstraintType = null;
                String softConstraint = parameters.get(2);
                Matcher softConstraintMatcher = ARRAY_PATTERN.matcher(softConstraint);
                if (softConstraintMatcher.matches()) {
                    softConstraintIsArray = true;
                    softConstraintType = softConstraintMatcher.group(1);
                }

                if (hardConstraintIsArray && softConstraintIsArray && hardConstraintType.equals(softConstraintType)) {
                    List<String> hardConstraints = StringUtils.splitArgumentsList(unwrapCurlyBrackets(hardConstraint));
                    List<ActionBendableHardConstraintMatch> bendableHardConstraintMatches = new ArrayList<>(hardConstraints.size());
                    for (int i = 0; i < hardConstraints.size(); i++) {
                        bendableHardConstraintMatches.add(new ActionBendableHardConstraintMatch(i,
                                                                                                PersistenceExtensionUtils.extractConstraintMatchValue(hardConstraints.get(i))));
                    }

                    List<String> softConstraints = StringUtils.splitArgumentsList(unwrapCurlyBrackets(softConstraint));
                    List<ActionBendableSoftConstraintMatch> bendableSoftConstraintMatches = new ArrayList<>(softConstraints.size());
                    for (int i = 0; i < softConstraints.size(); i++) {
                        bendableSoftConstraintMatches.add(new ActionBendableSoftConstraintMatch(i,
                                                                                                PersistenceExtensionUtils.extractConstraintMatchValue(softConstraints.get(i))));
                    }

                    switch (hardConstraintType) {
                        case "int":
                            return new ActionMultiConstraintBendableMatch(bendableHardConstraintMatches,
                                                                          bendableSoftConstraintMatches);
                        case "long":
                            return new ActionMultiConstraintBendableLongMatch(bendableHardConstraintMatches,
                                                                              bendableSoftConstraintMatches);
                        case "BigDecimal":
                        case "java.math.BigDecimal":
                            return new ActionMultiConstraintBendableBigDecimalMatch(bendableHardConstraintMatches,
                                                                                    bendableSoftConstraintMatches);
                    }
                } else if (!hardConstraintIsArray && !softConstraintIsArray) {
                    ActionHardConstraintMatch hardConstraintMatch = new ActionHardConstraintMatch(PersistenceExtensionUtils.extractConstraintMatchValue(parameters.get(1)));
                    ActionSoftConstraintMatch softConstraintMatch = new ActionSoftConstraintMatch(PersistenceExtensionUtils.extractConstraintMatchValue(parameters.get(2)));
                    return new ActionMultiConstraintHardSoftMatch(hardConstraintMatch,
                                                                  softConstraintMatch);
                }
            }
            if (parameters.size() == 4) {
                ActionHardConstraintMatch hardConstraintMatch = new ActionHardConstraintMatch(PersistenceExtensionUtils.extractConstraintMatchValue(parameters.get(1)));
                ActionMediumConstraintMatch mediumConstraintMatch = new ActionMediumConstraintMatch(PersistenceExtensionUtils.extractConstraintMatchValue(parameters.get(2)));
                ActionSoftConstraintMatch softConstraintMatch = new ActionSoftConstraintMatch(PersistenceExtensionUtils.extractConstraintMatchValue(parameters.get(3)));

                return new ActionMultiConstraintHardMediumSoftMatch(hardConstraintMatch,
                                                                    mediumConstraintMatch,
                                                                    softConstraintMatch);
            }
        }

        throw new RuleModelDRLPersistenceException("Could not unmarshal action string '" + iActionString);
    }


}
