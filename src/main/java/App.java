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
     * Launch App
     *
     * <p>
     *     Prepares:
     * </p>
     * <ul>
     *     <li>Properties Loader</li>
     *     <li>Font Provider</li>
     *     <li>Color Provider</li>
     *     <li>Images Loader</li>
     * </ul>
     */
    public static void launchApp() {
        /* Properties loader which loads the config.properties */
        PropertiesLoader propManager = new PropertiesLoader();

        /* Providers for Fonts and Colors*/
        FontProvider fontProvider = new FontProvider(propManager);
        ColorProvider colorProvider = new ColorProvider(propManager);

        /* Images loader */
        ImagesLoader imagesLoader = new ImagesLoader();

        /* Start the Application */
        new GUIFrame(fontProvider, colorProvider, imagesLoader).setVisible(true);
    }

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(App::launchApp);
    }

}
