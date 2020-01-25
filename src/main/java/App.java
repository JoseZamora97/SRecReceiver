import Utils.ColorProvider;
import Utils.FontProvider;
import Utils.ImagesLoader;
import Utils.PropertiesLoader;

import gui.GUIFrame;

public class App {

    public static void main() {
        PropertiesLoader propManager = new PropertiesLoader();

        FontProvider fontProvider = new FontProvider(propManager);
        ColorProvider colorProvider = new ColorProvider(propManager);
        ImagesLoader imagesLoader = new ImagesLoader();

        GUIFrame GUIFrame = new GUIFrame(fontProvider, colorProvider, imagesLoader);
        GUIFrame.setVisible(true);
    }

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(App::main);
    }

}
