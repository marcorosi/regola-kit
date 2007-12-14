package org.regola.model;

public class PropertyNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 8026293734823030096L;

	public PropertyNotFoundException(String property, ModelPattern pattern)
	{
		super(errorMsg(property, pattern));
	}
	
	private static String errorMsg(String property, ModelPattern pattern)
	{
		StringBuilder sb = new StringBuilder();
		sb.append("Cannot find the property '").append(property)
			.append("' in ").append(pattern)
			.append(". Valid properties are: ");
		for(ModelProperty p : pattern.getAllProperties())
		{
			sb.append(p.getName()).append(" ");
		}
		return sb.toString();
	}
}
