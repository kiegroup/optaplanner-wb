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

import java.util.List;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import org.gwtbootstrap3.extras.select.client.ui.Option;
import org.gwtbootstrap3.extras.select.client.ui.Select;
import org.jboss.errai.common.client.dom.Div;
import org.jboss.errai.common.client.dom.HTMLElement;
import org.jboss.errai.common.client.dom.Span;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.EventHandler;
import org.jboss.errai.ui.shared.api.annotations.Templated;
import org.uberfire.commons.data.Pair;

@Dependent
@Templated
public class ConstructionHeuristicFormViewImpl implements ConstructionHeuristicFormView {

    @DataField("view")
    @Inject
    Div view;

    @DataField("constructionHeuristicTypeSelect")
    Select constructionHeuristicTypeSelect;

    @DataField("removeConstructionHeuristic")
    @Inject
    Span removeConstructionHeuristic;

    private ConstructionHeuristicForm presenter;

    @Inject
    public ConstructionHeuristicFormViewImpl(final Select constructionHeuristicTypeSelect) {
        this.constructionHeuristicTypeSelect = constructionHeuristicTypeSelect;
        this.constructionHeuristicTypeSelect.setWidth( "auto" );
    }

    @Override
    public void setPresenter( ConstructionHeuristicForm presenter ) {
        this.presenter = presenter;
    }

    @EventHandler("constructionHeuristicTypeSelect")
    public void onConstructionHeuristicTypeSelected( ChangeEvent event ) {
        presenter.onConstructionHeuristicTypeSelected( constructionHeuristicTypeSelect.getValue() );
    }

    @EventHandler("removeConstructionHeuristic")
    public void onRemoveConstructionHeuristicClicked( ClickEvent event ) {
        presenter.onConstructionHeuristicRemoved();
    }

    @Override
    public void initConstructionHeuristicTypeSelectOptions( List<Pair<String, String>> options ) {
        for ( Pair<String, String> option : options ) {
            Option selectOption = new Option();
            selectOption.setText( option.getK1() );
            selectOption.setValue( option.getK2() );
            constructionHeuristicTypeSelect.add( selectOption );
        }
        constructionHeuristicTypeSelect.refresh();
    }

    @Override
    public void setSelectedConstructionHeuristicType( String constructionHeuristicType ) {
        constructionHeuristicTypeSelect.setValue( constructionHeuristicType );
        constructionHeuristicTypeSelect.refresh();
    }

    @Override
    public HTMLElement getElement() {
        return view;
    }
}
