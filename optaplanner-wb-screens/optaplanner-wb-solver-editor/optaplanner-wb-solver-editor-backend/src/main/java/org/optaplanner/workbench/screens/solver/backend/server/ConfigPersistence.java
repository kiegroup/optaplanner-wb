/*
 * Copyright 2015 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.optaplanner.workbench.screens.solver.backend.server;

import javax.inject.Inject;
import javax.inject.Named;

import com.thoughtworks.xstream.XStream;
import org.optaplanner.core.config.domain.ScanAnnotatedClassesConfig;
import org.optaplanner.core.config.score.definition.ScoreDefinitionType;
import org.optaplanner.core.config.score.director.ScoreDirectorFactoryConfig;
import org.optaplanner.core.config.solver.SolverConfig;
import org.optaplanner.core.config.solver.termination.TerminationConfig;
import org.optaplanner.core.impl.solver.XStreamXmlSolverFactory;
import org.optaplanner.workbench.screens.solver.model.ScoreDefinitionTypeModel;
import org.optaplanner.workbench.screens.solver.model.ScoreDirectorFactoryConfigModel;
import org.optaplanner.workbench.screens.solver.model.SolverConfigModel;
import org.optaplanner.workbench.screens.solver.model.TerminationConfigModel;
import org.uberfire.io.IOService;

public class ConfigPersistence {

    @Inject
    @Named("ioStrategy")
    private IOService ioService;

    private final XStream xStream;

    public ConfigPersistence() {
        xStream = XStreamXmlSolverFactory.buildXStream();
    }

    public SolverConfigModel toConfig( final String xml ) {
        SolverConfig solverConfig = toSolverConfig( xml );

        SolverConfigModel model = new SolverConfigModel();
        model.setTerminationConfig( create( solverConfig.getTerminationConfig() ) );
        model.setScoreDirectorFactoryConfig( create( solverConfig.getScoreDirectorFactoryConfig() ) );

        return model;
    }

    public SolverConfig toSolverConfig( String xml ) {
        return (SolverConfig) xStream.fromXML( xml );
    }

    private ScoreDirectorFactoryConfigModel create( ScoreDirectorFactoryConfig scoreDirectorFactoryConfig ) {
        ScoreDirectorFactoryConfigModel model = new ScoreDirectorFactoryConfigModel();

        if ( scoreDirectorFactoryConfig == null ) {
            return model;
        } else {
            model.setScoreDefinitionType( create( scoreDirectorFactoryConfig.getScoreDefinitionType() ) );
            model.setScoreDrlList( scoreDirectorFactoryConfig.getScoreDrlList() );

            return model;
        }
    }

    private ScoreDefinitionTypeModel create( ScoreDefinitionType scoreDefinitionType ) {
        if ( scoreDefinitionType != null ) {
            for (ScoreDefinitionTypeModel model : ScoreDefinitionTypeModel.values()) {
                if ( model.name().equals( scoreDefinitionType.name() ) ) {
                    return model;
                }
            }
        }

        return null;
    }

    private TerminationConfigModel create( TerminationConfig terminationConfig ) {
        TerminationConfigModel model = new TerminationConfigModel();

        if ( terminationConfig == null ) {
            return model;
        } else {
            model.setDaysSpentLimit( terminationConfig.getDaysSpentLimit() );
            model.setHoursSpentLimit( terminationConfig.getHoursSpentLimit() );
            model.setMinutesSpentLimit( terminationConfig.getMinutesSpentLimit() );
            model.setSecondsSpentLimit( terminationConfig.getSecondsSpentLimit() );
            model.setMillisecondsSpentLimit( terminationConfig.getMillisecondsSpentLimit() );

            model.setUnimprovedDaysSpentLimit( terminationConfig.getUnimprovedDaysSpentLimit() );
            model.setUnimprovedHoursSpentLimit( terminationConfig.getUnimprovedHoursSpentLimit() );
            model.setUnimprovedMinutesSpentLimit( terminationConfig.getUnimprovedMinutesSpentLimit() );
            model.setUnimprovedSecondsSpentLimit( terminationConfig.getUnimprovedSecondsSpentLimit() );
            model.setUnimprovedMillisecondsSpentLimit( terminationConfig.getUnimprovedMillisecondsSpentLimit() );

            return model;
    }
    }

    public String toXML( final SolverConfigModel config ) {

        SolverConfig solverConfig = new SolverConfig();

        solverConfig.setScanAnnotatedClassesConfig( new ScanAnnotatedClassesConfig() );

        solverConfig.setTerminationConfig( create( config.getTermination() ) );
        solverConfig.setScoreDirectorFactoryConfig( create( config.getScoreDirectorFactoryConfig() ) );

        return xStream.toXML( solverConfig );
    }

    private ScoreDirectorFactoryConfig create( ScoreDirectorFactoryConfigModel scoreDirectorFactoryConfig ) {
        ScoreDirectorFactoryConfig config = new ScoreDirectorFactoryConfig();

        if ( scoreDirectorFactoryConfig == null ) {
            return config;
        } else {
            config.setScoreDefinitionType( create( scoreDirectorFactoryConfig.getScoreDefinitionType() ) );
            config.setScoreDrlList( scoreDirectorFactoryConfig.getScoreDrlList() );
            return config;
        }
    }

    private ScoreDefinitionType create( ScoreDefinitionTypeModel scoreDefinitionType ) {
        if ( scoreDefinitionType != null ) {
            for (ScoreDefinitionType model : ScoreDefinitionType.values()) {
                if ( model.name().equals( scoreDefinitionType.name() ) ) {
                    return model;
                }
            }
        }
        return null;
    }

    private TerminationConfig create( TerminationConfigModel termination ) {
        TerminationConfig terminationConfig = new TerminationConfig();

        if ( termination == null ) {
            return terminationConfig;
        } else {
            terminationConfig.setDaysSpentLimit( termination.getDaysSpentLimit() );
            terminationConfig.setHoursSpentLimit( termination.getHoursSpentLimit() );
            terminationConfig.setMinutesSpentLimit( termination.getMinutesSpentLimit() );
            terminationConfig.setSecondsSpentLimit( termination.getSecondsSpentLimit() );
            terminationConfig.setMillisecondsSpentLimit( termination.getMillisecondsSpentLimit() );

            terminationConfig.setUnimprovedDaysSpentLimit( termination.getUnimprovedDaysSpentLimit() );
            terminationConfig.setUnimprovedHoursSpentLimit( termination.getUnimprovedHoursSpentLimit() );
            terminationConfig.setUnimprovedMinutesSpentLimit( termination.getUnimprovedMinutesSpentLimit() );
            terminationConfig.setUnimprovedSecondsSpentLimit( termination.getUnimprovedSecondsSpentLimit() );
            terminationConfig.setUnimprovedMillisecondsSpentLimit( termination.getUnimprovedMillisecondsSpentLimit() );

            return terminationConfig;
    }
    }
}
