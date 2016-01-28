package com.luvsoft.DAO;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.luvsoft.entities.AbstractEntity;

public abstract class EntityManagerDAO {
    private EntityManagerFactory emfactory;

    @PersistenceContext
    protected EntityManager entitymanager;
    public EntityManagerDAO(){
        emfactory = Persistence.createEntityManagerFactory( "STOCK_MANAGEMENT" );
        entitymanager = emfactory.createEntityManager( );
    }

    /**
     * Add new entity
     * @param object
     */
    public <T extends AbstractEntity> void addNew(T object){
        entitymanager.getTransaction( ).begin( );
        entitymanager.persist( object );
        entitymanager.getTransaction( ).commit( );
    }

    /**
     * Update entity
     *  - if entity exist, update it
     *  - if not, insert a new register
     *  But recommended use this function to update existing entity
     * @param object: object contains information
     */
    public <T extends AbstractEntity> void update(T entity){
        entitymanager.getTransaction( ).begin( );
        entitymanager.merge(entity);
        entitymanager.getTransaction( ).commit( );
    }

    /**
     * Remove entity by its id
     * @param id
     * @param object
     */
    public <T extends AbstractEntity> void remove(int id){
        entitymanager.getTransaction( ).begin( );
        T entity = findById(id);
        entitymanager.remove(entity);
        entitymanager.getTransaction( ).commit( );
    }

    @SuppressWarnings("unchecked")
    public <T extends AbstractEntity> List<T> findAll(){
        /*Query query = entitymanager.createQuery("Select id from " + getEntityName());
        List<Integer> idList = query.getResultList();
        List<T> entities = new ArrayList<T>();
        for( int id : idList ){
            T entity = findById(id);
            if( entity != null ){
                entities.add(entity);
            }
        }
        
        return entities;
        */
        Query query = entitymanager.createQuery("Select e from " + getEntityName() + " e");
        return query.getResultList();
    }

    /**
     * Find entity by its id
     * @param id
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T extends AbstractEntity> T findById(int id){
        Query query = entitymanager.createQuery("Select e from " + getEntityName() + " e WHERE id="+id);
        return (T)query.getSingleResult();
    }

    /**
     * We use JPQL (JPA query language) so we will create query to entity not table
     * @return
     */
    public abstract String getEntityName();
}
