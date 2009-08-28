package org.regola.webapp.action;

import org.regola.dao.ognl.OgnlGenericDao;
import org.regola.model.Customer;
import org.regola.model.Invoice;
import org.regola.model.Item;

import java.lang.Integer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import org.regola.model.pattern.CustomerPattern;
import org.regola.service.MemoryGenericManager;
import org.regola.service.impl.InvoiceManagerImpl;
import org.regola.webapp.action.FormPage;
import org.regola.webapp.action.plug.FormPagePlugProxy;
import org.regola.webapp.annotation.ScopeEnd;
import org.apache.commons.lang.StringUtils;
import org.regola.webapp.action.plug.FormPagePlugAnnotationProxy;
import org.regola.webapp.dialogs.InvoiceDialog;
import org.regola.webapp.jsf.Dialog.DialogCallback;

public class CustomerForm 
{
	private InvoiceList invoiceMemoryList;
	private InvoiceDialog invoiceDialog;
	
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

		MemoryGenericManager<Invoice, Integer> manager = (MemoryGenericManager<Invoice, Integer>)invoiceMemoryList.getListPage().getServiceManager();
		manager.setTarget(invoices);
		
		invoiceMemoryList.init();
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

	public InvoiceList getInvoiceMemoryList() {
		return invoiceMemoryList;
	}

	public void setInvoiceMemoryList(InvoiceList invoiceMemoryList) {
		this.invoiceMemoryList = invoiceMemoryList;
	}

	public InvoiceDialog getInvoiceDialog() {
		return invoiceDialog;
	}

	public void setInvoiceDialog(InvoiceDialog invoiceDialog) {
		this.invoiceDialog = invoiceDialog;
	}
	
	/*
	 * Metodi di gestione del dialog invoice
	 */
	public void doEditInvoiceDialog()
	{	
		getInvoiceDialog().openEdit(invoiceMemoryList.getListPage().getCurrentModelItem());
		
		getInvoiceDialog().show("Invoice", "", new DialogCallback()
		{
			public void onConfirm()
			{
				//occorre solo modificare l'oggetto nella collection
				//boolean unico = controllaUnicitaInterAteneo(getInterAteneoDlg().getForm().getModel());
				
				//if(getItemDialog().getForm().validate() /*&& unico*/)
				{
					getInvoiceDialog().setVisible(false);					
				}
				/*
				else
					if(!unico)
					{
						getInterAteneoDlg().getForm().setErrore("Ateneo già esistente!");
						setEffectPanel(new Shake());
					}
					*/
			}

			public void onCancel()
			{
				getInvoiceDialog().setVisible(false);
			}
		});		
	}
	
	public void doNewInvoiceDialog()
	{
		getInvoiceDialog().openEdit(new Invoice());
		
		getInvoiceDialog().show("Nuovo Invoice", "", new DialogCallback()
		{

			public void onConfirm()
			{
				//getInterAteneoDlg().onSaveEvent();
				//boolean unico = controllaUnicitaInterAteneo(getInterAteneoDlg().getForm().getModel());
				
				//validazione
				//if(getItemDialog().getForm().validate() /*&& unico*/)
				{
					getInvoiceDialog().setVisible(false);
					
					//riprendo il model del dialog e lo aggiungo alla Collection dell'ori corrente				
					Invoice invoice = (Invoice)getInvoiceDialog().getModel();
					invoice.setCustomer(getFormPage().getModel());
					getInvoiceMemoryList().getListPage().getServiceManager().save(invoice);
					getInvoiceMemoryList().getListPage().refresh();
				} /*else
					if(!unico)
					{
						getInterAteneoDlg().getForm().setErrore("Ateneo già esistente!");
						setEffectPanel(new Shake());
					}*/
				
			}

			public void onCancel()
			{
				//getItemDialog().onCancelEvent();
				getInvoiceDialog().setVisible(false);
			}
		});	
	}	
	
}
