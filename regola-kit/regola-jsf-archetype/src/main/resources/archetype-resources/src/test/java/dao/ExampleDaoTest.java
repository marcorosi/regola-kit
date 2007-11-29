package dao;

import java.io.Serializable;

import org.regola.dao.GenericDao;
import org.regola.dao.hibernate.BaseDaoTestCase;
import org.regola.model.ModelPattern;

public class ExampleDaoTest extends BaseDaoTestCase
{
	//tipizzarlo con l'oggetto di modello ed il relativo id
	private GenericDao<Object , Serializable> myModelDao;
	

	public GenericDao<Object, Serializable> getMyModelDao() {
		return myModelDao;
	}

	public void setMyModelDao(
			GenericDao<Object, Serializable> myModelDao) {
		this.myModelDao = myModelDao;
	}
	
	public void testGet()
	{
		//myModelDao.get(..id..);
		assertTrue(true);
	}	
	
	public void testExists()
	{
		//myModelDAO.exists(..id..);
		assertTrue(true);
	}
	
	public void testRemove()
	{
		//myModelDao.remove(..id..);
		assertTrue(true);		
	}

	public void testRemoveEntity()
	{
		//myModelDao.removeEntity(..myModel..);
		assertTrue(true);		
	}

	public void testSave()
	{
		//myModelDao.save(..myModel..);
		assertTrue(true);		
	}

	public void testFind(ModelPattern pattern)
	{
		//myModelDao.find(..modelPattern..);
		assertTrue(true);		
	}

	public void testCount(ModelPattern pattern)
	{
		//myModelDao.count(..modelPattern..);
		assertTrue(true);		
	}

	public void testGetAll()
	{
		//myModelDao.getAll();
		assertTrue(true);		
	}

}
