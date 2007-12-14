/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.regola.webapp.action.webgalileo;

import org.regola.webapp.action.component.BasePageComponent;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author nicola
 */
public abstract class BasePageWebGalileo implements BasePageComponent {

    protected final Log log = LogFactory.getLog(getClass());
   

   
    abstract public void init();

    public void shakeWindow() {
        
    }
}
