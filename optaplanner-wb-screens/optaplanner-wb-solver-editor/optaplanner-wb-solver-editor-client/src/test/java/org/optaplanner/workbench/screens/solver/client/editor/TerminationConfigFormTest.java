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

import java.util.Arrays;
import java.util.List;

import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwtmockito.GwtMockitoTestRunner;
import com.google.gwtmockito.WithClassesToStub;
import org.jboss.errai.common.client.ui.ElementWrapperWidget;
import org.jboss.errai.ioc.client.api.ManagedInstance;
import org.jboss.errai.ui.client.local.spi.TranslationService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.optaplanner.workbench.screens.solver.model.TerminationConfigModel;

import static org.mockito.Mockito.*;

@RunWith(GwtMockitoTestRunner.class)
@WithClassesToStub({ ElementWrapperWidget.class })
public class TerminationConfigFormTest {

    @Mock
    private TerminationConfigFormView view;

    @Mock
    private ManagedInstance<TerminationTreeItemContent> terminationTreeItemContentProvider;

    @Mock
    private TerminationTreeItemContentView terminationTreeItemContentView;

    @Mock
    private TranslationService translationService;

    private TerminationTreeItemContent terminationTreeItemContent;

    private TerminationConfigForm form;

    @Before
    public void setUp() throws Exception {
        form = new TerminationConfigForm( view,
                                          terminationTreeItemContentProvider );
        terminationTreeItemContent = new TerminationTreeItemContent( terminationTreeItemContentView,
                                                                     translationService );
    }

    @Test
    public void setModel() {
        TerminationConfigModel terminationConfigModel = new TerminationConfigModel();
        terminationConfigModel.setBestScoreFeasible( Boolean.TRUE );
        terminationConfigModel.setMillisecondsSpentLimit( 10l );
        List<TerminationConfigModel> terminationConfigModelList = Arrays.asList( new TerminationConfigModel() );
        terminationConfigModel.setTerminationConfigList( terminationConfigModelList );
        when( terminationTreeItemContentProvider.get() ).thenReturn( terminationTreeItemContent );

        form.setModel( terminationConfigModel );

        ArgumentCaptor<TreeItem> treeItemArgumentCaptor = ArgumentCaptor.forClass( TreeItem.class );
        verify( view ).initTree( treeItemArgumentCaptor.capture() );

        TreeItem treeItem = treeItemArgumentCaptor.getValue();
        Assert.assertEquals( 3,
                             treeItem.getChildCount() );
    }
}