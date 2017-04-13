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
import org.jboss.errai.common.client.api.Caller;
import org.jboss.errai.ui.client.local.spi.TranslationService;
import org.optaplanner.workbench.screens.guidedrule.client.resources.i18n.GuidedRuleEditorConstants;
import org.optaplanner.workbench.screens.guidedrule.client.widget.BendableConstraintMatchRuleModellerWidget;
import org.optaplanner.workbench.screens.guidedrule.model.ActionBendableSoftConstraintMatch;
import org.optaplanner.workbench.screens.guidedrule.model.BendableScoreLevelsWrapper;
import org.optaplanner.workbench.screens.guidedrule.service.ScoreHolderService;
import org.uberfire.mvp.Command;

@ApplicationScoped
public class BendableSoftConstraintMatchRuleModellerActionPlugin implements RuleModellerActionPlugin {

    private static final Set<String> SUPPORTED_SCORE_HOLDER_TYPES;

    static {
        SUPPORTED_SCORE_HOLDER_TYPES = new HashSet<>(3);
        SUPPORTED_SCORE_HOLDER_TYPES.add("org.optaplanner.core.api.score.buildin.bendable.BendableScoreHolder");
        SUPPORTED_SCORE_HOLDER_TYPES.add("org.optaplanner.core.api.score.buildin.bendablelong.BendableLongScoreHolder");
        SUPPORTED_SCORE_HOLDER_TYPES.add("org.optaplanner.core.api.score.buildin.bendablebigdecimal.BendableBigDecimalScoreHolder");
    }

    @Inject
    private Caller<ScoreHolderService> scoreHolderService;

    @Inject
    private ActionPluginClientService actionPluginClientService;

    @Inject
    private TranslationService translationService;

    public BendableSoftConstraintMatchRuleModellerActionPlugin() {
    }

    @Override
    public boolean accept(final IAction iAction) {
        return iAction instanceof ActionBendableSoftConstraintMatch;
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
        return new ActionBendableSoftConstraintMatch();
    }

    @Override
    public String getId() {
        return "BENDABLE_SOFT_CONSTRAINT_MATCH";
    }

    @Override
    public String getActionAddDescription() {
        return translationService.getTranslation(GuidedRuleEditorConstants.RuleModellerActionPluginModifySoftScore);
    }

    @Override
    public RuleModellerWidget createWidget(final RuleModeller ruleModeller,
                                           final EventBus eventBus,
                                           final IAction iAction,
                                           final Boolean readOnly) {
        BendableConstraintMatchRuleModellerWidget widget = new BendableConstraintMatchRuleModellerWidget(ruleModeller,
                                                                                                         eventBus,
                                                                                                         (ActionBendableSoftConstraintMatch) iAction,
                                                                                                         translationService,
                                                                                                         readOnly,
                                                                                                         GuidedRuleEditorConstants.RuleModellerActionPluginSoftScore);
        actionPluginClientService.initScoreHolderAwarePlugin(ruleModeller,
                                                             widget,
                                                             SUPPORTED_SCORE_HOLDER_TYPES);

        actionPluginClientService.invokeScoreInformationCachedOperation(ruleModeller,
                                                                        scoreInformation -> {
                                                                            Collection<BendableScoreLevelsWrapper> scoreLevels = scoreInformation.getBendableScoreLevelsWrappers();
                                                                            if (scoreLevels.size() == 1) {
                                                                                widget.setScoreLevels(scoreLevels.iterator().next().getSoftScoreLevels());
                                                                            }
                                                                        });

        return widget;
    }
}
