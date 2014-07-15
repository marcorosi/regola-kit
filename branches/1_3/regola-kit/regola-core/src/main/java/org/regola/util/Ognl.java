package org.regola.util;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ognl.OgnlException;

public class Ognl {
	protected static final Log log = LogFactory.getLog(Ognl.class);

	static public Object getValue(String exp, Object root) {
		try {
			return ognl.Ognl.getValue(exp, root);
		} catch (OgnlException oe) {
			/**
			 * Per evitare di fermarci con sotto-oggetti nulli di un grafo, ad
			 * esempio per gli errori -- source is null for getProperty(null,
			 * "id") -- non solleviamo l'eccezione ma la riportiamo solo nel log
			 */
			log.error(oe);

		}

		// valore restituito a seguito di un eccezione
		return "-";
	}

	static public void setValue(String exp, Object root, Object value) {
		try {
			ognl.Ognl.setValue(exp, root, value);
		} catch (OgnlException oe) {
			throw new RuntimeException(oe);
		}
	}
	
	/**
	 * cerca un oggetto per id all'interno di una collezione, di default si assume che 
	 * gli oggetti della collezione abbiano l'id nella property "id"
	 * 
	 * @param root l'oggetto radice
	 * @param collectionName il nome della collezione all'interno di root
	 * @param id l'id per la ricerca
	 * 
	 * @return l'oggetto se presente, null altrimenti 
	 */
	static public Object searchById(Object root, String collectionName, Object id) 
	{
        return searchById(root, collectionName, "id", id);
	}

	/**
	 * cerca un oggetto per id all'interno di una collezione
	 * 
	 * @param root l'oggetto radice
	 * @param collectionName il nome della collezione all'interno di root
	 * @param idName il nome della property che contiene l'id negli oggetti della collezione
	 * @param id l'id per la ricerca
	 * 
	 * @return l'oggetto se presente, null altrimenti
	 */
	static public Object searchById(Object root, String collectionName, String idName, Object id) 
	{
		try {
	        List<Object> result = (List<Object>) ognl.Ognl.getValue(collectionName+".{? #this."+idName+" == "+id+"}", root);
	        if(result==null||result.isEmpty())
	        	return null;
	        else
	        	return result.get(0);
		} catch (OgnlException oe) {
			throw new RuntimeException(oe);
		}        
	}	
}
