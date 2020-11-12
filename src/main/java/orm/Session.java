package orm;

import annotation.Column;
import annotation.Table;
import database.DBConnection;

import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Session {
    DBConnection dbConnection = null;
    Connection connection;

    public void save(Object object) {
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
        System.out.println(query);
        dbConnection = DBConnection.getDBConnection();
        try {
            connection=dbConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(query.toString());
            statement.execute();
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }
}
