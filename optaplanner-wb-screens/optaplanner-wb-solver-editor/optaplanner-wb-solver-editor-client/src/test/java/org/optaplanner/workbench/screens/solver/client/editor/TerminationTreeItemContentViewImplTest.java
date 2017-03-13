package org.optaplanner.workbench.screens.solver.client.editor;

import com.google.gwtmockito.GwtMockitoTestRunner;
import com.google.gwtmockito.WithClassesToStub;
import org.gwtbootstrap3.client.ui.AnchorListItem;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.DropDownMenu;
import org.gwtbootstrap3.client.ui.base.AbstractAnchorListItem;
import org.gwtbootstrap3.client.ui.html.Text;
import org.jboss.errai.ui.client.local.spi.TranslationService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import static org.mockito.Mockito.*;

@RunWith(GwtMockitoTestRunner.class)
@WithClassesToStub({ Text.class, AnchorListItem.class, AbstractAnchorListItem.class })
public class TerminationTreeItemContentViewImplTest {

    @Mock
    private DropDownMenu dropDownMenu;

    @Mock
    private Button removeTerminationButton;

    @Mock
    private TranslationService translationService;

    private TerminationTreeItemContentViewImpl view;

    @Before
    public void setUp() {
        view = new TerminationTreeItemContentViewImpl( removeTerminationButton,
                                                       dropDownMenu,
                                                       translationService );
    }

    @Test
    public void initDropDownList() {
        verify( dropDownMenu,
                atLeastOnce() ).add( any() );
    }
}
