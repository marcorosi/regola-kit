package org.regola.roo.addon.regola.web.flow;

import java.util.logging.Logger;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.springframework.roo.addon.web.mvc.controller.ControllerCommands;
import org.springframework.roo.addon.web.mvc.controller.ControllerOperations;
import org.springframework.roo.classpath.TypeLocationService;
import org.springframework.roo.metadata.MetadataService;
import org.springframework.roo.model.JavaPackage;
import org.springframework.roo.project.ProjectOperations;
import org.springframework.roo.shell.CliAvailabilityIndicator;
import org.springframework.roo.shell.CliCommand;
import org.springframework.roo.shell.CliOption;
import org.springframework.roo.shell.CommandMarker;
import org.springframework.roo.support.logging.HandlerUtils;

@Component
@Service
public class WebFlowCommands implements CommandMarker {
    @Reference private RegolaWebFlowOperations webFlowOperations;

    private static Logger LOGGER = HandlerUtils.getLogger(WebFlowCommands.class);

    @Reference private ControllerOperations controllerOperations;
    @Reference private MetadataService metadataService;
    @Reference private ProjectOperations projectOperations;
    @Reference private TypeLocationService typeLocationService;

    
    @CliCommand(value = "regola web flow", help = "Install Spring Web Flow configuration artifacts into your project")
    public void installWebFlow(
            @CliOption(key = { "flowName", "" }, mandatory = true, help = "The name for your web flow") final String flowName,
            @CliOption(key = "package", mandatory = true, optionContext = "update", help = "The package in which new action class will be placed") final JavaPackage javaPackage) {

    	if (!javaPackage.getFullyQualifiedPackageName().startsWith(
                projectOperations.getTopLevelPackage(
                        projectOperations.getFocusedModuleName())
                        .getFullyQualifiedPackageName())) {
            LOGGER.warning("Your action class was created outside of the project's top level package and is therefore not included in the preconfigured component scanning. Please adjust your component scanning manually in webmvc-config.xml");
        }
    	
        webFlowOperations.installWebFlow(flowName, javaPackage);
    }

    @CliAvailabilityIndicator("regola web flow")
    public boolean isInstallWebFlowAvailable() {
        return webFlowOperations.isWebFlowInstallationPossible();
    }
}
