package Utils;

import java.awt.Color;
import java.util.HashMap;

public class ColorProvider {

    private HashMap<String, Color> colors;

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

    public Color getColor(String color) {
        return this.colors.get(color);
    }

}
