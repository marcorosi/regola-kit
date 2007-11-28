package ${mbean_package};

import ${model_class};
import ${id_class};
import ${filter_class};

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.regola.webapp.action.FormPage;
import org.regola.webapp.action.plug.FormPagePlugProxy;
import org.regola.webapp.annotation.ScopeEnd;
import org.apache.commons.lang.StringUtils;

public class ${mbean_form_name}
{
    @SuppressWarnings("unchecked")
	public void init()
	{
		formPage.setPlug(new FormPagePlugProxy(this));
		formPage.init();
                formPage.setValidationContext("${mbean_form_name}Amendments.xml");
		
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
			formPage.setModel (new ${model_name}();
			formPage.getModel().setId(null);
		}
	}

	@ScopeEnd
	public String save()
	{
		String navigation = formPage.save();
		formPage.getEventBroker().publish("${field(model_name)}.persistence.changes", null);
		
		return navigation;
	}
	
	@ScopeEnd
	public String cancel() {
		return formPage.cancel();
	}
	
	FormPage<${model_name}, ${id_name}, ${filter_name}> formPage;

	public void setFormPage(
			FormPage<${model_name}, ${id_name}, ${filter_name}> formPage) {
		this.formPage=formPage;
		
	}

	public FormPage<${model_name}, ${id_name}, ${filter_name}> getFormPage() {
		return formPage;
	}
	
}
