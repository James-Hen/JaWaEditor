package org.xjtujavacourse.client;

import javax.swing.*;
import javax.swing.text.Document;
import javax.swing.text.StyledEditorKit;
import javax.swing.text.html.HTMLEditorKit;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EditorFrame extends JFrame implements ActionListener {
    private JMenuBar menuBar;

    private JMenu fileMenu;
    private JMenuItem newFileMenu, openFileMenu, saveFileMenu, saveAsFileMenu;

    private JMenu helpMenu;
    private JMenuItem debugMenu, aboutMenu;

    private JTextPane textArea;
    private DebugFrame debugFrame;

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
        debugMenu = new JMenuItem("Debug");
        debugMenu.addActionListener(this);
        aboutMenu = new JMenuItem("About");
        aboutMenu.addActionListener(this);
        helpMenu.add(debugMenu);
        helpMenu.add(aboutMenu);

        menuBar = new JMenuBar();
        menuBar.add(fileMenu);
        menuBar.add(helpMenu);
        this.setJMenuBar(menuBar);

        // Setup core UI components
        BorderLayout layout = new BorderLayout();
        this.setLayout(layout);

        HTMLEditorKit textKit = new HTMLEditorKit();
        Document defaultDocument = textKit.createDefaultDocument();
        textArea = new JTextPane();
        textArea.setEditorKit(textKit);
        textArea.setDocument(defaultDocument);
        JScrollPane scroll = new JScrollPane(textArea);
        this.add(scroll, BorderLayout.CENTER);

        textArea.setContentType("text/html");

        JPanel toolPanel = new JPanel();
        this.add(toolPanel, BorderLayout.NORTH);
        TextStyleOptionCheckbox boldCheckBox =
                new TextStyleOptionCheckbox("Bold", false, new StyledEditorKit.BoldAction());
        TextStyleOptionCheckbox italicCheckBox =
                new TextStyleOptionCheckbox("Italic", false, new StyledEditorKit.ItalicAction());
        JComboBox<Color> textColorCombo = new JComboBox<Color>();
        textColorCombo.addItem(Color.RED);
        textColorCombo.addItem(Color.GREEN);
        textColorCombo.addItem(Color.BLUE);
        TextStyleOptionCheckbox redCheckBox =
                new TextStyleOptionCheckbox("Red", false, new StyledEditorKit.ForegroundAction("red", Color.RED));
        toolPanel.add(boldCheckBox);
        toolPanel.add(italicCheckBox);
        toolPanel.add(textColorCombo);
        toolPanel.add(redCheckBox);

        // Setup debug frame
        debugFrame = new DebugFrame(textArea);
        defaultDocument.addDocumentListener(debugFrame);
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
        // "Debug Menu" options
        else if (e.getSource() == debugMenu) {
            debugFrame.setVisible(true);
        }
        // "Help Menu" options
        else if (e.getSource() == aboutMenu) {
            JOptionPane.showMessageDialog(this, "JaWa Editor v1.0");
        }
    }
}
