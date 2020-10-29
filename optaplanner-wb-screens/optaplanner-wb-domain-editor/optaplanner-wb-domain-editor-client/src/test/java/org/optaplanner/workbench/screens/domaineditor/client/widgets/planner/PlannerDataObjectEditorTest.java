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

package org.optaplanner.workbench.screens.domaineditor.client.widgets.planner;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Generated;
import javax.xml.bind.annotation.XmlRootElement;

import com.google.gwtmockito.GwtMockitoTestRunner;
import org.jboss.errai.common.client.api.Caller;
import org.jboss.errai.common.client.api.RemoteCallback;
import org.jboss.errai.ui.client.local.spi.TranslationService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kie.workbench.common.screens.datamodeller.client.widgets.DataModelerEditorsTestHelper;
import org.kie.workbench.common.services.datamodeller.core.Annotation;
import org.kie.workbench.common.services.datamodeller.core.AnnotationDefinition;
import org.kie.workbench.common.services.datamodeller.core.DataObject;
import org.kie.workbench.common.services.datamodeller.core.ObjectProperty;
import org.kie.workbench.common.services.datamodeller.core.impl.DataObjectImpl;
import org.kie.workbench.common.services.datamodeller.core.impl.ObjectPropertyImpl;
import org.kie.workbench.common.services.refactoring.service.AssetsUsageService;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.solution.PlanningScore;
import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.optaplanner.core.api.score.buildin.bendable.BendableScore;
import org.optaplanner.core.api.score.buildin.bendablelong.BendableLongScore;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;
import org.optaplanner.core.api.score.buildin.simple.SimpleScore;
import org.optaplanner.workbench.screens.domaineditor.model.PlannerDomainAnnotations;
import org.optaplanner.workbench.screens.domaineditor.service.ComparatorDefinitionService;
import org.uberfire.backend.vfs.Path;
import org.uberfire.backend.vfs.PathFactory;
import org.uberfire.commons.data.Pair;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(GwtMockitoTestRunner.class)
public class PlannerDataObjectEditorTest
        extends PlannerEditorBaseTest {

    @Mock
    private PlannerDataObjectEditorView view;

    @Mock
    private TranslationService translationService;

    @Mock
    private Caller<ComparatorDefinitionService> comparatorDefinitionService;

    @Mock
    private Caller<AssetsUsageService> assetsUsageServiceCaller;

    @Mock
    private AssetsUsageService assetsUsageService;

    protected PlannerDataObjectEditor createObjectEditor() {
        when(assetsUsageServiceCaller.call(any())).thenReturn(assetsUsageService);
        when(assetsUsageService.getAssetUsages(anyString(),
                                               any(),
                                               any(Path.class))).thenReturn(Collections.emptyList());

        PlannerDataObjectEditor objectEditor = new PlannerDataObjectEditor(view,
                                                                           handlerRegistry,
                                                                           dataModelerEvent,
                                                                           commandBuilder,
                                                                           translationService,
                                                                           comparatorDefinitionService,
                                                                           assetsUsageServiceCaller);
        return objectEditor;
    }

    @Test
    public void initEditor() {
        PlannerDataObjectEditor presenter = createObjectEditor();

        verify(view,
               times(1)).init(presenter);
        verify(view,
               times(1)).initPlanningSolutionScoreTypeOptions(anyList());
    }

    @Test
    public void loadDataObject() {
        PlannerDataObjectEditor objectEditor = createObjectEditor();

        //The domain editors typically reacts upon DataModelerContext changes.
        //when the context changes the editor will typically be reloaded.
        objectEditor.onContextChange(context);

        //the view should be populated with the values from the dataObject, initially with no
        //planner settings.
        verify(view,
               times(1)).clear();
        verify(view,
               times(1)).setPlanningEntityValue(false);
        verify(view,
               times(1)).setPlanningSolutionValue(false);
        verify(view,
               times(1)).showPlanningSolutionScoreType(false);
        verify(view,
               times(2)).setNotInPlanningValue(true);
        verify(view,
               times(1)).destroyFieldPicker();
        verify(view,
               times(1)).showPlanningSolutionBendableScoreInput(false);

        verify(assetsUsageService,
               times(1)).getAssetUsages(anyString(),
                                        any(),
                                        any());
    }

    @Test
    public void loadDataObjectPlanningSolution() {
        DataObject dataObject = context.getDataObject();
        dataObject.addAnnotation(DataModelerEditorsTestHelper.createAnnotation(PlanningSolution.class,
                                                                               null,
                                                                               null));

        PlannerDataObjectEditor objectEditor = createObjectEditor();
        objectEditor.onContextChange(context);

        verify(view,
               times(1)).enablePlanningSolutionCheckBox(true);
        verify(view,
               times(1)).showPlanningSolutionHelpIcon(false);

        verify(assetsUsageService,
               never()).getAssetUsages(anyString(),
                                       any(),
                                       any(Path.class));
    }

    @Test
    public void loadDataObjectWithBendableScoreType() {
        PlannerDataObjectEditor objectEditor = createObjectEditor();

        DataObject dataObject = context.getDataObject();

        dataObject.addAnnotation(DataModelerEditorsTestHelper.createAnnotation(PlanningSolution.class,
                                                                               null,
                                                                               null));

        ObjectProperty scoreObjectProperty = new ObjectPropertyImpl("score",
                                                                    BendableScore.class.getName(),
                                                                    false);
        dataObject.addProperty(scoreObjectProperty);

        scoreObjectProperty.addAnnotation(DataModelerEditorsTestHelper.createAnnotation(PlanningScore.class,
                                                                                        new Pair("bendableHardLevelsSize",
                                                                                                 5),
                                                                                        new Pair("bendableSoftLevelsSize",
                                                                                                 10)));
        scoreObjectProperty.addAnnotation(DataModelerEditorsTestHelper.createAnnotation(Generated.class,
                                                                                        null,
                                                                                        null));

        objectEditor.onContextChange(context);

        verify(view,
               times(1)).setPlanningSolutionBendableScoreHardLevelsSize(5);
        verify(view,
               times(1)).setPlanningSolutionBendableScoreSoftLevelsSize(10);
    }

    @Test
    public void changeToPlanningEntity() {

        PlannerDataObjectEditor objectEditor = createObjectEditor();

        //load the editor.
        objectEditor.onContextChange(context);

        // reset state changed by onContextChange
        Mockito.reset(view);

        //emulate user input.
        when(view.getPlanningEntityValue()).thenReturn(true);

        //notify the presenter about the changes in the UI
        objectEditor.onPlanningEntityChange();

        DataObject dataObject = context.getDataObject();

        //the dataObject should have been now configured as a PlanningEntity
        verify(view,
               times(1)).getPlanningEntityValue();
        assertNotNull(dataObject.getAnnotation(PlanningEntity.class.getName()));

        verify(view,
               times(1)).initFieldPicker(context.getDataModel(),
                                         dataObject,
                                         null);

        verify(view,
               times(1)).showPlanningSolutionScoreType(false);
        verify(view,
               times(1)).showPlanningSolutionBendableScoreInput(false);
        verify(view,
               times(1)).setPlanningSolutionBendableScoreHardLevelsSize(0);
        verify(view,
               times(1)).setPlanningSolutionBendableScoreSoftLevelsSize(0);
    }

    @Test
    public void changeToPlanningSolution() {

        PlannerDataObjectEditor objectEditor = createObjectEditor();

        //load the editor.
        objectEditor.onContextChange(context);

        context.getAnnotationDefinitions().put(PlannerDomainAnnotations.JAXB_XML_ROOT_ELEMENT,
                                               mock(AnnotationDefinition.class));
        context.getAnnotationDefinitions().put(PlannerDomainAnnotations.JAXB_XML_ACCESSOR_TYPE,
                                               mock(AnnotationDefinition.class));
        context.getAnnotationDefinitions().put(PlannerDomainAnnotations.JAXB_XML_JAVA_TYPE_ADAPTER_ANNOTATION,
                                               mock(AnnotationDefinition.class));
        context.getAnnotationDefinitions().put(PlannerDomainAnnotations.PLANNING_SCORE_ANNOTATION,
                                               mock(AnnotationDefinition.class));
        context.getAnnotationDefinitions().put(Generated.class.getName(),
                                               mock(AnnotationDefinition.class));

        // reset state changed by onContextChange
        Mockito.reset(view);

        //emulate user input.
        when(view.getPlanningSolutionValue()).thenReturn(true);
        when(view.getPlanningSolutionScoreType()).thenReturn(HardSoftScore.class.getName());

        //notify the presenter about the changes in the UI
        objectEditor.onPlanningSolutionChange();

        DataObject dataObject = context.getDataObject();

        //the dataObject should have been now configured as a PlanningEntity
        verify(view,
               times(1)).getPlanningSolutionValue();
        verify(view,
               times(1)).showPlanningSolutionScoreType(true);

        verify(view,
               times(1)).destroyFieldPicker();

        //the dataObject should have been now configured as a HardSoftCore PlanningSolution by default.
        assertNotNull(dataObject.getAnnotation(PlanningSolution.class.getName()));
        assertNotNull(dataObject.getAnnotation(XmlRootElement.class.getName()));

        ObjectProperty scoreObjectProperty = dataObject.getProperty("score");
        assertNotNull(scoreObjectProperty);
        assertEquals(HardSoftScore.class.getName(),
                     scoreObjectProperty.getClassName());
    }

    @Test
    public void onPlanningSolutionScoreTypeChange() {
        PlannerDataObjectEditor objectEditor = createObjectEditor();

        objectEditor.onContextChange(context);

        Mockito.reset(view);

        when(view.getPlanningSolutionScoreType()).thenReturn(BendableScore.class.getName());

        context.getAnnotationDefinitions().put(PlannerDomainAnnotations.PLANNING_SCORE_ANNOTATION,
                                               mock(AnnotationDefinition.class));
        context.getAnnotationDefinitions().put(PlannerDomainAnnotations.JAXB_XML_ROOT_ELEMENT,
                                               mock(AnnotationDefinition.class));
        context.getAnnotationDefinitions().put(PlannerDomainAnnotations.JAXB_XML_ACCESSOR_TYPE,
                                               mock(AnnotationDefinition.class));
        context.getAnnotationDefinitions().put(PlannerDomainAnnotations.JAXB_XML_JAVA_TYPE_ADAPTER_ANNOTATION,
                                               mock(AnnotationDefinition.class));
        context.getAnnotationDefinitions().put(Generated.class.getName(),
                                               mock(AnnotationDefinition.class));

        objectEditor.onPlanningSolutionScoreTypeChange();

        DataObject dataObject = context.getDataObject();

        ObjectProperty scoreObjectProperty = dataObject.getProperty("score");
        assertNotNull(scoreObjectProperty);
        assertEquals(BendableScore.class.getName(),
                     scoreObjectProperty.getClassName());
    }

    @Test
    public void onPlanningSolutionBendableScoreHardLevelsSizeChange() {
        testPlanningSolutionLevelsSizeChange(true);
    }

    @Test
    public void onPlanningSolutionBendableScoreSoftLevelsSizeChange() {
        testPlanningSolutionLevelsSizeChange(false);
    }

    public void testPlanningSolutionLevelsSizeChange(boolean isHardScore) {
        PlannerDataObjectEditor objectEditor = createObjectEditor();

        objectEditor.onContextChange(context);

        DataObject dataObject = context.getDataObject();

        dataObject.addAnnotation(DataModelerEditorsTestHelper.createAnnotation(PlanningSolution.class,
                                                                               null,
                                                                               null));

        AnnotationDefinition planningScoreAnnotationDefinition = mock(AnnotationDefinition.class);
        when(planningScoreAnnotationDefinition.getClassName()).thenReturn(PlanningScore.class.getName());

        context.getAnnotationDefinitions().put(PlannerDomainAnnotations.PLANNING_SCORE_ANNOTATION,
                                               planningScoreAnnotationDefinition);
        context.getAnnotationDefinitions().put(Generated.class.getName(),
                                               mock(AnnotationDefinition.class));

        ObjectProperty scoreObjectProperty = new ObjectPropertyImpl("score",
                                                                    BendableScore.class.getName(),
                                                                    false);
        dataObject.addProperty(scoreObjectProperty);

        scoreObjectProperty.addAnnotation(DataModelerEditorsTestHelper.createAnnotation(PlanningScore.class,
                                                                                        new Pair("bendableHardLevelsSize",
                                                                                                 5),
                                                                                        new Pair("bendableSoftLevelsSize",
                                                                                                 5)));
        scoreObjectProperty.addAnnotation(DataModelerEditorsTestHelper.createAnnotation(Generated.class,
                                                                                        null,
                                                                                        null));

        if (isHardScore) {
            when(view.getPlanningSolutionBendableScoreHardLevelsSize()).thenReturn(1);
        } else {
            when(view.getPlanningSolutionBendableScoreSoftLevelsSize()).thenReturn(1);
        }

        when(view.getPlanningSolutionScoreType()).thenReturn(BendableScore.class.getName());

        objectEditor.onPlanningSolutionBendableScoreHardLevelsSizeChange();

        scoreObjectProperty = dataObject.getProperty("score");

        assertNotNull(scoreObjectProperty);

        Annotation planningScoreAnnotation = scoreObjectProperty.getAnnotation(PlanningScore.class.getName());

        assertNotNull(planningScoreAnnotation);

        // check whether bendable levels size has been updated
        if (isHardScore) {
            assertEquals(1,
                         planningScoreAnnotation.getValue("bendableHardLevelsSize"));
        } else {
            assertEquals(1,
                         planningScoreAnnotation.getValue("bendableSoftLevelsSize"));
        }
    }

    @Test
    public void updatePlanningScoreProperty() {
        PlannerDataObjectEditor objectEditor = createObjectEditor();

        AnnotationDefinition planningScoreAnnotationDefinition = mock(AnnotationDefinition.class);
        when(planningScoreAnnotationDefinition.getClassName()).thenReturn(PlannerDomainAnnotations.PLANNING_SCORE_ANNOTATION);
        context.getAnnotationDefinitions().put(PlannerDomainAnnotations.PLANNING_SCORE_ANNOTATION,
                                               planningScoreAnnotationDefinition);
        context.getAnnotationDefinitions().put(PlannerDomainAnnotations.JAXB_XML_JAVA_TYPE_ADAPTER_ANNOTATION,
                                               mock(AnnotationDefinition.class));
        context.getAnnotationDefinitions().put(Generated.class.getName(),
                                               mock(AnnotationDefinition.class));

        objectEditor.onContextChange(context);

        DataObject dataObject = context.getDataObject();

        Mockito.reset(view);

        // Data object -> Planning Solution
        objectEditor.updatePlanningScoreProperty(HardSoftScore.class.getName());

        ObjectProperty scoreObjectProperty = dataObject.getProperty("score");
        assertNotNull(scoreObjectProperty);
        assertEquals(HardSoftScore.class.getName(),
                     scoreObjectProperty.getClassName());

        Mockito.reset(view);

        // Planning Solution (non-bendable score type 1) -> Planning Solution (non-bendable score type 2)
        when(view.getPlanningSolutionScoreType()).thenReturn(HardSoftScore.class.getName());

        objectEditor.onPlanningSolutionScoreTypeChange();

        scoreObjectProperty = dataObject.getProperty("score");
        assertNotNull(scoreObjectProperty);
        assertEquals(HardSoftScore.class.getName(),
                     scoreObjectProperty.getClassName());

        Mockito.reset(view);

        // Planning Solution (non-bendable score type) -> Planning Solution (bendable score type)
        when(view.getPlanningSolutionScoreType()).thenReturn(BendableScore.class.getName());

        objectEditor.onPlanningSolutionScoreTypeChange();

        scoreObjectProperty = dataObject.getProperty("score");
        assertNotNull(scoreObjectProperty);
        assertNotNull(scoreObjectProperty.getAnnotation(PlannerDomainAnnotations.PLANNING_SCORE_ANNOTATION));
        assertEquals(BendableScore.class.getName(),
                     scoreObjectProperty.getClassName());
        verify(view,
               times(1)).showPlanningSolutionBendableScoreInput(true);

        Mockito.reset(view);

        // Planning Solution (bendable score type1) -> Planning Solution (bendable score type 2)
        when(view.getPlanningSolutionScoreType()).thenReturn(BendableLongScore.class.getName());

        objectEditor.onPlanningSolutionScoreTypeChange();

        scoreObjectProperty = dataObject.getProperty("score");
        assertNotNull(scoreObjectProperty);
        assertNotNull(scoreObjectProperty.getAnnotation(PlannerDomainAnnotations.PLANNING_SCORE_ANNOTATION));
        assertEquals(BendableLongScore.class.getName(),
                     scoreObjectProperty.getClassName());
        verify(view,
               times(1)).showPlanningSolutionBendableScoreInput(true);

        Mockito.reset(view);

        // Planing Solution (bendable score type) -> Planning Solution (non-bendable score type)
        when(view.getPlanningSolutionScoreType()).thenReturn(SimpleScore.class.getName());

        objectEditor.onPlanningSolutionScoreTypeChange();

        scoreObjectProperty = dataObject.getProperty("score");
        assertNotNull(scoreObjectProperty);
        assertNotNull(scoreObjectProperty.getAnnotation(PlannerDomainAnnotations.PLANNING_SCORE_ANNOTATION));
        assertEquals(SimpleScore.class.getName(),
                     scoreObjectProperty.getClassName());
        verify(view,
               times(1)).showPlanningSolutionBendableScoreInput(false);

        Mockito.reset(view);

        // Planning Score field removed
        when(view.getPlanningSolutionScoreType()).thenReturn("");

        objectEditor.onPlanningSolutionScoreTypeChange();

        assertNull(dataObject.getProperty("score"));

        // Planning Solution -> Data Object/Planning Entity
        when(view.getNotInPlanningValue()).thenReturn(true);

        objectEditor.onNotInPlanningChange();

        scoreObjectProperty = dataObject.getProperty("score");
        assertNull(scoreObjectProperty);
        verify(view,
               times(1)).showPlanningSolutionScoreType(false);
    }

    @Test
    public void getFindClassUsagesCallbackObjectIsAPlanningSolution() {
        PlannerDataObjectEditor objectEditor = createObjectEditor();

        Path solutionPath = PathFactory.newPath("PlanningSolution.java",
                                                "default:///test/PlanningSolution.java");
        Map<String, Path> dataObjectPaths = new HashMap<>();
        dataObjectPaths.put("test.PlanningSolution",
                            solutionPath);
        context.getEditorModelContent().setDataObjectPaths(dataObjectPaths);

        DataObject dataObject = new DataObjectImpl("test",
                                                   "PlanningSolution");
        context.setDataObject(dataObject);

        objectEditor.onContextChange(context);

        RemoteCallback<List<Path>> callback = objectEditor.getFindClassUsagesCallback();

        Mockito.reset(view);

        callback.callback(Arrays.asList(solutionPath));

        verify(view,
               times(1)).enablePlanningSolutionCheckBox(true);
        verify(view,
               times(1)).showPlanningSolutionHelpIcon(false);
    }

    @Test
    public void getFindClassUsagesCallbackObjectIsNotAPlanningSolution() {
        PlannerDataObjectEditor objectEditor = createObjectEditor();

        Path solutionPath = PathFactory.newPath("PlanningSolution.java",
                                                "default:///test/PlanningSolution.java");
        Path entityPath = PathFactory.newPath("PlanningEntity.java",
                                              "default:///test/PlanningEntity.java");
        Map<String, Path> dataObjectPaths = new HashMap<>();
        dataObjectPaths.put("test.PlanningSolution",
                            solutionPath);
        context.getEditorModelContent().setDataObjectPaths(dataObjectPaths);

        DataObject dataObject = new DataObjectImpl("test",
                                                   "PlanningEntity");
        context.setDataObject(dataObject);

        objectEditor.onContextChange(context);

        RemoteCallback<List<Path>> callback = objectEditor.getFindClassUsagesCallback();

        Mockito.reset(view);

        callback.callback(Arrays.asList(solutionPath));

        verify(view,
               times(1)).enablePlanningSolutionCheckBox(false);
        verify(view,
               times(1)).showPlanningSolutionHelpIcon(true);
    }

    @Test
    public void getFindClassUsagesCallbackObjectWithoutContext() {
        PlannerDataObjectEditor objectEditor = createObjectEditor();
        objectEditor.onContextChange(null);

        objectEditor.getFindClassUsagesCallback().callback(Collections.emptyList());

        verify(view, never()).enablePlanningSolutionCheckBox(anyBoolean());
        verify(view, never()).showPlanningSolutionHelpIcon(anyBoolean());
    }
}
