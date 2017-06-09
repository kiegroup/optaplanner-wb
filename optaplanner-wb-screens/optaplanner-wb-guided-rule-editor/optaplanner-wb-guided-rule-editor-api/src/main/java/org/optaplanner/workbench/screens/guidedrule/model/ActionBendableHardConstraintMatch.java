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

package org.optaplanner.workbench.screens.guidedrule.model;

import org.jboss.errai.common.client.api.annotations.Portable;

@Portable
public class ActionBendableHardConstraintMatch extends AbstractActionBendableConstraintMatch {

    public ActionBendableHardConstraintMatch() {
    }

    public ActionBendableHardConstraintMatch(final int position,
                                             final String constraintMatch) {
        super(constraintMatch,
              position);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }

        ActionBendableHardConstraintMatch that = (ActionBendableHardConstraintMatch) o;

        return position == that.position;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = ~~result;
        result = 31 * result + position;
        result = ~~result;
        return result;
    }
}
