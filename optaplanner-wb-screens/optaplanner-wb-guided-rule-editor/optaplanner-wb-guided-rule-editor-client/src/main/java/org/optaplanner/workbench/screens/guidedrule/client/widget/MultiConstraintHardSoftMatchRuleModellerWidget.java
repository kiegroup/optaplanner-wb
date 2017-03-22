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

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import org.drools.workbench.screens.guided.rule.client.editor.RuleModeller;
import org.jboss.errai.ui.client.local.spi.TranslationService;
import org.optaplanner.workbench.screens.guidedrule.client.resources.GuidedRuleEditorResources;
import org.optaplanner.workbench.screens.guidedrule.client.resources.i18n.GuidedRuleEditorConstants;
import org.optaplanner.workbench.screens.guidedrule.model.ActionMultiConstraintHardSoftMatch;

public class MultiConstraintHardSoftMatchRuleModellerWidget extends AbstractConstraintMatchRuleModellerWidget {

    private TextBox hardConstraintMatchTextBox = new TextBox();
    private TextBox softConstraintMatchTextBox = new TextBox();

    public MultiConstraintHardSoftMatchRuleModellerWidget(final RuleModeller mod,
                                                          final EventBus eventBus,
                                                          final ActionMultiConstraintHardSoftMatch actionConstraintMatch,
                                                          final TranslationService translationService,
                                                          final Boolean readOnly) {
        super(mod,
              eventBus,
              translationService);

        VerticalPanel verticalPanel = new VerticalPanel();

        HorizontalPanel labelPanel = createLabelPanel(translationService.getTranslation(GuidedRuleEditorConstants.RuleModellerActionPluginMultiConstraintMatch));
        verticalPanel.add(labelPanel);
        verticalPanel.setCellHeight(labelPanel,
                                    "25px");

        String hardConstraintMatch = actionConstraintMatch.getActionHardConstraintMatch().getConstraintMatch();
        hardConstraintMatchTextBox.setValue(hardConstraintMatch == null ? "" : hardConstraintMatch);
        hardConstraintMatchTextBox.addValueChangeHandler(s -> actionConstraintMatch.getActionHardConstraintMatch().setConstraintMatch(s.getValue()));
        verticalPanel.add(getItemPanel(translationService.getTranslation(GuidedRuleEditorConstants.RuleModellerActionPluginHardScore),
                                       hardConstraintMatchTextBox));

        String softConstraintMatch = actionConstraintMatch.getActionSoftConstraintMatch().getConstraintMatch();
        softConstraintMatchTextBox.setValue(softConstraintMatch == null ? "" : softConstraintMatch);
        softConstraintMatchTextBox.addValueChangeHandler(s -> actionConstraintMatch.getActionSoftConstraintMatch().setConstraintMatch(s.getValue()));
        verticalPanel.add(getItemPanel(translationService.getTranslation(GuidedRuleEditorConstants.RuleModellerActionPluginSoftScore),
                                       softConstraintMatchTextBox));

        verticalPanel.setStyleName(GuidedRuleEditorResources.INSTANCE.css().multiConstraintMatch());

        initWidget(verticalPanel);
    }

    private HorizontalPanel getItemPanel(final String labelText,
                                         final TextBox constraintMatchTextBox) {
        HorizontalPanel horizontalPanel = new HorizontalPanel();

        HorizontalPanel labelPanel = new HorizontalPanel();
        Label label = new Label(labelText);
        labelPanel.add(label);
        horizontalPanel.add(labelPanel);

        constraintMatchTextBox.setEnabled(false);
        constraintMatchTextBox.setWidth("100%");
        horizontalPanel.add(constraintMatchTextBox);

        horizontalPanel.setWidth("100%");
        horizontalPanel.setCellWidth(labelPanel,
                                     "150px");

        return horizontalPanel;
    }

    public void scoreHolderGlobalLoadedCorrectly() {
        hardConstraintMatchTextBox.setEnabled(true);
        softConstraintMatchTextBox.setEnabled(true);
    }
}
