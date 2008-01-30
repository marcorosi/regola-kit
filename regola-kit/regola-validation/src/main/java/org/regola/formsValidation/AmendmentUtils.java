package org.regola.formsValidation;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.validator.NotEmpty;
import org.hibernate.validator.NotNull;
import org.hibernate.validator.Valid;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

public class AmendmentUtils
{
	private static Log log = LogFactory.getLog( AmendmentsClassValidator.class );
	
	private static Dummy d;
	
	private static final String ROOT_TAG = "model";
	private static final String ROOT_CLASS_ATTRIB = "class";
	private static final String CHILD_ADD_TAG = "add";
	private static final String CHILD_REMOVE_TAG = "remove";
	private static final String CHILD_TYPE_ATTRIB = "type";
	private static final String CHILD_PROPERTY_ATTRIB = "property";
	
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
		@Valid
		String dummy;
	}

	public List<Amendment> readDSLAmendments(String validationAmendmentsCfgFile , Class amendedModelClass) {
		//caricamento dsl emendamenti
		
		if( StringUtils.isBlank(validationAmendmentsCfgFile) ) //StringUtils.isBlank è null-safe 
			return new ArrayList<Amendment>();
		
		try{
			URL url = getClass().getClassLoader().getResource(validationAmendmentsCfgFile);
			
			SAXBuilder builder = new SAXBuilder();
			//Document doc = builder.build(new File(url.getFile()));
			Document doc = builder.build(new File(url.toURI().getPath())); //per evitare problemi di codifica dell'url

			Element rootElement = doc.getRootElement();
			
			if(!rootElement.getName().equals(ROOT_TAG))
			{
				log.error("Errore di sintassi nel file di configurazione " + validationAmendmentsCfgFile 
						+": il rootElement deve avere il tag " + ROOT_TAG);
			}
			
			String classAttrib = StringUtils.trimToEmpty(rootElement.getAttributeValue(ROOT_CLASS_ATTRIB));
			
			if(!amendedModelClass.getCanonicalName().equals(classAttrib))
			{
				log.error("Non è stato trovato nessun emendamento per la classe " 
						+ amendedModelClass.getCanonicalName() + " nel file " + validationAmendmentsCfgFile);
				return new ArrayList<Amendment>();
			}
			
			AmendedModelClass amendedModel = new AmendedModelClass();
			amendedModel.setModelClass(classAttrib);
			
			List children = doc.getRootElement().getChildren();
			Iterator iterator = children.iterator();
			while (iterator.hasNext()) {
				Amendment amendment = new Amendment();
				
				Element element = (Element) iterator.next();
				
				if(element.getName().equals(CHILD_ADD_TAG) || element.getName().equals(CHILD_REMOVE_TAG))
				{
					amendment.setAmendmentType(element.getName());
					
					if(element.getAttributeValue(CHILD_TYPE_ATTRIB) != null)
					{
						amendment.setValidationType(element.getAttributeValue(CHILD_TYPE_ATTRIB));
						if(element.getAttributeValue(CHILD_PROPERTY_ATTRIB) != null)
						{
							amendment.setModelProperty(element.getAttributeValue(CHILD_PROPERTY_ATTRIB));
							//riga well-formed
							amendedModel.getAmendments().add(amendment);
						}
						else
							log.error("Errore di sintassi nel file di configurazione " + validationAmendmentsCfgFile 
										+": non trovato l'attributo obbligatorio " + CHILD_PROPERTY_ATTRIB);
					}
					else
						log.error("Errore di sintassi nel file di configurazione " + validationAmendmentsCfgFile 
								+": non trovato l'attributo obbligatorio " + CHILD_TYPE_ATTRIB);
					
				}else{
					log.error("Errore di sintassi nel file di configurazione " + validationAmendmentsCfgFile 
						+": i child tag ammessi sono " + CHILD_ADD_TAG + " e " + CHILD_REMOVE_TAG + ". La riga verrà ignorata.");
				}
			}
			
			return amendedModel.getAmendments();
			
		}catch(Exception e)
		{
			//throw new RuntimeException(e);
			log.error("Errore nella lettura del file di configurazione degli emendamenti: " 
					+ validationAmendmentsCfgFile 
					+ "\n" + e.getClass() + " - " + e.getMessage());
			return new ArrayList<Amendment>();			
		}
		
	}	
	
	/*
	 * Vecchia implementazione tramite XStream
	 *
	public List<Amendment> readDSLAmendments(String validationAmendmentsCfgFile , Class amendedModelClass) {
		//caricamento dsl emendamenti
		
		if( StringUtils.isBlank(validationAmendmentsCfgFile) ) //StringUtils.isBlank è null-safe 
			return new ArrayList<Amendment>();
		
		XStream xstream = new XStream(new DomDriver()); // does not require XPP3 library
		xstream.alias("AmendedModelClass", AmendedModelClass.class);
		xstream.alias("Amendment", Amendment.class);
				
		URL url = getClass().getClassLoader().getResource(validationAmendmentsCfgFile);
		
		try {
			List<AmendedModelClass> emendedClasses = (List<AmendedModelClass>)xstream.fromXML(new FileReader(url.toURI().getPath()));
			return retrieveAmendments(emendedClasses , amendedModelClass);
		} catch (Exception e) {
			//throw new RuntimeException(e);
			log.error("Errore nella lettura del file di configurazione degli emendamenti: " + validationAmendmentsCfgFile);
			return new ArrayList<Amendment>();
		}
	}
	
	
	//ritorna gli emendamenti riferiti alla classe corrente del bean
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
	*/
}
