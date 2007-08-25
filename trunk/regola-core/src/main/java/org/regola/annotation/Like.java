package org.regola.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target( { ElementType.FIELD, ElementType.METHOD })
@FilterHandler(LikeHandler.class)
public @interface Like {

	String value() default "";

	boolean caseSensitive() default false;

}
