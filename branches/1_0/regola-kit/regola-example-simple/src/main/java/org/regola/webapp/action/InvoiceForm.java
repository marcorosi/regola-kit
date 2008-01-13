package org.regola.webapp.action;

import java.util.Collection;

import org.apache.commons.lang.StringUtils;
import org.regola.dao.ognl.OgnlGenericDao;
import org.regola.model.Invoice;
import org.regola.model.Item;
import org.regola.model.ItemId;
import org.regola.model.pattern.InvoicePattern;
import org.regola.service.MemoryGenericManager;
import org.regola.service.impl.ItemManagerImpl;
import org.regola.webapp.action.plug.FormPagePlugProxy;
import org.regola.webapp.annotation.ScopeEnd;
import org.regola.webapp.dialogs.ItemDialog;
import org.regola.webapp.jsf.Dialog.DialogCallback;
import org.springframework.aop.framework.Advised;

public class InvoiceForm
{
	
	private ItemList itemMemoryList;
	private ItemDialog itemDialog;
	
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
		MemoryGenericManager<Item, ItemId> manager = (MemoryGenericManager<Item, ItemId>)itemMemoryList.getListPage().getServiceManager();
		manager.setTarget(items);
		
		itemMemoryList.init();
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

	public ItemList getItemMemoryList() {
		return itemMemoryList;
	}

	public void setItemMemoryList(ItemList itemMemoryList) {
		this.itemMemoryList = itemMemoryList;
	}

	public ItemDialog getItemDialog() {
		return itemDialog;
	}

	public void setItemDialog(ItemDialog itemDialog) {
		this.itemDialog = itemDialog;
	}
	
	public void doEditItemDialog()
	{	
		getItemDialog().openEdit(itemMemoryList.getListPage().getCurrentModelItem());
		
		getItemDialog().show("Item", "", new DialogCallback()
		{
			public void onConfirm()
			{
				//occorre solo modificare l'oggetto nella collection
				//boolean unico = controllaUnicitaInterAteneo(getInterAteneoDlg().getForm().getModel());
				
				//if(getItemDialog().getForm().validate() /*&& unico*/)
				{
					getItemDialog().setVisible(false);					
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
				getItemDialog().setVisible(false);
			}
		});		
	}
	
	public void doNewItemDialog()
	{
		getItemDialog().openEdit(new Item());
		
		getItemDialog().show("Nuovo Item", "", new DialogCallback()
		{

			public void onConfirm()
			{
				//getInterAteneoDlg().onSaveEvent();
				//boolean unico = controllaUnicitaInterAteneo(getInterAteneoDlg().getForm().getModel());
				
				//validazione
				//if(getItemDialog().getForm().validate() /*&& unico*/)
				{
					getItemDialog().setVisible(false);
					
					//riprendo il model del dialog e lo aggiungo alla Collection dell'ori corrente				
					//getItemList().getListPage().getServiceManager().save((Item)getItemDialog().getForm().getModel() );
					getItemMemoryList().getListPage().getServiceManager().save((Item)getItemDialog().getModel() );
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
				getItemDialog().setVisible(false);
			}
		});	
	}	
	
}
