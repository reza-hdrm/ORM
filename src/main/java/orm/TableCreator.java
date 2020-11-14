package orm;

import database.DBConnection;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TableCreator {
    public void create(Object object) {
        String query = QueryBuilder.getCreateTableQuery(object);

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
