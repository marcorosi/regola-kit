package org.regola.formsValidation;

import java.util.List;

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
		return modelClass;
	}
	public void setModelClass(String modelClass)
	{
		this.modelClass = modelClass;
	}

}
