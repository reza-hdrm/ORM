package orm;

import database.DBConnection;

import javax.persistence.Column;
import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Session {
    private DBConnection dbConnection = null;
    private Connection connection;
    private PreparedStatement preparedStatement;

    private short isolationLevel = -1;

    public void beginTransaction() {
        dbConnection = DBConnection.getDBConnection();
        try {
            connection = dbConnection.getConnection();
            connection.setAutoCommit(false);
            if (isolationLevel == -1) {
                connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void commit() {
        try {
            try {
                connection.commit();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            try {
                connection.rollback();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    public void close() {
        if (preparedStatement != null) {
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (connection == null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void save(Object object) {
        String query = QueryBuilder.getInsertQuery(object);
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(Object object) {
        String query = QueryBuilder.getUpdateQuery(object);
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(Object object) {
        String query = QueryBuilder.getDeleteQuery(object);
        dbConnection = DBConnection.getDBConnection();
        try {
            connection.prepareStatement(query).executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Object find(Class clazz, Object id) {
        Object object = null;
        try {
            object = clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        String query = QueryBuilder.getSelectQuery(object, id);

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
        return object;
    }

    public List<Object> findAll(Class clazz) {
        List<Object> objectList = new ArrayList<>();
        Object object = null;
        try {
            object = clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        String query = QueryBuilder.getSelectAllQuery(object);
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

    public short getIsolationLevel() {
        return isolationLevel;
    }

    public void setIsolationLevel(short isolationLevel) {
        this.isolationLevel = isolationLevel;
    }
}
