package org.regola.webapp.jsf;

import java.io.Serializable;

import javax.faces.event.ActionEvent;

import org.regola.model.ModelPattern;
import org.regola.webapp.action.FormPage;

public class FormDialog<T, ID extends Serializable, F extends ModelPattern> extends Dialog 
{
	private FormPage<T,ID,F> form;
	
	public FormDialog()
	{
	}

	public FormPage<T,ID,F> getForm() {
		return form;
	}

	public void setForm(FormPage<T,ID,F> form) {
		this.form = form;
	}
	
	public String onSaveEvent()
	{
		return form.save();
	}
	
	public void onCancelEvent()
	{
		form.cancel();
	}
	
	
	public void processInput(ActionEvent e)
	{
		if (e.getComponent().getId().matches(".*OK.*") && getCallback() != null)
		{
			String ret = onSaveEvent();
			if(ret.contains("ok") || ret.contains("save"))
				setVisible(false);
				
		}
		else if (e.getComponent().getId().matches(".*Cancel.*") && getCallback() != null)
				{
					//getCallback().onCancel();
					setVisible(false);
				}
	}
	
}
