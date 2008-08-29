package org.regola.filter.criteria.ognl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.regola.filter.criteria.Order;
import static org.regola.util.Ognl.getValue;


@SuppressWarnings("unchecked")
public class DynamicComparator implements Comparator {
	
	List<Order> properties = new ArrayList<Order>();
	
	public int compare(Object o1, Object o2) {
		
		for (Order order: properties)
		{
			Comparable c1, c2;
			c1 = (Comparable) getValue(order.getPropertyName(), o1);
			c2 = (Comparable) getValue(order.getPropertyName(), o2);
			
			int ret = c1.compareTo(c2);
			
			if (!order.isAscending()) ret *=-1;
			
			if (ret!=0) return ret;
		}
		
		return 0;
	}

	public List<Order> getProperties() {
		return properties;
	}

}
