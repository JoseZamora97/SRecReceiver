package Utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * The type Properties loader.
 */
public class PropertiesLoader {

    private Properties properties;

    /**
     * Instantiates a new Properties loader.
     */
    public PropertiesLoader() {
        properties = new Properties();
        try {
            loadProperties();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Load the config.properties files.
     *
     * @throws IOException if the file doesnt exist.
     */
    private void loadProperties() throws IOException {
        String PROP_FILENAME = "config.properties";
        InputStream is = getClass().getClassLoader().getResourceAsStream(PROP_FILENAME);
        if (is != null)
            properties.load(is);
        else
            throw new FileNotFoundException("property file not found in the classpath");
    }

    /**
     * Gets property.
     *
     * @param property the property
     * @return the property
     */
    public String getProperty(String property) {
        return properties.getProperty(property);
    }


}
