package org.regola.webapp.flow;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.model.DataModel;


import org.regola.model.ModelPattern;
import org.regola.service.GenericManager;
import org.regola.validation.LazyLoadingArrayList;
import org.regola.validation.LazyLoadingArrayList.Fetcher;
import org.springframework.faces.model.SerializableListDataModel;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

public class ListActions   implements Serializable {
	
	@SuppressWarnings("unchecked")
	public static DataModel refresh(final GenericManager serviceManager, ModelPattern modelPattern) {
		
		System.out.println("Refresh....");
		
		modelPattern.setTotalItems(serviceManager.countFind(modelPattern));

//		List modelList = new LazyLoadingArrayList(new Fetcher()
//		{
//			public List fetch(ModelPattern filter)
//			{
//				return serviceManager.find(filter);
//			}
//		}, modelPattern);
		
		return new SerializableListDataModel(fetch(serviceManager, modelPattern));

	}
	
	@SuppressWarnings("unchecked")
	public static List fetch(final GenericManager serviceManager, ModelPattern modelPattern)
	{
		//List modelList = new ArrayList(modelPattern.getTotalItems());
		//for (int i=0; i<modelPattern.getTotalItems(); ++i) modelList.add(null);
		
		List modelList = new ArrayList(modelPattern.getPageSize());
		
		List newEntries = serviceManager.find(modelPattern);
		
		int pos=0,offset=modelPattern.getCurrentPage()*modelPattern.getPageSize();
		
		offset=0;
		
		for (Object newEntry: newEntries)
		{
			//modelList.set(offset+pos++,newEntry);
			modelList.add(offset+pos++,newEntry);
		}
		
		return modelList;
	}
	
	
	
}
