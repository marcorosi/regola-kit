package org.regola.dao.jpa;

import java.io.Serializable;

import org.regola.dao.UniversalDao;
import org.springframework.transaction.annotation.Transactional;

/**
 * Versione transazionale per {@link JpaUniversalDao}.
 * @author nicola
 *
 */
public class JpaTransactionalUniversalDao extends JpaUniversalDao implements UniversalDao {
	@Transactional(readOnly = false)
	public void remove(Class clazz, Serializable id) {
		super.remove(clazz, id);
	}

	@Transactional(readOnly = false)
	public void removeEntity(Object entity) {
		super.removeEntity(entity);
	}

	@Transactional(readOnly = false)
	public Object save(Object entity) {
		return super.save(entity);	
	}
}
