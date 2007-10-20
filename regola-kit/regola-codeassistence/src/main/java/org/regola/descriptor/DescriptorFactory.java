package org.regola.descriptor;

/**
 * Adapetd from org.trails.descriptor.DescriptorFactory
 */


import java.util.List;

public interface DescriptorFactory
{
	@SuppressWarnings("unchecked")
	public IClassDescriptor buildClassDescriptor(Class type);
	
	@SuppressWarnings("unchecked")
	public void setMethodExcludes(List methodExcludes);
	
	@SuppressWarnings("unchecked")
	public void setPropertyExcludes(List propertyExcludes);
}
