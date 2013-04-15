package org.regola.dao;




import java.util.List;


/**
 * 
 * @author nicola
 *
 */
public interface UniversalDao2 extends UniversalDao {
	
	public <T> List<T> queryList(Class<T> clazz, String jpql, Object ...params);
	public <T> List<T> queryListPaged(Class<T> clazz,String jpql, int first, int max, Object ...params) ;
	public <T> T querySingle(Class<T> clazz,String jpql, Object ...params);
	public int queryUpdate(String jpql, Object ...params);
	
	public void flush();
	public void clear();
	public <T> T merge(T entity);
    
}
