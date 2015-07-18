/*
 * Copyright 2015 JBoss Inc
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

package org.optaplanner.workbench.screens.domaineditor.client.resources.i18n;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.Messages;

/**
 * Domain Editor i18n constants.
 */
public interface DomainEditorConstants
        extends Messages {

    public static final DomainEditorConstants INSTANCE = GWT.create( DomainEditorConstants.class );

    String plannerSettingsLabel();

    String notInPlanningLabel();

    String planningEntityLabel();

    String planningSolutionLabel();

    String planningSolutionSettingsLabel();

    String valueRangeProviderLabel();

    String valueRangeProviderIdLabel();

    String planningEntitySettingsLabel();

    String planningEntityCollectionLabel();

    String planningVariableLabel();

    String valueRangeProviderRefsLabel();

}