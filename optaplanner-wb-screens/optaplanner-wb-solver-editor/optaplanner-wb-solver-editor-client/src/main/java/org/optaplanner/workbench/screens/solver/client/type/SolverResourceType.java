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
package org.optaplanner.workbench.screens.solver.client.type;

import javax.enterprise.context.ApplicationScoped;

import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.IsWidget;
import org.optaplanner.workbench.screens.solver.client.resources.SolverEditorResources;
import org.optaplanner.workbench.screens.solver.client.resources.i18n.SolverEditorConstants;
import org.optaplanner.workbench.screens.solver.type.SolverResourceTypeDefinition;
import org.uberfire.client.workbench.type.ClientResourceType;

@ApplicationScoped
public class SolverResourceType
        extends SolverResourceTypeDefinition
        implements ClientResourceType {

    @Override
    public IsWidget getIcon() {
        return new Image( SolverEditorResources.INSTANCE.images().typeSolver() );
    }

    @Override
    public String getDescription() {
        String desc = SolverEditorConstants.INSTANCE.solverResourceTypeDescription();
        if ( desc == null || desc.isEmpty() ) {
            return super.getDescription();
        }
        return desc;
    }
}
