package org.regola.roo.addon.regola.activerecord;

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
public @interface RooRegolaActiveRecord {

	String COUNT_METHOD_DEFAULT = "count";
	String FIND_METHOD_DEFAULT  = "find";

	/**
	 * @return the name of the "count" method to generate 
	 */
	String countMethod() default COUNT_METHOD_DEFAULT;

	/**
	 * @return the name of the "find" (by identifier) method to generate
	 */
	String findMethod() default FIND_METHOD_DEFAULT;

}
