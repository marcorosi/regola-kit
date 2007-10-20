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
	@SuppressWarnings("unchecked")
	public ${mbean_form_name}()
	{
		idClass = (Class<${id_name}>) id.getClass();
		model = new ${model_name}();
		model.setId(id);
	}

	public void init()
	{
		super.init();

		if (id != null)
		{
			model = getServiceManager().get(id);
			id = model.getId();
			setOriginalId(id.toString());
		}
	
	}

	@Override
	public String save()
	{
		return super.save();
	}

	
}
