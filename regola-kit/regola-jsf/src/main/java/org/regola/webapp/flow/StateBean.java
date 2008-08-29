package org.regola.webapp.flow;

import java.io.Serializable;

import org.regola.model.ModelPattern;
import org.springframework.faces.model.SerializableListDataModel;;

public class StateBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	protected ModelPattern modelPattern;
	protected SerializableListDataModel modelList;
	protected Object model;
	
	public ModelPattern getModelPattern() {
		return modelPattern;
	}
	public void setModelPattern(ModelPattern modelPattern) {
		this.modelPattern = modelPattern;
	}
	public SerializableListDataModel getModelList() {
		return modelList;
	}
	public void setModelList(SerializableListDataModel modelList) {
		this.modelList = modelList;
	}
	public Object getModel() {
		return model;
	}
	public void setModel(Object model) {
		this.model = model;
	}
	
}
