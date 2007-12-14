package org.regola.util.wrapper;

import org.regola.webapp.action.FormPage;

public class FormPageWrapper
{
	private FormPage formPage;
	
	public FormPageWrapper()
	{
	}
	
	public FormPageWrapper(FormPage formPage)
	{
		this.formPage = formPage;
	}
	
	public FormPage getFormPage()
	{
		return formPage;
	}
	
	public void setFormPage(FormPage formPage)
	{
		this.formPage = formPage;
	}

}
