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

package org.optaplanner.workbench.screens.domaineditor.model;

import java.util.ArrayList;
import java.util.List;

import org.jboss.errai.common.client.api.annotations.Portable;
import org.kie.workbench.common.services.datamodeller.core.Visibility;
import org.kie.workbench.common.services.datamodeller.core.impl.JavaClassImpl;

@Portable
public class ComparatorObjectImpl extends JavaClassImpl implements ComparatorObject {

    private List<ObjectPropertyPath> objectPropertyPathList = new ArrayList<>(  );
    private String type;

    public ComparatorObjectImpl() {
    }

    public ComparatorObjectImpl( String packageName, String name, Visibility visibility, boolean isAbstract, boolean isFinal ) {
        super( packageName, name, visibility, isAbstract, isFinal );
    }

    public ComparatorObjectImpl( String packageName, String className ) {
        super(packageName, className);
    }

    @Override
    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public List<ObjectPropertyPath> getObjectPropertyPathList() {
        return objectPropertyPathList;
    }

    @Override
    public void setObjectPropertyPathList(List<ObjectPropertyPath> objectPropertyPathList) {
        this.objectPropertyPathList = objectPropertyPathList;
    }

}
