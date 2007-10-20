package ${dao_impl_package};

import org.regola.dao.hibernate.HibernateGenericDao;

import ${model_class};
import ${id_class};
import ${dao_interface_class};

public class ${dao_impl_name} extends HibernateGenericDao<${model_name},${id_name}> implements ${dao_interface_name}
{
	public ${dao_impl_name}(Class<${model_name}> persistentClass) 
	{
		super(persistentClass);
	}
}