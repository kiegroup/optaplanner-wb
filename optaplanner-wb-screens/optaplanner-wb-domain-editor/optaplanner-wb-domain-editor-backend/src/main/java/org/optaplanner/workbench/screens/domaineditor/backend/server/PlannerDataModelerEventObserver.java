/*
 * Copyright 2016 Red Hat, Inc. and/or its affiliates.
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

package org.optaplanner.workbench.screens.domaineditor.backend.server;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.inject.Named;

import org.drools.workbench.screens.drltext.service.DRLTextEditorService;
import org.guvnor.common.services.project.model.Package;
import org.kie.workbench.common.screens.datamodeller.events.DataModelerEvent;
import org.kie.workbench.common.screens.datamodeller.events.DataObjectDeletedEvent;
import org.kie.workbench.common.screens.datamodeller.events.DataObjectSavedEvent;
import org.kie.workbench.common.services.datamodeller.core.DataObject;
import org.kie.workbench.common.services.shared.project.KieProjectService;
import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.uberfire.backend.server.util.Paths;
import org.uberfire.backend.vfs.Path;
import org.uberfire.backend.vfs.PathFactory;
import org.uberfire.io.IOService;

@ApplicationScoped
public class PlannerDataModelerEventObserver {

    private static final String ABSTRACT_SOLUTION_CLASS = "org.optaplanner.core.impl.domain.solution.AbstractSolution";

    private static final String SCORE_HOLDER_DRL_FILE = "scoreHolderGlobalDefinition.drl";

    private KieProjectService projectService;

    private IOService ioService;

    private DRLTextEditorService drlTextEditorService;

    @Inject
    public PlannerDataModelerEventObserver( final KieProjectService projectService,
                                            @Named("ioStrategy") final IOService ioService,
                                            final DRLTextEditorService drlTextEditorService ) {
        this.projectService = projectService;
        this.ioService = ioService;
        this.drlTextEditorService = drlTextEditorService;
    }

    public void onDataObjectSaved( @Observes DataObjectSavedEvent event ) {
        deleteAndCreateScoreHolderDefinitionDrlFile( event, true );
    }

    public void onDataObjectDeleted( @Observes DataObjectDeletedEvent event ) {
        deleteAndCreateScoreHolderDefinitionDrlFile( event, false );
    }

    private void deleteAndCreateScoreHolderDefinitionDrlFile( DataModelerEvent event, boolean createNewScoreHolderDrlFile ) {
        DataObject dataObject = event.getCurrentDataObject();
        if ( dataObject != null && dataObject.getAnnotation( PlanningSolution.class.getName() ) != null ) {
            String superClassDefinition = dataObject.getSuperClassName();
            if ( superClassDefinition.startsWith( ABSTRACT_SOLUTION_CLASS ) ) {
                // Data object file might not exist at this point, use parent package instead
                org.uberfire.java.nio.file.Path parentPath = Paths.convert( event.getPath() ).getParent();

                Package solutionDataObjectPackage = projectService.resolvePackage( Paths.convert( parentPath ) );

                if ( solutionDataObjectPackage == null ) {
                    throw new IllegalStateException( "Failed to resolve package for " + SCORE_HOLDER_DRL_FILE );
                }

                Path solutionDataObjectResourcesPath = solutionDataObjectPackage.getPackageMainResourcesPath();

                final Path scoreHolderDrlFilePath = PathFactory.newPathBasedOn( SCORE_HOLDER_DRL_FILE,
                        solutionDataObjectResourcesPath.toURI() + "/" + SCORE_HOLDER_DRL_FILE,
                        solutionDataObjectResourcesPath );

                if ( ioService.exists( Paths.convert( scoreHolderDrlFilePath ) ) ) {
                    drlTextEditorService.delete( scoreHolderDrlFilePath, "Delete existing score holder definition." );
                }

                if ( createNewScoreHolderDrlFile ) {
                    String scoreType = superClassDefinition.substring( superClassDefinition.indexOf( "<" ) + 1, superClassDefinition.indexOf( ">" ) );
                    String simpleScoreTypeName = scoreType.substring( scoreType.lastIndexOf( "." ) + 1 );
                    String lineSeparator = System.getProperty( "line.separator" );

                    StringBuilder contentStringBuilder = new StringBuilder();
                    contentStringBuilder
                            .append( "import " ).append( scoreType ).append( ";" ).append( lineSeparator )
                            .append( lineSeparator )
                            .append( "// This file was automatically generated to provide scoreHolder variable in Guided rule editor" ).append( lineSeparator )
                            .append( lineSeparator )
                            .append( "global " ).append( simpleScoreTypeName ).append( "Holder" ).append( " scoreHolder;" ).append( lineSeparator );

                    drlTextEditorService.create( solutionDataObjectResourcesPath,
                            SCORE_HOLDER_DRL_FILE,
                            contentStringBuilder.toString(),
                            "Create score holder definition for score type " + simpleScoreTypeName );
                }
            }
        }
    }
}
