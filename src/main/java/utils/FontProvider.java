package utils;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

/**
 * The type Font provider.
 */
public class FontProvider {

    private HashMap<String, Font> fonts;

    /**
     * Instantiates a new Font provider.
     *
     * @param propertiesLoader the properties loader
     */
    public FontProvider(PropertiesLoader propertiesLoader) {
        fonts = new HashMap<>();

        if(System.getProperty("os.name").startsWith("Windows"))
            fonts.put("default", new Font(propertiesLoader
                    .getProperty("default-windows-font"), Font.PLAIN, 1));
        else
            fonts.put("default", new Font(propertiesLoader
                    .getProperty("default-ubuntu-font"), Font.PLAIN, 1));

        try {
            loadCustomFonts();
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
        }
    }

    /**
     * Load the custom fonts.
     *
     * <p>
     *     Loads the fonts and fill the Map of fonts.
     * </p>
     *
     * @throws IOException if the font file doesnt exist.
     * @throws FontFormatException if the format font is incorrect.
     */
    private void loadCustomFonts() throws IOException, FontFormatException {

        Font nunito = Font.createFont(Font.TRUETYPE_FONT,
            Objects.requireNonNull(
                    getClass().getClassLoader().getResourceAsStream("fonts/Nunito-Regular.ttf")
            )
        );
        fonts.put("nunito", nunito);

        Font nunito_bold = Font.createFont(Font.TRUETYPE_FONT,
            Objects.requireNonNull(
                getClass().getClassLoader().getResourceAsStream("fonts/Nunito-Bold.ttf")
            )
        );
        fonts.put("nunito-bold", nunito_bold);
    }

    /**
     * Gets font.
     *
     * @param font the font
     * @return the font
     */
    public Font getFont(String font) {
        return this.fonts.get(font);
    }

}
