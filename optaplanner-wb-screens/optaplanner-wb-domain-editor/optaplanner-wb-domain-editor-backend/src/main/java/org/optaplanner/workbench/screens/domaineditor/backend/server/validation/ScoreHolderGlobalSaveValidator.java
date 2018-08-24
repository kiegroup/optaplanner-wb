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

package org.optaplanner.workbench.screens.domaineditor.backend.server.validation;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.drools.workbench.screens.globals.model.GlobalsModel;
import org.guvnor.common.services.shared.message.Level;
import org.guvnor.common.services.shared.metadata.MetadataService;
import org.guvnor.common.services.shared.metadata.model.Metadata;
import org.guvnor.common.services.shared.validation.model.ValidationMessage;
import org.kie.workbench.common.services.shared.validation.SaveValidator;
import org.optaplanner.workbench.screens.domaineditor.validation.ScoreHolderGlobalToBeDefinedManuallyMessage;
import org.uberfire.backend.vfs.Path;

@ApplicationScoped
public class ScoreHolderGlobalSaveValidator implements SaveValidator<GlobalsModel> {

    @Inject
    private MetadataService metadataService;

    @Override
    public Collection<ValidationMessage> validate(final Path path,
                                                  final GlobalsModel content) {

        Metadata metadata = metadataService.getMetadata(path);
        if (content != null && !metadata.isGenerated()) {
            final boolean containsScoreHolderGlobal = content.getGlobals().stream().anyMatch(g -> "scoreHolder".equals(g.getAlias()));
            if (containsScoreHolderGlobal) {
                return Arrays.asList(new ScoreHolderGlobalToBeDefinedManuallyMessage(Level.ERROR));
            }
        }
        return Collections.emptyList();
    }

    @Override
    public boolean accept(final Path path) {
        return path.getFileName().endsWith(".gdrl");
    }
}
