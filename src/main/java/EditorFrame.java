import javax.swing.*;
import javax.swing.text.StyledEditorKit;
import javax.swing.text.html.HTMLEditorKit;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EditorFrame extends JFrame implements ActionListener {
    private JMenuBar menuBar;

    private JMenu fileMenu;
    JMenuItem newFileMenu, openFileMenu, saveFileMenu, saveAsFileMenu;

    private JMenu helpMenu;
    JMenuItem aboutMenu;

    private JTextPane textArea;

    EditorFrame() {
        this.setSize(800, 600);
        this.setTitle("JaWa Editor");

        // Setup menu
        fileMenu = new JMenu("File");
        newFileMenu = new JMenuItem("New");
        newFileMenu.addActionListener(this);
        openFileMenu = new JMenuItem("Open");
        openFileMenu.addActionListener(this);
        saveFileMenu = new JMenuItem("Save");
        saveFileMenu.addActionListener(this);
        saveAsFileMenu = new JMenuItem("Save as");
        saveAsFileMenu.addActionListener(this);
        fileMenu.add(newFileMenu);
        fileMenu.add(openFileMenu);
        fileMenu.add(saveFileMenu);
        fileMenu.add(saveAsFileMenu);

        helpMenu = new JMenu("Help");
        aboutMenu = new JMenuItem("About");
        aboutMenu.addActionListener(this);
        helpMenu.add(aboutMenu);

        menuBar = new JMenuBar();
        menuBar.add(fileMenu);
        menuBar.add(helpMenu);
        this.setJMenuBar(menuBar);

        // Setup core UI components
        BorderLayout layout = new BorderLayout();
        this.setLayout(layout);

        textArea = new JTextPane();
        textArea.setEditorKit(new HTMLEditorKit());
        this.add(textArea, BorderLayout.CENTER);

        textArea.setContentType("text/html");
        textArea.setText("<p>Hi!</p>");

        JPanel toolPanel = new JPanel();
        this.add(toolPanel, BorderLayout.NORTH);
        TextStyleOptionCheckbox boldCheckBox =
                new TextStyleOptionCheckbox("Bold", false, new StyledEditorKit.BoldAction());
        TextStyleOptionCheckbox italicCheckBox =
                new TextStyleOptionCheckbox("Italic", false, new StyledEditorKit.ItalicAction());
        TextStyleOptionCheckbox redCheckBox =
                new TextStyleOptionCheckbox("Red", false, new StyledEditorKit.ForegroundAction("red", Color.RED));
        toolPanel.add(boldCheckBox);
        toolPanel.add(italicCheckBox);
        toolPanel.add(redCheckBox);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // "File Menu" options
        if (e.getSource() == newFileMenu) {
            JOptionPane.showMessageDialog(null, "TODO");
        }
        else if (e.getSource() == newFileMenu) {
            JOptionPane.showMessageDialog(null, "TODO");
        }
        else if (e.getSource() == newFileMenu) {
            JOptionPane.showMessageDialog(null, "TODO");
        }
        else if (e.getSource() == newFileMenu) {
            JOptionPane.showMessageDialog(null, "TODO");
        }
        // "Help Menu" options
        else if (e.getSource() == aboutMenu) {
            JOptionPane.showMessageDialog(null, "JaWa Editor v1.0");
        }
    }
}
