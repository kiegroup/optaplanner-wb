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
public class SolverConfigModel {

    protected String solutionClass = null;

    protected List<String> entityClassList = null;

    protected ScoreDirectorFactoryConfigModel scoreDirectorFactoryConfig = null;

    private TerminationConfigModel terminationConfig;

    public String getSolutionClass() {
        return solutionClass;
    }

    public void setSolutionClass( String solutionClass ) {
        this.solutionClass = solutionClass;
    }

    public List<String> getEntityClassList() {
        return entityClassList;
    }

    public void setEntityClassList( List<String> entityClassList ) {
        this.entityClassList = entityClassList;
    }

    public ScoreDirectorFactoryConfigModel getScoreDirectorFactoryConfig() {
        return scoreDirectorFactoryConfig;
    }

    public void setScoreDirectorFactoryConfig( ScoreDirectorFactoryConfigModel scoreDirectorFactoryConfig ) {
        this.scoreDirectorFactoryConfig = scoreDirectorFactoryConfig;
    }

    public TerminationConfigModel getTermination() {
        return terminationConfig;
    }

    public void setTerminationConfig( TerminationConfigModel terminationConfig ) {
        this.terminationConfig = terminationConfig;
    }
}
