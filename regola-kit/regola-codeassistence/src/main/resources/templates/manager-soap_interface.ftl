package ${service_package};

import org.regola.service.GenericManager;

import ${model_class};
import ${id_class};
import javax.jws.WebService;

@WebService
public interface ${service_interface_name} extends GenericManager<${model_name},${id_name}>
{
	${model_name} get${model_name}(${id_name} id);
}