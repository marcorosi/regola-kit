package org.regola.formsValidation;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

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
		//pulizia da eventuali caratteri di controllo provenienti dall'xml
		return StringUtils.trimToEmpty(modelProperty);
	}
	public void setModelProperty(String modelProperty)
	{
		this.modelProperty = modelProperty;
	}
	public String getValidationType()
	{
		//pulizia da eventuali caratteri di controllo provenienti dall'xml
		return StringUtils.trimToEmpty(validationType);
			
	}
	public void setValidationType(String validationType)
	{
		this.validationType = validationType;
	}
	public String getAmendmentType()
	{
		//pulizia da eventuali caratteri di controllo provenienti dall'xml
		return StringUtils.trimToEmpty(amendmentType);
	}
	
	public void setAmendmentType(String emendamentType)
	{
		this.amendmentType = emendamentType;
	}
	
}
