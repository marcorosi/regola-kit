package ${dao_mock_package};

import org.regola.dao.hibernate.HibernateGenericDao;
import org.springframework.stereotype.Repository;

import org.regola.model.ModelPattern;
import ${model_class};
import ${dao_interface_class};

import java.util.ArrayList;
import java.util.List;

/**
 * Dao mock, generated to testing purposes.
 * 
 * @author nicola
 *
 */
@Repository("${dao_bean_name}")
public class ${dao_mock_name} implements ${dao_interface_name}
{
	public int count(ModelPattern pattern) {
		return 0;
	}

	public boolean exists(Long id) {
		return false;
	}

	public List<${model_name}> find(ModelPattern pattern) {
		return new ArrayList<${model_name}>();
	}

	public ${model_name} get(Long id) {
		return null;
	}

	public List<${model_name}> getAll() {
		return new ArrayList<${model_name}>();
	}

	public void remove(Long id) {
	}

	public void removeEntity(${model_name} entity) {
	}

	public ${model_name} save(${model_name} entity) {
		return entity;
	}
	
}