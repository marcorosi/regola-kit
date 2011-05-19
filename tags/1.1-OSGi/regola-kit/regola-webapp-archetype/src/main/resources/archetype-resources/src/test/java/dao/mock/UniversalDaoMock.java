package ${package}.dao.mock;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.regola.dao.UniversalDao;
import org.regola.model.ModelPattern;
import org.springframework.stereotype.Repository;

/**
 * Questo mock... non fa nulla ;-)
 * 
 * @author nicola
 *
 */
@Repository("universalDao")
public class UniversalDaoMock implements UniversalDao {

	public int count(Class clazz, ModelPattern pattern) {
		return 0;
	}

	public boolean exists(Class clazz, Serializable id) {
		return false;
	}

	public List find(Class clazz, ModelPattern pattern) {
		return new ArrayList();
	}

	public Object get(Class clazz, Serializable id) {
		return null;
	}

	public List getAll(Class clazz) {
		return new ArrayList();
	}

	public void remove(Class clazz, Serializable id) {
	}

	public void removeEntity(Object entity) {
	}

	public Object save(Object entity) {
		return entity;
	}

}
