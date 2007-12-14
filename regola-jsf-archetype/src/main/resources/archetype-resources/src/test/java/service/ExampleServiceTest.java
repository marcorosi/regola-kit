package service;

import java.io.Serializable;

import org.regola.service.BaseManagerTestCase;
import org.regola.service.GenericManager;

public class ExampleServiceTest extends BaseManagerTestCase
{
	//tipizzarlo con l'oggetto di modello e la relativa public key
	private GenericManager<Object,Serializable> myModelManager;
	
	
	public GenericManager<Object, Serializable> getMyModelManager() {
		return myModelManager;
	}

	public void setMyModelManager(
			GenericManager<Object,Serializable> myModelManager) {
		this.myModelManager = myModelManager;
	}
	
	public void testGet()
	{
		//myModelManager.get(..id..);
		assertTrue(true);
	}	
	
	public void testRemove()
	{
		//myModelManager.remove(..id..);
		assertTrue(true);		
	}

	public void testSave()
	{
		//myModelManager.save(..myModel..);
		assertTrue(true);		
	}

	public void testFind()
	{
		//myModelManager.find(..modelPattern..);
		assertTrue(true);		
	}

	public void testCountFind()
	{
		//myModelManager.count(..modelPattern..);
		assertTrue(true);		
	}

	public void testGetAll()
	{
		//myModelManager.getAll();
		assertTrue(true);		
	}	
	
    public void testUpdate()
    {
    	//myModelManager.update(...entity,pk...)
    	assertTrue(true);
    }

	
}
