package org.regola.descriptor;

import java.lang.reflect.Type;
import java.util.Arrays;

import ognl.OgnlException;

import org.junit.Before;
import org.junit.Test;
import org.regola.codeassistence.FullStack;

import static org.junit.Assert.*;

public class RegolaDescriptorServiceTest {

	RegolaDescriptorService ds;
	
	@Before
	public void init() throws OgnlException
	{
		ds = new RegolaDescriptorService();
		//ds.setDescriptorFactory(new ReflectionDescriptorFactory());
	}

	@Test
	public void getDescriptor()
	{
		assertNotNull(ds.getClassDescriptor(Integer.class));
		assertNotNull(ds.getClassDescriptor(String.class));
		assertNotNull(ds.getClassDescriptor(FullStack.class));
	}

}
