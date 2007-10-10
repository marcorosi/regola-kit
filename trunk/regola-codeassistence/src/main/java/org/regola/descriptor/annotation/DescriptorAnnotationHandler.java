package org.regola.descriptor.annotation;

/**
 * Adapetd from org.trails.descriptor.annotation.DescriptorAnnotationHandler
 */


import org.regola.descriptor.IDescriptor;

import java.lang.annotation.Annotation;


public interface DescriptorAnnotationHandler<T extends Annotation,X extends IDescriptor>
{
    public X decorateFromAnnotation(T annotation, X descriptor);
}
