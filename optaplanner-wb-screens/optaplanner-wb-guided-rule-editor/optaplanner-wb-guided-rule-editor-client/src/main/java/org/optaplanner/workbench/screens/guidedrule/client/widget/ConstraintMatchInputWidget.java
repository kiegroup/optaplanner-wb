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

import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.DomEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import org.gwtbootstrap3.client.ui.FormGroup;
import org.gwtbootstrap3.client.ui.HelpBlock;
import org.gwtbootstrap3.client.ui.TextBox;
import org.optaplanner.workbench.models.datamodel.rule.AbstractActionConstraintMatch;
import org.uberfire.client.views.pfly.widgets.ValidationState;

public class ConstraintMatchInputWidget extends FormGroup {

    private TextBox constraintMatchTextBox;

    private HelpBlock helpBlock;

    public ConstraintMatchInputWidget(final AbstractActionConstraintMatch actionConstraintMatch) {
        constraintMatchTextBox = new TextBox();
        helpBlock = new HelpBlock();
        add(constraintMatchTextBox);
        add(helpBlock);

        constraintMatchTextBox.setValue(actionConstraintMatch.getConstraintMatch() == null ? "" : actionConstraintMatch.getConstraintMatch());
        constraintMatchTextBox.setEnabled(false);
    }

    public void showError(final String errorMessage) {
        addStyleName(ValidationState.ERROR.getCssName());
        helpBlock.setError(errorMessage);
    }

    public void clearError() {
        removeStyleName(ValidationState.ERROR.getCssName());
        helpBlock.clearError();
    }

    public String getConstraintMatchValue() {
        return constraintMatchTextBox.getValue();
    }

    public HandlerRegistration addConstraintMatchValueChangeHandler(final ValueChangeHandler<String> valueChangeHandler) {
        return constraintMatchTextBox.addValueChangeHandler(valueChangeHandler);
    }

    public HandlerRegistration addConstraintMatchBlurHandler(final BlurHandler blurHandler) {
        HandlerRegistration registration = constraintMatchTextBox.addBlurHandler(blurHandler);
        DomEvent.fireNativeEvent(Document.get().createBlurEvent(),
                                 constraintMatchTextBox);

        return registration;
    }

    public void setEnabled(final boolean enabled) {
        constraintMatchTextBox.setEnabled(enabled);
    }
}
