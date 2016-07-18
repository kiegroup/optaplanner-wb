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
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.Widget;
import org.gwtbootstrap3.client.ui.Label;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.Templated;
import org.optaplanner.workbench.screens.solver.client.resources.SolverEditorResources;

@Templated
public class TerminationConfigFormViewImpl
        extends Composite
        implements TerminationConfigFormView {

    @DataField("tree")
    Tree tree;

    @Inject
    @DataField("emptyTreeLabel")
    Label emptyTreeLabel;

    public TerminationConfigFormViewImpl() {
    }

    @Inject
    public TerminationConfigFormViewImpl( final Tree tree ) {
        this.tree = tree;
        this.tree.setStyleName( SolverEditorResources.INSTANCE.CSS().terminationTree() );
    }

    @Override
    public void initTree( TreeItem rootTreeItem ) {
        this.tree.clear();
        this.tree.addItem( rootTreeItem );
    }

    @Override
    public void displayEmptyTreeLabel( boolean visible ) {
        emptyTreeLabel.setVisible( visible );
    }

    public void refreshTree() {
        refreshNestedTreeItemBorders();
    }

    public static native void refreshNestedTreeItemBorders() /*-{
        $wnd.jQuery("#tree div:has(> table)").css("border-left", "1px solid #e5e5e5");
        $wnd.jQuery("#tree div:has(> table)").not(':last-child').css("border-bottom", "1px solid #e5e5e5");
        $wnd.jQuery("#tree div:has(> div.gwt-TreeItem)").not(':last-child').css("border-bottom", "1px solid #e5e5e5");
    }-*/;

}
