package org.regola.service;

/*
 * Adapted from org.appfuse.service.GenericManager
 */


import org.regola.model.ModelPattern;

import java.io.Serializable;
import java.util.List;
import org.regola.model.ModelPattern;

/**
 * Generic Manager that talks to GenericDao to CRUD POJOs.
 *
 * <p>Extend this interface if you want typesafe (no casting necessary) managers
 * for your domain objects.
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 */
public interface GenericManager<T, PK extends Serializable> {

    /**
     * Generic method used to get all objects of a particular type. This
     * is the same as lookup up all rows in a table.
     * @return List of populated objects
     */
    public List<T> getAll();

    /**
     * Generic method to get an object based on class and identifier. An
     * ObjectRetrievalFailureException Runtime Exception is thrown if
     * nothing is found.
     *
     * @param id the identifier (primary key) of the object to get
     * @return a populated object
     * @see org.springframework.orm.ObjectRetrievalFailureException
     */
    public T get(PK id);

    /**
     * Generic method to save an object - handles both update and insert.
     * @param object the object to save
     */
    public void save(T object);

    /**
     * Generic method to update an object. Call this when you need to update 
     * the primary key of an object.
     * 
     * @param object the object to save
     * @param id the original identifier of the object to update
     */
    public void update(T object, PK id);

    /**
     * Generic method to delete an object based on class and id
     * @param id the identifier (primary key) of the object to remove
     */
    public void remove(PK id);
    
    public List<T> find(ModelPattern filter);
    
    public int countFind( ModelPattern filter);
}
