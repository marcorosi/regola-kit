package org.regola.roo.addon.regola.procedures;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * @author nicola
 *
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface RooRegolaProcedures {
	
	String catalog() ;
	String schema() ;
	String procedureName() default "";
	String functionName() default "";

}
