package org.regola.formsValidation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.URL;
import java.util.List;

import org.hibernate.validator.AssertFalse;
import org.hibernate.validator.AssertTrue;
import org.junit.Before;
import org.junit.Test;
import org.regola.model.Customer;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class ReadAmendmentsTest
{

	@Test
	public void testreadDSL() 
	{
		XStream xstream = new XStream(new DomDriver()); // does not require XPP3 library
		xstream.alias("AmendedModelClass", AmendedModelClass.class);
		xstream.alias("Amendment", Amendment.class);
				
		URL url = getClass().getClassLoader().getResource("validationAmendments.xml");
		
		try{
			List<AmendedModelClass> emendedClasses = (List<AmendedModelClass>)xstream.fromXML(new FileReader(url.getFile()));
			//assertTrue(emendedClasses.size() > 0);
		}catch(Exception e){
			e.printStackTrace();
			assertFalse(true);
		}	
	}
}
