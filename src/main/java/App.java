import java.awt.Dimension;

import javax.swing.WindowConstants;

import frames.MainFrame;

public class App {

    public static void main() {
        MainFrame mainFrame = new MainFrame();
        mainFrame.setVisible(true);
    }


    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(App::main);
    }

}
