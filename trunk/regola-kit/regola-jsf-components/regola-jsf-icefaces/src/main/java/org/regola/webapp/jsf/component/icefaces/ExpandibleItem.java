package org.regola.webapp.jsf.component.icefaces;

import java.util.ArrayList;
import java.util.List;

public class ExpandibleItem {

	public boolean expanded = true;
	
	private Object target;
	
	public ExpandibleItem(Object target )
	{
		if (target==null) throw new IllegalArgumentException("Target must be not null!");
		
		this.target=target;

	}
	
	public boolean isExpanded() {
		return expanded;
	}

	public void setExpanded(boolean expanded) {
		this.expanded = expanded;
	}

	public Object getTarget() {
		return target;
	}

	public void setTarget(Object target) {
		this.target = target;
	}
	
	@SuppressWarnings("unchecked")
	public static List wrapList(List list)
	{
		List wrappedList = new ArrayList<ExpandibleItem>();
		
		//wrap list items
		for (Object item :  list)
		{
			wrappedList.add(new ExpandibleItem(item));
		}
		
		return wrappedList;
	}
	
}
