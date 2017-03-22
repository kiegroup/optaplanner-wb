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
import com.google.gwt.user.client.ui.TextBox;
import org.drools.workbench.screens.guided.rule.client.editor.RuleModeller;
import org.jboss.errai.ui.client.local.spi.TranslationService;
import org.optaplanner.workbench.screens.guidedrule.model.AbstractActionConstraintMatch;

public class ConstraintMatchRuleModellerWidget extends AbstractConstraintMatchRuleModellerWidget {

    private TextBox constraintMatchTextBox = new TextBox();

    public ConstraintMatchRuleModellerWidget(final RuleModeller mod,
                                             final EventBus eventBus,
                                             final AbstractActionConstraintMatch actionConstraintMatch,
                                             final TranslationService translationService,
                                             final Boolean readOnly,
                                             final String labelTranslationKey) {
        super(mod,
              eventBus,
              translationService);

        HorizontalPanel horizontalPanel = new HorizontalPanel();

        HorizontalPanel labelPanel = createLabelPanel(translationService.getTranslation(labelTranslationKey));
        horizontalPanel.add(labelPanel);

        HorizontalPanel constraintMatchPanel = createConstraintMatchPanel(actionConstraintMatch);
        horizontalPanel.add(constraintMatchPanel);

        horizontalPanel.setCellWidth(labelPanel,
                                     "150px");

        initWidget(horizontalPanel);
    }

    private HorizontalPanel createConstraintMatchPanel(final AbstractActionConstraintMatch actionConstraintMatch) {
        HorizontalPanel constraintMatchPanel = new HorizontalPanel();

        constraintMatchTextBox.setValue(actionConstraintMatch.getConstraintMatch() == null ? "" : actionConstraintMatch.getConstraintMatch());
        constraintMatchTextBox.addValueChangeHandler(s -> actionConstraintMatch.setConstraintMatch(s.getValue()));
        constraintMatchTextBox.setEnabled(false);
        constraintMatchTextBox.setWidth("100%");

        constraintMatchPanel.setWidth("100%");
        constraintMatchPanel.add(constraintMatchTextBox);

        return constraintMatchPanel;
    }

    public void scoreHolderGlobalLoadedCorrectly() {
        constraintMatchTextBox.setEnabled(true);
    }
}
