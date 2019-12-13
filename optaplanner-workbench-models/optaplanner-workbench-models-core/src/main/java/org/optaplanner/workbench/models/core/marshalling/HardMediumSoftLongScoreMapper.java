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

package org.optaplanner.workbench.models.core.marshalling;

import org.jboss.errai.codegen.meta.impl.java.JavaReflectionMethod;
import org.jboss.errai.marshalling.rebind.api.CustomMapping;
import org.jboss.errai.marshalling.rebind.api.model.MappingDefinition;
import org.jboss.errai.marshalling.rebind.api.model.impl.ReadMapping;
import org.jboss.errai.marshalling.rebind.api.model.impl.SimpleFactoryMapping;
import org.optaplanner.core.api.score.buildin.hardmediumsoftlong.HardMediumSoftLongScore;

@CustomMapping(HardMediumSoftLongScore.class)
public class HardMediumSoftLongScoreMapper extends MappingDefinition {

    public HardMediumSoftLongScoreMapper() throws NoSuchMethodException {
        super(HardMediumSoftLongScore.class);

        SimpleFactoryMapping factoryMapping = new SimpleFactoryMapping();
        factoryMapping.setMethod(new JavaReflectionMethod(HardMediumSoftLongScore.class.getMethod("valueOfUninitialized",
                                                                                                  int.class,
                                                                                                  long.class,
                                                                                                  long.class,
                                                                                                  long.class)));
        factoryMapping.mapParmToIndex("initScore",
                                      0,
                                      int.class);
        factoryMapping.mapParmToIndex("hardScore",
                                      1,
                                      long.class);
        factoryMapping.mapParmToIndex("mediumScore",
                                      2,
                                      long.class);
        factoryMapping.mapParmToIndex("softScore",
                                      3,
                                      long.class);

        setInstantiationMapping(factoryMapping);

        addMemberMapping(new ReadMapping("initScore",
                                         int.class,
                                         "getInitScore"));
        addMemberMapping(new ReadMapping("hardScore",
                                         long.class,
                                         "getHardScore"));
        addMemberMapping(new ReadMapping("mediumScore",
                                         long.class,
                                         "getMediumScore"));
        addMemberMapping(new ReadMapping("softScore",
                                         long.class,
                                         "getSoftScore"));
    }
}
