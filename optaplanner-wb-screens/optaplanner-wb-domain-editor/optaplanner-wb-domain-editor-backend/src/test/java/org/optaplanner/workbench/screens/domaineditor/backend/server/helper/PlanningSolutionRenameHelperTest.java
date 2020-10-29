/*
 * Copyright 2017 Red Hat, Inc. and/or its affiliates.
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

package org.optaplanner.workbench.screens.domaineditor.backend.server.helper;

import org.guvnor.common.services.project.model.Package;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kie.workbench.common.screens.datamodeller.model.GenerationResult;
import org.kie.workbench.common.screens.datamodeller.service.DataModelerService;
import org.kie.workbench.common.services.datamodeller.core.DataObject;
import org.kie.workbench.common.services.datamodeller.core.impl.AnnotationImpl;
import org.kie.workbench.common.services.datamodeller.core.impl.DataObjectImpl;
import org.kie.workbench.common.services.datamodeller.util.DriverUtils;
import org.kie.workbench.common.services.shared.project.KieModuleService;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.uberfire.backend.server.util.Paths;
import org.uberfire.backend.vfs.Path;
import org.uberfire.backend.vfs.PathFactory;
import org.uberfire.io.IOService;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PlanningSolutionRenameHelperTest {

    @Mock
    private IOService ioService;

    @Mock
    private DataModelerService dataModelerService;

    @Mock
    private KieModuleService kieModuleService;

    private Path sourcePath;
    private Path destinationPath;
    private GenerationResult generationResult;
    private PlanningSolutionRenameWorkaroundHelper renameHelper;

    @Before
    public void setUp() {
        generationResult = new GenerationResult();
        renameHelper = new PlanningSolutionRenameWorkaroundHelper(ioService,
                                                                  dataModelerService,
                                                                  kieModuleService);
        when(dataModelerService.loadDataObject(any(),
                                               any(),
                                               any())).thenReturn(generationResult);
        sourcePath = PathFactory.newPath("TestSource.java", "file:///dataObjects");
        destinationPath = PathFactory.newPath("TestDestination.java", "file:///dataObjects");
    }

    @Test
    public void postProcess() {
        DataObject dataObject = new DataObjectImpl("test",
                                                   "TestSource");
        dataObject.addAnnotation(new AnnotationImpl(DriverUtils.buildAnnotationDefinition(PlanningSolution.class)));
        generationResult.setDataObject(dataObject);

        Package _package = mock(Package.class);
        when(_package.getPackageMainResourcesPath()).thenReturn(PathFactory.newPath("dataObjects",
                                                                                    "file:///dataObjects"));
        when(kieModuleService.resolvePackage(sourcePath)).thenReturn(_package);

        when(ioService.exists(any())).thenReturn(true);

        renameHelper.postProcess(sourcePath,
                                 destinationPath);

        verify(ioService,
               times(1)).move(any(org.uberfire.java.nio.file.Path.class),
                              any(org.uberfire.java.nio.file.Path.class));
    }

    @Test
    public void sourceDataObjectIsNull() {
        generationResult.setDataObject(null);

        // must not throw NPE
        renameHelper.postProcess(sourcePath, destinationPath);

        verify(ioService, never()).exists(any());
    }

    @Test
    public void sourceDataObjectIsNotPlanningSolution() {
        DataObject dataObject = new DataObjectImpl("test", "TestSource");
        generationResult.setDataObject(dataObject);

        renameHelper.postProcess(sourcePath, destinationPath);

        verify(ioService, never()).exists(any());
    }
}
