package PresentationLayer.initialize;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {
    private final Properties properties;
    public ConfigReader() {
        properties = new Properties();
    }
    private static class ConfigProperties{
        public static final String INITIALIZE_PATH="InitializePath";
        public static final String DB_FIELD1="DB_FIELD1";
        public static final String DB_FIELD2="DB_FIELD2";
        public static final String DB_FIELD3="DB_FIELD3";
        public static final String DB_FIELD4="DB_FIELD4";

    }

    public String getInitializePath() {
        return getConfigDetail(ConfigProperties.INITIALIZE_PATH);
    }
    public String getDB1() {
        return getConfigDetail(ConfigProperties.DB_FIELD1);
    }
    private String getConfigDetail(String configProperty){
        String property="";

        try (FileInputStream fis = new FileInputStream("config.properties")) {
            properties.load(fis);

            // Read the relative addresses of the files
            property = properties.getProperty(configProperty);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return property;
    }
}
