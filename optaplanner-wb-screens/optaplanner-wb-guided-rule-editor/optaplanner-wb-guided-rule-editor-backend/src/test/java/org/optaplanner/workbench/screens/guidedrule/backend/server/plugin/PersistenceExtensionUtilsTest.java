package org.optaplanner.workbench.screens.guidedrule.backend.server.plugin;

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
        assertEquals(constantToUnwrapOne, PersistenceExtensionUtils.unwrapParenthesis(constantToUnwrapOne));
        assertEquals(constantToUnwrapTwo, PersistenceExtensionUtils.unwrapCurlyBrackets(constantToUnwrapTwo));
    }

    @Test
    public void testUnwrapMissingStart() throws Exception {
        final String constantToUnwrapOne = "abc)";
        final String constantToUnwrapTwo = "abc}";
        assertEquals(constantToUnwrapOne, PersistenceExtensionUtils.unwrapParenthesis(constantToUnwrapOne));
        assertEquals(constantToUnwrapTwo, PersistenceExtensionUtils.unwrapCurlyBrackets(constantToUnwrapTwo));
    }

    @Test(expected = StringIndexOutOfBoundsException.class)
    public void testUnwrapMissingWrongOrder() throws Exception {
        final String constant = ")abc(";
        PersistenceExtensionUtils.unwrapParenthesis(constant);
    }

    @Test(expected = StringIndexOutOfBoundsException.class)
    public void testUnwrapMissingWrongOrderCurly() throws Exception {
        final String constant = "}abc{";
        PersistenceExtensionUtils.unwrapCurlyBrackets(constant);
    }
}
