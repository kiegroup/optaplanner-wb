/*
 * Copyright 2015 Red Hat, Inc. and/or its affiliates.
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

package org.optaplanner.workbench.screens.domaineditor.client.widgets.planner;

import java.util.List;

import org.uberfire.client.mvp.UberView;
import org.uberfire.commons.data.Pair;

public interface PlannerDataObjectEditorView
        extends UberView<PlannerDataObjectEditorView.Presenter> {

    interface Presenter {

        void onNotInPlanningChange( );

        void onPlanningEntityChange( );

        void onPlanningSolutionChange( );

        void onPlanningSolutionScoreTypeChange();
    }

    void setNotInPlanningValue( boolean value );

    boolean getNotInPlanningValue( );

    void setPlanningEntityValue( boolean value );

    boolean getPlanningEntityValue( );

    void setPlanningSolutionValue( boolean value );

    boolean getPlanningSolutionValue( );

    void initPlanningSolutionScoreTypeOptions( List<Pair<String, String>> planningSolutionScoreTypeOptions,
            String selectedScoreType );

    String getPlanningSolutionScoreType();

    void setPlanningSolutionScoreType( String scoreType );

    void showPlanningSolutionScoreType( boolean show );

    void clear( );

}
