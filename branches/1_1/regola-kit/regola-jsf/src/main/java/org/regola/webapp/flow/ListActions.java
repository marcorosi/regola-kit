package org.regola.webapp.flow;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.regola.dao.UniversalDao;
import org.regola.model.ModelPattern;
import org.regola.service.GenericManager;
import org.regola.validation.LazyLoadingArrayList;
import org.regola.validation.LazyLoadingArrayList.Fetcher;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

/**
 * Fornisce la azioni comuni ad una pagina di list, ad esempio recupera 
 * gli elementi paginandoli.
 * @author nicola
 *
 */
public class ListActions   implements Serializable {
	
	private static final long serialVersionUID = 1L;

//	@SuppressWarnings("unchecked")
//	public static DataModel refresh(final GenericManager serviceManager, ModelPattern modelPattern) {
//		
//		modelPattern.setTotalItems(serviceManager.countFind(modelPattern));
//		return new SerializableListDataModel(fetch(serviceManager, modelPattern));
//
//	}
//	
//	@SuppressWarnings("unchecked")
//	public static DataModel refresh(final UniversalDao dao, Serializable model, ModelPattern modelPattern) {
//		
//		modelPattern.setTotalItems(dao.count(model.getClass(),modelPattern));
//		return new SerializableListDataModel(fetch(dao,model, modelPattern));
//
//	}
	
	@SuppressWarnings("unchecked")
	public static List fetch(final GenericManager serviceManager, ModelPattern modelPattern)
	{
		List modelList = new ArrayList(modelPattern.getPageSize());
		List newEntries = serviceManager.find(modelPattern);
		int pos=0,offset=modelPattern.getCurrentPage()*modelPattern.getPageSize();
		
		offset=0;
		
		for (Object newEntry: newEntries)
		{
			modelList.add(offset+pos++,newEntry);
		}
		
		return modelList;
	}
	
	@SuppressWarnings("unchecked")
	public static List fetch(final UniversalDao dao, Serializable model, ModelPattern modelPattern)
	{
		List modelList = new ArrayList(modelPattern.getPageSize());
		List newEntries = dao.find(model.getClass(),modelPattern);
		int pos=0,offset=modelPattern.getCurrentPage()*modelPattern.getPageSize();
		
		offset=0;
		
		for (Object newEntry: newEntries)
		{
			modelList.add(offset+pos++,newEntry);
		}
		
		return modelList;
	}

	
	
	
}
