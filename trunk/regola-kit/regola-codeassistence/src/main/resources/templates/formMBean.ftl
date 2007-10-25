package ${mbean_package};

import ${model_class};
import ${id_class};
import ${filter_class};

import org.regola.webapp.action.AutoCompleteBean;
import org.regola.webapp.action.FormPage;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ${mbean_form_name} extends FormPage<${model_name}, ${id_name}, ${filter_name}>
{
	public ${mbean_form_name}() {}
		
	@Override
	public void init()
	{
		super.init();
 
		if(StringUtils.isNotEmpty(getEncodedId()))
		{
			// update an existing model item
			id = new Integer(getEncodedId());
			model = getServiceManager().get(id);
		}
		else
		{
			// edit a new model item
			model = new ${model_name}();
			model.setId(id);
		}
		
	}


	@Override
	@ScopeEnd
	public String save()
	{
		String navigation = super.save();
		getEventBroker().publish("${field(model_name)}.persistence.changes", null);
		
		return navigation;
	}
	
	@Override
	@ScopeEnd
	public String cancel() {
		return super.cancel();
	}

	
}
