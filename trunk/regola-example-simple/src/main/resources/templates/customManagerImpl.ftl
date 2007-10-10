package ${service_impl_package};

import it.kion.regola.dao.GenericDao;
import it.kion.regola.service.impl.GenericManagerImpl;

import ${model_class};
import ${id_class};
import ${service_interface_class};

public class ${service_impl_name} extends GenericManagerImpl<${model_name},${id_name}> implements ${service_interface_name}
{
	public ${service_impl_name}(GenericDao<${model_name},${id_name}> genericDao) {
		super(genericDao);
		// TODO Auto-generated constructor stub
	}

}