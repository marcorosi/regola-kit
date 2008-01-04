package org.regola.webapp.action;

import org.regola.dao.ognl.OgnlGenericDao;
import org.regola.model.Customer;
import org.regola.model.Invoice;

import java.lang.Integer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import org.regola.model.pattern.CustomerPattern;
import org.regola.service.impl.InvoiceManagerImpl;
import org.regola.webapp.action.FormPage;
import org.regola.webapp.action.plug.FormPagePlugProxy;
import org.regola.webapp.annotation.ScopeEnd;
import org.apache.commons.lang.StringUtils;
import org.regola.webapp.action.plug.FormPagePlugAnnotationProxy;

public class CustomerForm 
{
	
	private InvoiceList invoiceList;
	
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
		
		//========== customizzazione per gestione inner-list delle invoices =======
		Collection<Invoice> invoices = formPage.getModel().getInvoices();
		List<Invoice> invoicesList = new ArrayList<Invoice>(invoices);
		/* -- non va (proxy di spring) --
		OgnlGenericDao<Invoice, Integer> ognlInvoiceDao = (OgnlGenericDao<Invoice, Integer>)((InvoiceManagerImpl)invoiceList.getListPage().getServiceManager()).getGenericDao();
		ognlInvoiceDao.setTarget(invoices);
		*/
		try{
			Object invoiceManager = invoiceList.getListPage().getServiceManager();
			Object ognlInvoiceDao = invoiceManager.getClass().getMethod("getGenericDao").invoke(invoiceManager);
			ognlInvoiceDao.getClass().getMethod("setTarget", Collection.class).invoke(ognlInvoiceDao, invoicesList);
		}catch(Exception e)
		{
			e.printStackTrace();
			throw new RuntimeException();
		}
		
		invoiceList.init();
		//=========================================================================
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

	public InvoiceList getInvoiceList() {
		return invoiceList;
	}

	public void setInvoiceList(InvoiceList invoiceList) {
		this.invoiceList = invoiceList;
	}
	
}
