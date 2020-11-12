package orm;

import annotation.Column;
import annotation.Id;
import annotation.Table;
import database.DBConnection;

import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Session {
    DBConnection dbConnection = null;
    Connection connection;

    public void save(Object object) {
        String query = getStringQuery(object);
        System.out.println(query);
        dbConnection = DBConnection.getDBConnection();
        try {
            connection = dbConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(query.toString());
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getStringQuery(Object object) {
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
        dbConnection = DBConnection.getDBConnection();
        try {
            dbConnection.getConnection().prepareStatement(query).executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Object get(Object object, Object id) {
        Table table = object.getClass().getDeclaredAnnotation(Table.class);
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
            resultSet = dbConnection.getDBConnection().getConnection().prepareStatement(query).executeQuery();
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
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {

        }
        System.out.println(query);
        return object;
    }
}
