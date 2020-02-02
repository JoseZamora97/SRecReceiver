import Utils.ColorProvider;
import Utils.FontProvider;
import Utils.ImagesLoader;
import Utils.PropertiesLoader;

import gui.GUIFrame;

/**
 * The type App.
 */
public class App {

    /**
     * Main.
     */
    public static void main() {
        PropertiesLoader propManager = new PropertiesLoader();

        FontProvider fontProvider = new FontProvider(propManager);
        ColorProvider colorProvider = new ColorProvider(propManager);
        ImagesLoader imagesLoader = new ImagesLoader();

        GUIFrame GUIFrame = new GUIFrame(fontProvider, colorProvider, imagesLoader);
        GUIFrame.setVisible(true);
    }

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(App::main);
    }

}
