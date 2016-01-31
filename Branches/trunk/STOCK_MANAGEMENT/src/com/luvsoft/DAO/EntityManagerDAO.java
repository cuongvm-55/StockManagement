package com.luvsoft.DAO;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.luvsoft.entities.AbstractEntity;

public class EntityManagerDAO {
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
    public <T extends AbstractEntity> void remove(String entityName, int id){
        entitymanager.getTransaction( ).begin( );
        T entity = findById(entityName, id);
        entitymanager.remove(entity);
        entitymanager.getTransaction( ).commit( );
    }

    /**
     * Find all entities
     * We use JPQL (JPA query language) so we will create query to entity not table
     * @param entityName
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T extends AbstractEntity> List<T> findAll(String entityName){
        Query query = entitymanager.createQuery("SELECT e FROM "+entityName+" e");
        return query.getResultList();
    }

    /**
     * Retrieve records of entity according to pagination
     * @param entityName
     * @param numberOfRecordPerPage
     * @param pageIndex
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T extends AbstractEntity> List<T> findAllWithPagination(String entityName, int pageIndex, int numberOfRecordPerPage){
        Query query = entitymanager.createQuery("SELECT e FROM "+entityName+" e");
        return query
                .setMaxResults(numberOfRecordPerPage)
                .setFirstResult(pageIndex * numberOfRecordPerPage)
                .getResultList();
    }

    /**
     * Find entity by its id
     * @param id
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T extends AbstractEntity> T findById(String entityName, int id){
        Query query = entitymanager.createQuery("SELECT e FROM " + entityName + " e WHERE id="+id);
        return (T)query.getSingleResult();
    }

    /**
     * Remove all entities
     */
    public void removeAll(String entityName){
        entitymanager.getTransaction( ).begin( );
        Query query = entitymanager.createQuery("DELETE FROM "+ entityName);
        query.executeUpdate();
        entitymanager.getTransaction( ).commit( );
    }

    /**
     * Count number of records
     * @param entityName
     * @return
     */
    public long countData(String entityName) {
        Query queryTotal = entitymanager.createQuery("SELECT count(e.id) FROM " + entityName + " e");
        long countResult = (long) queryTotal.getSingleResult();
        return countResult;
    }
}
