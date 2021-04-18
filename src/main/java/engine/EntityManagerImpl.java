package engine;

import database.DBConnection;
import orm.QueryBuilder;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.metamodel.Metamodel;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class EntityManagerImpl implements EntityManager {
    private DBConnection dbConnection = null;
    private Connection connection;
    private PreparedStatement preparedStatement;

    public EntityManagerImpl(DBConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    @Override
    public void persist(Object entity) {
        String query = QueryBuilder.getInsertQuery(entity);
        try {
            connection.prepareStatement(query).execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public <T> T merge(T entity) {
        String query = QueryBuilder.getUpdateQuery(entity.getClass());
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // TODO fresh entity
        return entity;
    }

    @Override
    public void remove(Object entity) {
        String query = QueryBuilder.getDeleteQuery(entity.getClass());
        dbConnection = DBConnection.getDBConnection();
        try {
            connection.prepareStatement(query).executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public <T> T find(Class<T> entityClass, Object primaryKey) {
        Object object = null;
        try {
            object = entityClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        String query = QueryBuilder.getSelectQuery(entityClass, primaryKey);

        //TODO fields isn't single responsible
        Field[] fields = object.getClass().getDeclaredFields();
        ResultSet resultSet = null;
        try {
            resultSet = connection.prepareStatement(query).executeQuery();
            while (resultSet.next())
                for (Field field : fields) {
                    field.setAccessible(true);
                    Column column = field.getDeclaredAnnotation(Column.class);
                    //TODO refactoring filed.getType.endsWith ...
                    if (column != null) {
                        if (field.getType().getSimpleName().endsWith("int"))
                            field.set(object, resultSet.getInt(column.name()));
                        else if (field.getType().getSimpleName().endsWith("String"))
                            field.set(object, resultSet.getString(column.name()));
                        else if (field.getType().getSimpleName().endsWith("Integer"))
                            field.set(object, resultSet.getInt(column.name()));
                    }
                }
        } catch (SQLException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return (T) object;
    }

    @Override
    public <T> T find(Class<T> entityClass, Object primaryKey, Map<String, Object> properties) {
        return null;
    }

    @Override
    public <T> T find(Class<T> entityClass, Object primaryKey, LockModeType lockMode) {
        return null;
    }

    @Override
    public <T> T find(Class<T> entityClass, Object primaryKey, LockModeType lockMode, Map<String, Object> properties) {
        return null;
    }

    @Override
    public <T> T getReference(Class<T> entityClass, Object primaryKey) {
        return null;
    }

    @Override
    public void flush() {

    }

    @Override
    public void setFlushMode(FlushModeType flushMode) {

    }

    @Override
    public FlushModeType getFlushMode() {
        return null;
    }

    @Override
    public void lock(Object entity, LockModeType lockMode) {

    }

    @Override
    public void lock(Object entity, LockModeType lockMode, Map<String, Object> properties) {

    }

    @Override
    public void refresh(Object entity) {

    }

    @Override
    public void refresh(Object entity, Map<String, Object> properties) {

    }

    @Override
    public void refresh(Object entity, LockModeType lockMode) {

    }

    @Override
    public void refresh(Object entity, LockModeType lockMode, Map<String, Object> properties) {

    }

    @Override
    public void clear() {

    }

    @Override
    public void detach(Object entity) {

    }

    @Override
    public boolean contains(Object entity) {
        return false;
    }

    @Override
    public LockModeType getLockMode(Object entity) {
        return null;
    }

    @Override
    public void setProperty(String propertyName, Object value) {

    }

    @Override
    public Map<String, Object> getProperties() {
        return null;
    }

    @Override
    public Query createQuery(String qlString) {
        return null;
    }

    @Override
    public <T> TypedQuery<T> createQuery(CriteriaQuery<T> criteriaQuery) {
        return null;
    }

    @Override
    public Query createQuery(CriteriaUpdate updateQuery) {
        return null;
    }

    @Override
    public Query createQuery(CriteriaDelete deleteQuery) {
        return null;
    }

    @Override
    public <T> TypedQuery<T> createQuery(String qlString, Class<T> resultClass) {
        return null;
    }

    @Override
    public Query createNamedQuery(String name) {
        return null;
    }

    @Override
    public <T> TypedQuery<T> createNamedQuery(String name, Class<T> resultClass) {
        return null;
    }

    @Override
    public Query createNativeQuery(String sqlString) {
        return null;
    }

    @Override
    public Query createNativeQuery(String sqlString, Class resultClass) {
        return null;
    }

    @Override
    public Query createNativeQuery(String sqlString, String resultSetMapping) {
        return null;
    }

    @Override
    public StoredProcedureQuery createNamedStoredProcedureQuery(String name) {
        return null;
    }

    @Override
    public StoredProcedureQuery createStoredProcedureQuery(String procedureName) {
        return null;
    }

    @Override
    public StoredProcedureQuery createStoredProcedureQuery(String procedureName, Class... resultClasses) {
        return null;
    }

    @Override
    public StoredProcedureQuery createStoredProcedureQuery(String procedureName, String... resultSetMappings) {
        return null;
    }

    @Override
    public void joinTransaction() {

    }

    @Override
    public boolean isJoinedToTransaction() {
        return false;
    }

    @Override
    public <T> T unwrap(Class<T> cls) {
        return null;
    }

    @Override
    public Object getDelegate() {
        return null;
    }

    @Override
    public void close() {
        if (Objects.nonNull(preparedStatement)) {
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (Objects.nonNull(connection)) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean isOpen() {
        return false;
    }

    @Override
    public EntityTransaction getTransaction() {
        return new EntityTransactionImpl(connection);
    }

    @Override
    public EntityManagerFactory getEntityManagerFactory() {
        return null;
    }

    @Override
    public CriteriaBuilder getCriteriaBuilder() {
        return null;
    }

    @Override
    public Metamodel getMetamodel() {
        return null;
    }

    @Override
    public <T> EntityGraph<T> createEntityGraph(Class<T> rootType) {
        return null;
    }

    @Override
    public EntityGraph<?> createEntityGraph(String graphName) {
        return null;
    }

    @Override
    public EntityGraph<?> getEntityGraph(String graphName) {
        return null;
    }

    @Override
    public <T> List<EntityGraph<? super T>> getEntityGraphs(Class<T> entityClass) {
        return null;
    }

    public List<Object> findAll(Class<?> clazz) {
        List<Object> objectList = new ArrayList<>();
        Object object = null;
        try {
            object = clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        String query = QueryBuilder.getSelectAllQuery(clazz);
        //TODO fields refactor - this code isn't single responsible
        Field[] fields = object.getClass().getDeclaredFields();

        ResultSet resultSet = null;
        try {
            resultSet = connection.prepareStatement(query).executeQuery();
            while (resultSet.next()) {
                object = object.getClass().newInstance();
                for (Field field : fields) {
                    field.setAccessible(true);
                    Column column = field.getDeclaredAnnotation(Column.class);
                    if (column != null) {
                        //TODO refactoring filed.getType.endsWith ...
                        if (field.getType().getSimpleName().endsWith("int"))
                            field.set(object, resultSet.getInt(column.name()));
                        else if (field.getType().getSimpleName().endsWith("String"))
                            field.set(object, resultSet.getString(column.name()));
                        else if (field.getType().getSimpleName().endsWith("Integer"))
                            field.set(object, resultSet.getInt(column.name()));
                    }
                }
                objectList.add(object);
            }
        } catch (SQLException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return objectList;
    }
}
