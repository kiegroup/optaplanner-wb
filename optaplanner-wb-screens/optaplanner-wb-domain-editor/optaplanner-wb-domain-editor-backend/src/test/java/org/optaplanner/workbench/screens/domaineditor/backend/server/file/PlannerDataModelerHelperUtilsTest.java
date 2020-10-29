/*
 * Copyright 2018 Red Hat, Inc. and/or its affiliates.
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

package org.optaplanner.workbench.screens.domaineditor.backend.server.file;

import java.util.Collections;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kie.workbench.common.screens.datamodeller.model.DataModelerError;
import org.kie.workbench.common.screens.datamodeller.model.GenerationResult;
import org.kie.workbench.common.screens.datamodeller.service.DataModelerService;
import org.kie.workbench.common.services.datamodeller.core.DataObject;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.optaplanner.workbench.screens.domaineditor.service.ComparatorDefinitionService;
import org.uberfire.backend.server.util.Paths;
import org.uberfire.backend.vfs.Path;
import org.uberfire.io.IOService;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PlannerDataModelerHelperUtilsTest {

    @Mock
    private IOService ioService;
    @Mock
    private ComparatorDefinitionService comparatorDefinitionService;
    @Mock
    private DataModelerService dataModelerService;
    @Mock
    private Path dataObjectPath;

    private PlannerDataModelerHelperUtils plannerDataModelerHelperUtils;
    private GenerationResult generationResult;

    @Before
    public void setUp() {
        plannerDataModelerHelperUtils = new PlannerDataModelerHelperUtils(ioService,
                                                                          comparatorDefinitionService,
                                                                          dataModelerService);
        generationResult = new GenerationResult();
        final String pathString = "test source";

        when(dataObjectPath.toURI())
                .thenReturn("file:///dataObjects/Test.java");
        when(ioService.readAllString(Paths.convert(dataObjectPath)))
                .thenReturn(pathString);
        when(dataModelerService.loadDataObject(dataObjectPath, pathString, dataObjectPath))
                .thenReturn(generationResult);
    }

    @Test
    public void generationResultHasErrors() {
        generationResult.setErrors(Collections.singletonList(mock(DataModelerError.class)));
        DataObject dataObject = mock(DataObject.class);
        generationResult.setDataObject(dataObject);
        plannerDataModelerHelperUtils.updateDataObject(dataObjectPath);

        verify(dataObject, never()).getAnnotation(any());
    }

    @Test
    public void dataObjectIsNull() {
        generationResult.setDataObject(null);
        // must not throw NPE
        plannerDataModelerHelperUtils.updateDataObject(dataObjectPath);
    }

    @Test
    public void generationResultErrorsIsNull() {
        generationResult.setErrors(null);
        DataObject dataObject = mock(DataObject.class);
        generationResult.setDataObject(dataObject);
        plannerDataModelerHelperUtils.updateDataObject(dataObjectPath);

        verify(dataObject).getAnnotation(any());
    }

    @Test
    public void generationResultErrorsIsEmpty() {
        generationResult.setErrors(Collections.emptyList());
        DataObject dataObject = mock(DataObject.class);
        generationResult.setDataObject(dataObject);
        plannerDataModelerHelperUtils.updateDataObject(dataObjectPath);

        verify(dataObject).getAnnotation(any());
    }
}
