/*
 * Copyright 2004 Chris Nelson
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */
package org.regola.descriptor;

/**
 * Adapetd from org.trails.descriptor.CollectionDescriptor
 */

/**
 * @author fus8882
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class CollectionDescriptor extends TrailsPropertyDescriptor
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 8196323287698887600L;

	@SuppressWarnings("unchecked")
	private Class elementType;
    
    private boolean childRelationship;

    @SuppressWarnings("unchecked")
    public CollectionDescriptor(Class beanType, IPropertyDescriptor descriptor)
    {
        super(beanType, descriptor);
    }
    
    /**
     * @param realDescriptor
     */
    @SuppressWarnings("unchecked")
    public CollectionDescriptor(Class beanType, Class type)
    {
        super(beanType, type);
        // TODO Auto-generated constructor stub
    }

    @SuppressWarnings("unchecked")
    public CollectionDescriptor(Class beanType, String name, Class type)
    {
        this(beanType, type);
        this.setName(name);
    }
    
    /* (non-Javadoc)
     * @see org.trails.descriptor.PropertyDescriptor#isCollection()
     */
    @Override
	public boolean isCollection()
    {
        return true;
    }

    /**
     * @return Returns the elementType.
     */
    @SuppressWarnings("unchecked")
    public Class getElementType()
    {
        return elementType;
    }

    /**
     * @param elementType The elementType to set.
     */
    @SuppressWarnings("unchecked")
    public void setElementType(Class elementType)
    {
        this.elementType = elementType;
    }
    /**
     * @return Returns the childRelationship.
     */
    public boolean isChildRelationship()
    {
        return childRelationship;
    }
    /**
     * @param childRelationship The childRelationship to set.
     */
    public void setChildRelationship(boolean childRelationship)
    {
        this.childRelationship = childRelationship;
        if (this.childRelationship)
        {
            setSearchable(false);
        }
    }

    @Override
	public Object clone()
    {
        return new CollectionDescriptor(getBeanType(), this);
    }
    
    
}