/*
 * Copyright 2016 Red Hat, Inc. and/or its affiliates.
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
package org.optaplanner.workbench.screens.solver.client.resources.i18n;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.ConstantsWithLookup;

public interface SolverEditorConstantsWithLookup extends ConstantsWithLookup {

    SolverEditorConstantsWithLookup INSTANCE = GWT.create( SolverEditorConstantsWithLookup.class );

    String FIRST_FIT();
    String FIRST_FIT_DECREASING();
    String WEAKEST_FIT();
    String WEAKEST_FIT_DECREASING();
    String STRONGEST_FIT();
    String STRONGEST_FIT_DECREASING();
    String ALLOCATE_ENTITY_FROM_QUEUE();
    String ALLOCATE_TO_VALUE_FROM_QUEUE();
    String CHEAPEST_INSERTION();
    String ALLOCATE_FROM_POOL();

    String NONE();
    String DECREASING_DIFFICULTY();
    String DECREASING_DIFFICULTY_IF_AVAILABLE();

    String INCREASING_STRENGTH();
    String INCREASING_STRENGTH_IF_AVAILABLE();
    String DECREASING_STRENGTH();
    String DECREASING_STRENGTH_IF_AVAILABLE();

}
