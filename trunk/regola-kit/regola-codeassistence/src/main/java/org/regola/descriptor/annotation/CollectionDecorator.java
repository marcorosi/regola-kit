package org.regola.descriptor.annotation;

/**
 * Adapetd from org.trails.descriptor.annotation.CollectionDecorator
 */


import org.regola.descriptor.CollectionDescriptor;

public class CollectionDecorator implements DescriptorAnnotationHandler<Collection,CollectionDescriptor>
{

    public CollectionDecorator()
    {
        super();
        // TODO Auto-generated constructor stub
    }

    public CollectionDescriptor decorateFromAnnotation(Collection annotation, CollectionDescriptor descriptor)
    {
        descriptor.setChildRelationship(annotation.child());
        return descriptor;
    }



}
