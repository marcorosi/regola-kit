package org.regola.webapp.action;

import org.regola.model.Customer;

import java.io.Serializable;
import java.lang.Integer;
import org.regola.model.pattern.CustomerPattern;
import org.regola.webapp.action.FormPage;
import org.regola.webapp.action.plug.FormPagePlug;
import org.regola.webapp.action.plug.FormPagePlugProxy;
import org.regola.webapp.annotation.ScopeEnd;
import org.apache.commons.lang.StringUtils;

public class CustomerForm //implements Serializable,FormPagePlug<Customer, Integer, CustomerPattern>
{
	
	private static final String VALIDATION_AMENDMENTS_CFG_FILE = "validationAmendments.xml";

	public void init() {
		
		formPage.setPlug(new FormPagePlugProxy(this));
		formPage.init();
		
		if(StringUtils.isNotEmpty(formPage.getEncodedId()))
		{
			// update an existing model item
			Integer id = new Integer(formPage.getEncodedId());
			formPage.setTypedID(id);
			formPage.setModel(formPage.getServiceManager().get(id));
		}
		else
		{
			// edit a new model item
			formPage.setModel (new Customer());
			formPage.getModel().setId(null);
		}

		
		formPage.setValidationContext(VALIDATION_AMENDMENTS_CFG_FILE);
		
	}
	
	@ScopeEnd
	public String save()
	{	
		String navigation = formPage.save();
		formPage.getEventBroker().publish("customer.persistence.changes", null);
		
		return navigation;
	}
	
	@ScopeEnd
	public String cancel() {
		return formPage.cancel();
	}
	
	FormPage<Customer, Integer, CustomerPattern> formPage;

	public void setFormPage(
			FormPage<Customer, Integer, CustomerPattern> formPage) {
		this.formPage=formPage;
		
	}

	public FormPage<Customer, Integer, CustomerPattern> getFormPage() {
		return formPage;
	}
	
}
