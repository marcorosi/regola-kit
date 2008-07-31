package org.regola.formsValidation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Locale;

import org.hibernate.validator.InvalidValue;
import org.junit.Before;
import org.junit.Test;
import org.regola.model.Customer;

/**
 * @author fabio
 */
public class LocaleBundleTest
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
	public void testBundleIT() 
	{
		//senza emendamenti
		validator = new AmendmentsClassValidator<Customer>(Customer.class, Locale.ITALY);
		customer.setFirstName("firstName");
		InvalidValue[] msgs = validator.getInvalidValues(customer);
		assertTrue(msgs != null);
		assertTrue(msgs.length == 1);
		assertEquals("non deve essere nullo o vuoto!", msgs[0].getMessage());
	}
	
	@Test
	public void testBundleEN() 
	{
		//senza emendamenti
		validator = new AmendmentsClassValidator<Customer>(Customer.class, Locale.ENGLISH);
		customer.setFirstName("firstName");
		InvalidValue[] msgs = validator.getInvalidValues(customer);
		assertTrue(msgs != null);
		assertTrue(msgs.length == 1);
		assertEquals("may not be null or empty!", msgs[0].getMessage());
	}	
	
	@Test
	public void testBundleITWithAmendments()
	{
		//con emendamenti
		validator = new AmendmentsClassValidator<Customer>(Customer.class, "validationAmendments_add&remove.xml", Locale.ITALY);
		InvalidValue[] msgs = validator.getInvalidValues(customer);
		assertTrue(msgs != null);
		assertTrue(msgs.length == 1);
		assertEquals("non deve essere nullo o vuoto!", msgs[0].getMessage());
	}
	
	@Test
	public void testBundleENWithAmendments()
	{
		//con emendamenti
		validator = new AmendmentsClassValidator<Customer>(Customer.class, "validationAmendments_add&remove.xml", Locale.ENGLISH);
		InvalidValue[] msgs = validator.getInvalidValues(customer);
		assertTrue(msgs != null);
		assertTrue(msgs.length == 1);
		assertEquals("may not be null or empty!", msgs[0].getMessage());
	}	
}
