package org.regola.util; 

import java.util.List;

import ognl.Ognl;
import ognl.OgnlException;
import freemarker.template.TemplateMethodModel;
import freemarker.template.TemplateModelException;

@SuppressWarnings("unchecked")
public class ValueReader implements TemplateMethodModel
{
	Object root;

	public ValueReader(Object root)
	{
		this.root = root;
	}

	public Object getRoot()
	{
		return root;
	}

	public void setRoot(Object root)
	{
		this.root = root;
	}

	public Object exec(List args) throws TemplateModelException
	{
		if (args.size() != 1)
		{
			throw new TemplateModelException("Troppi argomenti");
		}
		
		
		try
		{
		  Object result = Ognl.getValue((String) args.get(0), root);
		  
		  return result!=null ? result.toString() : "";
		} catch (OgnlException e)
		{
			new TemplateModelException(e);
		}
		
		return null;
	}
}
