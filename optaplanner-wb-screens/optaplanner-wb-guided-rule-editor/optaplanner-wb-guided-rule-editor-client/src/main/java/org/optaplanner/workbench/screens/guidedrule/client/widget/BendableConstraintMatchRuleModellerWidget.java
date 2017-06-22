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

import com.google.gwt.dom.client.Style;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.HorizontalPanel;

import org.drools.workbench.screens.guided.rule.client.editor.RuleModeller;
import org.gwtbootstrap3.client.ui.TextBox;
import org.jboss.errai.ui.client.local.spi.TranslationService;
import org.optaplanner.workbench.screens.guidedrule.model.AbstractActionBendableConstraintMatch;
import org.uberfire.client.views.pfly.widgets.HelpIcon;
import org.uberfire.ext.widgets.common.client.common.NumericIntegerTextBox;

public class BendableConstraintMatchRuleModellerWidget extends AbstractConstraintMatchRuleModellerWidget {

    private AbstractActionBendableConstraintMatch actionConstraintMatch;

    private TextBox constraintMatchTextBox = new NumericIntegerTextBox(false);

    private TextBox constraintLevelTextBox = new NumericIntegerTextBox(false);

    private HelpIcon constraintLevelSelectHelpIcon = new HelpIcon();

    public BendableConstraintMatchRuleModellerWidget(final RuleModeller mod,
                                                     final EventBus eventBus,
                                                     final AbstractActionBendableConstraintMatch actionConstraintMatch,
                                                     final TranslationService translationService,
                                                     final Boolean readOnly,
                                                     final String labelTranslationKey) {
        super(mod,
              eventBus,
              translationService);

        this.actionConstraintMatch = actionConstraintMatch;

        HorizontalPanel horizontalPanel = new HorizontalPanel();

        HorizontalPanel labelPanel = createLabelPanel(translationService.getTranslation(labelTranslationKey));
        horizontalPanel.add(labelPanel);

        HorizontalPanel selectPanel = createSelectPanel();
        horizontalPanel.add(selectPanel);

        HorizontalPanel constraintMatchPanel = createConstraintMatchPanel();
        horizontalPanel.add(constraintMatchPanel);

        horizontalPanel.setCellWidth(labelPanel,
                                     "150px");
        horizontalPanel.setCellWidth(selectPanel,
                                     "70px");

        initWidget(horizontalPanel);
    }

    private HorizontalPanel createSelectPanel() {
        HorizontalPanel selectPanel = new HorizontalPanel();

        constraintLevelTextBox.getElement().setAttribute("type",
                                                         "number");
        constraintLevelTextBox.getElement().setAttribute("min",
                                                         "0");
        constraintLevelTextBox.getElement().getStyle().setWidth(40,
                                                                Style.Unit.PX);
        constraintLevelTextBox.setEnabled(false);
        constraintLevelTextBox.setValue(String.valueOf(actionConstraintMatch.getPosition()));
        constraintLevelTextBox.addValueChangeHandler(s -> actionConstraintMatch.setPosition(Integer.parseInt(s.getValue())));
        selectPanel.add(constraintLevelTextBox);

        constraintLevelSelectHelpIcon.setVisible(false);
        constraintLevelSelectHelpIcon.getElement().getStyle().setPaddingLeft(5,
                                                                             Style.Unit.PX);

        selectPanel.add(constraintLevelSelectHelpIcon);

        actionConstraintMatch.setPosition(Integer.parseInt(constraintLevelTextBox.getValue()));

        return selectPanel;
    }

    private HorizontalPanel createConstraintMatchPanel() {
        HorizontalPanel constraintMatchPanel = new HorizontalPanel();

        constraintMatchTextBox.setValue(actionConstraintMatch.getConstraintMatch() == null ? "" : actionConstraintMatch.getConstraintMatch());
        constraintMatchTextBox.addValueChangeHandler(s -> actionConstraintMatch.setConstraintMatch(s.getValue()));
        constraintMatchTextBox.setEnabled(false);
        constraintMatchTextBox.setWidth("100%");

        constraintMatchPanel.setWidth("100%");
        constraintMatchPanel.add(constraintMatchTextBox);

        actionConstraintMatch.setConstraintMatch(constraintMatchTextBox.getValue());

        return constraintMatchPanel;
    }

    @Override
    public void scoreHolderGlobalLoadedCorrectly() {
        constraintMatchTextBox.setEnabled(true);
        constraintLevelTextBox.setEnabled(true);
    }

    public void setScoreLevels(final int scoreLevelSize) {
        int currentLevelSize = actionConstraintMatch.getPosition();

        if (currentLevelSize >= scoreLevelSize) {
            constraintLevelSelectHelpIcon.setHelpContent("Score level set for this score is greater than the maximum defined by current planning solution. Modify the bendable score levels size in the planning solution or change the level for this item.");
            constraintLevelSelectHelpIcon.setVisible(true);
        } else {
            constraintLevelTextBox.getElement().setAttribute("max",
                                                             String.valueOf(scoreLevelSize - 1));
        }
    }
}
