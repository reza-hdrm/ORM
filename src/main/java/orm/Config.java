package orm;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;

public class Config {
    private static final Properties properties = new Properties();
    private static volatile Config config = null;

    private Config() {
        try (final FileInputStream fileInputStream = new FileInputStream(
                new File("src/main/resources/application.properties"))) {
            properties.load(fileInputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getDBDriver() {
        return properties.getProperty("com.settings.db_driver");
    }

    public static String getDBUrl() {
        return properties.getProperty("com.settings.db_url");
    }

    public static String getDBUser() {
        return properties.getProperty("com.settings.db_user");
    }

    public static String getDBPassword() {
        return properties.getProperty("com.settings.db_password");
    }

    public static boolean getDBShowSql() {
        return Boolean.parseBoolean(properties.getProperty("com.settings.db_show_sql"));
    }

    public static void getConfig() {
        if (config == null)
            synchronized (Config.class) {
                if (config == null)
                    config = new Config();
            }
    }

}
