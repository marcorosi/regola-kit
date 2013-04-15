package org.regola.dao;

import java.io.Serializable;
import java.util.List;

/**
 * 
 * @author nicola
 *
 * @param <T>
 * @param <ID>
 */
public interface GenericDao2<T, ID extends Serializable> extends GenericDao<T, ID> {

	public List<T> queryList( String jpql, Object ...params);
	public List<T> queryListPaged(String jpql, int first, int max, Object ...params) ;
	public T querySingle(String jpql, Object ...params);
	public int queryUpdate(String jpql, Object ...params);
	
	public void flush();
	public void clear();
	public T merge(T entity);
}
