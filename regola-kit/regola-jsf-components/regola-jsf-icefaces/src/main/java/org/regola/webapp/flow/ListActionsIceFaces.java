package org.regola.webapp.flow;

import java.io.Serializable;
import java.util.List;

import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;

import org.regola.model.ModelPattern;
import org.regola.service.GenericManager;
import org.regola.util.ELFunction;
import org.regola.validation.LazyLoadingArrayList;
import org.regola.validation.LazyLoadingArrayList.Fetcher;
import org.springframework.faces.model.SerializableListDataModel;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

import com.icesoft.faces.webapp.xmlhttp.PersistentFacesState;

public class ListActionsIceFaces implements Serializable {

	private static final long serialVersionUID = 1L;

	public void init() {
		//state = PersistentFacesState.getInstance();
	}
	
	public static DataModel refresh(final GenericManager serviceManager, ModelPattern modelPattern) {
		
		DataModel dataModel = new  DynamicReadDataModel(modelPattern );
		dataModel.setWrappedData(ListActions.refresh(serviceManager, modelPattern).getWrappedData());
		
		return dataModel;
		//stateBean.setColumnList( new SerializableListDataModel( stateBean.getModelPattern().getVisibleProperties()));
	}
		
}
