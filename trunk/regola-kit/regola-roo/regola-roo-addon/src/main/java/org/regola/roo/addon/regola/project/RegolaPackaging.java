package org.regola.roo.addon.regola.project;

import org.springframework.roo.project.packaging.AbstractPackagingProvider;

import static org.springframework.roo.project.Path.SPRING_CONFIG_ROOT;
import static org.springframework.roo.project.Path.SRC_MAIN_JAVA;
import static org.springframework.roo.project.Path.SRC_MAIN_WEBAPP;
import static org.springframework.roo.project.Path.SRC_TEST_JAVA;
import static org.springframework.roo.project.Path.SRC_TEST_RESOURCES;

import java.util.Arrays;
import java.util.Collection;

import org.apache.commons.lang3.StringUtils;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import org.springframework.roo.model.JavaPackage;
import org.springframework.roo.project.Path;
import org.springframework.roo.project.ProjectOperations;
import org.w3c.dom.Document;
 
@Component
@Service
public class RegolaPackaging extends AbstractPackagingProvider {

	public RegolaPackaging() {
        super("regola-war","war", "regola-pom-template.xml");
    }

    @Override
    protected void createOtherArtifacts(final JavaPackage topLevelPackage,
            final String module, final ProjectOperations projectOperations) {
        super.createOtherArtifacts(topLevelPackage, module, projectOperations);
        if (StringUtils.isBlank(module)) {
            // This is a single-module web project
            final String fullyQualifiedModuleName = getFullyQualifiedModuleName(
                    module, projectOperations);
            applicationContextOperations.createMiddleTierApplicationContext(
                    topLevelPackage, fullyQualifiedModuleName);
        }
    }

    public Collection<Path> getPaths() {
        return Arrays.asList(SRC_MAIN_JAVA, SRC_TEST_JAVA, SRC_TEST_RESOURCES,
                SPRING_CONFIG_ROOT, SRC_MAIN_WEBAPP);
    }
    
    public boolean isDefault() {
        return false;
    }

    @Override
    protected final void setPackagingProviderId(final Document pom) {
        // Not needed, as the core providers use the Maven packaging name as
        // their IDs.
    }
}
