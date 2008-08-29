package org.regola.webapp.flow;

import java.io.Serializable;
import java.util.List;


import org.regola.model.ModelPattern;
import org.regola.service.GenericManager;
import org.regola.validation.LazyLoadingArrayList;
import org.regola.validation.LazyLoadingArrayList.Fetcher;
import org.springframework.faces.model.SerializableListDataModel;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

public class ListActions<T, ID extends Serializable, F extends ModelPattern>  extends BaseActions {

	GenericManager<T, ID> serviceManager;
	
	public GenericManager<T, ID> getServiceManager() {
		return serviceManager;
	}

	public void setServiceManager(GenericManager<T, ID> serviceManager) {
		this.serviceManager = serviceManager;
	}
	
	public void init() {
	}
	
	public void refresh(StateBean stateBean) {
		
		F modelPattern = (F) stateBean.getModelPattern();
		modelPattern.setTotalItems(serviceManager.countFind(modelPattern));

		List<T> modelList = new LazyLoadingArrayList<T, F>(new Fetcher<T, F>()
		{
			public List<T> fetch(F filter)
			{
				return serviceManager.find(filter);
			}
		}, modelPattern);
		
		stateBean.setModelList(new SerializableListDataModel(modelList));
		
	}
	
	
	
}
