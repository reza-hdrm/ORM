package database;

import orm.Config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private String dbDriver;
    private String dbUser;
    private String dbPassword;
    private String dbUrl;

    private Connection connection = null;

    private static volatile DBConnection databaseConnection = null;

    private DBConnection() {
        Config.getConfig();
        openConnection();
    }

    private void setDBProperties() {
        this.dbDriver = Config.getDBDriver();
        this.dbUrl = Config.getDBUrl();
        this.dbUser = Config.getDBUser();
        this.dbPassword = Config.getDBPassword();
    }

    public Connection openConnection() {
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

    public Connection getConnection() {
        return connection;
    }

}
