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

import org.guvnor.common.services.project.model.Package;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kie.workbench.common.services.refactoring.backend.server.indexing.DefaultIndexBuilder;
import org.kie.workbench.common.services.shared.project.KieModule;
import org.kie.workbench.common.services.shared.project.KieModuleService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.optaplanner.workbench.screens.solver.type.SolverResourceTypeDefinition;
import org.uberfire.backend.server.util.Paths;
import org.uberfire.backend.vfs.Path;
import org.uberfire.backend.vfs.PathFactory;

import static junit.framework.TestCase.assertNotNull;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class SolverFileIndexerTest {

    @Mock
    private KieModuleService moduleService;

    @Mock
    private SolverResourceTypeDefinition resourceTypeDefinition;

    @InjectMocks
    private SolverFileIndexer solverFileIndexer;

    @Test
    public void fillIndexBuilder() throws Exception {
        Path path = PathFactory.newPath("SolverConfig.solver.xml",
                                        "default:///test/SolverConfig.solver.xml");

        when(moduleService.resolveModule(path)).thenReturn(mock(KieModule.class));
        when(moduleService.resolvePackage(path)).thenReturn(mock(Package.class));

        DefaultIndexBuilder indexBuilder = solverFileIndexer.fillIndexBuilder(Paths.convert(path));

        assertNotNull(indexBuilder);
    }
}
