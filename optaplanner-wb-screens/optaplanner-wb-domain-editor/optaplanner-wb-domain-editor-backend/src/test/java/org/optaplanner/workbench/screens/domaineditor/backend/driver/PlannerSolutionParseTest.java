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

package org.optaplanner.workbench.screens.domaineditor.backend.driver;

import java.io.InputStream;

import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster._shade.org.eclipse.jdt.core.dom.ParameterizedType;
import org.jboss.forge.roaster._shade.org.eclipse.jdt.core.dom.Type;
import org.jboss.forge.roaster._shade.org.eclipse.jdt.core.dom.TypeDeclaration;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.junit.Test;

public class PlannerSolutionParseTest {


    @Test
    public void createBaseClass() {
        InputStream inputStream = getClass().getResourceAsStream( "/org/optaplanner/workbench/screens/domaineditor/backend/driver/PlannerSolutionExample.java" );
        JavaClassSource classSource = (JavaClassSource) Roaster.parse( inputStream );


        String superClass = classSource.getSuperType();
        TypeDeclaration typeDeclaration = ( ( TypeDeclaration ) ( ( org.jboss.forge.roaster._shade.org.eclipse.jdt.core.dom.CompilationUnit ) classSource.getInternal() ).types().get( 0 ) );

        Type type = typeDeclaration.getSuperclassType();
        ParameterizedType parameterizedType;
        Type typeParameter;
        String solutionClassName = null;
        String solutionClassNameParam = null;

        if ( type != null && type.isParameterizedType() &&
                ( type.toString().startsWith( "org.optaplanner.core.impl.domain.solution.AbstractSolution" ) ||
                    type.toString().startsWith( "AbstractSolution" ) ) ) {

            solutionClassName = "org.optaplanner.core.impl.domain.solution.AbstractSolution";

            parameterizedType = ( ParameterizedType ) type;
            if ( ( parameterizedType.typeArguments() != null && parameterizedType.typeArguments().size() == 1 ) &&
                    isValidSolutionTypeArgument( (Type) parameterizedType.typeArguments().get( 0 ) ) ) {
                typeParameter = (Type) parameterizedType.typeArguments().get( 0 );
                solutionClassNameParam = typeParameter.toString();
            }
        }


        int i = 0;

    }

    private boolean isValidSolutionTypeArgument( Type type ) {
        return
                org.optaplanner.core.api.score.buildin.simple.SimpleScore.class.getName().equals( type.toString() ) ||
                org.optaplanner.core.api.score.buildin.simple.SimpleScore.class.getSimpleName().equals( type.toString() );

    }
}
