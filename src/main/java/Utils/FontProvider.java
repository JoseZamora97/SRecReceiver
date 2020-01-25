package Utils;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

public class FontProvider {

    private HashMap<String, Font> fonts;

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

    public Font getFont(String font) {
        return this.fonts.get(font);
    }

}