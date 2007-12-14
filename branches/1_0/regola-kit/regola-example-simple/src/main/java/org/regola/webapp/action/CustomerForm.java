package org.regola.webapp.action;

import org.regola.model.Customer;

import java.lang.Integer;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import org.regola.model.pattern.CustomerPattern;
import org.regola.webapp.action.FormPage;
import org.regola.webapp.action.plug.FormPagePlugProxy;
import org.regola.webapp.annotation.ScopeEnd;
import org.apache.commons.lang.StringUtils;
import org.regola.webapp.action.plug.FormPagePlugAnnotationProxy;

public class CustomerForm 
{
	
        @PostConstruct
	public void init() {
		
		formPage.setPlug(new FormPagePlugAnnotationProxy(this));
		
                formPage.setValidationContext("validationAmendments.xml");
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

	@Resource
        public void setFormPage(
			FormPage<Customer, Integer, CustomerPattern> formPage) {
		this.formPage=formPage;
		
	}

        
	public FormPage<Customer, Integer, CustomerPattern> getFormPage() {
		return formPage;
	}
	
}
