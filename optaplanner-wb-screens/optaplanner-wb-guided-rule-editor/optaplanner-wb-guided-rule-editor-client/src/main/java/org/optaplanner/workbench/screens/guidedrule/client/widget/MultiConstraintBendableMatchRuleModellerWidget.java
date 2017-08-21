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

package org.optaplanner.workbench.screens.guidedrule.client.widget;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.dom.client.Style;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import org.drools.workbench.screens.guided.rule.client.editor.RuleModeller;
import org.gwtbootstrap3.client.ui.TextBox;
import org.jboss.errai.ui.client.local.spi.TranslationService;
import org.optaplanner.workbench.screens.guidedrule.client.resources.GuidedRuleEditorResources;
import org.optaplanner.workbench.screens.guidedrule.client.resources.i18n.GuidedRuleEditorConstants;
import org.optaplanner.workbench.screens.guidedrule.model.AbstractActionBendableConstraintMatch;
import org.optaplanner.workbench.screens.guidedrule.model.AbstractActionMultiConstraintBendableMatch;
import org.optaplanner.workbench.screens.guidedrule.model.BendableScoreLevelsWrapper;
import org.uberfire.client.views.pfly.widgets.HelpIcon;

public class MultiConstraintBendableMatchRuleModellerWidget extends AbstractConstraintMatchRuleModellerWidget {

    private List<ConstraintMatchInputWidget> hardConstraintMatchInputWidgets = new ArrayList<>();

    private List<HelpIcon> hardConstraintMatchHelpIcons = new ArrayList<>();

    private List<ConstraintMatchInputWidget> softConstraintMatchInputWidgets = new ArrayList<>();

    private List<HelpIcon> softConstraintMatchHelpIcons = new ArrayList<>();

    public MultiConstraintBendableMatchRuleModellerWidget(final RuleModeller mod,
                                                          final EventBus eventBus,
                                                          final AbstractActionMultiConstraintBendableMatch actionConstraintMatch,
                                                          final TranslationService translationService,
                                                          final Boolean readOnly) {
        super(mod,
              eventBus,
              translationService);

        VerticalPanel verticalPanel = new VerticalPanel();

        HorizontalPanel titlePanel = createLabelPanel(translationService.getTranslation(GuidedRuleEditorConstants.RuleModellerActionPlugin_MultiConstraintMatch));
        verticalPanel.add(titlePanel);
        verticalPanel.setCellHeight(titlePanel,
                                    "25px");

        VerticalPanel hardScorePanel = new VerticalPanel();
        hardScorePanel.getElement().getStyle().setWidth(100,
                                                        Style.Unit.PCT);
        if (actionConstraintMatch.getActionBendableHardConstraintMatches() == null || actionConstraintMatch.getActionBendableHardConstraintMatches().isEmpty()) {
            hardScorePanel.add(new Label(translationService.getTranslation(GuidedRuleEditorConstants.RuleModellerActionPlugin_HardScoreLevelSizeIsZero)));
        } else {
            for (int i = 0; i < actionConstraintMatch.getActionBendableHardConstraintMatches().size(); i++) {
                HorizontalPanel horizontalPanel = createBendableConstraintMatchRow(translationService.getTranslation(GuidedRuleEditorConstants.RuleModellerActionPlugin_HardScore),
                                                                                   i,
                                                                                   hardConstraintMatchHelpIcons,
                                                                                   hardConstraintMatchInputWidgets,
                                                                                   actionConstraintMatch.getActionBendableHardConstraintMatches().get(i));
                hardScorePanel.add(horizontalPanel);
            }
        }
        verticalPanel.add(hardScorePanel);

        VerticalPanel softScorePanel = new VerticalPanel();
        softScorePanel.getElement().getStyle().setWidth(100,
                                                        Style.Unit.PCT);
        if (actionConstraintMatch.getActionBendableSoftConstraintMatches() == null || actionConstraintMatch.getActionBendableSoftConstraintMatches().isEmpty()) {
            softScorePanel.add(new Label(translationService.getTranslation(GuidedRuleEditorConstants.RuleModellerActionPlugin_SoftScoreLevelSizeIsZero)));
        } else {
            for (int i = 0; i < actionConstraintMatch.getActionBendableSoftConstraintMatches().size(); i++) {
                HorizontalPanel horizontalPanel = createBendableConstraintMatchRow(translationService.getTranslation(GuidedRuleEditorConstants.RuleModellerActionPlugin_SoftScore),
                                                                                   i,
                                                                                   softConstraintMatchHelpIcons,
                                                                                   softConstraintMatchInputWidgets,
                                                                                   actionConstraintMatch.getActionBendableSoftConstraintMatches().get(i));
                softScorePanel.add(horizontalPanel);
            }
        }
        verticalPanel.add(softScorePanel);

        verticalPanel.addStyleName(GuidedRuleEditorResources.INSTANCE.css().multiConstraintMatch());

        initWidget(verticalPanel);
    }

    private HorizontalPanel createBendableConstraintMatchRow(final String labelText,
                                                             final int index,
                                                             final List<HelpIcon> hardConstraintMatchHelpIcons,
                                                             final List<ConstraintMatchInputWidget> constraintMatchInputWidgets,
                                                             final AbstractActionBendableConstraintMatch constraintMatch) {
        HorizontalPanel horizontalPanel = new HorizontalPanel();

        HorizontalPanel labelPanel = new HorizontalPanel();
        labelPanel.add(new Label(labelText));
        horizontalPanel.add(labelPanel);

        HorizontalPanel selectPanel = new HorizontalPanel();

        TextBox constraintLevelTextBox = new TextBox();
        constraintLevelTextBox.setEnabled(false);
        constraintLevelTextBox.getElement().getStyle().setWidth(40,
                                                                Style.Unit.PX);
        constraintLevelTextBox.setValue(String.valueOf(index));
        selectPanel.add(constraintLevelTextBox);

        HelpIcon constraintLevelSelectHelpIcon = new HelpIcon();
        hardConstraintMatchHelpIcons.add(constraintLevelSelectHelpIcon);
        constraintLevelSelectHelpIcon.setVisible(false);
        constraintLevelSelectHelpIcon.setHelpContent(translationService.getTranslation(GuidedRuleEditorConstants.RuleModellerActionPlugin_ScoreLevelExceeded));
        constraintLevelSelectHelpIcon.getElement().getStyle().setPaddingLeft(5,
                                                                             Style.Unit.PX);
        selectPanel.add(constraintLevelSelectHelpIcon);

        horizontalPanel.add(selectPanel);

        ConstraintMatchInputWidget constraintMatchInputWidget = new ConstraintMatchInputWidget(constraintMatch);
        constraintMatchInputWidget
                .addConstraintMatchValueChangeHandler(new ConstraintMatchValueChangeHandler(constraintMatch));
        constraintMatchInputWidgets.add(constraintMatchInputWidget);

        horizontalPanel.add(constraintMatchInputWidget);
        horizontalPanel.setCellWidth(labelPanel,
                                     "150px");
        horizontalPanel.setCellWidth(selectPanel,
                                     "70px");

        horizontalPanel.setStyleName(GuidedRuleEditorResources.INSTANCE.css().multiConstraintMatch());
        horizontalPanel.getElement().getStyle().setWidth(100,
                                                         Style.Unit.PCT);

        return horizontalPanel;
    }

    @Override
    public void scoreHolderGlobalLoadedCorrectly(final String scoreHolderType) {
        if (hardConstraintMatchInputWidgets != null) {
            for (ConstraintMatchInputWidget hardConstraintMatchInputWidget : hardConstraintMatchInputWidgets) {
                hardConstraintMatchInputWidget.setEnabled(true);
                hardConstraintMatchInputWidget.addConstraintMatchBlurHandler(new ConstraintMatchInputWidgetBlurHandler(hardConstraintMatchInputWidget,
                                                                                                                       translationService,
                                                                                                                       scoreHolderType));
            }
        }

        if (softConstraintMatchInputWidgets != null) {
            for (ConstraintMatchInputWidget softConstraintMatchInputWidget : softConstraintMatchInputWidgets) {
                softConstraintMatchInputWidget.setEnabled(true);
                softConstraintMatchInputWidget.addConstraintMatchBlurHandler(new ConstraintMatchInputWidgetBlurHandler(softConstraintMatchInputWidget,
                                                                                                                       translationService,
                                                                                                                       scoreHolderType));
            }
        }
    }

    public void setScoreLevels(final BendableScoreLevelsWrapper scoreLevelsWrapper) {
        if (hardConstraintMatchHelpIcons != null) {
            for (int i = 0; i < hardConstraintMatchHelpIcons.size(); i++) {
                if (scoreLevelsWrapper.getHardScoreLevels() <= i) {
                    hardConstraintMatchHelpIcons.get(i).setVisible(true);
                }
            }
        }

        if (softConstraintMatchHelpIcons != null) {
            for (int i = 0; i < softConstraintMatchHelpIcons.size(); i++) {
                if (scoreLevelsWrapper.getSoftScoreLevels() <= i) {
                    softConstraintMatchHelpIcons.get(i).setVisible(true);
                }
            }
        }
    }
}
