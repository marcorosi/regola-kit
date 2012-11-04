package org.regola.roo.addon.regola.web.mvc.jsp;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.springframework.roo.addon.web.mvc.jsp.JspOperations;
import org.springframework.roo.shell.CliAvailabilityIndicator;
import org.springframework.roo.shell.CliCommand;
import org.springframework.roo.shell.CommandMarker;

@Component
@Service
public class JspCommands implements CommandMarker{
	
	@Reference private RegolaJspOperations regolaOperations;
	@Reference private JspOperations jspOperations;
	
    @CliCommand(value = "regola web mvc setup", help = "Setup a basic web project structure for a Regola MVC / JSP application")
    public void webMvcSetup() {
    	jspOperations.installCommonViewArtefacts();
    	
    	regolaOperations.installRegolaMvc();
    }
    
    @CliAvailabilityIndicator({ "regola web mvc setup" })
    public boolean isProjectAvailable() {
        return jspOperations.isMvcInstallationPossible();
    }
    
    @CliCommand(value = "regola web mvc xxx", help = "Setup a basic web project structure for a Regola MVC / JSP application")
    public void regolaMvcSetup() {
    	regolaOperations.installRegolaMvc();
    }
    

}
