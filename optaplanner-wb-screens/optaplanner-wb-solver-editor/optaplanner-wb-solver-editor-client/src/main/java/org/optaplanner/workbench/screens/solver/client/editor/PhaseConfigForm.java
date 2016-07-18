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
import javax.annotation.PreDestroy;
import javax.inject.Inject;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import org.jboss.errai.ioc.client.container.SyncBeanManager;
import org.optaplanner.workbench.screens.solver.model.ConstructionHeuristicPhaseConfigModel;
import org.optaplanner.workbench.screens.solver.model.PhaseConfigModel;

public class PhaseConfigForm implements IsWidget {

    private List<PhaseConfigModel> model;

    private List phaseFormList = new ArrayList();

    private PhaseConfigFormView view;

    private SyncBeanManager syncBeanManager;

    @Inject
    public PhaseConfigForm( final PhaseConfigFormView view,
                            final SyncBeanManager syncBeanManager ) {
        this.view = view;
        this.syncBeanManager = syncBeanManager;

        view.setPresenter( this );
    }

    public void addConstructionHeuristic() {
        ConstructionHeuristicPhaseConfigModel constructionHeuristicPhaseConfigModel = new ConstructionHeuristicPhaseConfigModel();
        model.add( constructionHeuristicPhaseConfigModel );
        addConstructionHeuristic( constructionHeuristicPhaseConfigModel );
    }

    public void addConstructionHeuristic( ConstructionHeuristicPhaseConfigModel constructionHeuristicPhaseConfigModel ) {
        ConstructionHeuristicForm constructionHeuristicForm = syncBeanManager.lookupBean( ConstructionHeuristicForm.class ).newInstance();
        constructionHeuristicForm.setPhaseConfigForm( this );
        constructionHeuristicForm.setModel( constructionHeuristicPhaseConfigModel );
        phaseFormList.add( constructionHeuristicForm );
        view.addConstructionHeuristic( constructionHeuristicForm.getElement() );
    }

    public void removeConstructionHeuristic( ConstructionHeuristicForm constructionHeuristicForm ) {
        phaseFormList.remove( constructionHeuristicForm );
        view.removeConstructionHeuristic( constructionHeuristicForm.getElement() );
        model.remove( constructionHeuristicForm.getModel() );
    }

    public List<PhaseConfigModel> getModel() {
        return model;
    }

    public void setModel( List<PhaseConfigModel> model ) {
        this.model = model;
        for ( PhaseConfigModel phaseConfigModel : model ) {
            if ( phaseConfigModel instanceof ConstructionHeuristicPhaseConfigModel ) {
                addConstructionHeuristic( (ConstructionHeuristicPhaseConfigModel) phaseConfigModel );
            }
        }
    }

    @PreDestroy
    public void destroy() {
        for ( Object phaseForm : phaseFormList ) {
            syncBeanManager.destroyBean( phaseForm );
        }
    }

    @Override
    public Widget asWidget() {
        return view.asWidget();
    }
}
