package org.regola.webapp.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * da usare sui metodi dei managed bean per demarcarne 
 * la fine del ciclo di vita
 * 
 * @author marco
 *
 */
@Deprecated
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ScopeEnd {
}
