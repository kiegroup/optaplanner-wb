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

package org.optaplanner.workbench.screens.solver.backend.server;

import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.inject.Named;

import org.guvnor.common.services.backend.exceptions.ExceptionUtilities;
import org.guvnor.common.services.backend.util.CommentedOptionFactory;
import org.guvnor.common.services.shared.metadata.model.Metadata;
import org.guvnor.common.services.shared.metadata.model.Overview;
import org.guvnor.common.services.shared.validation.model.ValidationMessage;
import org.jboss.errai.bus.server.annotations.Service;
import org.kie.workbench.common.services.backend.service.KieService;
import org.optaplanner.workbench.screens.solver.model.SolverConfigModel;
import org.optaplanner.workbench.screens.solver.model.SolverModelContent;
import org.optaplanner.workbench.screens.solver.service.SolverEditorService;
import org.optaplanner.workbench.screens.solver.type.SolverResourceTypeDefinition;
import org.uberfire.backend.server.util.Paths;
import org.uberfire.backend.vfs.Path;
import org.uberfire.ext.editor.commons.service.CopyService;
import org.uberfire.ext.editor.commons.service.DeleteService;
import org.uberfire.ext.editor.commons.service.RenameService;
import org.uberfire.io.IOService;
import org.uberfire.java.nio.file.FileAlreadyExistsException;
import org.uberfire.rpc.SessionInfo;
import org.uberfire.workbench.events.ResourceOpenedEvent;

@Service
@ApplicationScoped
public class SolverEditorServiceImpl
        extends KieService<SolverModelContent>
        implements SolverEditorService {

    @Inject
    @Named("ioStrategy")
    private IOService ioService;

    @Inject
    private CopyService copyService;

    @Inject
    private DeleteService deleteService;

    @Inject
    private RenameService renameService;

    @Inject
    private Event<ResourceOpenedEvent> resourceOpenedEvent;

    @Inject
    private SolverResourceTypeDefinition solverResourceType;

    @Inject
    private ConfigPersistence configPersistence;

    @Inject
    private SolverValidator solverValidator;

    @Inject
    private SessionInfo sessionInfo;

    @Inject
    private CommentedOptionFactory commentedOptionFactory;

    @Override
    public Path create( final Path context,
                        final String fileName,
                        final SolverConfigModel config,
                        final String comment ) {
        try {
            final org.uberfire.java.nio.file.Path nioPath = Paths.convert( context ).resolve( fileName );
            final Path newPath = Paths.convert( nioPath );

            if ( ioService.exists( nioPath ) ) {
                throw new FileAlreadyExistsException( nioPath.toString() );
            }

            ioService.write( nioPath,
                             configPersistence.toXML( config ),
                             commentedOptionFactory.makeCommentedOption( comment ) );

            return newPath;

        } catch ( Exception e ) {
            throw ExceptionUtilities.handleException( e );
        }
    }

    @Override
    public SolverConfigModel load( final Path path ) {
        String xml = ioService.readAllString( Paths.convert( path ) );
        return configPersistence.toConfig( xml );
    }

    @Override
    public SolverModelContent loadContent( final Path path ) {
        return super.loadContent( path );
    }

    @Override
    protected SolverModelContent constructContent( Path path,
                                                   Overview overview ) {

        //Signal opening to interested parties
        resourceOpenedEvent.fire( new ResourceOpenedEvent( path,
                                                           sessionInfo ) );

        return new SolverModelContent( load( path ),
                                       overview );
    }

    @Override
    public String toSource( final Path path,
                            final SolverConfigModel model ) {
        return configPersistence.toXML( model );
    }

    @Override
    public Path save( final Path resource,
                      final SolverConfigModel config,
                      final Metadata metadata,
                      final String comment ) {
        try {
            Metadata currentMetadata = metadataService.getMetadata( resource );
            ioService.write( Paths.convert( resource ),
                             configPersistence.toXML( config ),
                             metadataService.setUpAttributes( resource,
                                                              metadata ),
                             commentedOptionFactory.makeCommentedOption( comment ) );

            fireMetadataSocialEvents( resource,
                                      currentMetadata,
                                      metadata );

            return resource;

        } catch ( Exception e ) {
            throw ExceptionUtilities.handleException( e );
        }
    }

    @Override
    public void delete( final Path path,
                        final String comment ) {
        try {
            deleteService.delete( path,
                                  comment );

        } catch ( Exception e ) {
            throw ExceptionUtilities.handleException( e );
        }
    }

    @Override
    public Path rename( final Path path,
                        final String newName,
                        final String comment ) {
        try {
            return renameService.rename( path,
                                         newName,
                                         comment );

        } catch (Exception e) {
            throw ExceptionUtilities.handleException( e );
        }
    }

    @Override
    public Path copy( final Path path,
                      final String newName,
                      final String comment ) {
        try {
            return copyService.copy( path,
                                     newName,
                                     comment );

        } catch ( Exception e ) {
            throw ExceptionUtilities.handleException( e );
        }
    }

    @Override
    public List<ValidationMessage> validate( final Path path,
                                             final SolverConfigModel config ) {
        try {

            return solverValidator.validate( path,
                                             toSource( path, config ) );

        } catch ( Exception e ) {
            throw ExceptionUtilities.handleException( e );
        }
    }
}
