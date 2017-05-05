/*
 * Copyright 2017 Red Hat, Inc. and/or its affiliates.
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

package org.optaplanner.workbench.screens.solver.backend.server.indexing;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.guvnor.common.services.project.model.Package;
import org.kie.workbench.common.services.refactoring.backend.server.indexing.AbstractFileIndexer;
import org.kie.workbench.common.services.refactoring.backend.server.indexing.DefaultIndexBuilder;
import org.kie.workbench.common.services.shared.project.KieProject;
import org.kie.workbench.common.services.shared.project.KieProjectService;
import org.optaplanner.workbench.screens.solver.type.SolverResourceTypeDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.uberfire.backend.server.util.Paths;
import org.uberfire.java.nio.file.Path;

@ApplicationScoped
public class SolverFileIndexer extends AbstractFileIndexer {

    private static final Logger logger = LoggerFactory.getLogger(SolverFileIndexer.class);

    @Inject
    private KieProjectService kieProjectService;

    @Inject
    private SolverResourceTypeDefinition solverResourceTypeDefinition;

    @Override
    protected DefaultIndexBuilder fillIndexBuilder(final Path path) throws Exception {
        final org.uberfire.backend.vfs.Path vfsPath = Paths.convert(path);
        final KieProject project = kieProjectService.resolveProject(vfsPath);
        if (project == null) {
            logger.error("Unable to index " + path.toUri() + ", project could not be resolved.");
            return null;
        }
        final Package _package = kieProjectService.resolvePackage(vfsPath);
        if (_package == null) {
            logger.error("Unable to index " + path.toUri() + ", package could not be resolved.");
            return null;
        }
        return new DefaultIndexBuilder(vfsPath.getFileName(),
                                       project,
                                       _package);
    }

    @Override
    public boolean supportsPath(final Path path) {
        return solverResourceTypeDefinition.accept(Paths.convert(path));
    }
}
