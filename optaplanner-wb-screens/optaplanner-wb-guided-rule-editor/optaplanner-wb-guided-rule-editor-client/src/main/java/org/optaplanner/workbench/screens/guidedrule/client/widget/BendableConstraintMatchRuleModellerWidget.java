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
import org.optaplanner.workbench.screens.guidedrule.client.resources.i18n.GuidedRuleEditorConstants;
import org.optaplanner.workbench.screens.guidedrule.model.AbstractActionBendableConstraintMatch;
import org.uberfire.client.views.pfly.widgets.HelpIcon;

public class BendableConstraintMatchRuleModellerWidget extends AbstractConstraintMatchRuleModellerWidget {

    private AbstractActionBendableConstraintMatch actionConstraintMatch;

    private ConstraintMatchInputWidget constraintMatchInputWidget;

    private TextBox constraintLevelTextBox = new TextBox();

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
        constraintMatchInputWidget = new ConstraintMatchInputWidget(actionConstraintMatch,
                                                                    translationService);
        constraintMatchInputWidget
                .addConstraintMatchBlurHandler(new ConstraintMatchInputWidgetBlurHandler(constraintMatchInputWidget));
        constraintMatchInputWidget
                .addConstraintMatchValueChangeHandler(new ConstraintMatchValueChangeHandler(actionConstraintMatch));

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

        return selectPanel;
    }

    private HorizontalPanel createConstraintMatchPanel() {
        final HorizontalPanel constraintMatchPanel = new HorizontalPanel();

        constraintMatchPanel.setWidth("100%");
        constraintMatchPanel.add(constraintMatchInputWidget);

        return constraintMatchPanel;
    }

    @Override
    public void scoreHolderGlobalLoadedCorrectly() {
        constraintMatchInputWidget.setEnabled(true);
        constraintLevelTextBox.setEnabled(true);
    }

    public void setScoreLevels(final int scoreLevelSize) {
        int currentLevelSize = actionConstraintMatch.getPosition();

        if (currentLevelSize >= scoreLevelSize) {
            constraintLevelSelectHelpIcon.setHelpContent(translationService.getTranslation(GuidedRuleEditorConstants.RuleModellerActionPluginScoreLevelExceeded));
            constraintLevelSelectHelpIcon.setVisible(true);
        } else {
            constraintLevelTextBox.getElement().setAttribute("max",
                                                             String.valueOf(scoreLevelSize - 1));
        }
    }
}
