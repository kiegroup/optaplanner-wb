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

import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.drools.core.base.ClassTypeResolver;
import org.kie.workbench.common.services.backend.project.ProjectClassLoaderHelper;
import org.kie.workbench.common.services.datamodeller.core.DataObject;
import org.kie.workbench.common.services.shared.project.KieProject;
import org.kie.workbench.common.services.shared.project.KieProjectService;
import org.optaplanner.core.api.score.buildin.bendable.BendableScore;
import org.optaplanner.core.api.score.buildin.bendable.BendableScoreHolder;
import org.optaplanner.core.api.score.buildin.bendablebigdecimal.BendableBigDecimalScore;
import org.optaplanner.core.api.score.buildin.bendablebigdecimal.BendableBigDecimalScoreHolder;
import org.optaplanner.core.api.score.buildin.bendablelong.BendableLongScore;
import org.optaplanner.core.api.score.buildin.bendablelong.BendableLongScoreHolder;
import org.optaplanner.core.api.score.buildin.hardmediumsoft.HardMediumSoftScore;
import org.optaplanner.core.api.score.buildin.hardmediumsoft.HardMediumSoftScoreHolder;
import org.optaplanner.core.api.score.buildin.hardmediumsoftbigdecimal.HardMediumSoftBigDecimalScore;
import org.optaplanner.core.api.score.buildin.hardmediumsoftbigdecimal.HardMediumSoftBigDecimalScoreHolder;
import org.optaplanner.core.api.score.buildin.hardmediumsoftlong.HardMediumSoftLongScore;
import org.optaplanner.core.api.score.buildin.hardmediumsoftlong.HardMediumSoftLongScoreHolder;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScoreHolder;
import org.optaplanner.core.api.score.buildin.hardsoftbigdecimal.HardSoftBigDecimalScore;
import org.optaplanner.core.api.score.buildin.hardsoftbigdecimal.HardSoftBigDecimalScoreHolder;
import org.optaplanner.core.api.score.buildin.hardsoftdouble.HardSoftDoubleScore;
import org.optaplanner.core.api.score.buildin.hardsoftdouble.HardSoftDoubleScoreHolder;
import org.optaplanner.core.api.score.buildin.hardsoftlong.HardSoftLongScore;
import org.optaplanner.core.api.score.buildin.hardsoftlong.HardSoftLongScoreHolder;
import org.optaplanner.core.api.score.buildin.simple.SimpleScore;
import org.optaplanner.core.api.score.buildin.simple.SimpleScoreHolder;
import org.optaplanner.core.api.score.buildin.simplebigdecimal.SimpleBigDecimalScore;
import org.optaplanner.core.api.score.buildin.simplebigdecimal.SimpleBigDecimalScoreHolder;
import org.optaplanner.core.api.score.buildin.simpledouble.SimpleDoubleScore;
import org.optaplanner.core.api.score.buildin.simpledouble.SimpleDoubleScoreHolder;
import org.optaplanner.core.api.score.buildin.simplelong.SimpleLongScore;
import org.optaplanner.core.api.score.buildin.simplelong.SimpleLongScoreHolder;
import org.uberfire.backend.vfs.Path;

@ApplicationScoped
public class ScoreHolderUtils {

    private static final Pattern ABSTRACT_SOLUTION_PATTERN = Pattern.compile( "[\\w+\\.]*AbstractSolution<([\\w+\\.]*\\w+)>" );

    private KieProjectService kieProjectService;

    private ProjectClassLoaderHelper classLoaderHelper;

    public ScoreHolderUtils() {
    }

    @Inject
    public ScoreHolderUtils( final KieProjectService kieProjectService,
                             final ProjectClassLoaderHelper classLoaderHelper ) {
        this.kieProjectService = kieProjectService;
        this.classLoaderHelper = classLoaderHelper;
    }

    public String extractScoreTypeFqn( final DataObject dataObject,
                                       final Path dataObjectPath ) {
        Matcher matcher = ABSTRACT_SOLUTION_PATTERN.matcher( dataObject.getSuperClassName() );

        if ( matcher.matches() ) {
            String scoreType = matcher.group( 1 );

            final String scoreTypeFqn;
            if ( scoreType.contains( "." ) ) {
                scoreTypeFqn = scoreType;
            } else {
                KieProject kieProject = kieProjectService.resolveProject( dataObjectPath );
                Set<String> imports = dataObject.getImports().stream().map( i -> i.getName() ).collect( Collectors.toSet() );
                ClassLoader projectClassLoader = classLoaderHelper.getProjectClassLoader( kieProject );
                ClassTypeResolver classTypeResolver = new ClassTypeResolver( imports,
                                                                             projectClassLoader );
                try {
                    scoreTypeFqn = classTypeResolver.getFullTypeName( scoreType );
                } catch ( ClassNotFoundException e ) {
                    return null;
                }
            }
            return scoreTypeFqn;
        }
        return null;
    }

    public String getScoreHolderTypeFqn( final String scoreTypeFqn ) {
        if ( BendableScore.class.getName().equals( scoreTypeFqn ) ) {
            return BendableScoreHolder.class.getName();
        } else if ( BendableBigDecimalScore.class.getName().equals( scoreTypeFqn ) ) {
            return BendableBigDecimalScoreHolder.class.getName();
        } else if ( BendableLongScore.class.getName().equals( scoreTypeFqn ) ) {
            return BendableLongScoreHolder.class.getName();
        } else if ( HardMediumSoftScore.class.getName().equals( scoreTypeFqn ) ) {
            return HardMediumSoftScoreHolder.class.getName();
        } else if ( HardMediumSoftBigDecimalScore.class.getName().equals( scoreTypeFqn ) ) {
            return HardMediumSoftBigDecimalScoreHolder.class.getName();
        } else if ( HardMediumSoftLongScore.class.getName().equals( scoreTypeFqn ) ) {
            return HardMediumSoftLongScoreHolder.class.getName();
        } else if ( HardSoftScore.class.getName().equals( scoreTypeFqn ) ) {
            return HardSoftScoreHolder.class.getName();
        } else if ( HardSoftBigDecimalScore.class.getName().equals( scoreTypeFqn ) ) {
            return HardSoftBigDecimalScoreHolder.class.getName();
        } else if ( HardSoftDoubleScore.class.getName().equals( scoreTypeFqn ) ) {
            return HardSoftDoubleScoreHolder.class.getName();
        } else if ( HardSoftLongScore.class.getName().equals( scoreTypeFqn ) ) {
            return HardSoftLongScoreHolder.class.getName();
        } else if ( SimpleScore.class.getName().equals( scoreTypeFqn ) ) {
            return SimpleScoreHolder.class.getName();
        } else if ( SimpleBigDecimalScore.class.getName().equals( scoreTypeFqn ) ) {
            return SimpleBigDecimalScoreHolder.class.getName();
        } else if ( SimpleDoubleScore.class.getName().equals( scoreTypeFqn ) ) {
            return SimpleDoubleScoreHolder.class.getName();
        } else if ( SimpleLongScore.class.getName().equals( scoreTypeFqn ) ) {
            return SimpleLongScoreHolder.class.getName();
        }
        return null;
    }
}
