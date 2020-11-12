package orm;

import annotation.Column;
import annotation.Table;
import database.DBConnection;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TableCreator {
    public void create(Object object) {
        String query = getStringQuery(object);
        System.out.println(query);

        DBConnection dbConnection = DBConnection.getDBConnection();
        Connection connection = null;
        try {
            connection = dbConnection.getConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private String getStringQuery(Object object) {
        Table table = object.getClass().getDeclaredAnnotation(Table.class);
        StringBuilder query = new StringBuilder("CREATE TABLE IF NOT EXISTS " + table.name() + " (");
        Field[] fields = object.getClass().getDeclaredFields();
        for (Field field : fields) {
            Annotation[] annotations = field.getDeclaredAnnotations();
            for (Annotation annotation : annotations)
                if (annotation instanceof Column) {
                    Column column = field.getAnnotation(Column.class);
                    query.append(column.name()).append(" ").append(column.dataType()).append("(").append(column.size()).append("),");
                }
        }
        if (query.toString().trim().endsWith(","))
            query = new StringBuilder(query.substring(0, query.length() - 1));
        query.append(");");
        return query.toString();
    }
}
