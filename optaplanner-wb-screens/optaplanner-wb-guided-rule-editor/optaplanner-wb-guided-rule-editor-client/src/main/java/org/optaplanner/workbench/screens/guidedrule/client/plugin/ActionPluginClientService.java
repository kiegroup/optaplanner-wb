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

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Consumer;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.drools.workbench.screens.guided.rule.client.editor.RuleModeller;
import org.jboss.errai.common.client.api.Caller;
import org.jboss.errai.ui.client.local.spi.TranslationService;
import org.optaplanner.workbench.screens.guidedrule.client.resources.i18n.GuidedRuleEditorConstants;
import org.optaplanner.workbench.screens.guidedrule.client.widget.ScoreHolderGlobalAware;
import org.optaplanner.workbench.screens.guidedrule.model.AbstractActionMultiConstraintBendableMatch;
import org.optaplanner.workbench.screens.guidedrule.model.ActionBendableHardConstraintMatch;
import org.optaplanner.workbench.screens.guidedrule.model.ActionBendableSoftConstraintMatch;
import org.optaplanner.workbench.screens.guidedrule.model.BendableScoreLevelsWrapper;
import org.optaplanner.workbench.screens.guidedrule.model.ScoreInformation;
import org.optaplanner.workbench.screens.guidedrule.service.ScoreHolderService;
import org.uberfire.mvp.Command;

@ApplicationScoped
public class ActionPluginClientService {

    static final String SCORE_INFORMATION = ActionPluginClientService.class.getName() + ".score.information";

    private Caller<ScoreHolderService> scoreHolderService;

    private TranslationService translationService;

    @Inject
    public ActionPluginClientService(final Caller<ScoreHolderService> scoreHolderService,
                                     final TranslationService translationService) {
        this.scoreHolderService = scoreHolderService;
        this.translationService = translationService;
    }

    public void invokeScoreInformationCachedOperation(final RuleModeller ruleModeller,
                                                      final Consumer<ScoreInformation> scoreInformationConsumer) {

        Object scoreInformationObject = ruleModeller.getServiceInvocationCache().get(SCORE_INFORMATION);

        if (!(scoreInformationObject instanceof ScoreInformation)) {
            scoreHolderService.call(scoreInformation -> {
                ruleModeller.getServiceInvocationCache().put(SCORE_INFORMATION,
                                                             scoreInformation);
                scoreInformationConsumer.accept((ScoreInformation) scoreInformation);
            }).getProjectScoreInformation(ruleModeller.getPath());
        } else {
            ScoreInformation scoreInformation = (ScoreInformation) ruleModeller.getServiceInvocationCache().get(SCORE_INFORMATION);
            scoreInformationConsumer.accept(scoreInformation);
        }
    }

    public void initScoreHolderAwarePlugin(final RuleModeller ruleModeller,
                                           final ScoreHolderGlobalAware scoreHolderGlobalAware,
                                           final Collection supportedScoreHolderTypes) {
        invokeScoreInformationCachedOperation(ruleModeller,
                                              scoreInformation -> {
                                                  Collection<String> scoreHolderFqns = scoreInformation.getScoreHolderFqnTypeFqns();
                                                  if (scoreHolderFqns.isEmpty()) {
                                                      scoreHolderGlobalAware.scoreHolderGlobalIssueDetected(translationService.getTranslation(GuidedRuleEditorConstants.ActionPluginClientService_ScoreHolderGlobalNotFound));
                                                  } else if (scoreHolderFqns.size() > 1) {
                                                      scoreHolderGlobalAware.scoreHolderGlobalIssueDetected(translationService.getTranslation(translationService.getTranslation(GuidedRuleEditorConstants.ActionPluginClientService_MultipleScoreHolderGlobals)));
                                                  } else if (scoreHolderFqns.size() == 1) {
                                                      if (!supportedScoreHolderTypes.containsAll(scoreHolderFqns)) {
                                                          scoreHolderGlobalAware.scoreHolderGlobalIssueDetected(translationService.getTranslation(GuidedRuleEditorConstants.ActionPluginClientService_ScoreTypeNotSupported));
                                                      } else {
                                                          scoreHolderGlobalAware.scoreHolderGlobalLoadedCorrectly(scoreHolderFqns.iterator().next());
                                                      }
                                                  }
                                              });
    }

    public void addPluginToActionList(final RuleModeller ruleModeller,
                                      final Command addCommand,
                                      final Collection supportedScoreHolderTypes) {
        invokeScoreInformationCachedOperation(ruleModeller,
                                              scoreInformation -> {
                                                  Collection<String> scoreHolderFqns = scoreInformation.getScoreHolderFqnTypeFqns();
                                                  if (scoreHolderFqns.size() == 1 && supportedScoreHolderTypes.containsAll(scoreHolderFqns)) {
                                                      addCommand.execute();
                                                  }
                                              });
    }

    public void initBendableScoreLevels(final RuleModeller ruleModeller,
                                        final AbstractActionMultiConstraintBendableMatch constraintMatch) {
        invokeScoreInformationCachedOperation(ruleModeller,
                                              scoreInformation -> {
                                                  Collection<BendableScoreLevelsWrapper> scoreLevels = scoreInformation.getBendableScoreLevelsWrappers();
                                                  if (scoreLevels.size() == 1) {
                                                      BendableScoreLevelsWrapper scoreLevelsWrapper = scoreLevels.iterator().next();

                                                      ArrayList<ActionBendableHardConstraintMatch> hardConstraints = new ArrayList<>(scoreLevelsWrapper.getHardScoreLevels());
                                                      for (int i = 0; i < scoreLevelsWrapper.getHardScoreLevels(); i++) {
                                                          hardConstraints.add(new ActionBendableHardConstraintMatch(i,
                                                                                                                    null));
                                                      }
                                                      constraintMatch.setActionBendableHardConstraintMatches(hardConstraints);

                                                      ArrayList<ActionBendableSoftConstraintMatch> softConstraints = new ArrayList<>(scoreLevelsWrapper.getSoftScoreLevels());
                                                      for (int i = 0; i < scoreLevelsWrapper.getSoftScoreLevels(); i++) {
                                                          softConstraints.add(new ActionBendableSoftConstraintMatch(i,
                                                                                                                    null));
                                                      }
                                                      constraintMatch.setActionBendableSoftConstraintMatches(softConstraints);
                                                  }
                                              });
    }
}
