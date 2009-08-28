
package org.regola.descriptor;

/**
 * Adapetd from org.trails.descriptor.IMethodDescriptor
 */


@SuppressWarnings("unchecked")
public interface IMethodDescriptor extends IDescriptor {

    public abstract Class[] getArgumentTypes();

    public abstract void setArgumentTypes(Class[] argumentTypes);

    public abstract String getName();

    public abstract void setName(String name);

}