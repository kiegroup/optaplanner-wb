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

package org.optaplanner.workbench.backend.server;

import javax.enterprise.event.Event;

import org.guvnor.structure.organizationalunit.OrganizationalUnitService;
import org.guvnor.structure.repositories.RepositoryService;
import org.guvnor.structure.server.config.ConfigGroup;
import org.guvnor.structure.server.config.ConfigItem;
import org.guvnor.structure.server.config.ConfigType;
import org.guvnor.structure.server.config.ConfigurationFactory;
import org.guvnor.structure.server.config.ConfigurationService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kie.workbench.common.services.shared.project.KieModuleService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.uberfire.commons.services.cdi.ApplicationStarted;
import org.uberfire.io.IOService;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AppSetupTest {

    @Mock
    private IOService ioService;

    @Mock
    private RepositoryService repositoryService;

    @Mock
    private OrganizationalUnitService organizationalUnitService;

    @Mock
    private KieModuleService moduleService;

    @Mock
    private ConfigurationService configurationService;

    @Mock
    private ConfigurationFactory configurationFactory;

    @Mock
    private Event<ApplicationStarted> applicationStartedEvent;

    @InjectMocks
    private AppSetup appSetup;

    @Test
    public void init() {
        when(configurationFactory.newConfigGroup(ConfigType.GLOBAL,
                                                 "settings",
                                                 "")).thenReturn(new ConfigGroup());

        ConfigItem configItem = new ConfigItem<>();
        configItem.setName("name");
        configItem.setName("value");
        when(configurationFactory.newConfigItem(anyString(),
                                                anyString())).thenReturn(configItem);

        appSetup.init();

        verify(applicationStartedEvent,
               times(1)).fire(any(ApplicationStarted.class));
    }
}
