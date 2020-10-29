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

package org.optaplanner.workbench.screens.guidedrule.client.plugin;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.google.gwtmockito.GwtMockitoTestRunner;
import org.drools.workbench.screens.guided.rule.client.editor.RuleModeller;
import org.jboss.errai.ui.client.local.spi.TranslationService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScoreHolder;
import org.optaplanner.core.api.score.buildin.simple.SimpleScoreHolder;
import org.optaplanner.workbench.models.datamodel.rule.AbstractActionMultiConstraintBendableMatch;
import org.optaplanner.workbench.screens.guidedrule.client.widget.ScoreHolderGlobalAware;
import org.optaplanner.workbench.models.datamodel.rule.ActionMultiConstraintBendableMatch;
import org.optaplanner.workbench.screens.guidedrule.model.BendableScoreLevelsWrapper;
import org.optaplanner.workbench.screens.guidedrule.model.ScoreInformation;
import org.optaplanner.workbench.screens.guidedrule.service.ScoreHolderService;
import org.uberfire.backend.vfs.Path;
import org.uberfire.mocks.CallerMock;
import org.uberfire.mvp.Command;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(GwtMockitoTestRunner.class)
public class ActionPluginClientServiceTest {

    @Mock
    private ScoreHolderService scoreHolderService;

    @Mock
    private TranslationService translationService;

    private ActionPluginClientService service;

    @Before
    public void setUp() {
        this.service = new ActionPluginClientService(new CallerMock(scoreHolderService),
                                                     translationService);
    }

    @Test
    public void invokeScoreInformationCachedOperationCacheEmpty() {
        Map<String, Object> serviceCache = new HashMap<>();
        RuleModeller ruleModeller = mock(RuleModeller.class);
        when(ruleModeller.getServiceInvocationCache()).thenReturn(serviceCache);

        service.invokeScoreInformationCachedOperation(ruleModeller,
                                                      (scoreInformation) -> {
                                                      });

        verify(scoreHolderService,
               times(1)).getProjectScoreInformation(any());
    }

    @Test
    public void invokeScoreInformationCachedOperationCacheNotEmpty() {
        Map<String, Object> serviceCache = new HashMap<>();
        serviceCache.put(ActionPluginClientService.SCORE_INFORMATION,
                         new ScoreInformation());

        RuleModeller ruleModeller = mock(RuleModeller.class);
        when(ruleModeller.getServiceInvocationCache()).thenReturn(serviceCache);

        service.invokeScoreInformationCachedOperation(ruleModeller,
                                                      (scoreInformation) -> {
                                                      });

        verify(scoreHolderService,
               never()).getProjectScoreInformation(any(Path.class));
    }

    @Test
    public void initScoreHolderAwarePlugin() {
        ScoreInformation scoreInformation = new ScoreInformation(Arrays.asList(HardSoftScoreHolder.class.getName()),
                                                                 Arrays.asList(new BendableScoreLevelsWrapper(1,
                                                                                                              1)));
        Map<String, Object> serviceCache = new HashMap<>();
        serviceCache.put(ActionPluginClientService.SCORE_INFORMATION,
                         scoreInformation);

        RuleModeller ruleModeller = mock(RuleModeller.class);
        when(ruleModeller.getServiceInvocationCache()).thenReturn(serviceCache);

        ScoreHolderGlobalAware scoreHolderGlobalAware = mock(ScoreHolderGlobalAware.class);

        service.initScoreHolderAwarePlugin(ruleModeller,
                                           scoreHolderGlobalAware,
                                           Arrays.asList(HardSoftScoreHolder.class.getName()));

        verify(scoreHolderGlobalAware,
               times(1)).scoreHolderGlobalLoadedCorrectly(HardSoftScoreHolder.class.getName());
    }

    @Test
    public void initScoreHolderAwarePluginAmbigiousScoreHolder() {
        // Emulate multiple scoreHolder globals within a project
        ScoreInformation scoreInformation = new ScoreInformation(Arrays.asList(HardSoftScoreHolder.class.getName(),
                                                                               SimpleScoreHolder.class.getName()),
                                                                 Arrays.asList(new BendableScoreLevelsWrapper(1,
                                                                                                              1)));
        Map<String, Object> serviceCache = new HashMap<>();
        serviceCache.put(ActionPluginClientService.SCORE_INFORMATION,
                         scoreInformation);

        RuleModeller ruleModeller = mock(RuleModeller.class);
        when(ruleModeller.getServiceInvocationCache()).thenReturn(serviceCache);

        ScoreHolderGlobalAware scoreHolderGlobalAware = mock(ScoreHolderGlobalAware.class);

        service.initScoreHolderAwarePlugin(ruleModeller,
                                           scoreHolderGlobalAware,
                                           Arrays.asList(HardSoftScoreHolder.class.getName()));

        verify(scoreHolderGlobalAware,
               times(1)).scoreHolderGlobalIssueDetected(any());
    }

    @Test
    public void addPluginToActionList() {
        ScoreInformation scoreInformation = new ScoreInformation(Arrays.asList(HardSoftScoreHolder.class.getName()),
                                                                 Arrays.asList(new BendableScoreLevelsWrapper(1,
                                                                                                              1)));
        Map<String, Object> serviceCache = new HashMap<>();
        serviceCache.put(ActionPluginClientService.SCORE_INFORMATION,
                         scoreInformation);

        RuleModeller ruleModeller = mock(RuleModeller.class);
        when(ruleModeller.getServiceInvocationCache()).thenReturn(serviceCache);

        Command addCommand = mock(Command.class);

        service.addPluginToActionList(ruleModeller,
                                      addCommand,
                                      Arrays.asList(HardSoftScoreHolder.class.getName()));

        verify(addCommand,
               times(1)).execute();
    }

    @Test
    public void addPluginToActionListAmbigiousScoreHolder() {
        // Emulate multiple scoreHolder globals within a project
        ScoreInformation scoreInformation = new ScoreInformation(Arrays.asList(HardSoftScoreHolder.class.getName(),
                                                                               SimpleScoreHolder.class.getName()),
                                                                 Arrays.asList(new BendableScoreLevelsWrapper(1,
                                                                                                              1)));
        Map<String, Object> serviceCache = new HashMap<>();
        serviceCache.put(ActionPluginClientService.SCORE_INFORMATION,
                         scoreInformation);

        RuleModeller ruleModeller = mock(RuleModeller.class);
        when(ruleModeller.getServiceInvocationCache()).thenReturn(serviceCache);

        Command addCommand = mock(Command.class);

        service.addPluginToActionList(ruleModeller,
                                      addCommand,
                                      Arrays.asList(HardSoftScoreHolder.class.getName()));

        verify(addCommand,
               never()).execute();
    }

    @Test
    public void initBendableScoreLevels() {
        ScoreInformation scoreInformation = new ScoreInformation(Arrays.asList(HardSoftScoreHolder.class.getName()),
                                                                 Arrays.asList(new BendableScoreLevelsWrapper(1,
                                                                                                              2)));
        Map<String, Object> serviceCache = new HashMap<>();
        serviceCache.put(ActionPluginClientService.SCORE_INFORMATION,
                         scoreInformation);

        RuleModeller ruleModeller = mock(RuleModeller.class);
        when(ruleModeller.getServiceInvocationCache()).thenReturn(serviceCache);

        AbstractActionMultiConstraintBendableMatch constraintMatch = new ActionMultiConstraintBendableMatch();

        service.initBendableScoreLevels(ruleModeller,
                                        constraintMatch);

        Assert.assertEquals(1,
                            constraintMatch.getActionBendableHardConstraintMatches().size());
        Assert.assertEquals(2,
                            constraintMatch.getActionBendableSoftConstraintMatches().size());
    }
}
