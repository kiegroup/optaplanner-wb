/*
 * Copyright 2015 JBoss Inc
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
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;
import org.optaplanner.workbench.screens.solver.model.ScoreDefinitionTypeModel;

public class ScoreDirectorFactoryFormViewImpl
        extends Composite
        implements ScoreDirectorFactoryFormView {

    interface Binder
            extends
            UiBinder<Widget, ScoreDirectorFactoryFormViewImpl> {

    }

    private static Binder uiBinder = GWT.create( Binder.class );

    @UiField
    ListBox scoreDefinitionType;

    @UiField(provided = true)
    FindFile findFile;

    private ScoreDirectorFactoryForm presenter;

    @Inject
    public ScoreDirectorFactoryFormViewImpl( final FindFile findFile ) {
        this.findFile = findFile;
        this.findFile.addValueChangeHandler( new ValueChangeHandler<String>() {
            @Override
            public void onValueChange( ValueChangeEvent<String> event ) {
                presenter.onFileNameChange( event.getValue() );
            }
        } );
        initWidget( uiBinder.createAndBindUi( this ) );
    }

    @Override
    public void setPresenter( ScoreDirectorFactoryForm form ) {
        this.presenter = form;
    }

    @Override
    public void setSelectedScoreDefinitionType( ScoreDefinitionTypeModel type ) {
        for (int i = 0; i < scoreDefinitionType.getItemCount(); i++) {
            if ( scoreDefinitionType.getItemText( i ).equals( type.name() ) ) {
                scoreDefinitionType.setSelectedIndex( i );
                break;
            }
        }
    }

    @Override
    public void setScoreDrl( String fileName ) {
        findFile.setFileName( fileName );
    }

    @Override
    public void addScoreDefinitionType( final ScoreDefinitionTypeModel type ) {
        scoreDefinitionType.addItem( type.toString() );
    }

    @UiHandler("scoreDefinitionType")
    public void handleChange( ChangeEvent event ) {
        presenter.onScoreDefinitionTypeSelected( scoreDefinitionType.getSelectedItemText() );
    }

}
