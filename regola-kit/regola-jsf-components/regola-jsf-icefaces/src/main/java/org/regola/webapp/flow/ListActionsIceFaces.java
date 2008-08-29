package org.regola.webapp.flow;

import java.util.List;

import javax.faces.model.ListDataModel;

import org.regola.model.ModelPattern;
import org.regola.util.ELFunction;
import org.regola.validation.LazyLoadingArrayList;
import org.regola.validation.LazyLoadingArrayList.Fetcher;
import org.springframework.faces.model.SerializableListDataModel;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

import com.icesoft.faces.webapp.xmlhttp.PersistentFacesState;

public class ListActionsIceFaces extends BaseActionsIceFaces {

	public void init() {
		state = PersistentFacesState.getInstance();
	}
	
	public void refresh(IceFacesStateBean stateBean) {
		stateBean.setColumnList( new SerializableListDataModel( stateBean.getModelPattern().getVisibleProperties()));
	}
		
}
