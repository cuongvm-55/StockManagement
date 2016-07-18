package com.luvsoft.DAO;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.luvsoft.entities.AbstractEntity;

public class EntityManagerDAO {
    private static EntityManagerDAO instance;
    
    private EntityManagerFactory emfactory;

    @PersistenceContext
    protected EntityManager entitymanager;
    
    public static EntityManagerDAO getInstance(){
        if( instance == null ){
            instance = new EntityManagerDAO();
        }

        return instance;
    }
    private EntityManagerDAO(){
        emfactory = Persistence.createEntityManagerFactory( "STOCK_MANAGEMENT" );
        entitymanager = emfactory.createEntityManager( );
    }

    /**
     * Add new entity
     * @param object
     */
    //public <T extends AbstractEntity> void addNew(T object){
    public boolean addNew(Object object){
        EntityTransaction et = entitymanager.getTransaction( );
        try{
            et.begin( );
            entitymanager.merge( object );
            et.commit( );
            return true;
        }catch(Exception e){
            et.rollback();
            return false;
        }
    }

    /**
     * Add new an entity by persist to know the id after commit
     * @param object
     * @return
     */
    public boolean addNewByPersist(Object object) {
        try{
            entitymanager.getTransaction( ).begin( );
            try{
                entitymanager.persist(object);
            }catch(EntityExistsException eee){
                System.out.println("Entity existed!");
                return false;
            }
            catch(IllegalArgumentException eiae){
                System.out.println("Illegal argument!");
                return false;
            }
            entitymanager.flush();
            entitymanager.getTransaction( ).commit( );
            return true;
        }catch(IllegalStateException e){
            System.out.println("Illegal state!");
            return false;
        }
    }

    /**
     * Update entity
     *  - if entity exist, update it
     *  - if not, insert a new register
     *  But recommended use this function to update existing entity
     * @param object: object contains information
     */
    public <T extends AbstractEntity> void update(T entity){
        EntityTransaction et = entitymanager.getTransaction( );
        try{
            et.begin( );
            entitymanager.merge(entity);
            et.commit( );
        }catch(Exception e){
            et.rollback();
        }
    }

    /**
     * Remove entity by its id
     * @param id
     * @param object
     */
    public <T extends AbstractEntity> void remove(String entityName, int id){
        EntityTransaction et = entitymanager.getTransaction( );
        try{
            et.begin( );
            T entity = findById(entityName, id);
            entitymanager.remove(entity);
            et.commit( );
        }catch(Exception e){
            et.rollback();
        }
    }

    /**
     * Find all entities
     * We use JPQL (JPA query language) so we will create query to entity not table
     * @param entityName
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<Object> findAll(String entityName){
        Query query = entitymanager.createQuery("SELECT e FROM "+entityName+" e");
        return query.getResultList();
    }

    /**
     * Search all records that match filter object with pagination
     * @param entityName
     * @param filterObject
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<Object> searchWithCriteriaWithPagination( String entityName, FilterObject filterObject ){
        return convertFilterObjectToSQLQuery(entityName, filterObject).getResultList();
    }

    /**
     * Find entity by its id
     * @param id
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T extends AbstractEntity> T findById(String entityName, int id){
        Query query = entitymanager.createQuery("SELECT e FROM " + entityName + " e WHERE id="+id);
        System.out.println("find by Id: " + id);
        return ( query.getResultList()!= null
                 && !query.getResultList().isEmpty()) ? (T)query.getResultList().get(0) : null;
    }

    /**
     * Remove all entities
     */
    public void removeAll(String entityName){
        EntityTransaction et = entitymanager.getTransaction( );
        try{
            et.begin( );
            Query query = entitymanager.createQuery("DELETE FROM "+ entityName);
            query.executeUpdate();
            et.commit( );
        }catch(Exception e){
            et.rollback();
        }
    }

    /**
     * Count number of records
     * @param entityName
     * @return
     */
    public long countData(String entityName, FilterObject filterObject) {
        Query queryTotal = convertFilterObjectToSQLQuery(entityName, filterObject);//entitymanager.createQuery(sqlStr);//"SELECT count(e.id) FROM " + entityName + " e");
        return queryTotal.getResultList().size();
    }

    /**
     * Get entity by its name
     * Remark: Only use this method for entity that has unique name
     * @param entityName
     * @param name
     * @return
     */
    public Object findByName(String entityName, String name){
        System.out.println("entityName: "+entityName+", name: "+name);
        Query query = entitymanager.createQuery("SELECT e FROM " + entityName + " e WHERE name LIKE :name");
        query.setParameter("name", name);
        try{
            return query.getSingleResult();
        }catch(Exception e){
            System.out.println("Exception " + e.getMessage());
            return null;
        }
    }

    /**
     * Convert entity name & filterObject to hibernate sql query
     * @param entityName
     * @param filterObject
     * @return
     */
    private Query convertFilterObjectToSQLQuery(String entityName, FilterObject filterObject){
        String sqlStr = "SELECT e FROM "+entityName+" e";
        Map<String, String> criteria = filterObject.getCriteria();
        int numberOfRecordPerPage = filterObject.getNumberOfRecordsPerPage();
        int pageIndex = filterObject.getPageIndex();

        Query query = entitymanager.createQuery(sqlStr);
        if( criteria != null ){
            List<String> fields = new ArrayList<String>();
            fields.addAll(criteria.keySet());
            if( !fields.isEmpty() ){
                int index = 0;
                sqlStr += " WHERE ";
                while( index < fields.size() ){
                    // support first letters filter
                    sqlStr+= "isFirstLettersMatched("+ fields.get(index) + ",:var"+ fields.get(index).replace(".", "") +")=1 ";
                    if( fields.size() > 1  && index < fields.size()-1 ){
                        sqlStr+=" AND "; // we combine the condition to get field value that closely matches the criteria
                    }
                    index++;
                }
                sqlStr+= " ORDER BY " + fields.get(0) + " ASC"; // ascending order by first field
                query = entitymanager.createQuery(sqlStr);
                for( String field : fields ){
                    query.setParameter("var"+field.replace(".", ""), criteria.get(field));
                }
            }
        }
        System.out.println(sqlStr);

        query.setMaxResults(numberOfRecordPerPage);
        query.setFirstResult(pageIndex * numberOfRecordPerPage);
        return query;
    }

    /**
     * Check a property of an entity is duplicated or not
     * @param entityName
     * @param propertyName
     * @param propertyValue
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<Object> findEntityByProperty(String entityName, String propertyName, Object propertyValue) {
        Query query = entitymanager.createQuery("SELECT e FROM " + entityName + " e WHERE e." + propertyName + "=:value");
        if(propertyValue instanceof String) {
            return query.setParameter("value", ((String) propertyValue).trim()).getResultList();
        } else {
            return query.setParameter("value", propertyValue.toString()).getResultList();
            // TODO we should check with other type of propertyValue
        }
    }
    
    /**
     * Find list of object by inputed query string
     * @param queryStr, e.g: SELECT :var0,:var1 FROM...
     * @param params, list of parameter corresponding to position 0, 1,...
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<Object> findByQuery(String queryStr, List<Object> params){
        Query query = entitymanager.createQuery(queryStr);
        System.out.println(queryStr);
        for(int i=0;i<params.size();i++){
            query.setParameter("var"+i, params.get(i));
        }
        return query.getResultList();
    }

    /**
     * Find list of object by inputed query string with limit number of return records
     * @param queryStr
     * @param params
     * @param nbrRecords
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<Object> findByQueryWithLimit(String queryStr, List<Object> params, int nbrRecords){
        Query query = entitymanager.createQuery(queryStr);
        System.out.println(queryStr);
        for(int i=0;i<params.size();i++){
            query.setParameter("var"+i, params.get(i));
        }
        query.setMaxResults(nbrRecords);
        return query.getResultList();
    }

    @SuppressWarnings("unchecked")
    public Object findLastItem(String entityName) {
        Query query = entitymanager.createQuery("FROM " + entityName + " ORDER BY id DESC");
        query.setMaxResults(1);
        List<Object> results = query.getResultList();
        return (results != null && results.size() > 0) ? results.get(0) : null;
    }

    public void refreshEntity(Object entity, Class<?> classtype, int id) {
        EntityTransaction et = entitymanager.getTransaction( );
        try{
            et.begin( );
            entity = entitymanager.find(classtype, id);
            entitymanager.refresh(entity);
            et.commit( );
        }catch(Exception e){
            et.rollback();
        }
    }
}
