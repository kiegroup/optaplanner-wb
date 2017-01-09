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

package org.optaplanner.workbench.screens.domaineditor.client.widgets.planner;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import com.google.gwt.event.dom.client.ClickEvent;
import org.jboss.errai.common.client.dom.Span;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.EventHandler;
import org.jboss.errai.ui.shared.api.annotations.Templated;
import org.uberfire.mvp.Command;

@Dependent
@Templated("DataObjectFieldPickerItemLabelViewImpl.html")
public class DataObjectFieldPickerItemLabelViewImpl implements DataObjectFieldPickerItemLabelView {

    @Inject
    @DataField("name")
    Span name;

    @Inject
    @DataField("removeIcon")
    Span removeIcon;

    private DataObjectFieldPickerItemView.Presenter presenter;

    private Command removeLabelCommand;

    @Override
    public void setName( final String name ) {
        this.name.setTextContent( name );
    }

    @Override
    public void setRemoveLabelCommand( final Command removeLabelCommand ) {
        this.removeLabelCommand = removeLabelCommand;
    }

    @EventHandler("removeIcon")
    public void onRemoveIconClicked( final ClickEvent event ) {
        if ( removeLabelCommand != null ) {
            removeLabelCommand.execute();
        }
    }

}
