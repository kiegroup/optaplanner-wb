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
package org.optaplanner.workbench.screens.solver.model;

import java.util.List;

import org.jboss.errai.common.client.api.annotations.Portable;

@Portable
public class ScoreDirectorFactoryConfigModel {

    protected ScoreDefinitionTypeModel scoreDefinitionType = null;

    protected List<String> scoreDrlList = null;

    public ScoreDefinitionTypeModel getScoreDefinitionType() {
        return scoreDefinitionType;
    }

    public void setScoreDefinitionType( ScoreDefinitionTypeModel scoreDefinitionType ) {
        this.scoreDefinitionType = scoreDefinitionType;
    }

    public List<String> getScoreDrlList() {
        return scoreDrlList;
    }

    public void setScoreDrlList( List<String> scoreDrlList ) {
        this.scoreDrlList = scoreDrlList;
    }
}
