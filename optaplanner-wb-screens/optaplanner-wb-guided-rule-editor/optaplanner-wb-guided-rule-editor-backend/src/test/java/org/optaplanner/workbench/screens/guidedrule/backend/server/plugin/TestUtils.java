package org.optaplanner.workbench.screens.guidedrule.backend.server.plugin;

import org.drools.workbench.models.commons.backend.rule.RuleModelIActionPersistenceExtension;
import org.drools.workbench.models.commons.backend.rule.exception.RuleModelDRLPersistenceException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class TestUtils {

    private RuleModelIActionPersistenceExtension extension;

    public TestUtils(RuleModelIActionPersistenceExtension extension) {
        this.extension = extension;
    }

    public void assertRuleModelDRLPersistenceExceptionWasThrown(final String actionText) {
        assertThatThrownBy(() -> extension.unmarshal(actionText))
                .isInstanceOf(RuleModelDRLPersistenceException.class)
                .hasMessageContaining(PersistenceExtensionUtils.EXCEPTION_MESSAGE_BASE)
                .hasMessageEndingWith(actionText);
    }
}
