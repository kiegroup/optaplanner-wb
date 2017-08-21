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
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import org.drools.workbench.screens.guided.rule.client.editor.RuleModeller;
import org.jboss.errai.ui.client.local.spi.TranslationService;
import org.optaplanner.workbench.screens.guidedrule.client.resources.GuidedRuleEditorResources;
import org.optaplanner.workbench.screens.guidedrule.client.resources.i18n.GuidedRuleEditorConstants;
import org.optaplanner.workbench.screens.guidedrule.model.ActionMultiConstraintHardMediumSoftMatch;

public class MultiConstraintHardMediumSoftMatchRuleModellerWidget extends AbstractConstraintMatchRuleModellerWidget {

    private ConstraintMatchInputWidget hardConstraintMatchInputWidget;
    private ConstraintMatchInputWidget mediumConstraintMatchInputWidget;
    private ConstraintMatchInputWidget softConstraintMatchInputWidget;

    public MultiConstraintHardMediumSoftMatchRuleModellerWidget(final RuleModeller mod,
                                                                final EventBus eventBus,
                                                                final ActionMultiConstraintHardMediumSoftMatch actionConstraintMatch,
                                                                final TranslationService translationService,
                                                                final Boolean readOnly) {
        super(mod,
              eventBus,
              translationService);

        hardConstraintMatchInputWidget = new ConstraintMatchInputWidget(actionConstraintMatch.getActionHardConstraintMatch());
        hardConstraintMatchInputWidget
                .addConstraintMatchValueChangeHandler(new ConstraintMatchValueChangeHandler(actionConstraintMatch.getActionHardConstraintMatch()));
        mediumConstraintMatchInputWidget = new ConstraintMatchInputWidget(actionConstraintMatch.getActionMediumConstraintMatch());
        mediumConstraintMatchInputWidget
                .addConstraintMatchValueChangeHandler(new ConstraintMatchValueChangeHandler(actionConstraintMatch.getActionMediumConstraintMatch()));
        softConstraintMatchInputWidget = new ConstraintMatchInputWidget(actionConstraintMatch.getActionSoftConstraintMatch());
        softConstraintMatchInputWidget
                .addConstraintMatchValueChangeHandler(new ConstraintMatchValueChangeHandler(actionConstraintMatch.getActionSoftConstraintMatch()));

        VerticalPanel verticalPanel = new VerticalPanel();

        HorizontalPanel labelPanel = createLabelPanel(translationService.getTranslation(GuidedRuleEditorConstants.RuleModellerActionPlugin_MultiConstraintMatch));
        verticalPanel.add(labelPanel);

        verticalPanel.add(getItemPanel(translationService.getTranslation(GuidedRuleEditorConstants.RuleModellerActionPlugin_HardScore),
                                       hardConstraintMatchInputWidget));
        verticalPanel.add(getItemPanel(translationService.getTranslation(GuidedRuleEditorConstants.RuleModellerActionPlugin_MediumScore),
                                       mediumConstraintMatchInputWidget));
        verticalPanel.add(getItemPanel(translationService.getTranslation(GuidedRuleEditorConstants.RuleModellerActionPlugin_SoftScore),
                                       softConstraintMatchInputWidget));

        verticalPanel.addStyleName(GuidedRuleEditorResources.INSTANCE.css().multiConstraintMatch());

        initWidget(verticalPanel);
    }

    private HorizontalPanel getItemPanel(final String labelText,
                                         final IsWidget constraintMatchWidget) {
        HorizontalPanel horizontalPanel = new HorizontalPanel();
        horizontalPanel.setWidth("100%");

        HorizontalPanel labelPanel = new HorizontalPanel();
        Label label = new Label(labelText);
        labelPanel.add(label);
        horizontalPanel.add(labelPanel);

        horizontalPanel.add(constraintMatchWidget);

        horizontalPanel.setCellWidth(labelPanel,
                                     "150px");

        return horizontalPanel;
    }

    public void scoreHolderGlobalLoadedCorrectly(final String scoreHolderType) {
        hardConstraintMatchInputWidget.setEnabled(true);
        mediumConstraintMatchInputWidget.setEnabled(true);
        softConstraintMatchInputWidget.setEnabled(true);

        hardConstraintMatchInputWidget
                .addConstraintMatchBlurHandler(new ConstraintMatchInputWidgetBlurHandler(hardConstraintMatchInputWidget,
                                                                                         translationService,
                                                                                         scoreHolderType));
        mediumConstraintMatchInputWidget
                .addConstraintMatchBlurHandler(new ConstraintMatchInputWidgetBlurHandler(mediumConstraintMatchInputWidget,
                                                                                         translationService,
                                                                                         scoreHolderType));
        softConstraintMatchInputWidget
                .addConstraintMatchBlurHandler(new ConstraintMatchInputWidgetBlurHandler(softConstraintMatchInputWidget,
                                                                                         translationService,
                                                                                         scoreHolderType));
    }
}
