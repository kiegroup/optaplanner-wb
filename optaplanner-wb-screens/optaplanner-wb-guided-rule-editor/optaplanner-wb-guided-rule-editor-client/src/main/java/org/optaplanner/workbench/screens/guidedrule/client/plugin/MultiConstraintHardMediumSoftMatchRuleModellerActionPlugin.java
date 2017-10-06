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
import org.optaplanner.workbench.screens.guidedrule.client.widget.MultiConstraintHardMediumSoftMatchRuleModellerWidget;
import org.optaplanner.workbench.models.datamodel.rule.ActionHardConstraintMatch;
import org.optaplanner.workbench.models.datamodel.rule.ActionMediumConstraintMatch;
import org.optaplanner.workbench.models.datamodel.rule.ActionMultiConstraintHardMediumSoftMatch;
import org.optaplanner.workbench.models.datamodel.rule.ActionSoftConstraintMatch;
import org.uberfire.mvp.Command;

@ApplicationScoped
public class MultiConstraintHardMediumSoftMatchRuleModellerActionPlugin implements RuleModellerActionPlugin {

    static final Set<String> SUPPORTED_SCORE_HOLDER_TYPES;

    static {
        SUPPORTED_SCORE_HOLDER_TYPES = new HashSet<>(3);
        SUPPORTED_SCORE_HOLDER_TYPES.add("org.optaplanner.core.api.score.buildin.hardmediumsoft.HardMediumSoftScoreHolder");
        SUPPORTED_SCORE_HOLDER_TYPES.add("org.optaplanner.core.api.score.buildin.hardmediumsoftbigdecimal.HardMediumSoftBigDecimalScoreHolder");
        SUPPORTED_SCORE_HOLDER_TYPES.add("org.optaplanner.core.api.score.buildin.hardmediumsoftlong.HardMediumSoftLongScoreHolder");
    }

    @Inject
    private ActionPluginClientService actionPluginClientService;

    @Inject
    private TranslationService translationService;

    public MultiConstraintHardMediumSoftMatchRuleModellerActionPlugin() {
    }

    @Override
    public boolean accept(final IAction iAction) {
        return iAction instanceof ActionMultiConstraintHardMediumSoftMatch;
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
        return new ActionMultiConstraintHardMediumSoftMatch(new ActionHardConstraintMatch(),
                                                            new ActionMediumConstraintMatch(),
                                                            new ActionSoftConstraintMatch());
    }

    @Override
    public String getId() {
        return "MULTI_CONSTRAINT_HARD_MEDIUM_SOFT_MATCH";
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

        MultiConstraintHardMediumSoftMatchRuleModellerWidget widget = new MultiConstraintHardMediumSoftMatchRuleModellerWidget(ruleModeller,
                                                                                                                               eventBus,
                                                                                                                               (ActionMultiConstraintHardMediumSoftMatch) iAction,
                                                                                                                               translationService,
                                                                                                                               readOnly);
        actionPluginClientService.initScoreHolderAwarePlugin(ruleModeller,
                                                             widget,
                                                             SUPPORTED_SCORE_HOLDER_TYPES);
        return widget;
    }
}
