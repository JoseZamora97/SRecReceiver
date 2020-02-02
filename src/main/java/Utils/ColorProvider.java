package Utils;

import java.awt.Color;
import java.util.HashMap;

/**
 * The type Color provider.
 */
public class ColorProvider {

    private HashMap<String, Color> colors;

    /**
     * Instantiates a new Color provider.
     *
     * @param propertiesLoader the properties loader
     */
    public ColorProvider(PropertiesLoader propertiesLoader) {
        colors = new HashMap<>();

        colors.put("colorAccent",
                Color.decode(propertiesLoader.getProperty("colorAccent")));
        colors.put("colorPrimary",
                Color.decode(propertiesLoader.getProperty("colorPrimary")));
        colors.put("colorPrimaryDark",
                Color.decode(propertiesLoader.getProperty("colorPrimaryDark")));
        colors.put("colorTitles",
                Color.decode(propertiesLoader.getProperty("colorTitles")));
        colors.put("colorBodies",
                Color.decode(propertiesLoader.getProperty("colorBodies")));
        colors.put("colorAccentDark",
                Color.decode(propertiesLoader.getProperty("colorAccentDark")));
    }

    /**
     * Gets color.
     *
     * @param color the color
     * @return the color
     */
    public Color getColor(String color) {
        return this.colors.get(color);
    }

}
