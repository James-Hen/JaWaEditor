package org.xjtujavacourse.client;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import javax.swing.text.Document;
import javax.swing.text.StyledEditorKit;
import javax.swing.text.html.HTMLEditorKit;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class EditorFrame extends JFrame implements ActionListener {
    private JMenuBar menuBar;

    private JMenu fileMenu;
    private JMenuItem newFileMenu, openFileMenu, saveFileMenu, saveAsFileMenu;

    private JMenu helpMenu;
    private JMenuItem debugMenu, aboutMenu;

    private JTextPane textArea;
    private DebugFrame debugFrame;

    private File documentFile;

    EditorFrame() {
        this.setSize(800, 600);
        this.setTitle("JaWa Editor");

        // Setup menu
        fileMenu = new JMenu("File");
        FileMenuListener fileMenuListener = new FileMenuListener();
        newFileMenu = new JMenuItem("New");
        newFileMenu.addActionListener(fileMenuListener);
        openFileMenu = new JMenuItem("Open");
        openFileMenu.addActionListener(fileMenuListener);
        saveFileMenu = new JMenuItem("Save");
        saveFileMenu.addActionListener(fileMenuListener);
        saveAsFileMenu = new JMenuItem("Save as");
        saveAsFileMenu.addActionListener(fileMenuListener);
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
        TextStyleCheckbox boldCheckBox =
                new TextStyleCheckbox("Bold", false, new StyledEditorKit.BoldAction());
        TextStyleCheckbox italicCheckBox =
                new TextStyleCheckbox("Italic", false, new StyledEditorKit.ItalicAction());
        ColorComboBox textColorCombo = new ColorComboBox(
                new Action[] {
                        new StyledEditorKit.ForegroundAction("Black", Color.BLACK),
                        new StyledEditorKit.ForegroundAction("Red", Color.RED),
                        new StyledEditorKit.ForegroundAction("Green", Color.GREEN),
                        new StyledEditorKit.ForegroundAction("Blue", Color.BLUE)
                }
        );
        toolPanel.add(boldCheckBox);
        toolPanel.add(italicCheckBox);
        toolPanel.add(textColorCombo);

        // Setup debug frame
        debugFrame = new DebugFrame(textArea);
        defaultDocument.addDocumentListener(debugFrame);
    }

    private class FileMenuListener implements ActionListener {
        JFileChooser fileChooser;
        FileSystemView fsv;
        FileMenuListener() {
            fileChooser = new JFileChooser();
            fsv = FileSystemView.getFileSystemView();
            fileChooser.setCurrentDirectory(fsv.getHomeDirectory());
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            int result = fileChooser.showOpenDialog(EditorFrame.this);
            if (result == JFileChooser.APPROVE_OPTION) {
                documentFile = fileChooser.getSelectedFile();
                System.err.println("path: " + documentFile);
            }
            else {
                return;
            }
            // "File Menu" options
            if (e.getSource() == newFileMenu) {

            }
            else if (e.getSource() == openFileMenu) {

            }
            else if (e.getSource() == saveFileMenu) {

            }
            else if (e.getSource() == saveAsFileMenu) {

            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // "Debug Menu" options
        if (e.getSource() == debugMenu) {
            debugFrame.setVisible(true);
        }
        // "Help Menu" options
        else if (e.getSource() == aboutMenu) {
            JOptionPane.showMessageDialog(this, "JaWa Editor v1.0");
        }
    }
}
