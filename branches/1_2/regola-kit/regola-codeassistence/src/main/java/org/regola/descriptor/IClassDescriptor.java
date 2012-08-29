/*
 * Created on Mar 18, 2005
 *
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
 * Adapetd from org.trails.descriptor.IClassDescriptor
 */


import java.util.List;

/**
 * @author fus8882
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
@SuppressWarnings("unchecked")
public interface IClassDescriptor extends IDescriptor
{
	@SuppressWarnings("unchecked")
    public Class getType();

    /**
     * @return Returns the methodDescriptors.
     */
	@SuppressWarnings("unchecked")
    public List getMethodDescriptors();

    /**
     * @param methodDescriptors
     *            The methodDescriptors to set.
     */
	@SuppressWarnings("unchecked")
    public void setMethodDescriptors(List methodDescriptors);

    /**
     * @return Returns the propertyDescriptors.
     */
    public List getPropertyDescriptors();

    /**
     * @param propertyDescriptors
     *            The propertyDescriptors to set.
     */
    @SuppressWarnings("unchecked")
    public void setPropertyDescriptors(List propertyDescriptors);

    public IPropertyDescriptor getIdentifierDescriptor();

    /**
     * @return
     */
    public String getDisplayName();
    
    public void setDisplayName(String displayName);

    /**
     * @return
     */
    public String getShortDescription();
    
    public void setShortDescription(String shortDescription);

    /**
     * @param string
     * @return
     */
    public IPropertyDescriptor getPropertyDescriptor(String name);

    /**
     * @return
     */
    public String getPluralDisplayName();

    /**
     * @return
     */
    public boolean isChild();

    /**
     * @param 
     */
    public void setChild(boolean child);

    @SuppressWarnings("unchecked")
    public List getPropertyDescriptors(String[] strings);
    
    public boolean isAllowSave();
    
    public void setAllowSave(boolean allowSave);
    
    public boolean isAllowRemove();
    
    public void setAllowRemove(boolean allowRemove);
  
}