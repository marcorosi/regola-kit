package ${service_impl_package};

import org.regola.service.impl.GenericManagerImpl;

import ${model_class};
import ${id_class};
import ${service_interface_class};
import ${dao_interface_class};

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("${service_bean_name}")
public class ${service_impl_name} extends GenericManagerImpl<${model_name},${id_name}> implements ${service_interface_name}
{
	@Autowired
	public ${service_impl_name}(${dao_interface_name}  ${dao_bean_name}) {
		super(${dao_bean_name});
		
	}

}