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

import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TreeItem;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.constants.IconType;
import org.optaplanner.workbench.screens.solver.client.resources.i18n.SolverEditorConstants;
import org.optaplanner.workbench.screens.solver.model.TerminationConfigModel;
import org.optaplanner.workbench.screens.solver.model.TerminationConfigOption;

public class TerminationTreeItemContent extends Composite {

    private Label terminationTypeLabel = new Label();
    private Label terminationValueLabel = new Label();
    private Button addTerminationButton = new Button();
    private Button removeTerminationButton = new Button();

    private TerminationConfigModel model;
    private TreeItem treeItem;

    public TerminationTreeItemContent( TerminationConfigForm terminationConfigForm, TreeItem treeItem, TerminationConfigOption terminationConfigOption, TerminationConfigModel model, String value ) {
        this.model = model;
        this.treeItem = treeItem;

        boolean root = treeItem.getParentItem() == null;

        terminationTypeLabel.setText( terminationConfigForm.getModelManager( terminationConfigOption ).getLocalizedTerminationConfigOption() );
        terminationTypeLabel.setVisible( !TerminationConfigOption.NESTED.equals( terminationConfigOption ) );

        terminationValueLabel.setText( value );
        terminationValueLabel.setVisible( !root && !TerminationConfigOption.NESTED.equals( terminationConfigOption ) );

        addTerminationButton.setText( SolverEditorConstants.INSTANCE.addTermination() );
        addTerminationButton.setIcon( IconType.PLUS );
        addTerminationButton.setVisible( TerminationConfigOption.NESTED.equals( terminationConfigOption ) );
        addTerminationButton.addClickHandler( h -> {
            terminationConfigForm.getTerminationPopup().init( this, terminationConfigForm.getAssignableTerminationConfigOptions( model ) );
            terminationConfigForm.getTerminationPopup().show();
        } );

        removeTerminationButton.setText( SolverEditorConstants.INSTANCE.delete() );
        removeTerminationButton.setIcon( IconType.TRASH );
        removeTerminationButton.setType( ButtonType.DANGER );
        removeTerminationButton.getElement().getStyle().setFloat( Style.Float.RIGHT );
        removeTerminationButton.setVisible( !root );
        removeTerminationButton.addClickHandler( h -> {
            TerminationTreeItemContent parent = ( treeItem.getParentItem() == null ? this : (TerminationTreeItemContent) treeItem.getParentItem().getUserObject() );
            terminationConfigForm.getModelManager( terminationConfigOption ).setTerminationValue( model, parent.getModel(), null );
            treeItem.remove();
        } );

        HorizontalPanel wrappingPanel = new HorizontalPanel();
        wrappingPanel.getElement().getStyle().setWidth( 100, Style.Unit.PCT );

        HorizontalPanel leftPanel = new HorizontalPanel();
        leftPanel.add( addTerminationButton );
        leftPanel.add( terminationTypeLabel );

        wrappingPanel.add( leftPanel );

        HorizontalPanel rightPanel = new HorizontalPanel();
        rightPanel.getElement().getStyle().setFloat( Style.Float.RIGHT );
        rightPanel.getElement().getStyle().setWidth( 250, Style.Unit.PX );
        rightPanel.add( terminationValueLabel );
        rightPanel.add( removeTerminationButton );

        wrappingPanel.add( rightPanel );

        initWidget( wrappingPanel );
    }

    public TreeItem getTreeItem() {
        return treeItem;
    }

    public TerminationConfigModel getModel() {
        return model;
    }
}
