package org.regola.formsValidation;

import java.util.List;

import org.apache.commons.lang.StringUtils;

public class AmendedModelClass
{
	
	private String modelClass;
	
	private List<Amendment> amendments;
	
	
	public List<Amendment> getAmendments()
	{
		return amendments;
	}
	public void setAmendments(List<Amendment> amendments)
	{
		this.amendments = amendments;
	}
	public String getModelClass()
	{
		//pulizia da eventuali caratteri di controllo provenienti dall'xml
		return StringUtils.trimToEmpty(modelClass);
	}
	public void setModelClass(String modelClass)
	{
		this.modelClass = modelClass;
	}

}
