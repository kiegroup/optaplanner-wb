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

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.gwt.dom.client.Style;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import org.drools.workbench.models.datamodel.rule.RuleModel;
import org.drools.workbench.screens.guided.rule.client.editor.RuleModeller;
import org.drools.workbench.screens.guided.rule.client.widget.RuleModellerWidget;
import org.jboss.errai.ui.client.local.spi.TranslationService;
import org.optaplanner.workbench.screens.guidedrule.client.resources.i18n.GuidedRuleEditorConstants;
import org.optaplanner.workbench.screens.guidedrule.model.ActionConstraintMatch;
import org.uberfire.client.views.pfly.widgets.HelpIcon;

public abstract class AbstractConstraintMatchRuleModellerWidget extends RuleModellerWidget implements ScoreHolderGlobalAware {

    protected TranslationService translationService;

    protected HelpIcon helpIcon = new HelpIcon();

    protected Set<String> messages = new HashSet<>();

    public AbstractConstraintMatchRuleModellerWidget(final RuleModeller modeller,
                                                     final EventBus eventBus,
                                                     final TranslationService translationService) {
        super(modeller,
              eventBus);

        this.translationService = translationService;
    }

    protected HorizontalPanel createLabelPanel(final String labelText) {
        HorizontalPanel labelPanel = new HorizontalPanel();

        labelPanel.add(new Label(labelText));

        helpIcon.setVisible(false);
        helpIcon.getElement().getStyle().setPaddingLeft(5,
                                                        Style.Unit.PX);
        labelPanel.add(helpIcon);
        checkMultipleActionConstraintMatchesExist();

        return labelPanel;
    }

    @Override
    public void scoreHolderGlobalIssueDetected(final String message) {
        messages.add(message);

        displayMessages();
    }

    protected void checkMultipleActionConstraintMatchesExist() {
        RuleModel model = getModeller().getModel();
        long actionConstraintMatchCount = Arrays.stream(model.rhs).filter(a -> a instanceof ActionConstraintMatch).count();

        if (actionConstraintMatchCount > 1) {
            messages.add(translationService.getTranslation(GuidedRuleEditorConstants.RuleModellerActionPluginAmbigiousConstraintMatchesDetected));

            displayMessages();
        }
    }

    private void displayMessages() {
        String joinedMessages = messages.stream().collect(Collectors.joining("<br/>"));

        helpIcon.setHelpContent(joinedMessages);
        helpIcon.setVisible(true);
    }

    @Override
    public boolean isReadOnly() {
        return false;
    }

    @Override
    public boolean isFactTypeKnown() {
        return true;
    }
}
