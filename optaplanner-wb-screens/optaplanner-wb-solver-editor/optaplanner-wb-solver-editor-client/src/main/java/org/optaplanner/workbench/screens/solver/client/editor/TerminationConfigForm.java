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

import java.util.ArrayList;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.Widget;
import org.jboss.errai.common.client.ui.ElementWrapperWidget;
import org.jboss.errai.ioc.client.api.ManagedInstance;
import org.optaplanner.workbench.screens.solver.model.TerminationConfigModel;
import org.optaplanner.workbench.screens.solver.model.TerminationConfigOption;

@Dependent
public class TerminationConfigForm
        implements IsWidget {

    private TerminationConfigModel model;

    private TerminationConfigFormView view;

    private ManagedInstance<TerminationTreeItemContent> terminationTreeItemContentProvider;

    private TreeItem rootTreeItem;

    @Inject
    public TerminationConfigForm(TerminationConfigFormView view,
                                 ManagedInstance<TerminationTreeItemContent> terminationTreeItemContentProvider) {
        this.view = view;
        this.terminationTreeItemContentProvider = terminationTreeItemContentProvider;
    }

    public void displayEmptyTreeLabel(boolean visible) {
        view.displayEmptyTreeLabel(visible);
    }

    public void addNewTerminationType(String terminationType,
                                      TerminationTreeItemContent treeItemContent) {
        TreeItem nestedTreeItem = new TreeItem();
        insertTreeItem(treeItemContent.getTreeItem(),
                       nestedTreeItem,
                       TerminationConfigOption.valueOf(terminationType));
        treeItemContent.getTreeItem().setState(true);

        TerminationConfigOption terminationConfigOption = TerminationConfigOption.valueOf(terminationType);
        final TerminationConfigModel terminationConfigModel;
        if (terminationConfigOption == TerminationConfigOption.NESTED) {
            TerminationConfigModel childTerminationConfigModel = new TerminationConfigModel();
            if (treeItemContent.getModel().getTerminationConfigList() == null) {
                treeItemContent.getModel().setTerminationConfigList(new ArrayList<>(1));
            }
            treeItemContent.getModel().getTerminationConfigList().add(childTerminationConfigModel);
            terminationConfigModel = childTerminationConfigModel;
        } else {
            terminationConfigModel = treeItemContent.getModel();
        }
        TerminationTreeItemContent terminationTreeItemContent = createTerminationTreeItemContent(terminationConfigOption,
                                                                                                 nestedTreeItem,
                                                                                                 terminationConfigModel);

        terminationTreeItemContent.setNewValue(terminationConfigOption);

        treeItemContent.removeDropDownOption(TerminationConfigOption.valueOf(terminationType));
        view.displayEmptyTreeLabel(rootTreeItem.getChildCount() == 0);
        view.refreshTree();
    }

    private void insertTreeItem(TreeItem treeItem,
                                TreeItem nestedTreeItem,
                                TerminationConfigOption terminationConfigOption) {
        if (treeItem.getChildCount() == 0) {
            treeItem.addItem(nestedTreeItem);
        } else {
            for (int i = 0; i < treeItem.getChildCount(); i++) {
                TerminationTreeItemContent childTreeItemContent = (TerminationTreeItemContent) treeItem.getChild(i).getUserObject();
                if (terminationConfigOption.ordinal() < childTreeItemContent.getTerminationConfigOption().ordinal()) {
                    treeItem.insertItem(i,
                                        nestedTreeItem);
                    break;
                }
                if (i == treeItem.getChildCount() - 1) {
                    treeItem.addItem(nestedTreeItem);
                    break;
                }
            }
        }
    }

    private TerminationTreeItemContent createTerminationTreeItemContent(TerminationConfigOption terminationConfigOption,
                                                                        TreeItem treeItem,
                                                                        TerminationConfigModel terminationConfigModel) {
        TerminationTreeItemContent terminationTreeItemContent = terminationTreeItemContentProvider.get();
        terminationTreeItemContent.setTerminationConfigForm(this);
        terminationTreeItemContent.setTerminationConfigOption(terminationConfigOption);
        terminationTreeItemContent.setTreeItem(treeItem);
        terminationTreeItemContent.setModel(terminationConfigModel);
        treeItem.setUserObject(terminationTreeItemContent);
        treeItem.setWidget(ElementWrapperWidget.getWidget(terminationTreeItemContent.getElement()));
        treeItem.setState(true);
        return terminationTreeItemContent;
    }

    public void destroyTerminationTreeItemContent(TerminationTreeItemContent terminationTreeItemContent) {
        terminationTreeItemContentProvider.destroy(terminationTreeItemContent);
    }

    public void setModel(TerminationConfigModel terminationConfigModel) {
        model = terminationConfigModel;
        initTerminationTree(model);
    }

    private void initTerminationTree(TerminationConfigModel terminationConfigModel) {
        this.rootTreeItem = new TreeItem();
        initTerminationTreeItemHierarchy(terminationConfigModel,
                                         rootTreeItem);
        view.initTree(rootTreeItem);
        rootTreeItem.setState(true);
        view.displayEmptyTreeLabel(rootTreeItem.getChildCount() == 0);
        view.refreshTree();
    }

    public TreeItem getRootTreeItem() {
        return rootTreeItem;
    }

    private void initTerminationTreeItemHierarchy(TerminationConfigModel terminationConfigModel,
                                                  TreeItem treeItem) {
        TerminationTreeItemContent terminationTreeItemContent = createTerminationTreeItemContent(TerminationConfigOption.NESTED,
                                                                                                 treeItem,
                                                                                                 terminationConfigModel);

        TerminationConfigOption timeSpentTerminationOption = null;
        boolean timeSpentInserted = false;
        boolean unimprovedTimeSpentInserted = false;
        outer:
        for (TerminationConfigOption terminationConfigOption : TerminationConfigOption.values()) {
            timeSpentTerminationOption = null;
            Object terminationValue = getTerminationValue(terminationConfigModel,
                                                          terminationConfigOption);
            if (terminationConfigOption == TerminationConfigOption.TERMINATION_COMPOSITION_STYLE) {
                TerminationTreeItemContent parentTreeItemContent = (TerminationTreeItemContent) treeItem.getUserObject();
                parentTreeItemContent.setExistingValue(terminationValue,
                                                       TerminationConfigOption.TERMINATION_COMPOSITION_STYLE);
                continue;
            }
            if (terminationValue != null) {
                if (timeSpentInserted && (terminationConfigOption == TerminationConfigOption.MILLISECONDS_SPENT_LIMIT || terminationConfigOption == TerminationConfigOption.SECONDS_SPENT_LIMIT
                        || terminationConfigOption == TerminationConfigOption.MINUTES_SPENT_LIMIT || terminationConfigOption == TerminationConfigOption.HOURS_SPENT_LIMIT
                        || terminationConfigOption == TerminationConfigOption.DAYS_SPENT_LIMIT)) {
                    for (int i = 0; i < treeItem.getChildCount(); i++) {
                        TerminationTreeItemContent treeItemContent = (TerminationTreeItemContent) treeItem.getChild(i).getUserObject();
                        if (treeItemContent.getTerminationConfigOption() == TerminationConfigOption.MILLISECONDS_SPENT_LIMIT || treeItemContent.getTerminationConfigOption() == TerminationConfigOption.SECONDS_SPENT_LIMIT
                                || treeItemContent.getTerminationConfigOption() == TerminationConfigOption.MINUTES_SPENT_LIMIT || treeItemContent.getTerminationConfigOption() == TerminationConfigOption.HOURS_SPENT_LIMIT
                                || treeItemContent.getTerminationConfigOption() == TerminationConfigOption.DAYS_SPENT_LIMIT) {
                            treeItemContent.setTerminationConfigOption(TerminationConfigOption.MILLISECONDS_SPENT_LIMIT);
                            treeItemContent.setExistingValue(terminationValue,
                                                             TerminationConfigOption.MILLISECONDS_SPENT_LIMIT);
                            continue outer;
                        }
                    }
                } else {
                    if (terminationConfigOption == TerminationConfigOption.MILLISECONDS_SPENT_LIMIT || terminationConfigOption == TerminationConfigOption.SECONDS_SPENT_LIMIT
                            || terminationConfigOption == TerminationConfigOption.MINUTES_SPENT_LIMIT || terminationConfigOption == TerminationConfigOption.HOURS_SPENT_LIMIT
                            || terminationConfigOption == TerminationConfigOption.DAYS_SPENT_LIMIT) {
                        timeSpentTerminationOption = TerminationConfigOption.MILLISECONDS_SPENT_LIMIT;
                        timeSpentInserted = true;
                    }
                }
            }
            if (terminationValue != null) {
                if (unimprovedTimeSpentInserted && (terminationConfigOption == TerminationConfigOption.UNIMPROVED_MILLISECONDS_SPENT_LIMIT || terminationConfigOption == TerminationConfigOption.UNIMPROVED_SECONDS_SPENT_LIMIT
                        || terminationConfigOption == TerminationConfigOption.UNIMPROVED_MINUTES_SPENT_LIMIT || terminationConfigOption == TerminationConfigOption.UNIMPROVED_HOURS_SPENT_LIMIT
                        || terminationConfigOption == TerminationConfigOption.UNIMPROVED_DAYS_SPENT_LIMIT)) {
                    for (int i = 0; i < treeItem.getChildCount(); i++) {
                        TerminationTreeItemContent treeItemContent = (TerminationTreeItemContent) treeItem.getChild(i).getUserObject();
                        if (treeItemContent.getTerminationConfigOption() == TerminationConfigOption.UNIMPROVED_MILLISECONDS_SPENT_LIMIT || treeItemContent.getTerminationConfigOption() == TerminationConfigOption.UNIMPROVED_SECONDS_SPENT_LIMIT
                                || treeItemContent.getTerminationConfigOption() == TerminationConfigOption.UNIMPROVED_MINUTES_SPENT_LIMIT || treeItemContent.getTerminationConfigOption() == TerminationConfigOption.UNIMPROVED_HOURS_SPENT_LIMIT
                                || treeItemContent.getTerminationConfigOption() == TerminationConfigOption.UNIMPROVED_DAYS_SPENT_LIMIT) {
                            treeItemContent.setTerminationConfigOption(TerminationConfigOption.UNIMPROVED_MILLISECONDS_SPENT_LIMIT);
                            treeItemContent.setExistingValue(terminationValue,
                                                             TerminationConfigOption.UNIMPROVED_MILLISECONDS_SPENT_LIMIT);
                            continue outer;
                        }
                    }
                } else {
                    if (terminationConfigOption == TerminationConfigOption.UNIMPROVED_MILLISECONDS_SPENT_LIMIT || terminationConfigOption == TerminationConfigOption.UNIMPROVED_SECONDS_SPENT_LIMIT
                            || terminationConfigOption == TerminationConfigOption.UNIMPROVED_MINUTES_SPENT_LIMIT || terminationConfigOption == TerminationConfigOption.UNIMPROVED_HOURS_SPENT_LIMIT
                            || terminationConfigOption == TerminationConfigOption.UNIMPROVED_DAYS_SPENT_LIMIT) {
                        timeSpentTerminationOption = TerminationConfigOption.UNIMPROVED_MILLISECONDS_SPENT_LIMIT;
                        unimprovedTimeSpentInserted = true;
                    }
                }
            }
            if (terminationValue != null && !TerminationConfigOption.NESTED.equals(terminationConfigOption)) {
                TreeItem nestedTreeItem = new TreeItem();

                if (timeSpentTerminationOption != null) {
                    terminationConfigOption = timeSpentTerminationOption;
                }
                insertTreeItem(treeItem,
                               nestedTreeItem,
                               terminationConfigOption);

                terminationTreeItemContent.removeDropDownOption(terminationConfigOption);
                TerminationTreeItemContent nestedTerminationTreeItemContent = createTerminationTreeItemContent(terminationConfigOption,
                                                                                                               nestedTreeItem,
                                                                                                               terminationConfigModel);
                nestedTerminationTreeItemContent.setExistingValue(terminationValue,
                                                                  terminationConfigOption);
            }
        }
        if (terminationConfigModel.getTerminationConfigList() != null) {
            for (TerminationConfigModel nestedTerminationConfigModel : terminationConfigModel.getTerminationConfigList()) {
                TreeItem nestedTreeItem = new TreeItem();
                treeItem.addItem(nestedTreeItem);
                initTerminationTreeItemHierarchy(nestedTerminationConfigModel,
                                                 nestedTreeItem);
                nestedTreeItem.setState(true);
            }
        }
    }

    public Object getTerminationValue(TerminationConfigModel terminationConfigModel,
                                      TerminationConfigOption terminationConfigOption) {
        switch (terminationConfigOption) {
            case MILLISECONDS_SPENT_LIMIT: {
                return terminationConfigModel.getMillisecondsSpentLimit();
            }
            case SECONDS_SPENT_LIMIT: {
                return terminationConfigModel.getSecondsSpentLimit();
            }
            case MINUTES_SPENT_LIMIT: {
                return terminationConfigModel.getMinutesSpentLimit();
            }
            case HOURS_SPENT_LIMIT: {
                return terminationConfigModel.getHoursSpentLimit();
            }
            case DAYS_SPENT_LIMIT: {
                return terminationConfigModel.getDaysSpentLimit();
            }
            case UNIMPROVED_MILLISECONDS_SPENT_LIMIT: {
                return terminationConfigModel.getUnimprovedMillisecondsSpentLimit();
            }
            case UNIMPROVED_SECONDS_SPENT_LIMIT: {
                return terminationConfigModel.getUnimprovedSecondsSpentLimit();
            }
            case UNIMPROVED_MINUTES_SPENT_LIMIT: {
                return terminationConfigModel.getUnimprovedMinutesSpentLimit();
            }
            case UNIMPROVED_HOURS_SPENT_LIMIT: {
                return terminationConfigModel.getUnimprovedHoursSpentLimit();
            }
            case UNIMPROVED_DAYS_SPENT_LIMIT: {
                return terminationConfigModel.getUnimprovedDaysSpentLimit();
            }
            case BEST_SCORE_LIMIT: {
                return terminationConfigModel.getBestScoreLimit();
            }
            case BEST_SCORE_FEASIBLE: {
                return terminationConfigModel.getBestScoreFeasible();
            }
            case STEP_COUNT_LIMIT: {
                return terminationConfigModel.getStepCountLimit();
            }
            case UNIMPROVED_STEP_COUNT_LIMIT: {
                return terminationConfigModel.getUnimprovedStepCountLimit();
            }
            case SCORE_CALCULATION_COUNT_LIMIT: {
                return terminationConfigModel.getScoreCalculationCountLimit();
            }
            case TERMINATION_COMPOSITION_STYLE: {
                return terminationConfigModel.getTerminationCompositionStyle();
            }
        }
        return null;
    }

    @Override
    public Widget asWidget() {
        return view.asWidget();
    }
}
