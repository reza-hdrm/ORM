package orm;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;

public class Config {
    private final String pathname = "src/main/resources/application.properties";
    private final File file = new File(pathname);
    private static final Properties properties = new Properties();
    private static volatile Config config = null;
    private final Logger LOGGER=null;

    private Config() {
        try {
            properties.load(new FileInputStream(file));
            //properties.load(new FileReader(pathname));
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

    public static boolean getDBShowSql(){
        return Boolean.parseBoolean(properties.getProperty("com.settings.db_show_sql"));
    }
    public static Config getConfig() {
        if (config == null)
            synchronized (Config.class) {
                if (config == null)
                    config = new Config();
            }
        return config;
    }

}
