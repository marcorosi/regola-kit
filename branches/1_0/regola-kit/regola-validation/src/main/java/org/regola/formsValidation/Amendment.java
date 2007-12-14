package org.regola.formsValidation;

import java.io.Serializable;

public class Amendment implements Serializable
{
	private static final long serialVersionUID = -1679251192375123381L;
	
	private String amendmentType;
	private String validationType;
	private String modelProperty;
	
	public Amendment()
	{
		
	}
	
	public Amendment(String amType, String validType, String modelProp)
	{
		amendmentType = amType;
		validationType = validType;
		modelProperty = modelProp;
	}
	
	public String getModelProperty()
	{
		return modelProperty;
	}
	public void setModelProperty(String modelProperty)
	{
		this.modelProperty = modelProperty;
	}
	public String getValidationType()
	{
		return validationType;
	}
	public void setValidationType(String validationType)
	{
		this.validationType = validationType;
	}
	public String getAmendmentType()
	{
		return amendmentType;
	}
	public void setAmendmentType(String emendamentType)
	{
		this.amendmentType = emendamentType;
	}
	
}
