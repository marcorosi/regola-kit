package org.regola.formsValidation;

import java.io.FileReader;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.validator.NotEmpty;
import org.hibernate.validator.NotNull;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class AmendmentUtils
{
	private static Log log = LogFactory.getLog( AmendmentsClassValidator.class );
	
	private static Dummy d;
	
	/*
	 * torna i tipi di annotation che è possibile aggiungere
	 * tramite il meccanismo degli emendamenti
	 * (per ora quelle annotazioni di hibernate che non necessitano di parametri nella initialize)
	 */
	public static Annotation[] getPermittedAddTypes()
	{
		if(d == null)
			d = new Dummy();
		Field[] fields = d.getClass().getDeclaredFields();
		Field f = fields[0];
		return f.getAnnotations();
	}
	
	private static class Dummy
	{
		@NotNull
		@NotEmpty
		String dummy;
	}
	
	public List<Amendment> readDSLAmendments(String validationAmendmentsCfgFile , Class amendedModelClass) {
		//caricamento dsl emendamenti
		
		if( StringUtils.isBlank(validationAmendmentsCfgFile) ) //StringUtils.isBlank è null-safe 
			return new ArrayList<Amendment>();
		
		XStream xstream = new XStream(new DomDriver()); // does not require XPP3 library
		xstream.alias("AmendedModelClass", AmendedModelClass.class);
		xstream.alias("Amendment", Amendment.class);
				
		URL url = getClass().getClassLoader().getResource(validationAmendmentsCfgFile);
		
		try {
			List<AmendedModelClass> emendedClasses = (List<AmendedModelClass>)xstream.fromXML(new FileReader(url.getFile()));
			return retrieveAmendments(emendedClasses , amendedModelClass);
		} catch (Exception e) {
			//throw new RuntimeException(e);
			log.error("Errore nella lettura del file di configurazione degli emendamenti: " + validationAmendmentsCfgFile);
			return new ArrayList<Amendment>();
		}
	}
	
	/*
	 * ritorna gli emendamenti riferiti alla classe corrente del bean
	 */
	protected List<Amendment> retrieveAmendments(List<AmendedModelClass> emendedModelClasses , Class amendedModelClass)
	{
		List<Amendment> emendaments = new ArrayList<Amendment>();
		for (AmendedModelClass emendedClass : emendedModelClasses)
		{
			if( amendedModelClass.getCanonicalName().equals(emendedClass.getModelClass()) )
				emendaments.addAll(emendedClass.getAmendments());
		}
		return emendaments;
	}
}
