package com.luvsoft.DAO;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import com.luvsoft.entities.AbstractEntity;
import com.luvsoft.utils.EntityAnalyzer;

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
    //public <T extends AbstractEntity> void addNew(T object){
    public boolean addNew(Object object){
        try{
            entitymanager.getTransaction( ).begin( );
            try{
                entitymanager.persist( object );
            }catch(EntityExistsException eee){
                System.out.println("Entity existed!");
                return false;
            }
            catch(IllegalArgumentException eiae){
                System.out.println("Illegal argument!");
                return false;
            }
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
        List<Object> results = convertFilterObjectToSQLQuery(entityName, filterObject).getResultList();

        if( results.isEmpty() ){
            return results;
        }

        List<Object> retList = new ArrayList<>();
        retList.addAll(results);

        EntityAnalyzer entityAnalyzer = new EntityAnalyzer(results.get(0).getClass().getName());
        Map<String, String> criteria = filterObject.getCriteria();
        List<String> fields = new ArrayList<String>();
        fields.addAll(criteria.keySet());
        // process the results to get closer matching records
        for(Iterator<Object> it = results.iterator(); it.hasNext(); ) {
            Object object = it.next();
            for( int index = 0; index < entityAnalyzer.getFieldList().size(); index++ ){
                Field field = entityAnalyzer.getFieldList().get(index);
                if( fields.contains(field.getName()) ){
                    // get the first letter list (combined into a string) of the field value
                    // then check if it match the first letter list from criteria
                    Object value = EntityAnalyzer.getFieldValue(object, field);
                    System.out.println(value.toString());
                    List<String> words = new ArrayList<String>();
                    words.addAll( Arrays.asList(value.toString().toUpperCase().trim().split(" ") )); // split to get all words

                    // Criteria
                    String criVal = criteria.get(field.getName()).toUpperCase();
                    List<String> criteriaWords = new ArrayList<String>();
                    criteriaWords.addAll( Arrays.asList(criVal.trim().split(" ") )); // split to get all words

                    System.out.println("words: " + words);
                    System.out.println("criteriaWords: " + criteriaWords);
                    // check all the words
                    boolean recordOk = false;
                    for( String wrd : words ){
                        if( wrd.contains(criteriaWords.get(0)) ){
                            boolean checkOk = true;
                            int idx = words.indexOf(wrd); // index to iterate words
                            int cwIdx = 0; // index to iterate criteriaWords
                            while( idx < words.size() ){
                                if( cwIdx == criteriaWords.size() ){
                                    break;
                                }
                                if( !words.get(idx).contains(criteriaWords.get(cwIdx))){
                                    checkOk = false;
                                    break;
                                }
                                idx++;
                                cwIdx++;
                            }
                            
                            // All criteria are matched
                            // stop checking
                            if( cwIdx == criteriaWords.size() && checkOk ){
                                recordOk = true;
                                break;
                            }
                        }
                    }

                    // if this record does not match the pattern, remove it from the list
                    if( !recordOk ){
                        retList.remove(object);
                        // it.remove();
                    }
                }
            }
        }
        return retList;
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
        Query query = entitymanager.createQuery("SELECT e FROM " + entityName + " e WHERE name="+name);
        return query.getSingleResult();
    }

    /**
     * Convert entity name & filterObject to hibernate sql query
     * @param entityName
     * @param filterObject
     * @return
     */
    private Query convertFilterObjectToSQLQuery(String entityName, FilterObject filterObject){
        String sqlStr = "SELECT e FROM "+entityName+" e";
        // Because of some issue when hibernate process keyword "LIKE"
        // We need to create query and then set parameters for it
        // Create query
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
                    sqlStr+= fields.get(index) + " LIKE :var"+fields.get(index);
                    if( fields.size() > 1  && index < fields.size()-1 ){
                        sqlStr+=" AND "; // we combine the condition to get field value that closely matches the criteria
                    }
                    index++;
                }
                query = entitymanager.createQuery(sqlStr);
                // Set parameter
                for( String field : fields ){
                    // criteria "c g" to be "%c% g%"
                    // to support first letter only searching
                    // e.x: type "c g" --> all value that contain "c% g" will be consider as matched
                    query.setParameter("var"+field, "%"+ criteria.get(field).replaceAll(" ", "% ") +"%");
                }
            }
        }
        System.out.println(sqlStr);

        query.setMaxResults(numberOfRecordPerPage);
        query.setFirstResult(pageIndex * numberOfRecordPerPage);
        return query;
    }

    /**
     * Check the name is duplicated or not
     * @param entityName
     * @param name
     * @return true if the name is existing in database
     */
    @SuppressWarnings("unchecked")
    public List<Object> findEntityByName(String entityName, String name) {
        Query query = entitymanager.createQuery("SELECT e FROM " + entityName + " e WHERE e.name=:name");
        return query.setParameter("name", name.trim()).getResultList();
    }
}
