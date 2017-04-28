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
import javax.enterprise.context.Dependent;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import com.google.gwt.user.client.ui.Widget;
import org.jboss.errai.common.client.api.Caller;
import org.jboss.errai.common.client.api.RemoteCallback;
import org.jboss.errai.ui.client.local.spi.TranslationService;
import org.kie.workbench.common.screens.datamodeller.client.DataModelerContext;
import org.kie.workbench.common.screens.datamodeller.client.command.DataModelCommandBuilder;
import org.kie.workbench.common.screens.datamodeller.client.command.ValuePair;
import org.kie.workbench.common.screens.datamodeller.client.handlers.DomainHandlerRegistry;
import org.kie.workbench.common.screens.datamodeller.client.util.ErrorPopupHelper;
import org.kie.workbench.common.screens.datamodeller.client.widgets.common.domain.ObjectEditor;
import org.kie.workbench.common.screens.datamodeller.events.ChangeType;
import org.kie.workbench.common.screens.datamodeller.events.DataModelerEvent;
import org.kie.workbench.common.screens.datamodeller.events.DataObjectChangeEvent;
import org.kie.workbench.common.screens.datamodeller.service.DataModelerService;
import org.kie.workbench.common.services.datamodeller.core.Annotation;
import org.kie.workbench.common.services.datamodeller.core.DataObject;
import org.kie.workbench.common.services.datamodeller.core.JavaClass;
import org.kie.workbench.common.services.datamodeller.core.Method;
import org.kie.workbench.common.services.datamodeller.core.ObjectProperty;
import org.kie.workbench.common.services.datamodeller.core.Parameter;
import org.kie.workbench.common.services.datamodeller.core.Type;
import org.kie.workbench.common.services.datamodeller.core.Visibility;
import org.kie.workbench.common.services.datamodeller.core.impl.MethodImpl;
import org.kie.workbench.common.services.datamodeller.core.impl.ParameterImpl;
import org.kie.workbench.common.services.datamodeller.core.impl.TypeImpl;
import org.optaplanner.core.api.score.Score;
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
    public void onContextChange(DataModelerContext context) {
        super.onContextChange(context);
        adjustSelectedPlanningSolutionScoreType();
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
                dataModelerService.call(new RemoteCallback<List<Path>>() {
                    @Override
                    public void callback(List<Path> paths) {
                        view.enablePlanningSolutionCheckBox(paths.isEmpty());
                        view.showPlanningSolutionHelpIcon(!paths.isEmpty());
                    }
                }).findClassUsages(context.getCurrentProject().getRootPath(),
                                   PlannerDomainAnnotations.PLANNING_SOLUTION_ANNOTATION);
            } else {
                view.enablePlanningSolutionCheckBox(true);
                view.showPlanningSolutionHelpIcon(false);
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
                Method getScoreMethod = dataObject.getMethod("getScore",
                                                             Collections.EMPTY_LIST);

                if (getScoreMethod != null) {
                    Type getScoreMethodReturnType = getScoreMethod.getReturnType();
                    if (getScoreMethodReturnType != null && isBendableScore(getScoreMethod.getReturnType().getName())) {
                        view.showPlanningSolutionBendableScoreInput(true);
                        Annotation annotation = getScoreMethod.getAnnotation(PlannerDomainAnnotations.PLANNING_SCORE_ANNOTATION);
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

            if (isPlannerSolution(getDataObject().getSuperClassName())) {
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
            removePlanningSolutionScoreAccessorMethods(view.getPlanningSolutionScoreType());
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

                if (isPlannerSolution(getDataObject().getSuperClassName())) {
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
                removePlanningSolutionScoreAccessorMethods(view.getPlanningSolutionScoreType());
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
                        PlannerDomainAnnotations.PLANNING_SOLUTION_ANNOTATION).execute();

                commandBuilder.buildDataObjectRemoveAnnotationCommand(
                        getContext(),
                        getName(),
                        getDataObject(),
                        PlannerDomainAnnotations.PLANNING_ENTITY_ANNOTATION).execute();

                commandBuilder.buildDataObjectSuperClassChangeCommand(getContext(),
                                                                      getName(),
                                                                      getDataObject(),
                                                                      buildPlanningSolutionScoreTypeSuperClass(getDefaultSolutionScoreType())).execute();
                removeComparatorDefinition(getDataObject(),
                                           false);
                view.destroyFieldPicker();
                view.showPlanningSolutionScoreType(true);
                view.setPlanningSolutionScoreType(getDefaultSolutionScoreType());
            } else {
                commandBuilder.buildDataObjectRemoveAnnotationCommand(
                        getContext(),
                        getName(),
                        getDataObject(),
                        PlannerDomainAnnotations.PLANNING_SOLUTION_ANNOTATION).execute();
                commandBuilder.buildDataObjectSuperClassChangeCommand(getContext(),
                                                                      getName(),
                                                                      getDataObject(),
                                                                      null).execute();
                view.showPlanningSolutionScoreType(false);

                view.showPlanningSolutionBendableScoreInput(false);
                view.setPlanningSolutionBendableScoreHardLevelsSize(0);
                view.setPlanningSolutionBendableScoreSoftLevelsSize(0);
                removePlanningSolutionScoreAccessorMethods(view.getPlanningSolutionScoreType());
            }
        }
    }

    @Override
    public void onPlanningSolutionScoreTypeChange() {
        String newPlanningSolutionScoreType = view.getPlanningSolutionScoreType();
        String superClass = buildPlanningSolutionScoreTypeSuperClass(newPlanningSolutionScoreType);
        if (superClass != null) {
            String oldPlanningSolutionScoreType = getSelectedPlanningSolutionTypeFromSource(context.getEditorModelContent().getSource());
            commandBuilder.buildDataObjectSuperClassChangeCommand(getContext(),
                                                                  getName(),
                                                                  getDataObject(),
                                                                  superClass).execute();
            updateScoreMethod(newPlanningSolutionScoreType,
                              oldPlanningSolutionScoreType);
        }
    }

    @Override
    public void onPlanningSolutionBendableScoreHardLevelsSizeChange() {
        updateScoreMethod(view.getPlanningSolutionScoreType(),
                          null);
    }

    @Override
    public void onPlanningSolutionBendableScoreSoftLevelsSizeChange() {
        updateScoreMethod(view.getPlanningSolutionScoreType(),
                          null);
    }

    private void updateScoreMethod(String newPlanningSolutionScoreType,
                                   String oldPlanningSolutionScoreType) {
        if (getDataObject() != null) {

            if (isBendableScore(newPlanningSolutionScoreType)) {

                Method getScoreMethod = dataObject.getMethod("getScore",
                                                             Collections.EMPTY_LIST);

                if (oldPlanningSolutionScoreType != null) {
                    view.setPlanningSolutionBendableScoreHardLevelsSize(0);
                    view.setPlanningSolutionBendableScoreSoftLevelsSize(0);

                    removePlanningSolutionScoreAccessorMethods(oldPlanningSolutionScoreType);

                    getScoreMethod = addGetScoreMethod(newPlanningSolutionScoreType);
                    addSetScoreMethod(newPlanningSolutionScoreType);
                } else if (getScoreMethod == null) {
                    getScoreMethod = addGetScoreMethod(newPlanningSolutionScoreType);
                    addSetScoreMethod(newPlanningSolutionScoreType);
                }

                view.showPlanningSolutionBendableScoreInput(true);

                List<ValuePair> valuePairList = Arrays.asList(new ValuePair("bendableHardLevelsSize",
                                                                            view.getPlanningSolutionBendableScoreHardLevelsSize()),
                                                              new ValuePair("bendableSoftLevelsSize",
                                                                            view.getPlanningSolutionBendableScoreSoftLevelsSize()));

                commandBuilder.buildMethodAnnotationAddCommand(getContext(),
                                                               getName(),
                                                               getDataObject(),
                                                               getScoreMethod,
                                                               PlannerDomainAnnotations.PLANNING_SCORE_ANNOTATION,
                                                               valuePairList).execute();
            } else {
                view.showPlanningSolutionBendableScoreInput(false);

                removePlanningSolutionScoreAccessorMethods(oldPlanningSolutionScoreType);
            }
        }
    }

    private void removePlanningSolutionScoreAccessorMethods(String solutionScoreType) {
        if (getDataObject() != null) {
            Method getScoreMethod = dataObject.getMethod("getScore",
                                                         Collections.EMPTY_LIST);
            if (getScoreMethod != null) {
                commandBuilder.buildMethodRemoveCommand(getContext(),
                                                        getName(),
                                                        getDataObject(),
                                                        getScoreMethod).execute();
            }

            Method setScoreMethod = dataObject.getMethod("setScore",
                                                         Arrays.asList(solutionScoreType));
            if (setScoreMethod != null) {
                commandBuilder.buildMethodRemoveCommand(getContext(),
                                                        getName(),
                                                        getDataObject(),
                                                        setScoreMethod).execute();
            }
        }
    }

    private Method addGetScoreMethod(String newPlanningSolutionScoreType) {
        Type getScoreMethodReturnType = new TypeImpl(newPlanningSolutionScoreType);
        Method getScoreMethod = new MethodImpl("getScore",
                                               Collections.EMPTY_LIST,
                                               "return score;",
                                               getScoreMethodReturnType,
                                               Visibility.PUBLIC);
        commandBuilder.buildMethodAddCommand(getContext(),
                                             getName(),
                                             getDataObject(),
                                             getScoreMethod).execute();
        commandBuilder.buildMethodAnnotationAddCommand(getContext(),
                                                       getName(),
                                                       getDataObject(),
                                                       getScoreMethod,
                                                       "javax.annotation.Generated",
                                                       Arrays.asList(new ValuePair("value",
                                                                                   PlannerDataObjectEditor.class.getName()))).execute();
        return getScoreMethod;
    }

    private Method addSetScoreMethod(String newPlanningSolutionScoreType) {
        Parameter setScoreParameter = new ParameterImpl(new TypeImpl(newPlanningSolutionScoreType),
                                                        "score");
        Type setScoreMethodReturnType = new TypeImpl(void.class.getName());
        MethodImpl setScoreMethod = new MethodImpl("setScore",
                                                   Arrays.asList(setScoreParameter),
                                                   "this.score = score;",
                                                   setScoreMethodReturnType,
                                                   Visibility.PUBLIC);
        commandBuilder.buildMethodAddCommand(getContext(),
                                             getName(),
                                             getDataObject(),
                                             setScoreMethod).execute();
        commandBuilder.buildMethodAnnotationAddCommand(getContext(),
                                                       getName(),
                                                       getDataObject(),
                                                       setScoreMethod,
                                                       "javax.annotation.Generated",
                                                       Arrays.asList(new ValuePair("value",
                                                                                   PlannerDataObjectEditor.class.getName()))).execute();

        return setScoreMethod;
    }

    protected void onDataObjectChange(@Observes DataObjectChangeEvent event) {
        super.onDataObjectChange(event);
        if (event.isFromContext(getContext() != null ? getContext().getContextId() : null) &&
                !event.isFrom(getName()) &&
                event.getChangeType() == ChangeType.SUPER_CLASS_NAME_CHANGE &&
                getDataObject() != null &&
                getDataObject().getAnnotation(PlannerDomainAnnotations.PLANNING_SOLUTION_ANNOTATION) != null &&
                !isPlannerSolution(getDataObject().getSuperClassName()) &&
                isPlannerSolution(event.getOldValue() != null ? event.getOldValue().toString() : null)) {

            commandBuilder.buildDataObjectRemoveAnnotationCommand(
                    getContext(),
                    getName(),
                    getDataObject(),
                    PlannerDomainAnnotations.PLANNING_SOLUTION_ANNOTATION).execute();
        }
    }

    private boolean isPlannerSolution(String superClassName) {
        return superClassName != null &&
                (superClassName.startsWith(PlannerDomainTypes.ABSTRACT_SOLUTION_CLASS_NAME) ||
                        superClassName.startsWith(PlannerDomainTypes.ABSTRACT_SOLUTION_SIMPLE_CLASS_NAME));
    }

    private List<Pair<String, String>> getPlanningSolutionScoreTypeOptions() {
        List<Pair<String, String>> planningSolutionScoreTypeOptions = new ArrayList<>(PlannerDomainTypes.SCORE_TYPES.size());
        for (Class<? extends Score> scoreClass : PlannerDomainTypes.SCORE_TYPES) {
            planningSolutionScoreTypeOptions.add(new Pair<>(DomainEditorLookupConstants.INSTANCE.getString(scoreClass.getSimpleName()),
                                                            scoreClass.getName()));
        }
        return planningSolutionScoreTypeOptions;
    }

    private String buildPlanningSolutionScoreTypeSuperClass(String planningSolutionScoreType) {
        return planningSolutionScoreType != null ?
                PlannerDomainTypes.ABSTRACT_SOLUTION_CLASS_NAME + "<" + planningSolutionScoreType + ">" :
                null;
    }

    private void adjustSelectedPlanningSolutionScoreType() {
        if (context != null && context.getEditorModelContent() != null && context.getEditorModelContent().getSource() != null) {
            String selectedScoreType = getSelectedPlanningSolutionTypeFromSource(context.getEditorModelContent().getSource());
            if (selectedScoreType == null) {
                selectedScoreType = getDefaultSolutionScoreType();
            }
            view.setPlanningSolutionScoreType(selectedScoreType);
        }
    }

    private String getSelectedPlanningSolutionTypeFromSource(String source) {
        //Implementation to make the planner prototype work, since data modeller by definition
        //do not manage parametrized types.
        for (Class<? extends Score> scoreClass : PlannerDomainTypes.SCORE_TYPES) {
            if (source.contains(PlannerDomainTypes.ABSTRACT_SOLUTION_SIMPLE_CLASS_NAME + "<" + scoreClass.getSimpleName() + ">") ||
                    source.contains(PlannerDomainTypes.ABSTRACT_SOLUTION_SIMPLE_CLASS_NAME + "<" + scoreClass.getName() + ">")) {
                return scoreClass.getName();
            }
        }
        return null;
    }

    private String getDefaultSolutionScoreType() {
        return HardSoftScore.class.getName();
    }
}