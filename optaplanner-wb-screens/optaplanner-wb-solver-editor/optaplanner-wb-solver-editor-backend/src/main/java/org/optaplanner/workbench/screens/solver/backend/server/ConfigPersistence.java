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
import org.optaplanner.core.config.solver.SolverConfig;
import org.optaplanner.core.impl.solver.XStreamXmlSolverFactory;
import org.optaplanner.workbench.screens.solver.model.SolverConfigModel;
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
        return new ToSolverConfigModel( toSolverConfig( xml ) ).get();
    }

    public SolverConfig toSolverConfig( String xml ) {
        return (SolverConfig) xStream.fromXML( xml );
    }

    public String toXML( final SolverConfigModel config ) {
        return xStream.toXML( new ToSolverConfig( config ).get() );
    }

}
