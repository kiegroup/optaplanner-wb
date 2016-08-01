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
import org.kie.workbench.common.services.datamodeller.core.ObjectProperty;

@Portable
public class ObjectPropertyPathImpl implements ObjectPropertyPath {

    private List<ObjectProperty> objectPropertyList = new ArrayList<>();
    private boolean descending;

    public ObjectPropertyPathImpl() {
    }

    @Override
    public List<ObjectProperty> getObjectPropertyPath() {
        return objectPropertyList;
    }

    @Override
    public void appendObjectProperty( ObjectProperty objectProperty ) {
        objectPropertyList.add( objectProperty );
    }

    @Override
    public void setDescending( boolean descending ) {
        this.descending = descending;
    }

    @Override
    public boolean isDescending() {
        return descending;
    }
}
