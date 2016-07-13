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
package org.optaplanner.workbench.screens.solver.client.editor;

import javax.inject.Inject;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.SelectionChangeEvent;
import org.kie.workbench.common.widgets.client.widget.KSessionSelector;
import org.uberfire.backend.vfs.Path;

public class ScoreDirectorFactoryFormViewImpl
        extends Composite
        implements ScoreDirectorFactoryFormView {

    interface Binder
            extends
            UiBinder<Widget, ScoreDirectorFactoryFormViewImpl> {

    }

    private static Binder uiBinder = GWT.create( Binder.class );

    @UiField( provided = true )
    KSessionSelector kSessionSelector;

    private ScoreDirectorFactoryForm presenter;

    @Inject
    public ScoreDirectorFactoryFormViewImpl( final KSessionSelector kSessionSelector ) {
        this.kSessionSelector = kSessionSelector;
        this.kSessionSelector.addSelectionChangeHandler( new SelectionChangeEvent.Handler() {
            @Override
            public void onSelectionChange( final SelectionChangeEvent selectionChangeEvent ) {
                presenter.onKSessionNameChange( kSessionSelector.getSelectedKSessionName() );
            }
        } );

        initWidget( uiBinder.createAndBindUi( this ) );
    }

    @Override
    public void setPresenter( final ScoreDirectorFactoryForm form ) {
        this.presenter = form;
    }

    @Override
    public void setKSession( final String kSessionName,
                             final Path path ) {
        kSessionSelector.init( path,
                               kSessionName );
    }

}
