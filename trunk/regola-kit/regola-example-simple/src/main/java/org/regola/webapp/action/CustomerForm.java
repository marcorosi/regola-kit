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
		}
		else
		{
			// edit a new model item
			model = new Customer();
			model.setId(id);
		}
		
	}

	@Override
	public String save()
	{
		String navigation = super.save();
		getEventBroker().publish("customer.persistence.changes", null);
		
		return navigation;
	}
	
	@Override
	@ScopeEnd
	public String cancel() {
		return super.cancel();
	}
}
