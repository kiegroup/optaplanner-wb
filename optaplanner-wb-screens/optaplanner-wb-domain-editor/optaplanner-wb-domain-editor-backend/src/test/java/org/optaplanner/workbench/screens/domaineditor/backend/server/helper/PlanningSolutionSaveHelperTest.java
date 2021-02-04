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

import org.drools.workbench.screens.globals.model.GlobalsModel;
import org.drools.workbench.screens.globals.service.GlobalsEditorService;
import org.guvnor.common.services.project.model.Package;
import org.guvnor.common.services.shared.metadata.MetadataService;
import org.guvnor.common.services.shared.metadata.model.Metadata;
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
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScoreHolder;
import org.optaplanner.workbench.screens.domaineditor.backend.server.validation.ScoreHolderUtils;
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
public class PlanningSolutionSaveHelperTest {

    @Mock
    private IOService ioService;

    @Mock
    private DataModelerService dataModelerService;

    @Mock
    private GlobalsEditorService globalsEditorService;

    @Mock
    private KieModuleService kieModuleService;

    @Mock
    private ScoreHolderUtils scoreHolderUtils;

    @Mock
    private MetadataService metadataService;

    private PlanningSolutionSaveHelper saveHelper;

    @Before
    public void setUp() {
        saveHelper = new PlanningSolutionSaveHelper(ioService,
                                                    dataModelerService,
                                                    globalsEditorService,
                                                    kieModuleService,
                                                    scoreHolderUtils,
                                                    metadataService);
    }

    @Test
    public void scoreTypeChangedScoreHolderGlobalFileExisting() {
        Path sourcePath = PathFactory.newPath("TestSource.java",
                                              "file:///dataObjects");
        Path destinationPath = PathFactory.newPath("TestSource.java",
                                                   "file:///dataObjects");
        testPlanningSolutionSaved(true,
                                  sourcePath,
                                  destinationPath);
    }

    @Test
    public void scoreTypeChangedScoreHolderGlobalFileNonExisting() {
        Path sourcePath = PathFactory.newPath("TestSource.java",
                                              "file:///dataObjects");
        Path destinationPath = PathFactory.newPath("TestSource.java",
                                                   "file:///dataObjects");
        testPlanningSolutionSaved(false,
                                  sourcePath,
                                  destinationPath);
    }

    @Test
    public void planningSolutionDataObjectRenamed() {
        Path sourcePath = PathFactory.newPath("TestSource.java",
                                              "file:///dataObjects1");
        Path destinationPath = PathFactory.newPath("TestDestination.java",
                                                   "file:///dataObjects2");
        testPlanningSolutionSaved(false,
                                  sourcePath,
                                  destinationPath);
    }

    private void testPlanningSolutionSaved(boolean scoreHolderGlobalFileExists,
                                           Path sourcePath,
                                           Path destinationPath) {
        when(ioService.readAllString(Paths.convert(sourcePath))).thenReturn("test source");

        DataObject dataObject = new DataObjectImpl("test",
                                                   "TestSource");
        dataObject.addAnnotation(new AnnotationImpl(DriverUtils.buildAnnotationDefinition(PlanningSolution.class)));
        GenerationResult generationResult = new GenerationResult();
        generationResult.setDataObject(dataObject);
        when(dataModelerService.loadDataObject(any(),
                                               any(),
                                               any())).thenReturn(generationResult);

        Package _package = mock(Package.class);
        when(_package.getPackageMainResourcesPath()).thenReturn(PathFactory.newPath("dataObjects",
                                                                                    "file:///dataObjects"));
        when(kieModuleService.resolvePackage(any(Path.class))).thenReturn(_package);

        when(scoreHolderUtils.extractScoreTypeFqn(dataObject)).thenReturn(HardSoftScore.class.getName());
        when(scoreHolderUtils.getScoreHolderTypeFqn(HardSoftScore.class.getName())).thenReturn(HardSoftScoreHolder.class.getName());
        when(ioService.exists(any())).thenReturn(scoreHolderGlobalFileExists);
        when(globalsEditorService.load(any(Path.class))).thenReturn(mock(GlobalsModel.class));

        saveHelper.postProcess(sourcePath,
                               destinationPath);

        if (sourcePath.equals(destinationPath)) {
            if (scoreHolderGlobalFileExists) {
                verify(globalsEditorService,
                       times(1)).save(any(Path.class),
                                      any(GlobalsModel.class),
                                      any(),
                                      anyString());
            } else {
                verify(globalsEditorService,
                       times(1)).generate(any(Path.class),
                                          anyString(),
                                          any(GlobalsModel.class),
                                          anyString());
            }
        } else {
            verify(ioService).deleteIfExists(any(org.uberfire.java.nio.file.Path.class));
        }
    }

    @Test
    public void saveDataObjectIsNull() {
        Path sourcePath = PathFactory.newPath("TestSource.java",
                                              "file:///dataObjects");
        Path destinationPath = PathFactory.newPath("TestSource.java",
                                                   "file:///dataObjects");

        when(ioService.readAllString(Paths.convert(sourcePath))).thenReturn("test source");

        GenerationResult generationResult = new GenerationResult();
        generationResult.setDataObject(null);
        when(dataModelerService.loadDataObject(any(),
                                               anyString(),
                                               any())).thenReturn(generationResult);

        Package _package = mock(Package.class);
        when(_package.getPackageMainResourcesPath()).thenReturn(PathFactory.newPath("dataObjects",
                                                                                    "file:///dataObjects"));
        when(kieModuleService.resolvePackage(any(Path.class))).thenReturn(_package);

        saveHelper.postProcess(sourcePath,
                               destinationPath);

        verify(scoreHolderUtils, never()).extractScoreTypeFqn(any());
    }

    @Test
    public void saveDataObjectNotAPlanningSolution() {
        Path sourcePath = PathFactory.newPath("TestSource.java",
                                              "file:///dataObjects");
        Path destinationPath = PathFactory.newPath("TestSource.java",
                                                   "file:///dataObjects");

        when(ioService.readAllString(Paths.convert(sourcePath))).thenReturn("test source");

        DataObject dataObject = new DataObjectImpl("test",
                                                   "TestSource");
        GenerationResult generationResult = new GenerationResult();
        generationResult.setDataObject(dataObject);
        when(dataModelerService.loadDataObject(any(),
                                               anyString(),
                                               any())).thenReturn(generationResult);

        Package _package = mock(Package.class);
        when(_package.getPackageMainResourcesPath()).thenReturn(PathFactory.newPath("dataObjects",
                                                                                    "file:///dataObjects"));
        when(kieModuleService.resolvePackage(any(Path.class))).thenReturn(_package);

        saveHelper.postProcess(sourcePath,
                               destinationPath);

        verify(ioService).deleteIfExists(any(org.uberfire.java.nio.file.Path.class));
    }
}
