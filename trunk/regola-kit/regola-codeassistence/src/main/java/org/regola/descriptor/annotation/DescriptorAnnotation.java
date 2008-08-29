package org.regola.descriptor.annotation;

/**
 * Adapetd from org.trails.descriptor.annotation.DescriptorAnnotation
 */


import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@SuppressWarnings("unchecked")
public @interface DescriptorAnnotation
{
    Class<? extends DescriptorAnnotationHandler> value();
}
