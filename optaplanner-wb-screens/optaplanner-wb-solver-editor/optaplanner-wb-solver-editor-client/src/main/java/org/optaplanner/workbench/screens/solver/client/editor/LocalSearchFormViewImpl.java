/*
 * Copyright 2017 Red Hat, Inc. and/or its affiliates.
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
public class LocalSearchFormViewImpl implements LocalSearchFormView {

    @DataField("view")
    @Inject
    Div view;

    @DataField("localSearchTypeSelect")
    Select localSearchTypeSelect;

    @DataField("removeLocalSearch")
    @Inject
    Span removeLocalSearch;

    private LocalSearchForm presenter;

    @Inject
    public LocalSearchFormViewImpl( final Select localSearchTypeSelect ) {
        this.localSearchTypeSelect = localSearchTypeSelect;

        this.localSearchTypeSelect.setWidth( "auto" );
    }

    @Override
    public void setPresenter( final LocalSearchForm presenter ) {
        this.presenter = presenter;
    }

    @EventHandler("localSearchTypeSelect")
    public void onLocalSearchTypeSelected( final ChangeEvent event ) {
        presenter.onLocalSearchTypeSelected( localSearchTypeSelect.getValue() );
    }

    @EventHandler("removeLocalSearch")
    public void onRemoveLocalSearchClicked( final ClickEvent event ) {
        presenter.onLocalSearchRemoved();
    }

    @Override
    public void initLocalSearchTypeSelectOptions( final List<Pair<String, String>> options ) {
        UIUtil.initList( localSearchTypeSelect,
                         options,
                         false );
    }

    @Override
    public void setSelectedLocalSearchType( final String localSearchType ) {
        UIUtil.setSelectedValue( localSearchTypeSelect,
                                 localSearchType );
    }

    @Override
    public HTMLElement getElement() {
        return view;
    }
}
