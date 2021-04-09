import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.intellijthemes.FlatLightFlatIJTheme;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
            FlatLightFlatIJTheme.install();
        } catch(Exception ex) {
            System.err.println("Failed to initialize LaF");
        }

        EditorFrame frame = new EditorFrame();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
