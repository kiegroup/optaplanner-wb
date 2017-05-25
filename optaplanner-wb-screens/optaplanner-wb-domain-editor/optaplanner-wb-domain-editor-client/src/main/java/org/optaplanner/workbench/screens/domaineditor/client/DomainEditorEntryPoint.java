package org.optaplanner.workbench.screens.domaineditor.client;

import org.jboss.errai.ioc.client.api.AfterInitialization;
import org.jboss.errai.ioc.client.api.EntryPoint;
import org.jboss.errai.ui.shared.api.annotations.Bundle;
import org.uberfire.client.views.pfly.sys.PatternFlyBootstrapper;

@EntryPoint
@Bundle("resources/i18n/DomainEditorConstants.properties")
public class DomainEditorEntryPoint {

    @AfterInitialization
    public void startApp() {
        PatternFlyBootstrapper.ensureBootstrapSelectIsAvailable();
    }
}
