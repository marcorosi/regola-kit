package org.regola.webapp.action;

import org.regola.model.Customer;

import org.regola.webapp.annotation.ScopeEnd;

import java.lang.Integer;
import org.regola.model.pattern.CustomerPattern;
import org.regola.webapp.action.FormPage;
import org.apache.commons.lang.StringUtils;

public class CustomerForm extends FormPage<Customer, Integer, CustomerPattern>
{
	public CustomerForm()
	{
	}

	public void init()
	{
		super.init();
 
		if(StringUtils.isNotEmpty(getEncodedId()))
		{
			// update an existing model item
			id = new Integer(getEncodedId());
			model = getServiceManager().get(id);
			id = model.getId();
			setOriginalId(id.toString());
		}
		else
		{
			// edit a new model item
			id = new Integer(1);
			model = new Customer();
			model.setId(id);
		}
		
		idClass = (Class<Integer>) id.getClass();
	}

	@Override
	public String save()
	{
		return super.save();
	}
	
	@Override
	@ScopeEnd
	public String cancel() {
		return super.cancel();
	}

	
}
