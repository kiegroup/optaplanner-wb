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

package org.optaplanner.workbench.screens.guidedrule.backend.server.plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javax.enterprise.context.ApplicationScoped;

import org.drools.core.util.StringUtils;
import org.drools.workbench.models.commons.backend.rule.RuleModelIActionPersistenceExtension;
import org.drools.workbench.models.datamodel.rule.FreeFormLine;
import org.drools.workbench.models.datamodel.rule.IAction;
import org.optaplanner.workbench.screens.guidedrule.model.ActionBendableHardConstraintMatch;
import org.optaplanner.workbench.screens.guidedrule.model.ActionBendableSoftConstraintMatch;
import org.optaplanner.workbench.screens.guidedrule.model.ActionHardConstraintMatch;
import org.optaplanner.workbench.screens.guidedrule.model.ActionMediumConstraintMatch;
import org.optaplanner.workbench.screens.guidedrule.model.ActionMultiConstraintBendableBigDecimalMatch;
import org.optaplanner.workbench.screens.guidedrule.model.ActionMultiConstraintBendableLongMatch;
import org.optaplanner.workbench.screens.guidedrule.model.ActionMultiConstraintBendableMatch;
import org.optaplanner.workbench.screens.guidedrule.model.ActionMultiConstraintHardMediumSoftMatch;
import org.optaplanner.workbench.screens.guidedrule.model.ActionMultiConstraintHardSoftMatch;
import org.optaplanner.workbench.screens.guidedrule.model.ActionSoftConstraintMatch;

@ApplicationScoped
public class MultiConstraintHardSoftMatchPersistenceExtension implements RuleModelIActionPersistenceExtension {

    private static final Pattern CONSTRAINT_MATCH_PATTERN = Pattern.compile( "scoreHolder\\.addMultiConstraintMatch\\(\\s*kcontext\\s*,.+\\);" );

    private static final Pattern ARRAY_PATTERN = Pattern.compile( "new\\s+\\b(int|long|BigDecimal|java\\.math\\.BigDecimal)\\b\\s*\\[\\s*\\]\\s*\\{.*\\}" );

    @Override
    public boolean accept( final IAction iAction ) {
        return iAction instanceof ActionMultiConstraintHardSoftMatch
                || iAction instanceof ActionMultiConstraintHardMediumSoftMatch
                || iAction instanceof ActionMultiConstraintBendableMatch
                || iAction instanceof ActionMultiConstraintBendableLongMatch
                || iAction instanceof ActionMultiConstraintBendableBigDecimalMatch;
    }

    @Override
    public String marshal( final IAction iAction ) {
        if ( iAction instanceof ActionMultiConstraintHardSoftMatch ) {
            ActionMultiConstraintHardSoftMatch actionConstraintMatch = (ActionMultiConstraintHardSoftMatch) iAction;
            return String.format( "scoreHolder.addMultiConstraintMatch(kcontext, %s, %s);",
                                  actionConstraintMatch.getActionHardConstraintMatch().getConstraintMatch(),
                                  actionConstraintMatch.getActionSoftConstraintMatch().getConstraintMatch() );
        } else if ( iAction instanceof ActionMultiConstraintHardMediumSoftMatch ) {
            ActionMultiConstraintHardMediumSoftMatch actionConstraintMatch = (ActionMultiConstraintHardMediumSoftMatch) iAction;
            return String.format( "scoreHolder.addMultiConstraintMatch(kcontext, %s, %s, %s);",
                                  actionConstraintMatch.getActionHardConstraintMatch().getConstraintMatch(),
                                  actionConstraintMatch.getActionMediumConstraintMatch().getConstraintMatch(),
                                  actionConstraintMatch.getActionSoftConstraintMatch().getConstraintMatch() );
        } else if ( iAction instanceof ActionMultiConstraintBendableMatch ) {
            ActionMultiConstraintBendableMatch actionConstraintMatch = (ActionMultiConstraintBendableMatch) iAction;
            return String.format( "scoreHolder.addMultiConstraintMatch(kcontext, new int[] {%s}, new int[] {%s});",
                                  actionConstraintMatch.getActionBendableHardConstraintMatches().stream().map( m -> m.getConstraintMatch() ).collect( Collectors.joining( ", " ) ),
                                  actionConstraintMatch.getActionBendableSoftConstraintMatches().stream().map( m -> m.getConstraintMatch() ).collect( Collectors.joining( ", " ) ) );
        } else if ( iAction instanceof ActionMultiConstraintBendableLongMatch ) {
            ActionMultiConstraintBendableLongMatch actionConstraintMatch = (ActionMultiConstraintBendableLongMatch) iAction;
            return String.format( "scoreHolder.addMultiConstraintMatch(kcontext, new long[] {%s}, new long[] {%s});",
                                  actionConstraintMatch.getActionBendableHardConstraintMatches().stream().map( m -> m.getConstraintMatch() ).collect( Collectors.joining( ", " ) ),
                                  actionConstraintMatch.getActionBendableSoftConstraintMatches().stream().map( m -> m.getConstraintMatch() ).collect( Collectors.joining( ", " ) ) );
        } else if ( iAction instanceof ActionMultiConstraintBendableBigDecimalMatch ) {
            ActionMultiConstraintBendableBigDecimalMatch actionConstraintMatch = (ActionMultiConstraintBendableBigDecimalMatch) iAction;
            return String.format( "scoreHolder.addMultiConstraintMatch(kcontext, new java.math.BigDecimal[] {%s}, new java.math.BigDecimal[] {%s});",
                                  actionConstraintMatch.getActionBendableHardConstraintMatches().stream().map( m -> m.getConstraintMatch() ).collect( Collectors.joining( ", " ) ),
                                  actionConstraintMatch.getActionBendableSoftConstraintMatches().stream().map( m -> m.getConstraintMatch() ).collect( Collectors.joining( ", " ) ) );
        }
        throw new IllegalArgumentException( "Action " + iAction + " is not supported by this extension" );
    }

    @Override
    public boolean accept( final String iActionString ) {
        return CONSTRAINT_MATCH_PATTERN.matcher( iActionString ).matches();
    }

    @Override
    public IAction unmarshal( final String iActionString ) {
        List<String> parameters = StringUtils.splitArgumentsList( PersistenceExtensionUtils.unwrapParenthesis( iActionString ) );

        if ( !parameters.isEmpty() && "kcontext".equals( parameters.get( 0 ) ) ) {
            if ( parameters.size() == 3 ) {
                boolean hardConstraintIsArray = false;
                String hardConstraintType = null;
                String hardConstraint = parameters.get( 1 );
                Matcher hardConstraintMatcher = ARRAY_PATTERN.matcher( hardConstraint );
                if ( hardConstraintMatcher.matches() ) {
                    hardConstraintIsArray = true;
                    hardConstraintType = hardConstraintMatcher.group( 1 );
                }

                boolean softConstraintIsArray = false;
                String softConstraintType = null;
                String softConstraint = parameters.get( 2 );
                Matcher softConstraintMatcher = ARRAY_PATTERN.matcher( softConstraint );
                if ( softConstraintMatcher.matches() ) {
                    softConstraintIsArray = true;
                    softConstraintType = softConstraintMatcher.group( 1 );
                }

                if ( hardConstraintIsArray && softConstraintIsArray && hardConstraintType.equals( softConstraintType ) ) {
                    List<String> hardConstraints = StringUtils.splitArgumentsList( unwrapCurlyBrackets( hardConstraint ) );
                    List<ActionBendableHardConstraintMatch> bendableHardConstraintMatches = new ArrayList<>( hardConstraints.size() );
                    for ( int i = 0; i < hardConstraints.size(); i++ ) {
                        bendableHardConstraintMatches.add( new ActionBendableHardConstraintMatch( i,
                                                                                                  PersistenceExtensionUtils.extractConstraintMatchValue( hardConstraints.get( i ) ) ) );
                    }

                    List<String> softConstraints = StringUtils.splitArgumentsList( unwrapCurlyBrackets( softConstraint ) );
                    List<ActionBendableSoftConstraintMatch> bendableSoftConstraintMatches = new ArrayList<>( softConstraints.size() );
                    for ( int i = 0; i < softConstraints.size(); i++ ) {
                        bendableSoftConstraintMatches.add( new ActionBendableSoftConstraintMatch( i,
                                                                                                  PersistenceExtensionUtils.extractConstraintMatchValue( softConstraints.get( i ) ) ) );
                    }

                    switch ( hardConstraintType ) {
                        case "int":
                            return new ActionMultiConstraintBendableMatch( bendableHardConstraintMatches,
                                                                           bendableSoftConstraintMatches );
                        case "long":
                            return new ActionMultiConstraintBendableLongMatch( bendableHardConstraintMatches,
                                                                               bendableSoftConstraintMatches );
                        case "BigDecimal":
                        case "java.math.BigDecimal":
                            return new ActionMultiConstraintBendableBigDecimalMatch( bendableHardConstraintMatches,
                                                                                     bendableSoftConstraintMatches );
                    }
                } else if ( !hardConstraintIsArray && !softConstraintIsArray ) {
                    ActionHardConstraintMatch hardConstraintMatch = new ActionHardConstraintMatch( PersistenceExtensionUtils.extractConstraintMatchValue( parameters.get( 1 ) ) );
                    ActionSoftConstraintMatch softConstraintMatch = new ActionSoftConstraintMatch( PersistenceExtensionUtils.extractConstraintMatchValue( parameters.get( 2 ) ) );
                    return new ActionMultiConstraintHardSoftMatch( hardConstraintMatch,
                                                                   softConstraintMatch );
                }
            }
            if ( parameters.size() == 4 ) {
                ActionHardConstraintMatch hardConstraintMatch = new ActionHardConstraintMatch( PersistenceExtensionUtils.extractConstraintMatchValue( parameters.get( 1 ) ) );
                ActionMediumConstraintMatch mediumConstraintMatch = new ActionMediumConstraintMatch( PersistenceExtensionUtils.extractConstraintMatchValue( parameters.get( 2 ) ) );
                ActionSoftConstraintMatch softConstraintMatch = new ActionSoftConstraintMatch( PersistenceExtensionUtils.extractConstraintMatchValue( parameters.get( 3 ) ) );

                return new ActionMultiConstraintHardMediumSoftMatch( hardConstraintMatch,
                                                                     mediumConstraintMatch,
                                                                     softConstraintMatch );
            }
        }

        // Line can't be parsed as ActionMultiConstraint*Match, return a FreeFormLine
        FreeFormLine freeFormLine = new FreeFormLine();
        freeFormLine.setText( iActionString );

        return freeFormLine;
    }

    private String unwrapCurlyBrackets( final String s ) {
        int start = s.indexOf( '{' );
        int end = s.lastIndexOf( '}' );
        if ( start < 0 || end < 0 ) {
            return s;
        }
        return s.substring( start + 1,
                            end ).trim();
    }
}
