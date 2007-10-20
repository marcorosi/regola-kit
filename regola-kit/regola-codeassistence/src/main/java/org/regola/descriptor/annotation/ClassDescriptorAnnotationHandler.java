package org.regola.descriptor.annotation;

/**
 * Adapetd from org.trails.descriptor.annotation.ClassDescriptorAnnotationHandler
 */


import org.regola.descriptor.IClassDescriptor;

public class ClassDescriptorAnnotationHandler extends AbstractAnnotationHandler implements DescriptorAnnotationHandler<ClassDescriptor,IClassDescriptor>
{

    public ClassDescriptorAnnotationHandler()
    {
        super();
        // TODO Auto-generated constructor stub
    }

    public IClassDescriptor decorateFromAnnotation(ClassDescriptor annotation, IClassDescriptor descriptor)
    {
        setPropertiesFromAnnotation(annotation, descriptor);
        return descriptor;
    }


}
