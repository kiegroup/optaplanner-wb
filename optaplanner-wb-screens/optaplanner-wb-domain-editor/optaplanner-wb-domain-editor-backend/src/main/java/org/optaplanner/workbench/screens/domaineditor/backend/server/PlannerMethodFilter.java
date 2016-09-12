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

import java.util.HashSet;
import java.util.Set;
import javax.annotation.Generated;
import javax.enterprise.context.ApplicationScoped;

import org.jboss.forge.roaster.model.Method;
import org.kie.workbench.common.services.datamodeller.driver.MethodFilter;
import org.optaplanner.core.api.score.buildin.bendable.BendableScore;
import org.optaplanner.core.api.score.buildin.bendablebigdecimal.BendableBigDecimalScore;
import org.optaplanner.core.api.score.buildin.bendablelong.BendableLongScore;

@ApplicationScoped
public class PlannerMethodFilter implements MethodFilter {

    private static Set<String> BENDABLE_SCORE_TYPES;

    static {
        BENDABLE_SCORE_TYPES = new HashSet<>();
        BENDABLE_SCORE_TYPES.add( BendableScore.class.getName() );
        BENDABLE_SCORE_TYPES.add( BendableScore.class.getSimpleName() );
        BENDABLE_SCORE_TYPES.add( BendableLongScore.class.getName() );
        BENDABLE_SCORE_TYPES.add( BendableLongScore.class.getSimpleName() );
        BENDABLE_SCORE_TYPES.add( BendableBigDecimalScore.class.getName() );
        BENDABLE_SCORE_TYPES.add( BendableBigDecimalScore.class.getSimpleName() );
    }

    @Override
    public boolean accept( Method<?, ?> method ) {
        return !method.isConstructor() && method.getAnnotation( Generated.class ) != null
                && ( isGetScoreMethod( method ) || isSetScoreMethod( method ) || isCompareMethod( method ) );
    }

    private boolean isGetScoreMethod( Method<?, ?> method ) {
        return "getScore".equals( method.getName() )
                && ( method.getParameters() == null || method.getParameters().isEmpty() )
                && ( method.getReturnType() != null && BENDABLE_SCORE_TYPES.contains( method.getReturnType().getName() ) );
    }

    private boolean isSetScoreMethod( Method<?, ?> method ) {
        return "setScore".equals( method.getName() )
                && ( method.getParameters() != null && method.getParameters().size() == 1 && BENDABLE_SCORE_TYPES.contains( method.getParameters().get( 0 ).getType().getName() ) )
                && ( method.getReturnType() == null || method.getReturnType().getName().equals( "void" ) );
    }

    private boolean isCompareMethod( Method<?, ?> method ) {
        return "compare".equals( method.getName() )
                && ( method.getParameters() != null && method.getParameters().size() == 2 )
                && ( method.getReturnType() != null && method.getReturnType().getName().equals( "int" ) );
    }

}
