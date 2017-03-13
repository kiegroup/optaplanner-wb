package org.optaplanner.workbench.screens.solver.client.editor;

import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwtmockito.GwtMockitoTestRunner;
import org.jboss.errai.ui.client.local.spi.TranslationService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.optaplanner.workbench.screens.solver.model.TerminationCompositionStyleModel;
import org.optaplanner.workbench.screens.solver.model.TerminationConfigModel;
import org.optaplanner.workbench.screens.solver.model.TerminationConfigOption;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(GwtMockitoTestRunner.class)
public class TerminationTreeItemContentTest {

    @Mock
    private TerminationTreeItemContentView view;

    @Mock
    private TranslationService translationService;

    @Mock
    private TerminationConfigModel terminationConfigModel;

    @Mock
    private TerminationConfigForm terminationConfigForm;

    private TerminationTreeItemContent terminationTreeItemContent;

    @Before
    public void setUp() {
        terminationTreeItemContent = new TerminationTreeItemContent( view,
                                                                     translationService );
        terminationTreeItemContent.setTerminationConfigForm( terminationConfigForm );
        terminationTreeItemContent.setModel( terminationConfigModel );
    }

    @Test
    public void presenterSet() {
        verify( view ).setPresenter( terminationTreeItemContent );
    }

    @Test
    public void removeNestedDropDownOption() {
        terminationTreeItemContent.removeDropDownOption( TerminationConfigOption.NESTED );
        verify( view,
                times( 0 ) ).removeDropDownOption( any() );
    }

    @Test
    public void removeNonNestedDropDownOption() {
        terminationTreeItemContent.removeDropDownOption( TerminationConfigOption.MILLISECONDS_SPENT_LIMIT );
        verify( view ).removeDropDownOption( TerminationConfigOption.MILLISECONDS_SPENT_LIMIT );
    }

    @Test
    public void setNestedTerminationConfigOption() {
        terminationTreeItemContent.setTerminationConfigOption( TerminationConfigOption.NESTED );
        verify( view,
                times( 1 ) ).setDropDownHelpContent( any() );
    }

    @Test
    public void setNonNestedTerminationConfigOption() {
        terminationTreeItemContent.setTerminationConfigOption( TerminationConfigOption.MILLISECONDS_SPENT_LIMIT );
        verify( view ).setNestedTreeItem( false );
        verify( view,
                times( 0 ) ).setDropDownHelpContent( any() );
        verify( view ).setFormLabelText( any() );
        verify( view ).setFormLabelHelpContent( any() );
    }

    @Test
    public void setTreeItem() {
        TreeItem treeItem = new TreeItem();
        terminationTreeItemContent.setTreeItem( treeItem );
        verify( view,
                times( 1 ) ).setRoot( true );
    }

    @Test
    public void removeTreeItem() {
        terminationTreeItemContent.setTreeItem( new TreeItem() );
        terminationTreeItemContent.setTerminationConfigOption( TerminationConfigOption.MILLISECONDS_SPENT_LIMIT );

        TreeItem rootTreeItem = mock( TreeItem.class );
        when( rootTreeItem.getChildCount() ).thenReturn( 0 );
        when( terminationConfigForm.getRootTreeItem() ).thenReturn( rootTreeItem );

        terminationTreeItemContent.removeTreeItem();

        verify( terminationConfigModel,
                times( 1 ) ).setMillisecondsSpentLimit( null );
        verify( view,
                times( 1 ) ).addDropDownOption( TerminationConfigOption.MILLISECONDS_SPENT_LIMIT );
        verify( terminationConfigForm,
                times( 1 ) ).displayEmptyTreeLabel( true );
    }

    @Test
    public void setExistingTimeSpentValue() {
        when( terminationConfigForm.getTerminationValue( terminationConfigModel,
                                                         TerminationConfigOption.MILLISECONDS_SPENT_LIMIT ) ).thenReturn( 1l );
        when( terminationConfigForm.getTerminationValue( terminationConfigModel,
                                                         TerminationConfigOption.SECONDS_SPENT_LIMIT ) ).thenReturn( 2l );
        when( terminationConfigForm.getTerminationValue( terminationConfigModel,
                                                         TerminationConfigOption.MINUTES_SPENT_LIMIT ) ).thenReturn( 3l );
        when( terminationConfigForm.getTerminationValue( terminationConfigModel,
                                                         TerminationConfigOption.HOURS_SPENT_LIMIT ) ).thenReturn( 4l );
        when( terminationConfigForm.getTerminationValue( terminationConfigModel,
                                                         TerminationConfigOption.DAYS_SPENT_LIMIT ) ).thenReturn( 5l );

        terminationTreeItemContent.setExistingValue( 1l,
                                                     TerminationConfigOption.MILLISECONDS_SPENT_LIMIT );

        verify( view ).setMillisecondsSpent( 1l );
        verify( view ).setSecondsSpent( 2l );
        verify( view ).setMinutesSpent( 3l );
        verify( view ).setHoursSpent( 4l );
        verify( view ).setDaysSpent( 5l );
    }

    @Test
    public void setNewTimeSpentValue() {
        terminationTreeItemContent.setNewValue( TerminationConfigOption.MILLISECONDS_SPENT_LIMIT );

        verify( view ).setMillisecondsSpent( 0l );
        verify( terminationConfigModel ).setMillisecondsSpentLimit( 0l );
        verify( view ).setSecondsSpent( 0l );
        verify( terminationConfigModel ).setSecondsSpentLimit( 0l );
        verify( view ).setMinutesSpent( TerminationTreeItemContent.MINUTES_SPENT_DEFAULT_VALUE );
        verify( terminationConfigModel ).setMinutesSpentLimit( TerminationTreeItemContent.MINUTES_SPENT_DEFAULT_VALUE );
        verify( view ).setHoursSpent( 0l );
        verify( terminationConfigModel ).setHoursSpentLimit( 0l );
        verify( view ).setDaysSpent( 0l );
        verify( terminationConfigModel ).setDaysSpentLimit( 0l );
    }

    @Test
    public void setExistingUnimprovedTimeSpentValue() {
        when( terminationConfigForm.getTerminationValue( terminationConfigModel,
                                                         TerminationConfigOption.UNIMPROVED_MILLISECONDS_SPENT_LIMIT ) ).thenReturn( 1l );
        when( terminationConfigForm.getTerminationValue( terminationConfigModel,
                                                         TerminationConfigOption.UNIMPROVED_SECONDS_SPENT_LIMIT ) ).thenReturn( 2l );
        when( terminationConfigForm.getTerminationValue( terminationConfigModel,
                                                         TerminationConfigOption.UNIMPROVED_MINUTES_SPENT_LIMIT ) ).thenReturn( 3l );
        when( terminationConfigForm.getTerminationValue( terminationConfigModel,
                                                         TerminationConfigOption.UNIMPROVED_HOURS_SPENT_LIMIT ) ).thenReturn( 4l );
        when( terminationConfigForm.getTerminationValue( terminationConfigModel,
                                                         TerminationConfigOption.UNIMPROVED_DAYS_SPENT_LIMIT ) ).thenReturn( 5l );

        terminationTreeItemContent.setExistingValue( 1l,
                                                     TerminationConfigOption.UNIMPROVED_MILLISECONDS_SPENT_LIMIT );

        verify( view ).setUnimprovedMillisecondsSpent( 1l );
        verify( view ).setUnimprovedSecondsSpent( 2l );
        verify( view ).setUnimprovedMinutesSpent( 3l );
        verify( view ).setUnimprovedHoursSpent( 4l );
        verify( view ).setUnimprovedDaysSpent( 5l );
    }

    @Test
    public void setNewUnimprovedTimeSpentValue() {
        terminationTreeItemContent.setNewValue( TerminationConfigOption.UNIMPROVED_MILLISECONDS_SPENT_LIMIT );

        verify( view ).setUnimprovedMillisecondsSpent( 0l );
        verify( terminationConfigModel ).setUnimprovedMillisecondsSpentLimit( 0l );
        verify( view ).setUnimprovedSecondsSpent( 0l );
        verify( terminationConfigModel ).setUnimprovedSecondsSpentLimit( 0l );
        verify( view ).setUnimprovedMinutesSpent( TerminationTreeItemContent.UNIMPROVED_MINUTES_SPENT_DEFAULT_VALUE );
        verify( terminationConfigModel ).setUnimprovedMinutesSpentLimit( TerminationTreeItemContent.UNIMPROVED_MINUTES_SPENT_DEFAULT_VALUE );
        verify( view ).setUnimprovedHoursSpent( 0l );
        verify( terminationConfigModel ).setUnimprovedHoursSpentLimit( 0l );
        verify( view ).setUnimprovedDaysSpent( 0l );
        verify( terminationConfigModel ).setUnimprovedDaysSpentLimit( 0l );
    }

    @Test
    public void setExistingBestScoreLimitValue() {
        terminationTreeItemContent.setExistingValue( "0",
                                                     TerminationConfigOption.BEST_SCORE_LIMIT );

        verify( view ).setBestScoreLimit( "0" );
    }

    @Test
    public void setExistingBestScoreFeasibleValue() {
        terminationTreeItemContent.setExistingValue( true,
                                                     TerminationConfigOption.BEST_SCORE_FEASIBLE );

        verify( view ).setBestScoreFeasible( true );
    }

    @Test
    public void setNewBestScoreFeasibleValue() {
        terminationTreeItemContent.setNewValue( TerminationConfigOption.BEST_SCORE_FEASIBLE );

        verify( view ).setBestScoreFeasible( true );
        verify( terminationConfigModel ).setBestScoreFeasible( true );
    }

    @Test
    public void setExistingStepCountLimitValue() {
        terminationTreeItemContent.setExistingValue( 1,
                                                     TerminationConfigOption.STEP_COUNT_LIMIT );

        verify( view ).setStepCountLimit( 1 );
    }

    @Test
    public void setNewStepCountLimitValue() {
        terminationTreeItemContent.setNewValue( TerminationConfigOption.STEP_COUNT_LIMIT );

        verify( view ).setStepCountLimit( 0 );
        verify( terminationConfigModel ).setStepCountLimit( 0 );
    }

    @Test
    public void setExistingUnimprovedStepCountLimitValue() {
        terminationTreeItemContent.setExistingValue( 1,
                                                     TerminationConfigOption.UNIMPROVED_STEP_COUNT_LIMIT );

        verify( view ).setUnimprovedStepCountLimit( 1 );
    }

    @Test
    public void setNewUnimprovedStepCountLimitValue() {
        terminationTreeItemContent.setNewValue( TerminationConfigOption.UNIMPROVED_STEP_COUNT_LIMIT );

        verify( view ).setUnimprovedStepCountLimit( 0 );
        verify( terminationConfigModel ).setUnimprovedStepCountLimit( 0 );
    }

    @Test
    public void setExistingScoreCalculationCountLimitValue() {
        terminationTreeItemContent.setExistingValue( 1,
                                                     TerminationConfigOption.SCORE_CALCULATION_COUNT_LIMIT );

        verify( view ).setScoreCalculationCountLimit( 1l );
    }

    @Test
    public void setNewScoreCalculationCountLimitValue() {
        terminationTreeItemContent.setNewValue( TerminationConfigOption.SCORE_CALCULATION_COUNT_LIMIT );

        verify( view ).setScoreCalculationCountLimit( 0l );
        verify( terminationConfigModel ).setScoreCalculationCountLimit( 0l );
    }

    @Test
    public void setExistingTerminationCompositionStyleValue() {
        terminationTreeItemContent.setExistingValue( TerminationCompositionStyleModel.AND,
                                                     TerminationConfigOption.TERMINATION_COMPOSITION_STYLE );

        verify( view ).setTerminationCompositionStyle( TerminationCompositionStyleModel.AND );
    }
}
