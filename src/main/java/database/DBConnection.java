package database;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnection {

    private String dbDriver;
    private String dbUser;
    private String dbPassword;
    private String dbUrl;

    private Connection connection = null;

    private static volatile DBConnection databaseConnection = null;

    private DBConnection() {

    }

    private void setDBProperties() throws IOException {
        String pathname="src/main/resources/application.properties";
        File file = new File(pathname);
        Properties properties = new Properties();
        properties.load(new FileInputStream(file));

        this.dbDriver = properties.getProperty("com.settings.db_driver");
        this.dbUrl = properties.getProperty("com.settings.db_url");
        this.dbUser = properties.getProperty("com.settings.db_user");
        this.dbPassword = properties.getProperty("com.settings.db_password");
    }

    public Connection getConnection() throws IOException {
        setDBProperties();
        try {
            connection = DriverManager.getConnection(this.dbUrl, this.dbUser, this.dbPassword);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public static DBConnection getDBConnection() {
        if (databaseConnection == null)
            synchronized (DBConnection.class) {
                if (databaseConnection == null)
                    databaseConnection = new DBConnection();
            }
        return databaseConnection;
    }

}
