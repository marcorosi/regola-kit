package org.regola.webapp.action.plug;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import org.regola.util.DynamicProxy;
import org.regola.webapp.action.FormPage;

@SuppressWarnings("unchecked")
public class FormPagePlugAnnotationProxy extends DynamicProxy implements FormPagePlug {

	public FormPagePlugAnnotationProxy(Object target) {
		super(target);
	
	}

	public void setFormPage(FormPage formPage) {
		Class[] argsType = { FormPage.class};
		invoke(Resource.class, argsType, formPage );

	}
	
	public void init() {
		Class[] argsType = { };
                invoke(PostConstruct.class,argsType,(Object[]) null);
        
	}

}
