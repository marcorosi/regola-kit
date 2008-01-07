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
		assertEquals(3, cp.getAllProperties().size());
		assertEquals(3, cp.getVisibleProperties().size());
		assertEquals(2, cp.getSortedProperties().size());
		
		CustomerPattern cp1 = Clonator.clone(cp);
		assertEquals("firstName", cp1.getFirstName());
		assertEquals(3, cp1.getAllProperties().size());
		assertEquals(3, cp1.getVisibleProperties().size());
		assertEquals(2, cp1.getSortedProperties().size());
			
	}
	
	public void testItemPattern()
	{
		
		ItemPattern ip = new ItemPattern();
		ip.setQuantity(200);
		ip.addSortedProperty("cost", Order.asc);
		assertEquals(3, ip.getAllProperties().size());
		assertEquals(3, ip.getVisibleProperties().size());
		assertEquals(1, ip.getSortedProperties().size());
		
		ItemPattern ip1 = Clonator.clone(ip);
		assertEquals(new Integer(200), ip1.getQuantity());
		assertEquals(3, ip1.getAllProperties().size());
		assertEquals(3, ip1.getVisibleProperties().size());
		assertEquals(1, ip1.getSortedProperties().size());
				
	}
	
}
