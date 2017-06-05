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
        terminationTreeItemContent = new TerminationTreeItemContent(view,
                                                                    translationService);
        terminationTreeItemContent.setTerminationConfigForm(terminationConfigForm);
        terminationTreeItemContent.setModel(terminationConfigModel);
    }

    @Test
    public void presenterSet() {
        verify(view).setPresenter(terminationTreeItemContent);
    }

    @Test
    public void removeNestedDropDownOption() {
        terminationTreeItemContent.removeDropDownOption(TerminationConfigOption.NESTED);
        verify(view,
               times(0)).removeDropDownOption(any());
    }

    @Test
    public void removeNonNestedDropDownOption() {
        terminationTreeItemContent.removeDropDownOption(TerminationConfigOption.MILLISECONDS_SPENT_LIMIT);
        verify(view).removeDropDownOption(TerminationConfigOption.MILLISECONDS_SPENT_LIMIT);
    }

    @Test
    public void setNestedTerminationConfigOption() {
        terminationTreeItemContent.setTerminationConfigOption(TerminationConfigOption.NESTED);
        verify(view,
               times(1)).setDropDownHelpContent(any());
    }

    @Test
    public void setNonNestedTerminationConfigOption() {
        terminationTreeItemContent.setTerminationConfigOption(TerminationConfigOption.MILLISECONDS_SPENT_LIMIT);
        verify(view).setNestedTreeItem(false);
        verify(view,
               times(0)).setDropDownHelpContent(any());
        verify(view).setFormLabelText(any());
        verify(view).setFormLabelHelpContent(any());
    }

    @Test
    public void setTreeItem() {
        TreeItem treeItem = new TreeItem();
        terminationTreeItemContent.setTreeItem(treeItem);
        verify(view,
               times(1)).setRoot(true);
    }

    @Test
    public void removeTreeItem() {
        terminationTreeItemContent.setTreeItem(new TreeItem());
        terminationTreeItemContent.setTerminationConfigOption(TerminationConfigOption.MILLISECONDS_SPENT_LIMIT);

        TreeItem rootTreeItem = mock(TreeItem.class);
        when(rootTreeItem.getChildCount()).thenReturn(0);
        when(terminationConfigForm.getRootTreeItem()).thenReturn(rootTreeItem);

        terminationTreeItemContent.removeTreeItem();

        verify(terminationConfigModel,
               times(1)).setMillisecondsSpentLimit(null);
        verify(view,
               times(1)).addDropDownOption(TerminationConfigOption.MILLISECONDS_SPENT_LIMIT);
        verify(terminationConfigForm,
               times(1)).displayEmptyTreeLabel(true);
    }

    @Test
    public void setExistingTimeSpentValue() {
        when(terminationConfigForm.getTerminationValue(terminationConfigModel,
                                                       TerminationConfigOption.MILLISECONDS_SPENT_LIMIT)).thenReturn(1L);
        when(terminationConfigForm.getTerminationValue(terminationConfigModel,
                                                       TerminationConfigOption.SECONDS_SPENT_LIMIT)).thenReturn(2L);
        when(terminationConfigForm.getTerminationValue(terminationConfigModel,
                                                       TerminationConfigOption.MINUTES_SPENT_LIMIT)).thenReturn(3L);
        when(terminationConfigForm.getTerminationValue(terminationConfigModel,
                                                       TerminationConfigOption.HOURS_SPENT_LIMIT)).thenReturn(4L);
        when(terminationConfigForm.getTerminationValue(terminationConfigModel,
                                                       TerminationConfigOption.DAYS_SPENT_LIMIT)).thenReturn(5L);

        terminationTreeItemContent.setExistingValue(1L,
                                                    TerminationConfigOption.MILLISECONDS_SPENT_LIMIT);

        verify(view).setMillisecondsSpent(1L);
        verify(view).setSecondsSpent(2L);
        verify(view).setMinutesSpent(3L);
        verify(view).setHoursSpent(4L);
        verify(view).setDaysSpent(5L);

        verify(terminationConfigModel).setMillisecondsSpentLimit(1L);
        verify(terminationConfigModel).setSecondsSpentLimit(2L);
        verify(terminationConfigModel).setMinutesSpentLimit(3L);
        verify(terminationConfigModel).setHoursSpentLimit(4L);
        verify(terminationConfigModel).setDaysSpentLimit(5L);
    }

    @Test
    public void setNewTimeSpentValue() {
        terminationTreeItemContent.setNewValue(TerminationConfigOption.MILLISECONDS_SPENT_LIMIT);

        verify(view).setMillisecondsSpent(0L);
        verify(terminationConfigModel).setMillisecondsSpentLimit(0L);
        verify(view).setSecondsSpent(0L);
        verify(terminationConfigModel).setSecondsSpentLimit(0L);
        verify(view).setMinutesSpent(TerminationTreeItemContent.MINUTES_SPENT_DEFAULT_VALUE);
        verify(terminationConfigModel).setMinutesSpentLimit(TerminationTreeItemContent.MINUTES_SPENT_DEFAULT_VALUE);
        verify(view).setHoursSpent(0L);
        verify(terminationConfigModel).setHoursSpentLimit(0L);
        verify(view).setDaysSpent(0L);
        verify(terminationConfigModel).setDaysSpentLimit(0L);
    }

    @Test
    public void setExistingUnimprovedTimeSpentValue() {
        when(terminationConfigForm.getTerminationValue(terminationConfigModel,
                                                       TerminationConfigOption.UNIMPROVED_MILLISECONDS_SPENT_LIMIT)).thenReturn(1L);
        when(terminationConfigForm.getTerminationValue(terminationConfigModel,
                                                       TerminationConfigOption.UNIMPROVED_SECONDS_SPENT_LIMIT)).thenReturn(2L);
        when(terminationConfigForm.getTerminationValue(terminationConfigModel,
                                                       TerminationConfigOption.UNIMPROVED_MINUTES_SPENT_LIMIT)).thenReturn(3L);
        when(terminationConfigForm.getTerminationValue(terminationConfigModel,
                                                       TerminationConfigOption.UNIMPROVED_HOURS_SPENT_LIMIT)).thenReturn(4L);
        when(terminationConfigForm.getTerminationValue(terminationConfigModel,
                                                       TerminationConfigOption.UNIMPROVED_DAYS_SPENT_LIMIT)).thenReturn(5L);

        terminationTreeItemContent.setExistingValue(1L,
                                                    TerminationConfigOption.UNIMPROVED_MILLISECONDS_SPENT_LIMIT);

        verify(view).setUnimprovedMillisecondsSpent(1L);
        verify(view).setUnimprovedSecondsSpent(2L);
        verify(view).setUnimprovedMinutesSpent(3L);
        verify(view).setUnimprovedHoursSpent(4L);
        verify(view).setUnimprovedDaysSpent(5L);

        verify(terminationConfigModel).setUnimprovedMillisecondsSpentLimit(1L);
        verify(terminationConfigModel).setUnimprovedSecondsSpentLimit(2L);
        verify(terminationConfigModel).setUnimprovedMinutesSpentLimit(3L);
        verify(terminationConfigModel).setUnimprovedHoursSpentLimit(4L);
        verify(terminationConfigModel).setUnimprovedDaysSpentLimit(5L);
    }

    @Test
    public void setNewUnimprovedTimeSpentValue() {
        terminationTreeItemContent.setNewValue(TerminationConfigOption.UNIMPROVED_MILLISECONDS_SPENT_LIMIT);

        verify(view).setUnimprovedMillisecondsSpent(0L);
        verify(terminationConfigModel).setUnimprovedMillisecondsSpentLimit(0L);
        verify(view).setUnimprovedSecondsSpent(0L);
        verify(terminationConfigModel).setUnimprovedSecondsSpentLimit(0L);
        verify(view).setUnimprovedMinutesSpent(TerminationTreeItemContent.UNIMPROVED_MINUTES_SPENT_DEFAULT_VALUE);
        verify(terminationConfigModel).setUnimprovedMinutesSpentLimit(TerminationTreeItemContent.UNIMPROVED_MINUTES_SPENT_DEFAULT_VALUE);
        verify(view).setUnimprovedHoursSpent(0L);
        verify(terminationConfigModel).setUnimprovedHoursSpentLimit(0L);
        verify(view).setUnimprovedDaysSpent(0L);
        verify(terminationConfigModel).setUnimprovedDaysSpentLimit(0L);
    }

    @Test
    public void setExistingBestScoreLimitValue() {
        terminationTreeItemContent.setExistingValue("0",
                                                    TerminationConfigOption.BEST_SCORE_LIMIT);

        verify(view).setBestScoreLimit("0");
    }

    @Test
    public void setExistingBestScoreFeasibleValue() {
        terminationTreeItemContent.setExistingValue(true,
                                                    TerminationConfigOption.BEST_SCORE_FEASIBLE);

        verify(view).setBestScoreFeasible(true);
    }

    @Test
    public void setNewBestScoreFeasibleValue() {
        terminationTreeItemContent.setNewValue(TerminationConfigOption.BEST_SCORE_FEASIBLE);

        verify(view).setBestScoreFeasible(true);
        verify(terminationConfigModel).setBestScoreFeasible(true);
    }

    @Test
    public void setExistingStepCountLimitValue() {
        terminationTreeItemContent.setExistingValue(1,
                                                    TerminationConfigOption.STEP_COUNT_LIMIT);

        verify(view).setStepCountLimit(1);
    }

    @Test
    public void setNewStepCountLimitValue() {
        terminationTreeItemContent.setNewValue(TerminationConfigOption.STEP_COUNT_LIMIT);

        verify(view).setStepCountLimit(0);
        verify(terminationConfigModel).setStepCountLimit(0);
    }

    @Test
    public void setExistingUnimprovedStepCountLimitValue() {
        terminationTreeItemContent.setExistingValue(1,
                                                    TerminationConfigOption.UNIMPROVED_STEP_COUNT_LIMIT);

        verify(view).setUnimprovedStepCountLimit(1);
    }

    @Test
    public void setNewUnimprovedStepCountLimitValue() {
        terminationTreeItemContent.setNewValue(TerminationConfigOption.UNIMPROVED_STEP_COUNT_LIMIT);

        verify(view).setUnimprovedStepCountLimit(0);
        verify(terminationConfigModel).setUnimprovedStepCountLimit(0);
    }

    @Test
    public void setExistingScoreCalculationCountLimitValue() {
        terminationTreeItemContent.setExistingValue(1,
                                                    TerminationConfigOption.SCORE_CALCULATION_COUNT_LIMIT);

        verify(view).setScoreCalculationCountLimit(1L);
    }

    @Test
    public void setNewScoreCalculationCountLimitValue() {
        terminationTreeItemContent.setNewValue(TerminationConfigOption.SCORE_CALCULATION_COUNT_LIMIT);

        verify(view).setScoreCalculationCountLimit(0L);
        verify(terminationConfigModel).setScoreCalculationCountLimit(0L);
    }

    @Test
    public void setExistingTerminationCompositionStyleValue() {
        terminationTreeItemContent.setExistingValue(TerminationCompositionStyleModel.AND,
                                                    TerminationConfigOption.TERMINATION_COMPOSITION_STYLE);

        verify(view).setTerminationCompositionStyle(TerminationCompositionStyleModel.AND);
    }
}
