package ${service_impl_package};

import ${model_class};
import ${id_class};
import ${service_interface_class};
import ${dao_interface_class};

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ${service_impl_name} implements ${service_interface_name}
{
	protected final Log log = LogFactory.getLog(getClass());
	
	private ${dao_interface_name} ${field(dao_interface_name)};

	public ${dao_interface_name} get${dao_interface_name}()
	{
		return ${field(dao_interface_name)};
	}

	public void set${dao_interface_name}(${dao_interface_name} ${field(dao_interface_name)})
	{
		this.${field(dao_interface_name)} = ${field(dao_interface_name)};
	}
}