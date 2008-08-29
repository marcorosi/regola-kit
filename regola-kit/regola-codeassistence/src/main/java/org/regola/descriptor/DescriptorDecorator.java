package org.regola.descriptor;

/**
 * Adapetd from org.trails.descriptor.DescriptorDecorator
 */


public interface DescriptorDecorator
{
    public IClassDescriptor decorate(IClassDescriptor descriptor);
}
