package org.optaplanner.workbench.screens.domaineditor.client;

import javax.annotation.PostConstruct;

import org.jboss.errai.ioc.client.api.EntryPoint;
import org.jboss.errai.ui.shared.api.annotations.Bundle;
import org.uberfire.client.views.pfly.sys.PatternFlyBootstrapper;

@EntryPoint
@Bundle("resources/i18n/DomainEditorConstants.properties")
public class DomainEditorEntryPoint {

    @PostConstruct
    public void startApp() {
        PatternFlyBootstrapper.ensureBootstrapSelectIsAvailable();
    }
}
