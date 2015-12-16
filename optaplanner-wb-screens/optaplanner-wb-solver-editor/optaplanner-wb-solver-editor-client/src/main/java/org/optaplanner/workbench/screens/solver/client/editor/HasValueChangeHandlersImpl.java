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
package org.optaplanner.workbench.screens.solver.client.editor;

import java.util.HashSet;
import java.util.Set;

import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

public class HasValueChangeHandlersImpl<T>
        implements
        HasValueChangeHandlers<T> {

    private Set<ValueChangeHandler<T>> valueChangeHandlers = new HashSet<ValueChangeHandler<T>>();

    public HandlerRegistration addValueChangeHandler( final ValueChangeHandler<T> valueChangeHandler ) {

        // Just in case this is the first time ValueChangeEvent is used, we need to setup the TYPE.
        ValueChangeEvent.getType();

        this.valueChangeHandlers.add( valueChangeHandler );

        return new HandlerRegistration() {
            @Override
            public void removeHandler() {
                valueChangeHandlers.remove( valueChangeHandler );
            }
        };
    }

    @Override
    public void fireEvent( GwtEvent<?> event ) {
        for (ValueChangeHandler<T> valueChangeHandler : valueChangeHandlers) {
            valueChangeHandler.onValueChange( (ValueChangeEvent<T>) event );
        }
    }
}
