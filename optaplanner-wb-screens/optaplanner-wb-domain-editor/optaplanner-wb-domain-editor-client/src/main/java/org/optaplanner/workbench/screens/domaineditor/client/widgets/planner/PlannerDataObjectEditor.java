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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import javax.enterprise.context.Dependent;
import javax.enterprise.event.Event;
import javax.inject.Inject;

import com.google.gwt.user.client.ui.Widget;
import org.jboss.errai.common.client.api.Caller;
import org.jboss.errai.common.client.api.RemoteCallback;
import org.jboss.errai.ui.client.local.spi.TranslationService;
import org.kie.workbench.common.screens.datamodeller.client.command.DataModelCommandBuilder;
import org.kie.workbench.common.screens.datamodeller.client.command.ValuePair;
import org.kie.workbench.common.screens.datamodeller.client.handlers.DomainHandlerRegistry;
import org.kie.workbench.common.screens.datamodeller.client.util.ErrorPopupHelper;
import org.kie.workbench.common.screens.datamodeller.client.widgets.common.domain.ObjectEditor;
import org.kie.workbench.common.screens.datamodeller.events.DataModelerEvent;
import org.kie.workbench.common.screens.datamodeller.service.DataModelerService;
import org.kie.workbench.common.services.datamodeller.core.Annotation;
import org.kie.workbench.common.services.datamodeller.core.DataObject;
import org.kie.workbench.common.services.datamodeller.core.JavaClass;
import org.kie.workbench.common.services.datamodeller.core.ObjectProperty;
import org.optaplanner.core.api.score.buildin.bendable.BendableScore;
import org.optaplanner.core.api.score.buildin.bendablebigdecimal.BendableBigDecimalScore;
import org.optaplanner.core.api.score.buildin.bendablelong.BendableLongScore;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;
import org.optaplanner.workbench.screens.domaineditor.client.handlers.planner.ComparatorDefinitionAnnotationValueHandler;
import org.optaplanner.workbench.screens.domaineditor.client.resources.i18n.DomainEditorConstants;
import org.optaplanner.workbench.screens.domaineditor.client.resources.i18n.DomainEditorLookupConstants;
import org.optaplanner.workbench.screens.domaineditor.client.util.PlannerDomainTypes;
import org.optaplanner.workbench.screens.domaineditor.model.ComparatorDefinition;
import org.optaplanner.workbench.screens.domaineditor.model.ObjectPropertyPath;
import org.optaplanner.workbench.screens.domaineditor.model.ObjectPropertyPathImpl;
import org.optaplanner.workbench.screens.domaineditor.model.PlannerDomainAnnotations;
import org.optaplanner.workbench.screens.domaineditor.service.ComparatorDefinitionService;
import org.uberfire.backend.vfs.Path;
import org.uberfire.commons.data.Pair;

@Dependent
public class PlannerDataObjectEditor
        extends ObjectEditor
        implements PlannerDataObjectEditorView.Presenter {

    private PlannerDataObjectEditorView view;

    private TranslationService translationService;

    private JavaClass comparatorObject;

    private Caller<ComparatorDefinitionService> comparatorDefinitionService;

    private Caller<DataModelerService> dataModelerService;

    @Inject
    public PlannerDataObjectEditor(PlannerDataObjectEditorView view,
                                   DomainHandlerRegistry handlerRegistry,
                                   Event<DataModelerEvent> dataModelerEvent,
                                   DataModelCommandBuilder commandBuilder,
                                   TranslationService translationService,
                                   Caller<ComparatorDefinitionService> comparatorDefinitionService,
                                   Caller<DataModelerService> dataModelerService) {
        super(handlerRegistry,
              dataModelerEvent,
              commandBuilder);
        this.view = view;
        this.translationService = translationService;
        this.comparatorDefinitionService = comparatorDefinitionService;
        this.dataModelerService = dataModelerService;

        view.init(this);
        view.initPlanningSolutionScoreTypeOptions(getPlanningSolutionScoreTypeOptions());
    }

    @Override
    public Widget asWidget() {
        return view.asWidget();
    }

    @Override
    public String getName() {
        return "PLANNER_OBJECT_EDITOR";
    }

    @Override
    public String getDomainName() {
        return PlannerDomainEditor.PLANNER_DOMAIN;
    }

    @Override
    protected void loadDataObject(DataObject dataObject) {
        clear();
        this.dataObject = dataObject;
        this.comparatorObject = null;
        if (dataObject != null) {
            boolean hasPlanningEntity = dataObject.getAnnotation(PlannerDomainAnnotations.PLANNING_ENTITY_ANNOTATION) != null;
            boolean hasPlanningSolution = dataObject.getAnnotation(PlannerDomainAnnotations.PLANNING_SOLUTION_ANNOTATION) != null;

            if (!hasPlanningSolution) {
                view.enablePlanningSolutionCheckBox(false);
                view.showPlanningSolutionHelpIcon(false);
                dataModelerService.call(getFindClassUsagesCallback()).findClassUsages(context.getCurrentProject().getRootPath(),
                                   PlannerDomainAnnotations.PLANNING_SOLUTION_ANNOTATION);
            } else {
                view.enablePlanningSolutionCheckBox(true);
                view.showPlanningSolutionHelpIcon(false);
                adjustSelectedPlanningSolutionScoreType();
            }

            view.setPlanningEntityValue(hasPlanningEntity);
            view.setPlanningSolutionValue(hasPlanningSolution);
            view.showPlanningSolutionScoreType(hasPlanningSolution);
            view.setNotInPlanningValue(!hasPlanningEntity && !hasPlanningSolution);
            view.destroyFieldPicker();
            view.showPlanningSolutionBendableScoreInput(false);

            if (hasPlanningEntity) {
                if (!dataObject.getNestedClasses().isEmpty()) {
                    for (JavaClass javaClass : dataObject.getNestedClasses()) {
                        if (javaClass.getAnnotation(ComparatorDefinition.class.getName()) != null) {
                            try {
                                view.initFieldPicker(getContext().getDataModel(),
                                                     getDataObject(),
                                                     getObjectPropertyPathList(javaClass.getAnnotation(ComparatorDefinition.class.getName())));
                                this.comparatorObject = javaClass;
                                break;
                            } catch (IllegalStateException e) {
                                ErrorPopupHelper.showErrorPopup(translationService.getTranslation(DomainEditorConstants.PlannerDataObjectEditorComparatorDefinitionProcessingFailed) + " " + e.getMessage());
                            }
                        }
                    }
                } else {
                    view.initFieldPicker(getContext().getDataModel(),
                                         getDataObject(),
                                         null);
                }
            }

            if (hasPlanningSolution) {
                ObjectProperty scoreObjectProperty = dataObject.getProperty("score");

                if (scoreObjectProperty != null) {
                    if (isBendableScore(scoreObjectProperty.getClassName())) {
                        view.showPlanningSolutionBendableScoreInput(true);
                        Annotation annotation = scoreObjectProperty.getAnnotation(PlannerDomainAnnotations.PLANNING_SCORE_ANNOTATION);
                        if (annotation != null) {
                            Object hardLevelsSize = annotation.getValue("bendableHardLevelsSize");
                            if (hardLevelsSize != null) {
                                view.setPlanningSolutionBendableScoreHardLevelsSize((int) hardLevelsSize == -1 ? 0 : (int) hardLevelsSize);
                            } else {
                                view.setPlanningSolutionBendableScoreHardLevelsSize(0);
                            }

                            Object softLevelsSize = annotation.getValue("bendableSoftLevelsSize");
                            if (softLevelsSize != null) {
                                view.setPlanningSolutionBendableScoreSoftLevelsSize((int) softLevelsSize == -1 ? 0 : (int) softLevelsSize);
                            } else {
                                view.setPlanningSolutionBendableScoreSoftLevelsSize(0);
                            }
                        }
                    } else {
                        view.showPlanningSolutionBendableScoreInput(false);

                        view.setPlanningSolutionBendableScoreHardLevelsSize(0);
                        view.setPlanningSolutionBendableScoreSoftLevelsSize(0);
                    }
                }
            }
        }
    }

    RemoteCallback<List<Path>> getFindClassUsagesCallback() {
        return new RemoteCallback<List<Path>>() {
            @Override
            public void callback(List<Path> paths) {
                // Remove current data object from the path list
                List<Path> pathsCopy = new ArrayList<>(paths);

                Path currentDataObjectPath = context.getDataObjectPath(dataObject.getClassName());
                pathsCopy.remove(currentDataObjectPath);

                view.enablePlanningSolutionCheckBox(pathsCopy.isEmpty());
                view.showPlanningSolutionHelpIcon(!pathsCopy.isEmpty());
            }
        };
    }

    private List<ObjectPropertyPath> getObjectPropertyPathList(Annotation comparatorDefinition) {
        ComparatorDefinitionAnnotationValueHandler comparatorAnnotationHandler = new ComparatorDefinitionAnnotationValueHandler(comparatorDefinition);
        List<ObjectPropertyPath> objectPropertyPathList = new ArrayList<>();
        List<Annotation> comparatorFieldPaths = comparatorAnnotationHandler.getObjectPropertyPaths();

        for (Annotation comparatorFieldPath : comparatorFieldPaths) {
            ObjectPropertyPath objectPropertyPath = new ObjectPropertyPathImpl();
            objectPropertyPath.setDescending(!comparatorAnnotationHandler.isAscending(comparatorFieldPath));

            List<Annotation> comparatorFields = comparatorAnnotationHandler.getObjectProperties(comparatorFieldPath);
            if (!comparatorFields.isEmpty()) {

                String objectPropertyName = comparatorAnnotationHandler.getName(comparatorFields.get(0));

                ObjectProperty objectProperty = dataObject.getProperty(objectPropertyName);
                if (objectProperty == null) {
                    throw new IllegalStateException(dataObject.getName() + "Comparator: Property " + objectPropertyName + " not found in data object " + dataObject.getClassName());
                }
                objectPropertyPath.appendObjectProperty(objectProperty);
                for (int i = 1; i < comparatorFields.size(); i++) {

                    objectPropertyName = comparatorAnnotationHandler.getName(comparatorFields.get(i));

                    ObjectProperty lastObjectPropertyInPath = objectPropertyPath.getObjectPropertyPath().get(objectPropertyPath.getObjectPropertyPath().size() - 1);
                    if (lastObjectPropertyInPath.isBaseType() || lastObjectPropertyInPath.isPrimitiveType()) {
                        throw new IllegalStateException(dataObject.getName() + "Comparator: Cannot append property " + objectPropertyName + " to primitive/base type " + lastObjectPropertyInPath.getClassName());
                    }
                    DataObject lastDataObjectInPath = getContext().getDataModel().getDataObject(lastObjectPropertyInPath.getClassName());
                    if (lastObjectPropertyInPath == null) {
                        throw new IllegalStateException(dataObject.getName() + "Comparator: Data object " + lastObjectPropertyInPath.getClassName() + " not found");
                    }
                    ObjectProperty currentObjectProperty = lastDataObjectInPath.getProperty(objectPropertyName);
                    if (currentObjectProperty == null) {
                        throw new IllegalStateException(dataObject.getName() + "Comparator: Property " + objectPropertyName + " not found in data object " + lastDataObjectInPath.getClassName());
                    }
                    objectPropertyPath.appendObjectProperty(currentObjectProperty);
                }
            }
            objectPropertyPathList.add(objectPropertyPath);
        }

        return objectPropertyPathList;
    }

    private boolean isBendableScore(String planningSolutionScoreType) {
        return BendableScore.class.getName().equals(planningSolutionScoreType)
                || BendableLongScore.class.getName().equals(planningSolutionScoreType)
                || BendableBigDecimalScore.class.getName().equals(planningSolutionScoreType);
    }

    @Override
    public void clear() {
        view.clear();
        view.setNotInPlanningValue(true);
    }

    @Override
    public void objectPropertyPathChanged(List<ObjectPropertyPath> objectPropertyPaths,
                                          boolean itemsRemoved) {
        if (objectPropertyPaths.isEmpty() && view.isFieldPickerEmpty() && itemsRemoved) {
            commandBuilder.buildDataObjectRemoveNestedClassCommand(context,
                                                                   getName(),
                                                                   dataObject,
                                                                   comparatorObject).execute();
            commandBuilder.buildDataObjectAddAnnotationCommand(getContext(),
                                                               getName(),
                                                               getDataObject(),
                                                               PlannerDomainAnnotations.PLANNING_ENTITY_ANNOTATION).execute();
            this.comparatorObject = null;
            view.initFieldPicker(getContext().getDataModel(),
                                 getDataObject(),
                                 Collections.EMPTY_LIST);
            return;
        }

        if (comparatorObject != null) {
            comparatorObject.removeAnnotation(ComparatorDefinition.class.getName());
            comparatorObject.addAnnotation(ComparatorDefinitionAnnotationValueHandler.createAnnotation(objectPropertyPaths,
                                                                                                       context.getAnnotationDefinitions()));

            comparatorDefinitionService.call(new RemoteCallback<JavaClass>() {
                @Override
                public void callback(JavaClass updatedComparatorObject) {
                    dataObject.removeNestedClass(PlannerDataObjectEditor.this.comparatorObject);
                    dataObject.addNestedClass(updatedComparatorObject);

                    PlannerDataObjectEditor.this.comparatorObject = updatedComparatorObject;
                }
            }).updateComparatorObject(dataObject,
                                      comparatorObject);
        } else {
            comparatorDefinitionService.call(new RemoteCallback<JavaClass>() {
                @Override
                public void callback(JavaClass newComparatorObject) {
                    dataObject.addNestedClass(newComparatorObject);

                    PlannerDataObjectEditor.this.comparatorObject = newComparatorObject;
                }
            }).createComparatorObject(dataObject);
        }

        List<ValuePair> valuePairList = Arrays.asList(new ValuePair("difficultyComparatorClass",
                                                                    getDataObject().getName() + ".DifficultyComparator.class"));
        commandBuilder.buildDataObjectAddAnnotationCommand(getContext(),
                                                           getName(),
                                                           getDataObject(),
                                                           PlannerDomainAnnotations.PLANNING_ENTITY_ANNOTATION,
                                                           valuePairList).execute();
    }

    @Override
    public void removeComparatorDefinition(DataObject dataObject,
                                           boolean resetPlanningEntityAnnotation) {
        if (comparatorObject != null) {
            commandBuilder.buildDataObjectRemoveNestedClassCommand(getContext(),
                                                                   getName(),
                                                                   dataObject,
                                                                   comparatorObject).execute();
            this.comparatorObject = null;
        }

        commandBuilder.buildDataObjectRemoveAnnotationCommand(getContext(),
                                                              getName(),
                                                              getDataObject(),
                                                              ComparatorDefinition.class.getName()).execute();

        if (resetPlanningEntityAnnotation) {
            commandBuilder.buildDataObjectAddAnnotationCommand(getContext(),
                                                               getName(),
                                                               getDataObject(),
                                                               PlannerDomainAnnotations.PLANNING_ENTITY_ANNOTATION).execute();
        }
    }

    @Override
    public void onNotInPlanningChange() {
        boolean value = view.getNotInPlanningValue();
        if (value && dataObject != null) {
            commandBuilder.buildDataObjectRemoveAnnotationCommand(getContext(),
                                                                  getName(),
                                                                  getDataObject(),
                                                                  PlannerDomainAnnotations.PLANNING_ENTITY_ANNOTATION).execute();

            commandBuilder.buildDataObjectRemoveAnnotationCommand(getContext(),
                                                                  getName(),
                                                                  getDataObject(),
                                                                  PlannerDomainAnnotations.PLANNING_SOLUTION_ANNOTATION).execute();

            if (isPlanningSolution(getDataObject())) {
                commandBuilder.buildDataObjectSuperClassChangeCommand(getContext(),
                                                                      getName(),
                                                                      getDataObject(),
                                                                      null).execute();
            }
            removeComparatorDefinition(getDataObject(),
                                       false);
            view.destroyFieldPicker();
            view.showPlanningSolutionScoreType(false);
            view.showPlanningSolutionBendableScoreInput(false);
            view.setPlanningSolutionBendableScoreHardLevelsSize(0);
            view.setPlanningSolutionBendableScoreSoftLevelsSize(0);
            removePlanningSolutionMarshalingAnnotations();
            removePlanningScoreProperty();
        }
    }

    @Override
    public void onPlanningEntityChange() {
        boolean value = view.getPlanningEntityValue();
        if (dataObject != null) {
            if (value) {
                commandBuilder.buildDataObjectAddAnnotationCommand(
                        getContext(),
                        getName(),
                        getDataObject(),
                        PlannerDomainAnnotations.PLANNING_ENTITY_ANNOTATION).execute();

                commandBuilder.buildDataObjectRemoveAnnotationCommand(
                        getContext(),
                        getName(),
                        getDataObject(),
                        PlannerDomainAnnotations.PLANNING_SOLUTION_ANNOTATION).execute();

                if (isPlanningSolution(getDataObject())) {
                    commandBuilder.buildDataObjectSuperClassChangeCommand(getContext(),
                                                                          getName(),
                                                                          getDataObject(),
                                                                          null).execute();
                }
                view.initFieldPicker(getContext().getDataModel(),
                                     getDataObject(),
                                     null);
                view.showPlanningSolutionScoreType(false);

                view.showPlanningSolutionBendableScoreInput(false);
                view.setPlanningSolutionBendableScoreHardLevelsSize(0);
                view.setPlanningSolutionBendableScoreSoftLevelsSize(0);
                removePlanningSolutionMarshalingAnnotations();
                removePlanningScoreProperty();
            } else {
                commandBuilder.buildDataObjectRemoveAnnotationCommand(
                        getContext(),
                        getName(),
                        getDataObject(),
                        PlannerDomainAnnotations.PLANNING_ENTITY_ANNOTATION).execute();
            }
        }
    }

    @Override
    public void onPlanningSolutionChange() {
        boolean value = view.getPlanningSolutionValue();
        if (dataObject != null) {
            if (value) {
                commandBuilder.buildDataObjectAddAnnotationCommand(
                        getContext(),
                        getName(),
                        getDataObject(),
                        PlannerDomainAnnotations.PLANNING_SOLUTION_ANNOTATION,
                        Arrays.asList(new ValuePair("autoDiscoverMemberType",
                                                    "FIELD"))).execute();

                commandBuilder.buildDataObjectAddAnnotationCommand(
                        getContext(),
                        getName(),
                        getDataObject(),
                        PlannerDomainAnnotations.JAXB_XML_ROOT_ELEMENT).execute();

                commandBuilder.buildDataObjectAddAnnotationCommand(
                        getContext(),
                        getName(),
                        getDataObject(),
                        PlannerDomainAnnotations.JAXB_XML_ACCESSOR_TYPE,
                        Arrays.asList(new ValuePair("value",
                                                    "FIELD"))).execute();

                commandBuilder.buildDataObjectRemoveAnnotationCommand(
                        getContext(),
                        getName(),
                        getDataObject(),
                        PlannerDomainAnnotations.PLANNING_ENTITY_ANNOTATION).execute();
                removeComparatorDefinition(getDataObject(),
                                           false);
                view.destroyFieldPicker();
                view.showPlanningSolutionScoreType(true);
                view.setPlanningSolutionScoreType(getDefaultSolutionScoreType());
                updatePlanningScoreProperty(view.getPlanningSolutionScoreType());
            } else {
                commandBuilder.buildDataObjectRemoveAnnotationCommand(
                        getContext(),
                        getName(),
                        getDataObject(),
                        PlannerDomainAnnotations.PLANNING_SOLUTION_ANNOTATION).execute();
                view.showPlanningSolutionScoreType(false);
                view.showPlanningSolutionBendableScoreInput(false);
                view.setPlanningSolutionBendableScoreHardLevelsSize(0);
                view.setPlanningSolutionBendableScoreSoftLevelsSize(0);
                removePlanningSolutionMarshalingAnnotations();
                removePlanningScoreProperty();
            }
        }
    }

    @Override
    public void onPlanningSolutionScoreTypeChange() {
        updatePlanningScoreProperty(view.getPlanningSolutionScoreType());
    }

    @Override
    public void onPlanningSolutionBendableScoreHardLevelsSizeChange() {
        updateBendableScoreLevelsSize();
    }

    @Override
    public void onPlanningSolutionBendableScoreSoftLevelsSizeChange() {
        updateBendableScoreLevelsSize();
    }

    private void updateBendableScoreLevelsSize() {
        ObjectProperty scoreObjectProperty = dataObject.getProperty("score");

        if (scoreObjectProperty != null) {
            List<ValuePair> valuePairList = Arrays.asList(new ValuePair("bendableHardLevelsSize",
                                                                        view.getPlanningSolutionBendableScoreHardLevelsSize()),
                                                          new ValuePair("bendableSoftLevelsSize",
                                                                        view.getPlanningSolutionBendableScoreSoftLevelsSize()));
            commandBuilder.buildFieldAnnotationAddCommand(getContext(),
                                                          getName(),
                                                          getDataObject(),
                                                          scoreObjectProperty,
                                                          PlannerDomainAnnotations.PLANNING_SCORE_ANNOTATION,
                                                          valuePairList).execute();
        }
    }

    void updatePlanningScoreProperty(final String newPlanningSolutionScoreType) {
        if (getDataObject() != null) {
            removePlanningScoreProperty();
            addPlanningScoreProperty(newPlanningSolutionScoreType);
        }
    }

    private void removePlanningSolutionMarshalingAnnotations() {
        if (dataObject != null) {
            commandBuilder.buildDataObjectRemoveAnnotationCommand(
                    getContext(),
                    getName(),
                    getDataObject(),
                    PlannerDomainAnnotations.JAXB_XML_ROOT_ELEMENT).execute();

            commandBuilder.buildDataObjectRemoveAnnotationCommand(
                    getContext(),
                    getName(),
                    getDataObject(),
                    PlannerDomainAnnotations.JAXB_XML_ACCESSOR_TYPE).execute();
        }
    }

    private void removePlanningScoreProperty() {
        final ObjectProperty scoreProperty = dataObject.getProperty("score");
        if (scoreProperty != null) {
            commandBuilder.buildRemovePropertyCommand(getContext(),
                                                      getName(),
                                                      getDataObject(),
                                                      "score").execute();
        }
    }

    private void addPlanningScoreProperty(final String newPlanningSolutionScoreType) {
        commandBuilder.buildAddPropertyCommand(getContext(),
                                               getName(),
                                               getDataObject(),
                                               "score",
                                               "Generated Planner score field",
                                               newPlanningSolutionScoreType,
                                               false).execute();

        commandBuilder.buildFieldAnnotationAddCommand(getContext(),
                                                      getName(),
                                                      getDataObject(),
                                                      getDataObject().getProperty("score"),
                                                      "javax.annotation.Generated",
                                                      Arrays.asList(new ValuePair("value",
                                                                                  Arrays.asList(PlannerDataObjectEditor.class.getName())))).execute();

        commandBuilder.buildFieldAnnotationAddCommand(getContext(),
                                                      getName(),
                                                      getDataObject(),
                                                      getDataObject().getProperty("score"),
                                                      PlannerDomainAnnotations.PLANNING_SCORE_ANNOTATION).execute();

        commandBuilder.buildFieldAnnotationAddCommand(getContext(),
                                                      getName(),
                                                      getDataObject(),
                                                      getDataObject().getProperty("score"),
                                                      PlannerDomainAnnotations.JAXB_XML_JAVA_TYPE_ADAPTER_ANNOTATION,
                                                      Arrays.asList(new ValuePair("value",
                                                                                  resolveXmlAdapterClass(newPlanningSolutionScoreType) + ".class"))).execute();

        view.setPlanningSolutionBendableScoreHardLevelsSize(0);
        view.setPlanningSolutionBendableScoreSoftLevelsSize(0);

        if (isBendableScore(newPlanningSolutionScoreType)) {
            view.showPlanningSolutionBendableScoreInput(true);
            List<ValuePair> valuePairList = Arrays.asList(new ValuePair("bendableHardLevelsSize",
                                                                        view.getPlanningSolutionBendableScoreHardLevelsSize()),
                                                          new ValuePair("bendableSoftLevelsSize",
                                                                        view.getPlanningSolutionBendableScoreSoftLevelsSize()));
            commandBuilder.buildFieldAnnotationAddCommand(getContext(),
                                                          getName(),
                                                          getDataObject(),
                                                          getDataObject().getProperty("score"),
                                                          PlannerDomainAnnotations.PLANNING_SCORE_ANNOTATION,
                                                          valuePairList).execute();
        } else {
            view.showPlanningSolutionBendableScoreInput(false);
        }
    }

    private String resolveXmlAdapterClass(final String scoreType) {
        return PlannerDomainTypes.SCORE_CONFIGURATION_MAP
                .entrySet()
                .stream()
                .filter(e -> e.getKey().getName().equals(scoreType))
                .map(c -> c.getValue().getJaxbXmlAdapterClass())
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("Score type " + scoreType + " not recognized."));
    }

    private boolean isPlanningSolution(final DataObject dataObject) {
        return dataObject != null && dataObject.getAnnotation(PlannerDomainAnnotations.PLANNING_SOLUTION_ANNOTATION) != null;
    }

    private List<Pair<String, String>> getPlanningSolutionScoreTypeOptions() {
        return PlannerDomainTypes.SCORE_CONFIGURATION_MAP
                .keySet()
                .stream()
                .map(s -> new Pair<>(DomainEditorLookupConstants.INSTANCE.getString(s.getSimpleName()),
                                     s.getName())).collect(Collectors.toList());
    }

    private void adjustSelectedPlanningSolutionScoreType() {
        if (getDataObject() != null) {
            ObjectProperty scoreObjectProperty = getDataObject().getProperty("score");
            if (scoreObjectProperty != null) {
                view.setPlanningSolutionScoreType(scoreObjectProperty.getClassName());
            }
        }
    }

    private String getDefaultSolutionScoreType() {
        return HardSoftScore.class.getName();
    }
}