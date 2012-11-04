package org.regola.roo.addon.regola.pattern;

import org.springframework.roo.model.JavaType;

/**
 * Interface of operations this add-on offers. Typically used by a command type or an external add-on.
 *
 * @since 1.1
 */
public interface PatternOperations {

    /**
     * Indicate commands should be available
     * 
     * @return true if it should be available, otherwise false
     */
    boolean isCommandAvailable();

   
    /**
     * Annotate all Java types with the trigger of this add-on
     */
    void annotateAll();
    
	public void newModelPattern(final JavaType entity);
}