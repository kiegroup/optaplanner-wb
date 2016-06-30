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

import com.google.gwtmockito.GwtMockitoTestRunner;
import org.jboss.errai.ioc.client.container.SyncBeanDef;
import org.jboss.errai.ioc.client.container.SyncBeanManager;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.optaplanner.workbench.screens.solver.model.ConstructionHeuristicPhaseConfigModel;
import org.optaplanner.workbench.screens.solver.model.PhaseConfigModel;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(GwtMockitoTestRunner.class)
public class PhaseConfigFormTest {

    @Mock
    private PhaseConfigFormView view;

    @Mock
    private SyncBeanManager syncBeanManager;

    @Mock
    private SyncBeanDef<ConstructionHeuristicForm> syncBeanDef;

    @Mock
    private ConstructionHeuristicForm constructionHeuristicForm;

    @Mock
    private List model;

    private PhaseConfigForm phaseConfigForm;

    @Before
    public void setUp() {
        phaseConfigForm = new PhaseConfigForm( view, syncBeanManager );
        phaseConfigForm.setModel( new ArrayList<>() );
    }

    @Test
    public void setPresenter() {
        Mockito.verify( view ).setPresenter( phaseConfigForm );
    }

    @Test
    public void setModel() {
        List<PhaseConfigModel> phaseConfigModelList = new ArrayList<>();
        phaseConfigModelList.add( new ConstructionHeuristicPhaseConfigModel() );
        phaseConfigModelList.add( new ConstructionHeuristicPhaseConfigModel() );

        when( syncBeanManager.lookupBean( ConstructionHeuristicForm.class ) ).thenReturn( syncBeanDef );
        when( syncBeanDef.newInstance() ).thenReturn( constructionHeuristicForm );

        phaseConfigForm.setModel( phaseConfigModelList );

        verify( view, times( 2 ) ).addConstructionHeuristic( any() );
    }

    @Test
    public void addConstructionHeuristic() {
        addConstructionHeuristic( true );
    }

    @Test
    public void addConstructionHeuristicExisting() {
        addConstructionHeuristic( false );
    }

    private void addConstructionHeuristic( boolean newConstructionHeuristic ) {
        when( syncBeanManager.lookupBean( ConstructionHeuristicForm.class ) ).thenReturn( syncBeanDef );
        when( syncBeanDef.newInstance() ).thenReturn( constructionHeuristicForm );

        if ( newConstructionHeuristic ) {
            phaseConfigForm.addConstructionHeuristic();
        } else {
            phaseConfigForm.addConstructionHeuristic( new ConstructionHeuristicPhaseConfigModel() );
        }
        Mockito.verify( view ).addConstructionHeuristic( any() );
    }

    @Test
    public void removeConstructionHeuristic() {
        phaseConfigForm.removeConstructionHeuristic( constructionHeuristicForm );
        verify( view ).removeConstructionHeuristic( constructionHeuristicForm.getElement() );
    }
}
