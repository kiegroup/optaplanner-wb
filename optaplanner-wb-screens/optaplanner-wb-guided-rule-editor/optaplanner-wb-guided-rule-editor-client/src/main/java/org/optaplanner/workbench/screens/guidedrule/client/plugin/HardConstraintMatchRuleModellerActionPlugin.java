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
import org.optaplanner.workbench.screens.guidedrule.client.widget.ConstraintMatchRuleModellerWidget;
import org.optaplanner.workbench.models.datamodel.rule.ActionHardConstraintMatch;
import org.uberfire.mvp.Command;

@ApplicationScoped
public class HardConstraintMatchRuleModellerActionPlugin implements RuleModellerActionPlugin {

    static final Set<String> SUPPORTED_SCORE_HOLDER_TYPES;

    static {
        SUPPORTED_SCORE_HOLDER_TYPES = new HashSet<>(7);
        SUPPORTED_SCORE_HOLDER_TYPES.add("org.optaplanner.core.api.score.buildin.hardmediumsoft.HardMediumSoftScoreHolder");
        SUPPORTED_SCORE_HOLDER_TYPES.add("org.optaplanner.core.api.score.buildin.hardmediumsoftbigdecimal.HardMediumSoftBigDecimalScoreHolder");
        SUPPORTED_SCORE_HOLDER_TYPES.add("org.optaplanner.core.api.score.buildin.hardmediumsoftlong.HardMediumSoftLongScoreHolder");
        SUPPORTED_SCORE_HOLDER_TYPES.add("org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScoreHolder");
        SUPPORTED_SCORE_HOLDER_TYPES.add("org.optaplanner.core.api.score.buildin.hardsoftbigdecimal.HardSoftBigDecimalScoreHolder");
        SUPPORTED_SCORE_HOLDER_TYPES.add("org.optaplanner.core.api.score.buildin.hardsoftdouble.HardSoftDoubleScoreHolder");
        SUPPORTED_SCORE_HOLDER_TYPES.add("org.optaplanner.core.api.score.buildin.hardsoftlong.HardSoftLongScoreHolder");
    }

    @Inject
    private ActionPluginClientService actionPluginClientService;

    @Inject
    private TranslationService translationService;

    public HardConstraintMatchRuleModellerActionPlugin() {
    }

    @Override
    public boolean accept(final IAction iAction) {
        return iAction instanceof ActionHardConstraintMatch;
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
        return new ActionHardConstraintMatch();
    }

    @Override
    public String getId() {
        return "HARD_CONSTRAINT_MATCH";
    }

    @Override
    public String getActionAddDescription() {
        return translationService.getTranslation(GuidedRuleEditorConstants.RuleModellerActionPlugin_ModifyHardScore);
    }

    @Override
    public RuleModellerWidget createWidget(final RuleModeller ruleModeller,
                                           final EventBus eventBus,
                                           final IAction iAction,
                                           final Boolean readOnly) {
        ConstraintMatchRuleModellerWidget widget = new ConstraintMatchRuleModellerWidget(ruleModeller,
                                                                                         eventBus,
                                                                                         (ActionHardConstraintMatch) iAction,
                                                                                         translationService,
                                                                                         readOnly,
                                                                                         GuidedRuleEditorConstants.RuleModellerActionPlugin_HardScore);
        actionPluginClientService.initScoreHolderAwarePlugin(ruleModeller,
                                                             widget,
                                                             SUPPORTED_SCORE_HOLDER_TYPES);

        return widget;
    }
}
