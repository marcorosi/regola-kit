package org.regola.roo.addon.regola.activerecord;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.springframework.roo.model.JavaType;
import org.springframework.roo.shell.CliAvailabilityIndicator;
import org.springframework.roo.shell.CliCommand;
import org.springframework.roo.shell.CliOption;
import org.springframework.roo.shell.CommandMarker;

@Component // Use these Apache Felix annotations to register your commands class in the Roo container
@Service
public class ActiveRecordCommands implements CommandMarker {
	
	@Reference ActiveRecordOperations operations;
	
    @CliAvailabilityIndicator({ "regola jpa add", "regola jpa add all" })
    public boolean isCommandAvailable() {
        return operations.isCommandAvailable();
    }
	
	 /**
     * This method registers a command with the Roo shell. It also offers a mandatory command attribute.
     * 
     * @param type 
     */
    @CliCommand(value = "regola jpa add", help = "Add model pattern support to ActiveRecord persistence for a certain class")
    public void add(
    		@CliOption(key = "type", mandatory = true, help = "The java type to apply RegolaActiveRecord annotation to") JavaType target) {
        operations.annotateType(target);
    }
    
    /**
     * This method registers a command with the Roo shell. It has no command attribute.
     * 
     */
    @CliCommand(value = "regola jpa add all", help = "Add model pattern support to ActiveRecord persistence for all classes")
    public void all() {
        operations.annotateAll();
    }

}
