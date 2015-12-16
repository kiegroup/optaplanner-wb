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
package org.optaplanner.workbench.screens.solver.model;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamImplicit;
import org.jboss.errai.common.client.api.annotations.Portable;

@Portable
public class TerminationConfigModel {

    private String terminationClass = null;

    private TerminationCompositionStyleModel terminationCompositionStyle = null;

    private Long millisecondsSpentLimit = null;
    private Long secondsSpentLimit = null;
    private Long minutesSpentLimit = null;
    private Long hoursSpentLimit = null;

    private Long unimprovedMillisecondsSpentLimit = null;
    private Long unimprovedSecondsSpentLimit = null;
    private Long unimprovedMinutesSpentLimit = null;
    private Long unimprovedHoursSpentLimit = null;

    private String bestScoreLimit = null;
    private Boolean bestScoreFeasible = null;

    private Integer stepCountLimit = null;
    private Integer unimprovedStepCountLimit = null;

    @XStreamImplicit(itemFieldName = "termination")
    private List<TerminationConfigModel> terminationConfigList = null;
    private Long daysSpentLimit;
    private Long unimprovedDaysSpentLimit;

    public String getTerminationClass() {
        return terminationClass;
    }

    public void setTerminationClass( String terminationClass ) {
        this.terminationClass = terminationClass;
    }

    public TerminationCompositionStyleModel getTerminationCompositionStyle() {
        return terminationCompositionStyle;
    }

    public void setTerminationCompositionStyle( TerminationCompositionStyleModel terminationCompositionStyle ) {
        this.terminationCompositionStyle = terminationCompositionStyle;
    }

    public Long getMillisecondsSpentLimit() {
        return millisecondsSpentLimit;
    }

    public void setMillisecondsSpentLimit( Long millisecondsSpentLimit ) {
        this.millisecondsSpentLimit = millisecondsSpentLimit;
    }

    public Long getSecondsSpentLimit() {
        return secondsSpentLimit;
    }

    public void setSecondsSpentLimit( Long secondsSpentLimit ) {
        this.secondsSpentLimit = secondsSpentLimit;
    }

    public Long getMinutesSpentLimit() {
        return minutesSpentLimit;
    }

    public void setMinutesSpentLimit( Long minutesSpentLimit ) {
        this.minutesSpentLimit = minutesSpentLimit;
    }

    public Long getHoursSpentLimit() {
        return hoursSpentLimit;
    }

    public void setHoursSpentLimit( Long hoursSpentLimit ) {
        this.hoursSpentLimit = hoursSpentLimit;
    }

    public Long getUnimprovedMillisecondsSpentLimit() {
        return unimprovedMillisecondsSpentLimit;
    }

    public void setUnimprovedMillisecondsSpentLimit( Long unimprovedMillisecondsSpentLimit ) {
        this.unimprovedMillisecondsSpentLimit = unimprovedMillisecondsSpentLimit;
    }

    public Long getUnimprovedSecondsSpentLimit() {
        return unimprovedSecondsSpentLimit;
    }

    public void setUnimprovedSecondsSpentLimit( Long unimprovedSecondsSpentLimit ) {
        this.unimprovedSecondsSpentLimit = unimprovedSecondsSpentLimit;
    }

    public Long getUnimprovedMinutesSpentLimit() {
        return unimprovedMinutesSpentLimit;
    }

    public void setUnimprovedMinutesSpentLimit( Long unimprovedMinutesSpentLimit ) {
        this.unimprovedMinutesSpentLimit = unimprovedMinutesSpentLimit;
    }

    public Long getUnimprovedHoursSpentLimit() {
        return unimprovedHoursSpentLimit;
    }

    public void setUnimprovedHoursSpentLimit( Long unimprovedHoursSpentLimit ) {
        this.unimprovedHoursSpentLimit = unimprovedHoursSpentLimit;
    }

    public String getBestScoreLimit() {
        return bestScoreLimit;
    }

    public void setBestScoreLimit( String bestScoreLimit ) {
        this.bestScoreLimit = bestScoreLimit;
    }

    public Boolean getBestScoreFeasible() {
        return bestScoreFeasible;
    }

    public void setBestScoreFeasible( Boolean bestScoreFeasible ) {
        this.bestScoreFeasible = bestScoreFeasible;
    }

    public Integer getStepCountLimit() {
        return stepCountLimit;
    }

    public void setStepCountLimit( Integer stepCountLimit ) {
        this.stepCountLimit = stepCountLimit;
    }

    public Integer getUnimprovedStepCountLimit() {
        return unimprovedStepCountLimit;
    }

    public void setUnimprovedStepCountLimit( Integer unimprovedStepCountLimit ) {
        this.unimprovedStepCountLimit = unimprovedStepCountLimit;
    }

    public List<TerminationConfigModel> getTerminationConfigList() {
        return terminationConfigList;
    }

    public void setTerminationConfigList( List<TerminationConfigModel> terminationConfigList ) {
        this.terminationConfigList = terminationConfigList;
    }

    public void setDaysSpentLimit( Long daysSpentLimit ) {
        this.daysSpentLimit = daysSpentLimit;
    }

    public Long getDaysSpentLimit() {
        return daysSpentLimit;
    }

    public void setUnimprovedDaysSpentLimit( Long unimprovedDaysSpentLimit ) {
        this.unimprovedDaysSpentLimit = unimprovedDaysSpentLimit;
    }

    public Long getUnimprovedDaysSpentLimit() {
        return unimprovedDaysSpentLimit;
    }
}
