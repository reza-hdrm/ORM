package database;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection {
    private String dbDriver;
    private String dbUser;
    private String dbpPassword;
    private String dbUrl;
    private Connection connection = null;
    private static volatile DatabaseConnection databaseConnection = null;

    private DatabaseConnection() throws IOException {
        setDBProperties();
        getConnection();
    }

    private void setDBProperties() throws IOException {
        File file = new File("application.properties");
        Properties properties = new Properties();
        properties.load(new FileInputStream(file));

        this.dbDriver = properties.getProperty("com.settings.db_driver");
        this.dbUrl = properties.getProperty("com.settings.db_url");
        this.dbUser = properties.getProperty("com.settings.db_user");
        this.dbpPassword = properties.getProperty("com.settings.db_password");
    }

    private Connection getConnection() {
        try {
            connection = DriverManager.getConnection(this.dbUrl, this.dbUser, this.dbpPassword);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public static DatabaseConnection getDatabaseConnection() throws IOException {
        if (databaseConnection == null)
            synchronized (DatabaseConnection.class) {
                if (databaseConnection == null)
                    return databaseConnection = new DatabaseConnection();
            }
        return databaseConnection;
    }

}
