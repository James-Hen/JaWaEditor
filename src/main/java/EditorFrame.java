import javax.swing.*;
import javax.swing.text.StyledEditorKit;
import javax.swing.text.html.HTMLEditorKit;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;

public class EditorFrame extends JFrame {
    private JMenuBar menuBar;
    private JMenu fileMenu;
    private JMenu helpMenu;
    private JTextPane textArea;

    EditorFrame() {
        this.setSize(800, 600);
        this.setTitle("JaWa Editor");

        // Setup menu
        fileMenu = new JMenu("File");
        JMenuItem newFileMenu = new JMenuItem("New");
        JMenuItem openFileMenu = new JMenuItem("Open");
        JMenuItem saveFileMenu = new JMenuItem("Save");
        JMenuItem saveAsFileMenu = new JMenuItem("Save as");
        fileMenu.add(newFileMenu);
        fileMenu.add(openFileMenu);
        fileMenu.add(saveFileMenu);
        fileMenu.add(saveAsFileMenu);

        helpMenu = new JMenu("Help");
        JMenuItem aboutMenu = new JMenuItem("About");
        aboutMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "JaWa Editor v1.0");
            }
        });
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
        JButton boldButton = new JButton(new StyledEditorKit.BoldAction());
        JButton italicButton = new JButton(new StyledEditorKit.ItalicAction());
        JButton redButton = new JButton(new StyledEditorKit.ForegroundAction("red", Color.RED));
        toolPanel.add(boldButton);
        toolPanel.add(italicButton);
        toolPanel.add(redButton);
    }
}
