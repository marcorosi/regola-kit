package org.regola.util;
import junit.framework.TestCase;

import org.regola.model.Order;
import org.regola.model.pattern.CustomerPattern;
import org.regola.model.pattern.ItemPattern;


public class ClonatorTest extends TestCase 
{

	public void testCustomerPattern()
	{
		
		CustomerPattern cp = new CustomerPattern();
		cp.setFirstName("firstName");
		cp.addSortedProperty("firstName", Order.asc);
		
		CustomerPattern cp1 = Clonator.clone(cp);
		assertEquals("firstName", cp1.getFirstName());
		assertEquals(cp.getAllProperties().size(), cp1.getAllProperties().size());
		assertEquals(cp.getVisibleProperties().size(), cp1.getVisibleProperties().size());
		assertEquals(cp.getSortedProperties().size(), cp1.getSortedProperties().size());
			
	}
	
	public void testItemPattern()
	{
		
		ItemPattern ip = new ItemPattern();
		ip.setQuantity(200);
		ip.addSortedProperty("cost", Order.asc);
		
		ItemPattern ip1 = Clonator.clone(ip);
		assertEquals(new Integer(200), ip1.getQuantity());
		assertEquals(ip.getAllProperties().size(), ip1.getAllProperties().size());
		assertEquals(ip.getVisibleProperties().size(), ip1.getVisibleProperties().size());
		assertEquals(ip.getSortedProperties().size(), ip1.getSortedProperties().size());
				
	}
	
}
