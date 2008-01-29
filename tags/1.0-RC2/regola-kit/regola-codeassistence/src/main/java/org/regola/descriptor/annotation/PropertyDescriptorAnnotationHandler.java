package org.regola.descriptor.annotation;

/**
 * Adapetd from org.trails.descriptor.annotation.PropertyDescriptorAnnotationHandler
 */


import org.regola.descriptor.IDescriptor;

/**
 * 
 * @author fus8882
 *
 * Applies the annotation to a given property.  
 * @see it.kion.regola.descriptor.annotation.AbstractAnnotationHandler.setDescriptorPropertiesFromAnnotation
 */
public class PropertyDescriptorAnnotationHandler extends AbstractAnnotationHandler implements DescriptorAnnotationHandler<PropertyDescriptor,IDescriptor>
{
    /**
     * 
     * @param propertyDescriptorAnno
     * @param descriptor
     * @return
     * @see AbstractAnnotationHandler.setDescriptorPropertiesFromAnnotation
     */
    public IDescriptor decorateFromAnnotation(PropertyDescriptor propertyDescriptorAnno, 
            IDescriptor descriptor)
    {
        setPropertiesFromAnnotation(propertyDescriptorAnno, descriptor);
        return descriptor;
    }

}
