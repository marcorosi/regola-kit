package org.regola.util;

import java.lang.reflect.Method;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

public class EnumConverter implements Converter
{

	private Class getTargetClass(final FacesContext context, final UIComponent component) {
		return component.getValueBinding("value").getType(context);
	}
	
	public Object getAsObject(FacesContext context, UIComponent component, String index)
			throws ConverterException
	{
		Class clazz = getTargetClass(context, component);
		
		Method method;
		try
		{
			method = clazz.getMethod("values",(Class[])null);
			Enum[] values = (Enum[]) method.invoke(null, (Object[]) null);

			for (Enum value : values)
			{
				if (value.ordinal() == Integer.valueOf(index) )
					return value;
			}
		
		} catch (Exception e)
		{
			throw new ConverterException(e);
		} 
		
		return null;
	}

	public String getAsString(FacesContext arg0, UIComponent arg1, Object object)
			throws ConverterException
	{
		if (object instanceof String)
		{
			return (String) object;
		}

		Enum type = (Enum) object;
		int index = type.ordinal();

		return "" + index;
	}

}
