package org.xjtujavacourse.client;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import javax.swing.text.AttributeSet;
import javax.swing.text.Document;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledEditorKit;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class EditorFrame extends JFrame implements ActionListener, MouseListener {
    // Menu and menu options
    private JMenuBar menuBar;

    private JMenu fileMenu;
    private JMenuItem newFileMenu, openFileMenu, saveFileMenu, saveAsFileMenu;

    private JMenu helpMenu;
    private JMenuItem debugMenu, aboutMenu;

    private JMenu editMenu;
    private JMenuItem findMenu, findAndReplaceMenu;

    // Find and replace utils
    private FindAndReplacePanel findAndReplaceFrame;
    private Document documentBackup;
    private Document nowDocument;

    // Editor container and style tools
    private JTextPane textArea;
    private JPanel toolPanel;
    private TextStyleCheckbox boldCheckBox, italicCheckBox;
    private ColorComboBox textColorCombo;
    private HTMLEditorKit textKit;

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

        editMenu = new JMenu("Edit");
        findMenu = new JMenuItem("Find");
        findMenu.addActionListener(this);
        findAndReplaceMenu = new JMenuItem("Replace");
        findAndReplaceMenu.addActionListener(this);
        editMenu.add(findMenu);
        editMenu.add(findAndReplaceMenu);

        menuBar = new JMenuBar();
        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(helpMenu);
        this.setJMenuBar(menuBar);


        // Setup core UI components
        BorderLayout layout = new BorderLayout();
        this.setLayout(layout);

        textKit = new HTMLEditorKit();
        nowDocument = textKit.createDefaultDocument();
        textArea = new JTextPane();
        textArea.setEditorKit(textKit);
        textArea.addMouseListener(this);
        textArea.setDocument(nowDocument);
        JScrollPane scroll = new JScrollPane(textArea);
        this.add(scroll, BorderLayout.CENTER);

        textArea.setContentType("text/html");

        toolPanel = new JPanel();
        this.add(toolPanel, BorderLayout.NORTH);
        boldCheckBox =
                new TextStyleCheckbox("Bold", false, new StyledEditorKit.BoldAction());
        italicCheckBox =
                new TextStyleCheckbox("Italic", false, new StyledEditorKit.ItalicAction());

        textColorCombo = new ColorComboBox(
                new Action[] {
                        new StyledEditorKit.ForegroundAction("BLACK", Color.BLACK),
                        new StyledEditorKit.ForegroundAction("DARK_GRAY", Color.DARK_GRAY),
                        new StyledEditorKit.ForegroundAction("LIGHT_GRAY", Color.LIGHT_GRAY),
                        new StyledEditorKit.ForegroundAction("RED", Color.RED),
                        new StyledEditorKit.ForegroundAction("GREEN", Color.GREEN),
                        new StyledEditorKit.ForegroundAction("BLUE", Color.BLUE),
                        new StyledEditorKit.ForegroundAction("GRAY", Color.GRAY),
                        new StyledEditorKit.ForegroundAction("CYAN", Color.CYAN),
                        new StyledEditorKit.ForegroundAction("MAGENTA", Color.MAGENTA),
                        new StyledEditorKit.ForegroundAction("ORANGE", Color.ORANGE),
                        new StyledEditorKit.ForegroundAction("PINK", Color.PINK),
                        new StyledEditorKit.ForegroundAction("YELLOW", Color.YELLOW)
                }
        );
        toolPanel.add(boldCheckBox);
        toolPanel.add(italicCheckBox);
        toolPanel.add(textColorCombo);

        // Setup debug frame
        debugFrame = new DebugFrame(textArea);
        nowDocument.addDocumentListener(debugFrame);
    }

    private void refreshStyleToolStatus() {
        AttributeSet attributeSet = textKit.getInputAttributes();
        if (!StyleConstants.getForeground(attributeSet).equals(textColorCombo.getSelectedColor())) {
            textColorCombo.setSelectedColor(StyleConstants.getForeground(attributeSet));
        }
        boldCheckBox.setSelected(StyleConstants.isBold(attributeSet));
        italicCheckBox.setSelected(StyleConstants.isItalic(attributeSet));
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        refreshStyleToolStatus();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        refreshStyleToolStatus();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        refreshStyleToolStatus();
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        refreshStyleToolStatus();
    }

    @Override
    public void mouseExited(MouseEvent e) {
        refreshStyleToolStatus();
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

    private ArrayList<ArrayList<Integer>> findInText(String pattern) {
        ArrayList result = new ArrayList();
        int indFound = 0;
        while ((indFound = textArea.getText().indexOf(pattern, indFound)) != -1) {
            ArrayList<Integer> a = new ArrayList<Integer>();
            a.add(indFound);
            a.add(pattern.length());
            result.add(a);
            indFound += pattern.length();
        }
        return result;
    }

    private void showFoundText(String pattern) {
        ArrayList foundStrings = findInText(pattern);
        for (ArrayList<Integer> found : findInText(pattern)) {
            System.err.println(found);
            textArea.setSelectionStart(found.get(0));
            textArea.setSelectionEnd(found.get(1));
            textArea.setSelectedTextColor(Color.MAGENTA);
        }
    }

    private void replaceFoundText() {

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // "Debug Menu" options
        if (e.getSource() == debugMenu) {
            debugFrame.setVisible(true);
        }
        // "Edit Menu" options
        else if (e.getSource() == findMenu) {
            findAndReplaceFrame = new FindAndReplacePanel(FindAndReplacePanel.FIND, this);
            this.add(findAndReplaceFrame, BorderLayout.SOUTH);
            textArea.setEditable(false);
            this.revalidate();
        }
        else if (e.getSource() == findAndReplaceMenu) {
            findAndReplaceFrame = new FindAndReplacePanel(FindAndReplacePanel.REPLACE, this);
            this.add(findAndReplaceFrame, BorderLayout.SOUTH);
            textArea.setEditable(false);
            this.revalidate();
        }
        // "Find and Replace Panel" options
        else if (e.getSource() == findAndReplaceFrame.exitButton) {
            this.remove(findAndReplaceFrame);
            textArea.setEditable(true);
            this.revalidate();
        }
        else if (e.getSource() == findAndReplaceFrame.findNextButton) {

        }
        else if (e.getSource() == findAndReplaceFrame.replaceNextButton) {

        }
        else if (e.getSource() == findAndReplaceFrame.replaceAllButton) {

        }
        else if (e.getSource() == findAndReplaceFrame.findField) {
            showFoundText(findAndReplaceFrame.findField.getText());
        }
        // "Help Menu" options
        else if (e.getSource() == aboutMenu) {
            JOptionPane.showMessageDialog(this, "JaWa Editor v1.0");
        }
    }
}
