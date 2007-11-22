package org.regola.webapp.action.plug;

import org.regola.util.DynamicProxy;
import org.regola.webapp.action.FormPage;

@SuppressWarnings("unchecked")
public class FormPagePlugProxy extends DynamicProxy implements FormPagePlug {

	public FormPagePlugProxy(Object target) {
		super(target);
	
	}

	public void setFormPage(FormPage formPage) {
		Class[] argsType = { FormPage.class};
		invoke("setFormPage", argsType, formPage );

	}
	
	public void init() {
		Class[] argsType = { };
		invoke("init", argsType, (Object[]) null);

	}

}
