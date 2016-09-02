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
package org.optaplanner.workbench.screens.solver.client.editor;

import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import com.google.gwt.core.client.GWT;
import org.jboss.errai.common.client.api.IsElement;
import org.jboss.errai.common.client.dom.HTMLElement;
import org.optaplanner.workbench.screens.solver.client.resources.i18n.SolverEditorLookupConstants;
import org.optaplanner.workbench.screens.solver.model.ConstructionHeuristicPhaseConfigModel;
import org.optaplanner.workbench.screens.solver.model.ConstructionHeuristicTypeModel;
import org.uberfire.commons.data.Pair;

@Dependent
public class ConstructionHeuristicForm implements IsElement {

    private final ConstructionHeuristicFormView view;
    private ConstructionHeuristicPhaseConfigModel model;

    private PhaseConfigForm phaseConfigForm;

    private SolverEditorLookupConstants solverEditorLookupConstants = GWT.create( SolverEditorLookupConstants.class );

    @Inject
    public ConstructionHeuristicForm( final ConstructionHeuristicFormView view ) {
        this.view = view;
        view.setPresenter( this );
        List<Pair<String, String>> constructionHeuristicTypeOptions = new ArrayList<>();
        for ( ConstructionHeuristicTypeModel constructionHeuristicTypeModel : ConstructionHeuristicTypeModel.values() ) {
            Pair<String, String> option = new Pair<>( solverEditorLookupConstants.getString( constructionHeuristicTypeModel.name() ), constructionHeuristicTypeModel.name() );
            constructionHeuristicTypeOptions.add( option );
        }
        view.initConstructionHeuristicTypeSelectOptions( constructionHeuristicTypeOptions );
    }

    public void setPhaseConfigForm( PhaseConfigForm phaseConfigForm ) {
        this.phaseConfigForm = phaseConfigForm;
    }

    public void onConstructionHeuristicTypeSelected( String constructionHeuristicType ) {
        model.setConstructionHeuristicType( ConstructionHeuristicTypeModel.valueOf( constructionHeuristicType ) );
    }

    public void onConstructionHeuristicRemoved() {
        phaseConfigForm.removeConstructionHeuristic( this );
    }

    public ConstructionHeuristicPhaseConfigModel getModel() {
        return model;
    }

    public void setModel( ConstructionHeuristicPhaseConfigModel model ) {
        this.model = model;

        if ( model.getConstructionHeuristicType() == null ) {
            model.setConstructionHeuristicType( ConstructionHeuristicTypeModel.FIRST_FIT );
        }

        view.setSelectedConstructionHeuristicType( model.getConstructionHeuristicType().name() );
    }

    @Override
    public HTMLElement getElement() {
        return view.getElement();
    }
}
