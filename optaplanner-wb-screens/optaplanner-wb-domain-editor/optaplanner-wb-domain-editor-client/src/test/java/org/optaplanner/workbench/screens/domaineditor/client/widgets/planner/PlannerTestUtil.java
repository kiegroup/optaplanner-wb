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

import java.util.HashMap;
import java.util.Map;

import org.kie.workbench.common.services.datamodeller.core.AnnotationDefinition;
import org.kie.workbench.common.services.datamodeller.util.DriverUtils;
import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.solution.PlanningEntityCollectionProperty;
import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.optaplanner.core.api.domain.valuerange.ValueRangeProvider;
import org.optaplanner.core.api.domain.variable.PlanningVariable;

public class PlannerTestUtil {

    public static Map<String, AnnotationDefinition> getPlannerAnnotations() {
        HashMap<String, AnnotationDefinition> annotations = new HashMap<String, AnnotationDefinition>(  );

        annotations.put( PlanningEntity.class.getName(),
                DriverUtils.buildAnnotationDefinition( PlanningEntity.class ) );
        annotations.put( PlanningSolution.class.getName(),
                DriverUtils.buildAnnotationDefinition( PlanningSolution.class ) );
        annotations.put( PlanningVariable.class.getName(),
                DriverUtils.buildAnnotationDefinition( PlanningVariable.class ) );
        annotations.put( ValueRangeProvider.class.getName(),
                DriverUtils.buildAnnotationDefinition( ValueRangeProvider.class ) );
        annotations.put( PlanningEntityCollectionProperty.class.getName(),
                DriverUtils.buildAnnotationDefinition( PlanningEntityCollectionProperty.class ) );

        return annotations;
    }
}