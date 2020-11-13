package orm;

import annotation.Column;
import annotation.Entity;
import annotation.Id;
import annotation.Table;
import database.DBConnection;
import exception.EntityException;

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
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    public void commite() {
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
        Entity entity = object.getClass().getDeclaredAnnotation(Entity.class);
        if (entity == null)
            try {
                throw new EntityException(object);
            } catch (EntityException e) {
                System.out.println(e.getMessage());
            }
        String query = getStringQuery(object);

        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private String getStringQuery(Object object) {
        Entity entity = object.getClass().getDeclaredAnnotation(Entity.class);
        if (entity == null)
            try {
                throw new EntityException(object);
            } catch (EntityException e) {
                System.out.println(e.getMessage());
            }
        StringBuilder query = new StringBuilder("INSERT INTO ");
        Table table = object.getClass().getDeclaredAnnotation(Table.class);
        query.append(table.name()).append("(");
        Field[] fields = object.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            Column column = field.getAnnotation(Column.class);
            if (column != null)
                query.append(column.name()).append(",");
        }
        if (query.toString().trim().endsWith(","))
            query = new StringBuilder(query.substring(0, query.length() - 1));
        query.append(") VALUES (");
        for (Field field : fields) {
            try {
                if (field.getType().getSimpleName().endsWith("String"))
                    query.append("'").append(field.get(object)).append("' ,");
                else
                    query.append(field.get(object)).append(",");
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        if (query.toString().trim().endsWith(",")) {
            query = new StringBuilder(query.substring(0, query.length() - 1));
        }
        query.append(")");
        return query.toString();
    }

    public void update(Object object) {
        Table table = object.getClass().getDeclaredAnnotation(Table.class);
        Entity entity = object.getClass().getDeclaredAnnotation(Entity.class);
        if (entity == null)
            try {
                throw new EntityException(object);
            } catch (EntityException e) {
                System.out.println(e.getMessage());
            }
        String query = "UPDATE " + table.name() + " SET ";
        Field[] fields = object.getClass().getDeclaredFields();
        Object oid = null;
        String idColumn = "";
        for (Field field : fields) {
            Column column = field.getAnnotation(Column.class);
            field.setAccessible(true);
            Id id = field.getAnnotation(Id.class);
            if (id != null) {
                try {
                    oid = field.get(object);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                idColumn = column.name();
            }
            if (column != null) {
                try {
                    if (id == null)
                        if (field.getType().getSimpleName().endsWith("String"))
                            query += column.name() + "='" + field.get(object) + "',";
                        else
                            query += column.name() + "=" + field.get(object) + ",";
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        if (query.trim().endsWith(",")) {
            query = query.substring(0, query.length() - 1);
        }
        query += " WHERE " + idColumn + "=" + oid;
        System.out.println(query);
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Object get(Class clazz, Object id) {
        Object object = null;
        try {
            object = clazz.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        Table table = object.getClass().getDeclaredAnnotation(Table.class);
        Entity entity = object.getClass().getDeclaredAnnotation(Entity.class);
        if (entity == null)
            try {
                throw new EntityException(object);
            } catch (EntityException e) {
                System.out.println(e.getMessage());
            }
        Field[] fields = object.getClass().getDeclaredFields();
        String tableName = table.name();
        String idColumnName = "";
        for (Field field : fields) {
            Id idAnnotation = field.getDeclaredAnnotation(Id.class);
            if (idAnnotation != null) {
                Column column = field.getDeclaredAnnotation(Column.class);
                idColumnName = column.name();
            }
        }
        String query = "SELECT * FROM " + tableName + " WHERE " + idColumnName + " = " + id;

        ResultSet resultSet = null;
        try {
            resultSet = connection.prepareStatement(query).executeQuery();
            while (resultSet.next())
                for (Field field : fields) {
                    field.setAccessible(true);
                    Column column = field.getDeclaredAnnotation(Column.class);
                    if (column != null) {
                        if (field.getType().getSimpleName().endsWith("int"))
                            field.set(object, resultSet.getInt(column.name()));
                        else if (field.getType().getSimpleName().endsWith("String"))
                            field.set(object, resultSet.getString(column.name()));
                        else if (field.getType().getSimpleName().endsWith("Integer"))
                            field.set(object, resultSet.getInt(column.name()));
                    }
                }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        System.out.println(query);
        return object;
    }

    public void delete(Object object) {
        Table table = object.getClass().getDeclaredAnnotation(Table.class);
        Entity entity = object.getClass().getDeclaredAnnotation(Entity.class);
        if (entity == null)
            try {
                throw new EntityException(object);
            } catch (EntityException e) {
                System.out.println(e.getMessage());
            }
        String query = "DELETE FROM " + table.name() + " WHERE ";
        Field[] fields = object.getClass().getDeclaredFields();
        Object oid = null;
        String idColumn = "";
        for (Field field : fields) {
            field.setAccessible(true);
            Id id = field.getAnnotation(Id.class);
            if (id != null) {
                Column column = field.getAnnotation(Column.class);
                try {
                    oid = field.get(object);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                idColumn = column.name();
            }
        }
        query += idColumn + " = " + oid;
        System.out.println(query);
        dbConnection = DBConnection.getDBConnection();
        try {
            connection.prepareStatement(query).executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Object> findAll(Class clazz) {
        Object object = null;
        try {
            object = clazz.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        Entity entity = object.getClass().getDeclaredAnnotation(Entity.class);
        if (entity == null)
            try {
                throw new EntityException(object);
            } catch (EntityException e) {
                System.out.println(e.getMessage());
            }
        List<Object> objectList = new ArrayList<>();
        Table table = object.getClass().getDeclaredAnnotation(Table.class);
        Field[] fields = object.getClass().getDeclaredFields();
        String tableName = table.name();
        String query = "SELECT * FROM " + tableName;

        ResultSet resultSet = null;
        try {
            resultSet = connection.prepareStatement(query).executeQuery();
            while (resultSet.next()) {
                object = object.getClass().newInstance();
                for (Field field : fields) {
                    field.setAccessible(true);
                    Column column = field.getDeclaredAnnotation(Column.class);
                    if (column != null) {
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
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        System.out.println(query);
        return objectList;
    }

    public short getIsolationLevel() {
        return isolationLevel;
    }

    public void setIsolationLevel(short isolationLevel) {
        this.isolationLevel = isolationLevel;
    }
}
