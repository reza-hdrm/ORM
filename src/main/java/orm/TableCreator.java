package orm;

import annotation.Column;
import annotation.Id;
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
        String query = QueryBuilder.getCreateTableQuery(object);

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
}
