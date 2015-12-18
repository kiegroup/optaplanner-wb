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

package org.optaplanner.workbench.screens.domaineditor.backend.server;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.enterprise.context.ApplicationScoped;

import org.kie.workbench.common.screens.datamodeller.backend.server.handler.DomainHandler;
import org.kie.workbench.common.services.datamodeller.core.AnnotationDefinition;
import org.kie.workbench.common.services.datamodeller.core.DataObject;
import org.kie.workbench.common.services.datamodeller.util.DriverUtils;
import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.solution.PlanningEntityCollectionProperty;
import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.optaplanner.core.api.domain.valuerange.ValueRangeProvider;
import org.optaplanner.core.api.domain.variable.PlanningVariable;

@ApplicationScoped
public class PlannerDomainHandler implements DomainHandler {


    private static List<AnnotationDefinition> domainAnnotations = new ArrayList<AnnotationDefinition>( );
    static {
        //Planner managed annotations
        domainAnnotations.add( DriverUtils.buildAnnotationDefinition( PlanningEntity.class ) );
        domainAnnotations.add( DriverUtils.buildAnnotationDefinition( PlanningSolution.class ) );
        domainAnnotations.add( DriverUtils.buildAnnotationDefinition( PlanningVariable.class ) );
        domainAnnotations.add( DriverUtils.buildAnnotationDefinition( ValueRangeProvider.class ) );
        domainAnnotations.add( DriverUtils.buildAnnotationDefinition( PlanningEntityCollectionProperty.class ) );
    }

    @Override
    public void setDefaultValues( DataObject dataObject, Map<String, Object> options ) {
        //This domain doesn't do any by default processing at data object creation time
    }

    @Override
    public List<AnnotationDefinition> getManagedAnnotations() {
        return domainAnnotations;
    }
}
