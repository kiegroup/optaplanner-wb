package org.optaplanner.workbench.screens.domaineditor.backend.server;

import java.util.Comparator;
import javax.annotation.Generated;
import javax.enterprise.context.ApplicationScoped;

import org.jboss.forge.roaster.model.JavaClass;
import org.jboss.forge.roaster.model.JavaType;
import org.kie.workbench.common.services.datamodeller.driver.NestedClassFilter;
import org.optaplanner.workbench.screens.domaineditor.model.ComparatorDefinition;

@ApplicationScoped
public class PlannerNestedClassFilter implements NestedClassFilter {

    @Override
    public boolean accept( JavaType<?> javaType ) {
        return javaType.isClass() && ( javaType.getAnnotation( Generated.class.getName() ) != null )
                && ( javaType.getAnnotation( ComparatorDefinition.class.getName() ) != null )
                && ( (JavaClass) javaType ).getInterfaces().stream().anyMatch( s -> s.startsWith( Comparator.class.getName() ) );
    }
}
