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

package org.optaplanner.workbench.screens.guidedrule.client.plugin;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.google.gwt.event.shared.EventBus;
import org.drools.workbench.models.datamodel.rule.IAction;
import org.drools.workbench.screens.guided.rule.client.editor.RuleModeller;
import org.drools.workbench.screens.guided.rule.client.editor.plugin.RuleModellerActionPlugin;
import org.drools.workbench.screens.guided.rule.client.widget.RuleModellerWidget;
import org.jboss.errai.ui.client.local.spi.TranslationService;
import org.optaplanner.workbench.screens.guidedrule.client.resources.i18n.GuidedRuleEditorConstants;
import org.optaplanner.workbench.screens.guidedrule.client.widget.MultiConstraintBendableMatchRuleModellerWidget;
import org.optaplanner.workbench.models.datamodel.rule.ActionMultiConstraintBendableMatch;
import org.optaplanner.workbench.screens.guidedrule.model.BendableScoreLevelsWrapper;
import org.uberfire.mvp.Command;

@ApplicationScoped
public class MultiConstraintBendableMatchRuleModellerActionPlugin implements RuleModellerActionPlugin {

    static final Set<String> SUPPORTED_SCORE_HOLDER_TYPES;

    static {
        SUPPORTED_SCORE_HOLDER_TYPES = new HashSet<>(1);
        SUPPORTED_SCORE_HOLDER_TYPES.add("org.optaplanner.core.api.score.buildin.bendable.BendableScoreHolder");
    }

    @Inject
    private ActionPluginClientService actionPluginClientService;

    @Inject
    private TranslationService translationService;

    public MultiConstraintBendableMatchRuleModellerActionPlugin() {
    }

    @Override
    public boolean accept(final IAction iAction) {
        return iAction instanceof ActionMultiConstraintBendableMatch;
    }

    @Override
    public void addPluginToActionList(final RuleModeller ruleModeller,
                                      final Command addCommand) {
        actionPluginClientService.addPluginToActionList(ruleModeller,
                                                        addCommand,
                                                        SUPPORTED_SCORE_HOLDER_TYPES);
    }

    @Override
    public IAction createIAction(final RuleModeller ruleModeller) {
        ActionMultiConstraintBendableMatch constraintMatch = new ActionMultiConstraintBendableMatch();

        actionPluginClientService.initBendableScoreLevels(ruleModeller,
                                                          constraintMatch);

        return constraintMatch;
    }

    @Override
    public String getId() {
        return "MULTI_CONSTRAINT_BENDABLE_MATCH";
    }

    @Override
    public String getActionAddDescription() {
        return translationService.getTranslation(GuidedRuleEditorConstants.RuleModellerActionPlugin_ModifyMultipleScoreLevels);
    }

    @Override
    public RuleModellerWidget createWidget(final RuleModeller ruleModeller,
                                           final EventBus eventBus,
                                           final IAction iAction,
                                           final Boolean readOnly) {

        MultiConstraintBendableMatchRuleModellerWidget widget = new MultiConstraintBendableMatchRuleModellerWidget(ruleModeller,
                                                                                                                   eventBus,
                                                                                                                   (ActionMultiConstraintBendableMatch) iAction,
                                                                                                                   translationService,
                                                                                                                   readOnly);
        actionPluginClientService.initScoreHolderAwarePlugin(ruleModeller,
                                                             widget,
                                                             SUPPORTED_SCORE_HOLDER_TYPES);

        actionPluginClientService.invokeScoreInformationCachedOperation(ruleModeller,
                                                                        scoreInformation -> {
                                                                            Collection<BendableScoreLevelsWrapper> scoreLevels = scoreInformation.getBendableScoreLevelsWrappers();
                                                                            if (scoreLevels.size() == 1) {
                                                                                widget.setScoreLevels(scoreLevels.iterator().next());
                                                                            }
                                                                        });

        return widget;
    }
}
