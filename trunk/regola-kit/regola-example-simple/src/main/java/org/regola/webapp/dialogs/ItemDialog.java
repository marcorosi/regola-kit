package org.regola.webapp.dialogs;

import javax.faces.event.ActionEvent;

import org.regola.model.Item;
import org.regola.model.ItemId;
import org.regola.model.pattern.ItemPattern;
import org.regola.webapp.action.icefaces.FormPageIceFaces;
import org.regola.webapp.jsf.Dialog;
import org.regola.webapp.jsf.FormDialog;

public class ItemDialog extends Dialog {
	
	private Item model;
	
	public void openEdit(Item model) 
	{	
		this.model = model;
	}
	
	public void closePopupAfterValidation(ActionEvent e)
	{
		//lo fa il DialogCallback se la validazione va a buon fine
		//setVisible(false);
		
		if (e.getComponent().getId().matches(".*OK.*") && getCallback() != null)
			getCallback().onConfirm();
		else if (e.getComponent().getId().matches(".*Cancel.*") && getCallback() != null)
			getCallback().onCancel();
	}

	public Item getModel() {
		return model;
	}

	public void setModel(Item model) {
		this.model = model;
	}
	
}
