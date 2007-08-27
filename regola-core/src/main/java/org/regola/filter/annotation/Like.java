package org.regola.filter.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.regola.filter.builder.FilterHandler;

@Retention(RetentionPolicy.RUNTIME)
@Target( { ElementType.FIELD, ElementType.METHOD })
@FilterHandler(LikeHandler.class)
public @interface Like {

	String value() default "";

	boolean caseSensitive() default false;

}
