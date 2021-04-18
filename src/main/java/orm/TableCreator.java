package orm;

import database.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TableCreator {
    public void create(Class<?> clazz) {
        String query = QueryBuilder.getCreateTableQuery(clazz);

        DBConnection dbConnection = DBConnection.getDBConnection();
        Connection connection = null;
        connection = dbConnection.getConnection();
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
