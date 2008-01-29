package org.regola.formsValidation;

import java.lang.annotation.Annotation;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class AmendmentUtilsTest
{
	
	@Test
	public void testGetPermittedAddTypes()
	{
		Annotation[] a = AmendmentUtils.getPermittedAddTypes();
		assertTrue(a != null);
		assertTrue(a.length == 2);
		
		assertTrue(a[0].annotationType().getCanonicalName().equals("org.hibernate.validator.NotNull"));
		assertTrue(a[1].annotationType().getCanonicalName().equals("org.hibernate.validator.NotEmpty"));
	}
}
