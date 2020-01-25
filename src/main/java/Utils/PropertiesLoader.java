package Utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesLoader {

    private Properties properties;

    public PropertiesLoader() {
        properties = new Properties();
        try {
            loadProperties();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadProperties() throws IOException {
        String PROP_FILENAME = "config.properties";
        InputStream is = getClass().getClassLoader().getResourceAsStream(PROP_FILENAME);
        if (is != null)
            properties.load(is);
        else
            throw new FileNotFoundException("property file not found in the classpath");
    }

    public String getProperty(String property) {
        return properties.getProperty(property);
    }


}
