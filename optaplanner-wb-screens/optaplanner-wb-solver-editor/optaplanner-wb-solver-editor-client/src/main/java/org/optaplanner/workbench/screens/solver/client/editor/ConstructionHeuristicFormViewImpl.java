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
import org.gwtbootstrap3.extras.select.client.ui.Select;
import org.jboss.errai.common.client.dom.Div;
import org.jboss.errai.common.client.dom.HTMLElement;
import org.jboss.errai.common.client.dom.Span;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.EventHandler;
import org.jboss.errai.ui.shared.api.annotations.Templated;
import org.kie.workbench.common.screens.datamodeller.client.util.UIUtil;
import org.uberfire.commons.data.Pair;

@Dependent
@Templated
public class ConstructionHeuristicFormViewImpl implements ConstructionHeuristicFormView {

    @DataField("view")
    @Inject
    Div view;

    @DataField("constructionHeuristicTypeSelect")
    Select constructionHeuristicTypeSelect;

    @DataField("entitySorterMannerSelect")
    Select entitySorterMannerSelect;

    @DataField("valueSorterMannerSelect")
    Select valueSorterMannerSelect;

    @DataField("removeConstructionHeuristic")
    @Inject
    Span removeConstructionHeuristic;

    private ConstructionHeuristicForm presenter;

    @Inject
    public ConstructionHeuristicFormViewImpl(final Select constructionHeuristicTypeSelect,
                                             final Select entitySorterMannerSelect,
                                             final Select valueSorterMannerSelect) {
        this.constructionHeuristicTypeSelect = constructionHeuristicTypeSelect;
        this.entitySorterMannerSelect = entitySorterMannerSelect;
        this.valueSorterMannerSelect = valueSorterMannerSelect;

        this.constructionHeuristicTypeSelect.setWidth( "auto" );
        this.entitySorterMannerSelect.setWidth( "auto" );
        this.valueSorterMannerSelect.setWidth( "auto" );
    }

    @Override
    public void setPresenter( ConstructionHeuristicForm presenter ) {
        this.presenter = presenter;
    }

    @EventHandler("constructionHeuristicTypeSelect")
    public void onConstructionHeuristicTypeSelected( ChangeEvent event ) {
        presenter.onConstructionHeuristicTypeSelected( constructionHeuristicTypeSelect.getValue() );
    }

    @EventHandler("entitySorterMannerSelect")
    public void onEntitySorterMannerSelected( ChangeEvent event ) {
        presenter.onEntitySorterMannerSelected( entitySorterMannerSelect.getValue() );
    }

    @EventHandler("valueSorterMannerSelect")
    public void onValueSorterMannerSelected( ChangeEvent event ) {
        presenter.onValueSorterMannerSelected( valueSorterMannerSelect.getValue() );
    }

    @EventHandler("removeConstructionHeuristic")
    public void onRemoveConstructionHeuristicClicked( ClickEvent event ) {
        presenter.onConstructionHeuristicRemoved();
    }

    @Override
    public void initConstructionHeuristicTypeSelectOptions( List<Pair<String, String>> options ) {
        UIUtil.initList( constructionHeuristicTypeSelect, options, false );
    }

    @Override
    public void initEntitySorterMannerSelectOptions( List<Pair<String, String>> options ) {
        UIUtil.initList( entitySorterMannerSelect, options, false );
    }

    @Override
    public void initValueSorterMannerSelectOptions( List<Pair<String, String>> options ) {
        UIUtil.initList( valueSorterMannerSelect, options, false );
    }

    @Override
    public void setSelectedConstructionHeuristicType( String constructionHeuristicType ) {
        UIUtil.setSelectedValue( constructionHeuristicTypeSelect, constructionHeuristicType );
    }

    @Override
    public void setSelectedEntitySorterManner( String entitySorterManner ) {
        UIUtil.setSelectedValue( entitySorterMannerSelect, entitySorterManner );
    }

    @Override
    public void setSelectedValueSorterManner( String valueSorterManner ) {
        UIUtil.setSelectedValue( valueSorterMannerSelect, valueSorterManner);
    }

    @Override
    public HTMLElement getElement() {
        return view;
    }
}
