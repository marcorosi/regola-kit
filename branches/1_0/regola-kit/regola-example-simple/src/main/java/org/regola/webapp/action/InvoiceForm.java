package org.regola.webapp.action;

import org.regola.model.Invoice;
import org.regola.model.Item;

import java.lang.Integer;
import org.regola.model.pattern.InvoicePattern;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.regola.webapp.action.FormPage;
import org.regola.webapp.action.plug.FormPagePlugProxy;
import org.regola.webapp.annotation.ScopeEnd;
import org.apache.commons.lang.StringUtils;

public class InvoiceForm
{
	
	private ItemList itemList;
	
    @SuppressWarnings("unchecked")
	public void init()
	{
		formPage.setPlug(new FormPagePlugProxy(this));
		formPage.init();
        formPage.setValidationContext("InvoiceFormAmendments.xml");
		
		if(StringUtils.isNotEmpty(formPage.getEncodedId()))
		{
			// update an existing model item
			Integer id = Integer.valueOf(formPage.getEncodedId());
			formPage.initUpdate(id);
		}
		else
		{
			// edit a new model item
			formPage.initInsert(new Invoice());
		}
		
		//========== customizzazione per gestione inner-list degli items =======
		Collection<Item> items = formPage.getModel().getItems();
		/* -- non va (proxy di spring) --
		OgnlGenericDao<Invoice, Integer> ognlInvoiceDao = (OgnlGenericDao<Invoice, Integer>)((InvoiceManagerImpl)invoiceList.getListPage().getServiceManager()).getGenericDao();
		ognlInvoiceDao.setTarget(invoices);
		*/
		try{
			Object itemManager = itemList.getListPage().getServiceManager();
			Object ognlItemDao = itemManager.getClass().getMethod("getGenericDao").invoke(itemManager);
			ognlItemDao.getClass().getMethod("setTarget", Collection.class).invoke(ognlItemDao, items);
		}catch(Exception e)
		{
			e.printStackTrace();
			throw new RuntimeException();
		}
		
		itemList.init();
		//=========================================================================		
	}

	@ScopeEnd
	public String save()
	{
		String navigation = formPage.save();
		formPage.getEventBroker().publish("invoice.persistence.changes", null);
		
		return navigation;
	}
	
	@ScopeEnd
	public String cancel() {
		return formPage.cancel();
	}
	
	FormPage<Invoice, Integer, InvoicePattern> formPage;

	public void setFormPage(
			FormPage<Invoice, Integer, InvoicePattern> formPage) {
		this.formPage=formPage;
		
	}

	public FormPage<Invoice, Integer, InvoicePattern> getFormPage() {
		return formPage;
	}

	public ItemList getItemList() {
		return itemList;
	}

	public void setItemList(ItemList itemList) {
		this.itemList = itemList;
	}
	
}
