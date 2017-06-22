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
import org.optaplanner.workbench.screens.guidedrule.model.ActionMultiConstraintBendableBigDecimalMatch;
import org.optaplanner.workbench.screens.guidedrule.model.ActionMultiConstraintBendableLongMatch;
import org.optaplanner.workbench.screens.guidedrule.model.BendableScoreLevelsWrapper;
import org.uberfire.client.views.pfly.widgets.HelpIcon;
import org.uberfire.ext.widgets.common.client.common.NumericBigDecimalTextBox;
import org.uberfire.ext.widgets.common.client.common.NumericIntegerTextBox;
import org.uberfire.ext.widgets.common.client.common.NumericLongTextBox;

public class MultiConstraintBendableMatchRuleModellerWidget extends AbstractConstraintMatchRuleModellerWidget {

    private List<TextBox> hardConstraintMatchTextBoxes = new ArrayList<>();

    private List<HelpIcon> hardConstraintMatchHelpIcons = new ArrayList<>();

    private List<TextBox> softConstraintMatchTextBoxes = new ArrayList<>();

    private List<HelpIcon> softConstraintMatchHelpIcons = new ArrayList<>();

    private AbstractActionMultiConstraintBendableMatch actionConstraintMatch;

    public MultiConstraintBendableMatchRuleModellerWidget(final RuleModeller mod,
                                                          final EventBus eventBus,
                                                          final AbstractActionMultiConstraintBendableMatch actionConstraintMatch,
                                                          final TranslationService translationService,
                                                          final Boolean readOnly) {
        super(mod,
              eventBus,
              translationService);

        this.actionConstraintMatch = actionConstraintMatch;

        VerticalPanel verticalPanel = new VerticalPanel();

        HorizontalPanel titlePanel = createLabelPanel(translationService.getTranslation(GuidedRuleEditorConstants.RuleModellerActionPluginMultiConstraintMatch));
        verticalPanel.add(titlePanel);
        verticalPanel.setCellHeight(titlePanel,
                                    "25px");

        VerticalPanel hardScorePanel = new VerticalPanel();
        hardScorePanel.getElement().getStyle().setWidth(100,
                                                        Style.Unit.PCT);
        if (actionConstraintMatch.getActionBendableHardConstraintMatches() == null || actionConstraintMatch.getActionBendableHardConstraintMatches().isEmpty()) {
            hardScorePanel.add(new Label(translationService.getTranslation(GuidedRuleEditorConstants.RuleModellerActionPluginHardScoreLevelSizeIsZero)));
        } else {
            for (int i = 0; i < actionConstraintMatch.getActionBendableHardConstraintMatches().size(); i++) {
                HorizontalPanel horizontalPanel = createBendableConstraintMatchRow(translationService.getTranslation(GuidedRuleEditorConstants.RuleModellerActionPluginHardScore),
                                                                                   i,
                                                                                   hardConstraintMatchHelpIcons,
                                                                                   hardConstraintMatchTextBoxes,
                                                                                   actionConstraintMatch.getActionBendableHardConstraintMatches().get(i));
                hardScorePanel.add(horizontalPanel);
            }
        }
        verticalPanel.add(hardScorePanel);

        VerticalPanel softScorePanel = new VerticalPanel();
        softScorePanel.getElement().getStyle().setWidth(100,
                                                        Style.Unit.PCT);
        if (actionConstraintMatch.getActionBendableSoftConstraintMatches() == null || actionConstraintMatch.getActionBendableSoftConstraintMatches().isEmpty()) {
            softScorePanel.add(new Label(translationService.getTranslation(GuidedRuleEditorConstants.RuleModellerActionPluginSoftScoreLevelSizeIsZero)));
        } else {
            for (int i = 0; i < actionConstraintMatch.getActionBendableSoftConstraintMatches().size(); i++) {
                HorizontalPanel horizontalPanel = createBendableConstraintMatchRow(translationService.getTranslation(GuidedRuleEditorConstants.RuleModellerActionPluginSoftScore),
                                                                                   i,
                                                                                   softConstraintMatchHelpIcons,
                                                                                   softConstraintMatchTextBoxes,
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
                                                             final List<HelpIcon> constraintMatchHelpIcons,
                                                             final List<TextBox> constraintMatchTextBoxes,
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
        constraintMatchHelpIcons.add(constraintLevelSelectHelpIcon);
        constraintLevelSelectHelpIcon.setVisible(false);
        constraintLevelSelectHelpIcon.setHelpContent(translationService.getTranslation(GuidedRuleEditorConstants.RuleModellerActionPluginScoreLevelExceeded));
        constraintLevelSelectHelpIcon.getElement().getStyle().setPaddingLeft(5,
                                                                             Style.Unit.PX);
        selectPanel.add(constraintLevelSelectHelpIcon);

        horizontalPanel.add(selectPanel);

        TextBox constraintMatchTextBox = null;
        if (actionConstraintMatch instanceof ActionMultiConstraintBendableBigDecimalMatch) {
            constraintMatchTextBox = new NumericBigDecimalTextBox(false);
        } else if ((actionConstraintMatch instanceof ActionMultiConstraintBendableLongMatch)) {
            constraintMatchTextBox = new NumericLongTextBox(false);
        } else {
            constraintMatchTextBox = new NumericIntegerTextBox(false);
        }
        constraintMatchTextBoxes.add(constraintMatchTextBox);
        constraintMatchTextBox.setValue(constraintMatch.getConstraintMatch() == null ? "" : constraintMatch.getConstraintMatch());
        constraintMatchTextBox.addValueChangeHandler(c -> constraintMatch.setConstraintMatch(c.getValue()));
        constraintMatchTextBox.setEnabled(false);
        constraintMatchTextBox.setWidth("100%");

        horizontalPanel.add(constraintMatchTextBox);
        horizontalPanel.setCellWidth(labelPanel,
                                     "150px");
        horizontalPanel.setCellWidth(selectPanel,
                                     "70px");

        horizontalPanel.setStyleName(GuidedRuleEditorResources.INSTANCE.css().multiConstraintMatch());
        horizontalPanel.getElement().getStyle().setWidth(100,
                                                         Style.Unit.PCT);

        constraintMatch.setConstraintMatch(constraintMatchTextBox.getValue());
        constraintMatch.setPosition(index);

        return horizontalPanel;
    }

    @Override
    public void scoreHolderGlobalLoadedCorrectly() {
        if (hardConstraintMatchTextBoxes != null) {
            for (TextBox hardConstraintMatchTextBox : hardConstraintMatchTextBoxes) {
                hardConstraintMatchTextBox.setEnabled(true);
            }
        }

        if (softConstraintMatchTextBoxes != null) {
            for (TextBox softConstraintMatchTextBox : softConstraintMatchTextBoxes) {
                softConstraintMatchTextBox.setEnabled(true);
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
