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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.enterprise.context.ApplicationScoped;

import org.jboss.errai.bus.server.annotations.Service;
import org.kie.workbench.common.services.datamodeller.codegen.GenerationTools;
import org.kie.workbench.common.services.datamodeller.core.Annotation;
import org.kie.workbench.common.services.datamodeller.core.DataModel;
import org.kie.workbench.common.services.datamodeller.core.DataObject;
import org.kie.workbench.common.services.datamodeller.core.Method;
import org.kie.workbench.common.services.datamodeller.core.ObjectProperty;
import org.kie.workbench.common.services.datamodeller.core.impl.MethodImpl;
import org.optaplanner.workbench.screens.domaineditor.model.ComparatorDefinition;
import org.optaplanner.workbench.screens.domaineditor.model.ComparatorObject;
import org.optaplanner.workbench.screens.domaineditor.model.ComparatorObjectImpl;
import org.optaplanner.workbench.screens.domaineditor.model.ObjectPropertyPath;
import org.optaplanner.workbench.screens.domaineditor.model.ObjectPropertyPathImpl;
import org.optaplanner.workbench.screens.domaineditor.service.PlannerDataObjectEditorService;

import static org.kie.workbench.common.services.datamodeller.codegen.GenerationTools.EOL;

@Service
@ApplicationScoped
public class PlannerDataObjectEditorServiceImpl implements PlannerDataObjectEditorService {

    private GenerationTools generationTools = new GenerationTools();

    @Override
    public ComparatorObject extractComparatorObject( DataObject dataObject, DataModel dataModel ) {
        Annotation comparatorDefinition = dataObject.getAnnotation( ComparatorDefinition.class.getName() );
        if ( comparatorDefinition == null ) {
            return null;
        }
        List<String> fieldPathDefinitions = (List<String>) comparatorDefinition.getValue( "fieldPaths" );

        if ( fieldPathDefinitions == null || fieldPathDefinitions.isEmpty() ) {
            return null;
        }

        List<ObjectPropertyPath> objectPropertyPaths = new ArrayList<>();

        for ( String fieldPathDefinition : fieldPathDefinitions ) {
            if ( !fieldPathDefinition.matches( "\\w[\\.\\w]*:\\w[\\-\\w[\\.\\w]*:\\w]*=(asc|desc)" ) ) {
                throw new IllegalStateException( "Invalid field path string (service)" + fieldPathDefinition );
            }
            String[] fieldPathDefinitionParts = fieldPathDefinition.split( "=" );
            String[] fieldPath = fieldPathDefinitionParts[0].split( "\\-" );
            List<String> fieldPaths = Arrays.asList( fieldPath ).stream().map( o -> o.split( ":" )[1] ).collect( Collectors.toList() );
            boolean descending = fieldPathDefinitionParts[1].equals( "desc" );

            ObjectPropertyPath objectPropertyPath = new ObjectPropertyPathImpl();

            ObjectProperty objectProperty = dataObject.getProperty( fieldPaths.get( 0 ) );
            if ( objectProperty == null ) {
                throw new IllegalStateException( "Field " + fieldPaths.get( 0 ) + " not found in data object " + dataObject.getClassName() );
            }

            objectPropertyPath.appendObjectProperty( objectProperty );

            for ( int i = 1; i < fieldPaths.size(); i++ ) {
                ObjectProperty lastObjectPropertyInPath = objectPropertyPath.getObjectPropertyPath().get( objectPropertyPath.getObjectPropertyPath().size() - 1 );
                if ( lastObjectPropertyInPath.isBaseType() || lastObjectPropertyInPath.isPrimitiveType() ) {
                    throw new IllegalStateException( "Cannot append property " + fieldPaths.get( i ) + " to primitive/base type " + lastObjectPropertyInPath.getClassName() );
                }
                DataObject lastDataObjectInPath = dataModel.getDataObject( lastObjectPropertyInPath.getClassName() );
                if ( lastObjectPropertyInPath == null ) {
                    throw new IllegalStateException( "Data object " + lastObjectPropertyInPath.getClassName() + " not found" );
                }
                ObjectProperty currentObjectProperty = lastDataObjectInPath.getProperty( fieldPaths.get( i ) );
                if ( currentObjectProperty == null ) {
                    throw new IllegalStateException( "Property " + fieldPaths.get( i ) + " not found in data object " + lastDataObjectInPath.getClassName() );
                }
                objectPropertyPath.appendObjectProperty( currentObjectProperty );
            }

            objectPropertyPath.setDescending( descending );
            objectPropertyPaths.add( objectPropertyPath );
        }

        List<String> objectPropertyPathList = new ArrayList<>();
        for ( ObjectPropertyPath objectPropertyPath : objectPropertyPaths ) {
            StringBuilder pathBuilder = new StringBuilder();
            List<ObjectProperty> path = objectPropertyPath.getObjectPropertyPath();
            for ( int i = 0; i < path.size(); i++ ) {
                ObjectProperty objectProperty = path.get( i );

                pathBuilder.append( objectProperty.getClassName() )
                        .append( ":" )
                        .append( objectProperty.getName() );
                if ( i != path.size() - 1 ) {
                    pathBuilder.append( "-" );
                }
            }
            pathBuilder.append( "=" )
                    .append( objectPropertyPath.isDescending() ? "desc" : "asc" );
            objectPropertyPathList.add( pathBuilder.toString() );
        }

        if ( objectPropertyPathList.isEmpty() ) {
            return null;
        }

        ComparatorObject comparatorObject = new ComparatorObjectImpl( "", dataObject.getName() + "Comparator" );
        comparatorObject.setType( dataObject.getClassName() );
        comparatorObject.addInterface( "java.util.Comparator<" + dataObject.getClassName() + ">" );
        comparatorObject.setObjectPropertyPathList( objectPropertyPaths );
        comparatorObject.addMethod( getCompareMethod( comparatorObject ) );

        return comparatorObject;
    }

    @Override
    public ComparatorObject updateComparatorObject( DataObject dataObject, ComparatorObject comparatorObject, List<ObjectPropertyPath> objectPropertyPaths ) {
        if ( objectPropertyPaths == null || objectPropertyPaths.isEmpty() ) {
            return null;
        }
        if ( comparatorObject == null ) {
            comparatorObject = new ComparatorObjectImpl( "", dataObject.getName() + "Comparator" );
        }
        comparatorObject.setName( dataObject.getName() + "Comparator" );
        comparatorObject.setType( dataObject.getClassName() );
        comparatorObject.getInterfaces().clear();
        comparatorObject.addInterface( "java.util.Comparator<" + dataObject.getClassName() + ">" );
        comparatorObject.setObjectPropertyPathList( objectPropertyPaths );
        comparatorObject.getMethods().clear();
        comparatorObject.addMethod( getCompareMethod( comparatorObject ) );

        return comparatorObject;
    }


    private Method getCompareMethod( ComparatorObject comparatorObject ) {
        return new MethodImpl( "compare", Arrays.asList( comparatorObject.getType(), comparatorObject.getType() ), generateCompareBody( comparatorObject ), "int" );
    }

    public String generateCompareBody( ComparatorObject comparatorObject ) {
        StringBuilder sb = new StringBuilder();
        sb.append( "return java.util.Comparator" );

        List<ObjectPropertyPath> objectPropertyPathList = comparatorObject.getObjectPropertyPathList();
        if ( objectPropertyPathList == null || objectPropertyPathList.isEmpty() ) {
            throw new IllegalStateException( "No comparing fields have been specified" );
        }
        ObjectPropertyPath firstObjectPropertyPath = objectPropertyPathList.get( 0 );
        sb.append( getComparingRow( firstObjectPropertyPath, true, comparatorObject ) );
        for ( int i = 1; i < objectPropertyPathList.size(); i++ ) {
            sb.append( getComparingRow( objectPropertyPathList.get( i ), false, comparatorObject ) );
        }
        sb.append( ".compare(o1, o2);" );
        return sb.toString();
    }

    private String getComparingRow( ObjectPropertyPath objectPropertyPath, boolean firstElement, ComparatorObject dataObject ) {
        StringBuilder sb = new StringBuilder();
        String comparatorMethodName = firstElement ? "comparing" : "thenComparing";
        sb.append( "." )
                .append( comparatorMethodName )
                .append( "( (" )
                .append( dataObject.getType() )
                .append( " o) -> { Object tempResult = o;" );
        String previousObjectPropertyClassName = dataObject.getType();
        for ( ObjectProperty pathElement : objectPropertyPath.getObjectPropertyPath() ) {
            sb.append( "tempResult = tempResult == null ? null : " )
                    .append( "(( " )
                    .append( previousObjectPropertyClassName )
                    .append( ") tempResult )." )
                    .append( generationTools.toJavaGetter( pathElement.getName(), pathElement.getClassName() ) )
                    .append( "();" );
            previousObjectPropertyClassName = pathElement.getClassName();
        }
        sb.append( "return tempResult; }" );
        if ( objectPropertyPath.isDescending() ) {
            sb.append( ", java.util.Comparator.reverseOrder()" );
        }
        sb.append( ")" )
                .append( EOL );
        return sb.toString();
    }

}
