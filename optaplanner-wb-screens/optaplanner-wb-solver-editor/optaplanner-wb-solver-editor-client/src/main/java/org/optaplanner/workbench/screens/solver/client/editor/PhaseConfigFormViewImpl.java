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

import javax.inject.Inject;

import com.google.gwt.user.client.ui.Composite;
import org.gwtbootstrap3.client.ui.AnchorListItem;
import org.gwtbootstrap3.client.ui.DropDownMenu;
import org.jboss.errai.common.client.dom.Div;
import org.jboss.errai.common.client.dom.HTMLElement;
import org.jboss.errai.ui.client.local.spi.TranslationService;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.Templated;
import org.optaplanner.workbench.screens.solver.client.resources.i18n.SolverEditorConstants;

@Templated
public class PhaseConfigFormViewImpl extends Composite implements PhaseConfigFormView {

    @DataField("view")
    @Inject
    Div view;

    @DataField("addPhaseButtonGroup")
    @Inject
    Div addPhaseButtonGroup;

    @DataField("dropdownMenuList")
    DropDownMenu dropdownMenuList;

    @DataField("emptyPhaseConfigurationLabel")
    @Inject
    Div emptyPhaseConfigurationLabel;

    private PhaseConfigForm presenter;

    @Inject
    public PhaseConfigFormViewImpl( final DropDownMenu dropdownMenuList,
                                    final TranslationService translationService ) {
        this.dropdownMenuList = dropdownMenuList;
        AnchorListItem constructionHeuristicListItem = new AnchorListItem( translationService.getTranslation( SolverEditorConstants.PhaseConfigFormViewImplConstructionHeuristic ) );
        constructionHeuristicListItem.addClickHandler( h -> presenter.addConstructionHeuristic() );
        this.dropdownMenuList.add( constructionHeuristicListItem );

        AnchorListItem localSearchListItem = new AnchorListItem( translationService.getTranslation( SolverEditorConstants.PhaseConfigFormViewImplLocalSearch ) );
        localSearchListItem.addClickHandler( h -> presenter.addLocalSearch() );
        this.dropdownMenuList.add( localSearchListItem );

        // TODO add Exhaustive search, ...
    }

    @Override
    public void setPresenter( final PhaseConfigForm presenter ) {
        this.presenter = presenter;
    }

    @Override
    public void addConstructionHeuristic( final HTMLElement element ) {
        view.insertBefore( element,
                           addPhaseButtonGroup );
    }

    @Override
    public void removeConstructionHeuristic( final HTMLElement element ) {
        view.removeChild( element );
    }

    @Override
    public void addLocalSearch( final HTMLElement element ) {
        view.insertBefore( element,
                           addPhaseButtonGroup );
    }

    @Override
    public void removeLocalSearch( final HTMLElement element ) {
        view.removeChild( element );
    }

    @Override
    public void displayEmptyPhaseConfigurationLabel( final boolean visible ) {
        emptyPhaseConfigurationLabel.setHidden( !visible );
    }
}
