package org.regola.formsValidation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.hibernate.validator.InvalidValue;
import org.junit.Before;
import org.junit.Test;
import org.regola.model.Customer;

/**
 * @author fabio
 */
public class CustomerValidationTest //extends TestCase
{

	private Customer customer;
	private AmendmentsClassValidator<Customer> validator;

	@Before
	public void init() 
	{
		customer = new Customer();
		//validator = new AmendmentsClassValidator<Customer>(Customer.class);
	}

	@Test
	public void testEmendamentsValidator1() 
	{
		//senza emendamenti
		validator = new AmendmentsClassValidator<Customer>(Customer.class);
		customer.setFirstName("firstName");
		customer.setLastName("lastName");
		InvalidValue[] msgs = validator.getInvalidValues(customer);
		assertTrue(msgs != null);
		assertTrue(msgs.length == 0);
	}
	
	@Test
	public void testEmendamentsValidator2() 
	{
		//senza emendamenti
		validator = new AmendmentsClassValidator<Customer>(Customer.class);
		customer.setFirstName("firstName");
		InvalidValue[] msgs = validator.getInvalidValues(customer);
		assertTrue(msgs != null);
		assertTrue(msgs.length == 1);
	}
	
	@Test
	public void testEmendamentsValidator3() 
	{
		//con emendamenti (file dell'applicazione)
		validator = new AmendmentsClassValidator<Customer>(Customer.class, "validationAmendments.xml");
		InvalidValue[] msgs = validator.getInvalidValues(customer);
		//assertTrue(msgs != null);
		//assertTrue(msgs.length == 1);
	}
	
	@Test
	public void testEmendamentsValidator4() 
	{
		//senza emendamenti
		validator = new AmendmentsClassValidator<Customer>(Customer.class);
		InvalidValue[] msgs = validator.getInvalidValues(customer);
		assertTrue(msgs != null);
		assertTrue(msgs.length == 2);
	}
	
	@Test
	public void testEmendamentsValidator5() 
	{
		//con emendamenti che tolgono entrambe le validazioni
		validator = new AmendmentsClassValidator<Customer>(Customer.class, "validationAmendments_remove.xml");
		InvalidValue[] msgs = validator.getInvalidValues(customer);
		assertTrue(msgs != null);
		assertTrue(msgs.length == 0);
	}
	
	@Test
	public void testEmendamentsValidator6() 
	{
		validator = new AmendmentsClassValidator<Customer>(Customer.class, "validationAmendments_add&remove.xml");
		InvalidValue[] msgs = validator.getInvalidValues(customer);
		assertTrue(msgs != null);
		assertTrue(msgs.length == 1);
	}
	
	@Test
	public void testEmendamentsValidator7() 
	{
		//con file emendamenti vuoto
		validator = new AmendmentsClassValidator<Customer>(Customer.class, "validationAmendments_empty.xml");
		InvalidValue[] msgs = validator.getInvalidValues(customer);
		assertTrue(msgs != null);
		assertTrue(msgs.length == 2);
	}
	
	@Test
	public void testEmendamentsValidator8() 
	{
		//file inesistente
		validator = new AmendmentsClassValidator<Customer>(Customer.class, "xxxx.xml");
		InvalidValue[] msgs = validator.getInvalidValues(customer);
		assertTrue(msgs != null);
		assertTrue(msgs.length == 2);
	}
	
}
