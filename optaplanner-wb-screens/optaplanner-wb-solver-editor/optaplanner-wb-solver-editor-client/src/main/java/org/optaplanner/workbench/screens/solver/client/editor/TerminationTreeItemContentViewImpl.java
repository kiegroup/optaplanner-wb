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

import java.util.HashMap;
import java.util.Map;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import org.gwtbootstrap3.client.ui.AnchorListItem;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.DropDownMenu;
import org.jboss.errai.common.client.dom.CheckboxInput;
import org.jboss.errai.common.client.dom.Div;
import org.jboss.errai.common.client.dom.HTMLElement;
import org.jboss.errai.common.client.dom.NumberInput;
import org.jboss.errai.common.client.dom.Select;
import org.jboss.errai.common.client.dom.TextInput;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.EventHandler;
import org.jboss.errai.ui.shared.api.annotations.Templated;
import org.optaplanner.workbench.screens.solver.client.resources.i18n.SolverEditorConstants;
import org.optaplanner.workbench.screens.solver.model.TerminationCompositionStyleModel;
import org.optaplanner.workbench.screens.solver.model.TerminationConfigOption;
import org.uberfire.client.views.pfly.widgets.FormLabelHelp;
import org.uberfire.client.views.pfly.widgets.HelpIcon;

@Dependent
@Templated
public class TerminationTreeItemContentViewImpl implements TerminationTreeItemContentView {

    private static final SolverEditorConstants I18N_CONSTANTS = GWT.create( SolverEditorConstants.class );

    @Inject
    @DataField("view")
    Div view;

    @Inject
    @DataField("addTerminationDiv")
    Div addTerminationDiv;

    @Inject
    @DataField("formLabelDiv")
    Div formLabelDiv;

    @Inject
    @DataField("formLabel")
    FormLabelHelp formLabel;

    @Inject
    @DataField("dropDownHelpIcon")
    HelpIcon dropDownHelpIcon;

    @Inject
    @DataField("timeSpentDiv")
    Div timeSpentDiv;

    @DataField("dropdownMenuList")
    DropDownMenu dropdownMenuList;

    @Inject
    @DataField("terminationCompositionStyleSelect")
    Select terminationCompositionStyleSelect;

    @Inject
    @DataField("daysSpentInput")
    NumberInput daysSpentInput;

    @Inject
    @DataField("hoursSpentInput")
    NumberInput hoursSpentInput;

    @Inject
    @DataField("minutesSpentInput")
    NumberInput minutesSpentInput;

    @Inject
    @DataField("secondsSpentInput")
    NumberInput secondsSpentInput;

    @Inject
    @DataField("millisecondsSpentInput")
    NumberInput millisecondsSpentInput;

    @Inject
    @DataField("unimprovedTimeSpentDiv")
    Div unimprovedTimeSpentDiv;

    @Inject
    @DataField("unimprovedDaysSpentInput")
    NumberInput unimprovedDaysSpentInput;

    @Inject
    @DataField("unimprovedHoursSpentInput")
    NumberInput unimprovedHoursSpentInput;

    @Inject
    @DataField("unimprovedMinutesSpentInput")
    NumberInput unimprovedMinutesSpentInput;

    @Inject
    @DataField("unimprovedSecondsSpentInput")
    NumberInput unimprovedSecondsSpentInput;

    @Inject
    @DataField("unimprovedMillisecondsSpentInput")
    NumberInput unimprovedMillisecondsSpentInput;

    @Inject
    @DataField("stepCountLimitInput")
    NumberInput stepCountLimitInput;

    @Inject
    @DataField("unimprovedStepCountLimitInput")
    NumberInput unimprovedStepCountLimitInput;

    @Inject
    @DataField("scoreCalculationCountLimitInput")
    NumberInput scoreCalculationCountLimitInput;

    @Inject
    @DataField("bestScoreInput")
    TextInput bestScoreInput;

    @Inject
    @DataField("bestScoreFeasibleInput")
    CheckboxInput bestScoreFeasibleInput;

    @DataField("removeTerminationButton")
    Button removeTerminationButton;

    private TerminationTreeItemContent presenter;
    private Map<TerminationConfigOption, String> dropDownNameMap = new HashMap<>();

    @Inject
    public TerminationTreeItemContentViewImpl( final Button removeTerminationButton,
                                               final DropDownMenu dropdownMenuList) {
        this.removeTerminationButton = removeTerminationButton;
        this.dropdownMenuList = dropdownMenuList;

        removeTerminationButton.addClickHandler( h -> {
            presenter.removeTreeItem();
        } );
        initDropDownList();
    }

    private void initDropDownList() {
        dropDownNameMap.put( TerminationConfigOption.MILLISECONDS_SPENT_LIMIT, I18N_CONSTANTS.TimeSpent() );
        AnchorListItem li1 = new AnchorListItem( I18N_CONSTANTS.TimeSpent() );
        li1.addClickHandler( h -> {
            presenter.onTerminationTypeSelected( TerminationConfigOption.MILLISECONDS_SPENT_LIMIT.name() );
        } );
        dropdownMenuList.add( li1 );
        dropDownNameMap.put( TerminationConfigOption.UNIMPROVED_MILLISECONDS_SPENT_LIMIT, I18N_CONSTANTS.UnimprovedTimeSpent() );
        AnchorListItem li2 = new AnchorListItem( I18N_CONSTANTS.UnimprovedTimeSpent() );
        li2.addClickHandler( h -> {
            presenter.onTerminationTypeSelected( TerminationConfigOption.UNIMPROVED_MILLISECONDS_SPENT_LIMIT.name() );
        } );
        dropdownMenuList.add( li2 );

        dropDownNameMap.put( TerminationConfigOption.BEST_SCORE_LIMIT, I18N_CONSTANTS.BestScoreLimit() );
        AnchorListItem li4 = new AnchorListItem( I18N_CONSTANTS.BestScoreLimit() );
        li4.addClickHandler( h -> {
            presenter.onTerminationTypeSelected( TerminationConfigOption.BEST_SCORE_LIMIT.name() );
        } );
        dropdownMenuList.add( li4 );

        dropDownNameMap.put( TerminationConfigOption.BEST_SCORE_FEASIBLE, I18N_CONSTANTS.BestScoreFeasible() );
        AnchorListItem li3 = new AnchorListItem( I18N_CONSTANTS.BestScoreFeasible() );
        li3.addClickHandler( h -> {
            presenter.onTerminationTypeSelected( TerminationConfigOption.BEST_SCORE_FEASIBLE.name() );
        } );
        dropdownMenuList.add( li3 );

        dropDownNameMap.put( TerminationConfigOption.STEP_COUNT_LIMIT, I18N_CONSTANTS.StepCountLimit() );
        AnchorListItem li6 = new AnchorListItem( I18N_CONSTANTS.StepCountLimit() );
        li6.addClickHandler( h -> {
            presenter.onTerminationTypeSelected( TerminationConfigOption.STEP_COUNT_LIMIT.name() );
        } );
        dropdownMenuList.add( li6 );

        dropDownNameMap.put( TerminationConfigOption.UNIMPROVED_STEP_COUNT_LIMIT, I18N_CONSTANTS.UnimprovedStepCountLimit() );
        AnchorListItem li7 = new AnchorListItem( I18N_CONSTANTS.UnimprovedStepCountLimit() );
        li7.addClickHandler( h -> {
            presenter.onTerminationTypeSelected( TerminationConfigOption.UNIMPROVED_STEP_COUNT_LIMIT.name() );
        } );
        dropdownMenuList.add( li7 );

        dropDownNameMap.put( TerminationConfigOption.SCORE_CALCULATION_COUNT_LIMIT, I18N_CONSTANTS.ScoreCalculationCountLimit() );
        AnchorListItem li5 = new AnchorListItem( I18N_CONSTANTS.ScoreCalculationCountLimit() );
        li5.addClickHandler( h -> {
            presenter.onTerminationTypeSelected( TerminationConfigOption.SCORE_CALCULATION_COUNT_LIMIT.name() );
        } );
        dropdownMenuList.add( li5 );

        dropDownNameMap.put( TerminationConfigOption.NESTED, I18N_CONSTANTS.NestedTermination() );
        AnchorListItem li8 = new AnchorListItem( I18N_CONSTANTS.NestedTermination() );
        li8.addClickHandler( h -> {
            presenter.onTerminationTypeSelected( TerminationConfigOption.NESTED.name() );
        } );
        dropdownMenuList.add( li8 );
    }

    @EventHandler("daysSpentInput")
    public void handleDaysSpentInputChange( ChangeEvent event ) {
        boolean isNumeric = daysSpentInput.getValue().matches( "\\d+" );
        if (isNumeric) {
            presenter.onDaysSpentChange( Long.parseLong( daysSpentInput.getValue() ) );
        } else {
            presenter.onDaysSpentChange( 0l );
            daysSpentInput.setValue( "0" );
        }
    }

    @EventHandler("hoursSpentInput")
    public void handleHoursSpentInputChange( ChangeEvent event ) {
        boolean isNumeric = hoursSpentInput.getValue().matches( "\\d+" );
        if (isNumeric) {
            presenter.onHoursSpentChange( Long.parseLong( hoursSpentInput.getValue() ) );
        } else {
            presenter.onHoursSpentChange( 0l );
            hoursSpentInput.setValue( "0" );
        }
    }

    @EventHandler("minutesSpentInput")
    public void handleMinutesSpentInputChange( ChangeEvent event ) {
        boolean isNumeric = minutesSpentInput.getValue().matches( "\\d+" );
        if (isNumeric) {
            presenter.onMinutesSpentChange( Long.parseLong( minutesSpentInput.getValue() ) );
        } else {
            presenter.onMinutesSpentChange( 0l );
            minutesSpentInput.setValue( "0" );
        }
    }

    @EventHandler("secondsSpentInput")
    public void handleSecondsSpentInputChange( ChangeEvent event ) {
        boolean isNumeric = secondsSpentInput.getValue().matches( "\\d+" );
        if (isNumeric) {
            presenter.onSecondsSpentChange( Long.parseLong( secondsSpentInput.getValue() ) );
        } else {
            presenter.onSecondsSpentChange( 0l );
            secondsSpentInput.setValue( "0" );
        }
    }

    @EventHandler("millisecondsSpentInput")
    public void handleMilliSecondsSpentInputChange( ChangeEvent event ) {
        boolean isNumeric = millisecondsSpentInput.getValue().matches( "\\d+" );
        if (isNumeric) {
            presenter.onMillisecondsSpentChange( Long.parseLong( millisecondsSpentInput.getValue() ) );
        } else {
            presenter.onMillisecondsSpentChange( 0l );
            millisecondsSpentInput.setValue( "0" );
        }
    }

    @EventHandler("unimprovedDaysSpentInput")
    public void handleUnimprovedDaysSpentInputChange( ChangeEvent event ) {
        boolean isNumeric = unimprovedDaysSpentInput.getValue().matches( "\\d+" );
        if (isNumeric) {
            presenter.onUnimprovedDaysSpentChange( Long.parseLong( unimprovedDaysSpentInput.getValue() ) );
        } else {
            presenter.onUnimprovedDaysSpentChange( 0l );
            unimprovedDaysSpentInput.setValue( "0" );
        }
    }

    @EventHandler("unimprovedHoursSpentInput")
    public void handleUnimprovedHoursSpentInputChange( ChangeEvent event ) {
        boolean isNumeric = unimprovedHoursSpentInput.getValue().matches( "\\d+" );
        if (isNumeric) {
            presenter.onUnimprovedHoursSpentChange( Long.parseLong( unimprovedHoursSpentInput.getValue() ) );
        } else {
            presenter.onUnimprovedHoursSpentChange( 0l );
            unimprovedHoursSpentInput.setValue( "0" );
        }
    }

    @EventHandler("unimprovedMinutesSpentInput")
    public void handleUnimprovedMinutesSpentInputChange( ChangeEvent event ) {
        boolean isNumeric = unimprovedMinutesSpentInput.getValue().matches( "\\d+" );
        if (isNumeric) {
            presenter.onUnimprovedMinutesSpentChange( Long.parseLong( unimprovedMinutesSpentInput.getValue() ) );
        } else {
            presenter.onUnimprovedMinutesSpentChange( 0l );
            unimprovedMinutesSpentInput.setValue( "0" );
        }
    }

    @EventHandler("unimprovedSecondsSpentInput")
    public void handleUnimprovedSecondsSpentInputChange( ChangeEvent event ) {
        boolean isNumeric = unimprovedSecondsSpentInput.getValue().matches( "\\d+" );
        if (isNumeric) {
            presenter.onUnimprovedSecondsSpentChange( Long.parseLong( unimprovedSecondsSpentInput.getValue() ) );
        } else {
            presenter.onUnimprovedSecondsSpentChange( 0l );
            unimprovedSecondsSpentInput.setValue( "0" );
        }
    }

    @EventHandler("unimprovedMillisecondsSpentInput")
    public void handleUnimprovedMilliSecondsSpentInputChange( ChangeEvent event ) {
        boolean isNumeric = unimprovedMillisecondsSpentInput.getValue().matches( "\\d+" );
        if (isNumeric) {
            presenter.onUnimprovedMillisecondsSpentChange( Long.parseLong( unimprovedMillisecondsSpentInput.getValue() ) );
        } else {
            presenter.onUnimprovedMillisecondsSpentChange( 0l );
            unimprovedMillisecondsSpentInput.setValue( "0" );
        }
    }

    @EventHandler("stepCountLimitInput")
    public void onStepCountLimitInputChange( ChangeEvent event ) {
        boolean isNumeric = stepCountLimitInput.getValue().matches( "\\d+" );
        if (isNumeric) {
            presenter.onStepCountLimitChange( Integer.parseInt( stepCountLimitInput.getValue() ) );
        } else {
            presenter.onStepCountLimitChange( 0 );
            stepCountLimitInput.setValue( "0" );
        }
    }

    @EventHandler("unimprovedStepCountLimitInput")
    public void onUnimprovedStepCountLimitInputChange( ChangeEvent event ) {
        boolean isNumeric = unimprovedStepCountLimitInput.getValue().matches( "\\d+" );
        if (isNumeric) {
            presenter.onUnimprovedStepCountLimitChange( Integer.parseInt( unimprovedStepCountLimitInput.getValue() ) );
        } else {
            presenter.onUnimprovedStepCountLimitChange( 0 );
            unimprovedStepCountLimitInput.setValue( "0" );
        }
    }

    @EventHandler("scoreCalculationCountLimitInput")
    public void onScoreCalculationLimitInputChange( ChangeEvent event ) {
        boolean isNumeric = scoreCalculationCountLimitInput.getValue().matches( "\\d+" );
        if (isNumeric) {
            presenter.onScoreCalculationLimitChange( Long.parseLong( scoreCalculationCountLimitInput.getValue() ) );
        } else {
            presenter.onScoreCalculationLimitChange( 0l );
            scoreCalculationCountLimitInput.setValue( "0" );
        }
    }

    @EventHandler("bestScoreInput")
    public void handleBestScoreInputChange( ChangeEvent event ) {
        String value = bestScoreInput.getValue().isEmpty() ? null : bestScoreInput.getValue();
        presenter.onBestScoreLimitChange( value );
    }

    @EventHandler("bestScoreFeasibleInput")
    public void handleBestScoreFeasibleInputChange( ChangeEvent event ) {
        presenter.onFeasibilityChange( bestScoreFeasibleInput.getChecked() );
    }

    @EventHandler("terminationCompositionStyleSelect")
    public void handleTerminationCompositionStyleSelectChange( ChangeEvent event ) {
        presenter.onTerminationCompositionStyleChange( "and".equals( terminationCompositionStyleSelect.getValue() ) ? TerminationCompositionStyleModel.AND : TerminationCompositionStyleModel.OR );

    }

    @Override
    public void setPresenter( TerminationTreeItemContent presenter ) {
        this.presenter = presenter;
    }

    @Override
    public void setRoot( boolean root ) {
        if ( root ) {
            removeTerminationButton.getElement().getStyle().setProperty( "display", "none" );
        }
    }

    @Override
    public void addDropDownOption( TerminationConfigOption terminationConfigOption ) {
        if ( terminationConfigOption == TerminationConfigOption.NESTED ) {
            return;
        }
        AnchorListItem li1 = new AnchorListItem( dropDownNameMap.get( terminationConfigOption ) );
        li1.addClickHandler( h -> {
            presenter.onTerminationTypeSelected( terminationConfigOption.name() );
        } );
        for ( int i = 0; i < dropdownMenuList.getWidgetCount(); i++ ) {
            AnchorListItem item = (AnchorListItem) dropdownMenuList.getWidget( i );
            TerminationConfigOption dropDownTerminationConfigOption = getDropDownOption( item.getText() );
            if ( dropDownTerminationConfigOption == null || terminationConfigOption.ordinal() < dropDownTerminationConfigOption.ordinal() ) {
                dropdownMenuList.insert( li1, i );
                break;
            }
            if ( i == dropdownMenuList.getWidgetCount() - 1 ) {
                dropdownMenuList.add( li1 );
            }
        }
    }

    private TerminationConfigOption getDropDownOption( String text ) {
        for ( Map.Entry<TerminationConfigOption, String> entry : dropDownNameMap.entrySet() ) {
            if ( entry.getValue().equals( text ) ) {
                return entry.getKey();
            }
        }
        return null;
    }

    @Override
    public void removeDropDownOption( TerminationConfigOption option ) {
        for ( int i = 0; i < dropdownMenuList.getWidgetCount(); i++ ) {
            AnchorListItem item = (AnchorListItem) dropdownMenuList.getWidget( i );
            if ( dropDownNameMap.get( option ).equals( item.getText() ) ) {
                dropdownMenuList.remove( item );
                break;
            }
        }
    }

    @Override
    public void setNestedTreeItem( boolean nested ) {
        if ( nested ) {
            formLabelDiv.getStyle().setProperty( "display", "none" );
        } else {
            addTerminationDiv.getStyle().setProperty( "display", "none" );
        }
    }

    @Override
    public void hideTimeSpentInput() {
        timeSpentDiv.getStyle().setProperty( "display", "none" );
    }

    @Override
    public void hideUnimprovedTimeSpentInput() {
        unimprovedTimeSpentDiv.getStyle().setProperty( "display", "none" );
    }

    @Override
    public void hideStepCountLimitInput() {
        stepCountLimitInput.getStyle().setProperty( "display", "none" );
    }

    @Override
    public void hideUnimprovedStepCountLimitInput() {
        unimprovedStepCountLimitInput.getStyle().setProperty( "display", "none" );
    }

    @Override
    public void hideScoreCalculationCountLimitInput() {
        scoreCalculationCountLimitInput.getStyle().setProperty( "display", "none" );
    }

    @Override
    public void hideBestScoreFeasibleInput() {
        bestScoreFeasibleInput.getStyle().setProperty( "display", "none" );
    }

    @Override
    public void hideBestScoreLimitInput() {
        bestScoreInput.getStyle().setProperty( "display", "none" );
    }

    @Override
    public void setFormLabelText( String text ) {
        formLabel.setText( text );
    }

    @Override
    public void setFormLabelHelpContent( String content ) {
        formLabel.setHelpContent( content );
    }

    @Override
    public void setDropDownHelpContent( String content ) {
        dropDownHelpIcon.setHelpContent( content );
    }

    @Override
    public void setDaysSpent( Long value ) {
        daysSpentInput.setValue( value == null ? "0" : value.toString() );
    }

    @Override
    public void setHoursSpent( Long value ) {
        hoursSpentInput.setValue( value == null ? "0" : value.toString() );
    }

    @Override
    public void setMinutesSpent( Long value ) {
        minutesSpentInput.setValue( value == null ? "0" : value.toString() );
    }

    @Override
    public void setSecondsSpent( Long value ) {
        secondsSpentInput.setValue( value == null ? "0" : value.toString() );
    }

    @Override
    public void setMillisecondsSpent( Long value ) {
        millisecondsSpentInput.setValue( value == null ? "0" : value.toString() );
    }

    @Override
    public void setUnimprovedDaysSpent( Long value ) {
        unimprovedDaysSpentInput.setValue( value == null ? "0" : value.toString() );
    }

    @Override
    public void setUnimprovedHoursSpent( Long value ) {
        unimprovedHoursSpentInput.setValue( value == null ? "0" : value.toString() );
    }

    @Override
    public void setUnimprovedMinutesSpent( Long value ) {
        unimprovedMinutesSpentInput.setValue( value == null ? "0" : value.toString() );
    }

    @Override
    public void setUnimprovedSecondsSpent( Long value ) {
        unimprovedSecondsSpentInput.setValue( value == null ? "0" : value.toString() );
    }

    @Override
    public void setUnimprovedMillisecondsSpent( Long value ) {
        unimprovedMillisecondsSpentInput.setValue( value == null ? "0" : value.toString() );
    }

    @Override
    public void setStepCountLimit( Integer value ) {
        stepCountLimitInput.setValue( value == null ? "0" : value.toString() );
    }

    @Override
    public void setUnimprovedStepCountLimit( Integer value ) {
        unimprovedStepCountLimitInput.setValue( value == null ? "0" : value.toString() );
    }

    @Override
    public void setScoreCalculationCountLimit( Long value ) {
        scoreCalculationCountLimitInput.setValue( value == null ? "0" : value.toString() );
    }

    @Override
    public void setBestScoreFeasible( Boolean value ) {
        bestScoreFeasibleInput.setChecked( value == null ? false : value );
    }

    @Override
    public void setBestScoreLimit( String value ) {
        bestScoreInput.setValue( value );
    }

    @Override
    public void setTerminationCompositionStyle( TerminationCompositionStyleModel value ) {
        terminationCompositionStyleSelect.setValue( value == TerminationCompositionStyleModel.AND ? "and" : "or" );
    }

    @Override
    public HTMLElement getElement() {
        return view;
    }
}
