package org.regola.descriptor;

/**
 * Adapetd from org.trails.descriptor.TrailsDesciptorService
 */

import java.util.List;

/**
 * Servizio per ottenere i descrittori dei tipi tramite reflection.
 * 
 * Questa versione non implementa nessun meccanismo di cache ma è + 
 * comoda di TrailsDescriptorService perchè non serve la registrazione
 * dei tipi.
 *  
 * @author marco
 *
 */
@SuppressWarnings("unchecked")
public class RegolaDescriptorService implements DescriptorService
{        
    private DescriptorFactory descriptorFactory = new ReflectionDescriptorFactory();

    public IClassDescriptor getClassDescriptor(Class type)
    {
        if (type.getName().contains("CGLIB"))
        {
        	return getDescriptorFactory().buildClassDescriptor(type.getSuperclass());
        }
        else
        {
        	return getDescriptorFactory().buildClassDescriptor(type);
        }
    }

	public List getAllDescriptors()
    {
    	throw new RuntimeException("Operation not supported by this type of DescriptorService");
    }    

    public void setDecorators(List decorators)
    {
    	throw new RuntimeException("Operation not supported by this type of DescriptorService");
    }

	public DescriptorFactory getDescriptorFactory()
	{
		return descriptorFactory;
	}

	public void setDescriptorFactory(DescriptorFactory descriptorFactory)
	{
		this.descriptorFactory = descriptorFactory;
	}
}
