package org.optaplanner.workbench.screens.guidedrule.backend.server.plugin;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PersistenceExtensionUtilsTest {

    @Test
    public void testUnwrapWhenNothingToDo() throws Exception {
        final String constantToUnwrap = "abc";
        assertEquals(constantToUnwrap, PersistenceExtensionUtils.unwrapParenthesis(constantToUnwrap));
        assertEquals(constantToUnwrap, PersistenceExtensionUtils.unwrapCurlyBrackets(constantToUnwrap));
    }

    @Test
    public void testUnwrapMissingEnd() throws Exception {
        final String constantToUnwrapOne = "(abc";
        final String constantToUnwrapTwo = "{abc";
        Assertions.assertThatThrownBy(() -> PersistenceExtensionUtils.unwrapParenthesis(constantToUnwrapOne))
                .hasMessage("\"(abc\" had not characters '(':0 and ')':-1 in appropriate order.");
        Assertions.assertThatThrownBy(() -> PersistenceExtensionUtils.unwrapCurlyBrackets(constantToUnwrapTwo))
                .hasMessage("\"{abc\" had not characters '{':0 and '}':-1 in appropriate order.");
    }

    @Test
    public void testUnwrapMissingStart() throws Exception {
        final String constantToUnwrapOne = "abc)";
        final String constantToUnwrapTwo = "abc}";
        Assertions.assertThatThrownBy(() -> PersistenceExtensionUtils.unwrapParenthesis(constantToUnwrapOne))
                .hasMessage("\"abc)\" had not characters '(':-1 and ')':3 in appropriate order.");
        Assertions.assertThatThrownBy(() -> PersistenceExtensionUtils.unwrapCurlyBrackets(constantToUnwrapTwo))
                .hasMessage("\"abc}\" had not characters '{':-1 and '}':3 in appropriate order.");
    }

    @Test
    public void testUnwrapMissingWrongOrder() throws Exception {
        final String constant = ")abc(";
        Assertions.assertThatThrownBy(() -> PersistenceExtensionUtils.unwrapParenthesis(constant))
                .hasMessage("\")abc(\" had not characters '(':4 and ')':0 in appropriate order.");
    }

    @Test
    public void testUnwrapMissingWrongOrderCurly() throws Exception {
        final String constant = "}abc{";
        Assertions.assertThatThrownBy(() -> PersistenceExtensionUtils.unwrapCurlyBrackets(constant))
                .hasMessage("\"}abc{\" had not characters '{':4 and '}':0 in appropriate order.");
    }
}
