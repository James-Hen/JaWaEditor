package org.xjtujavacourse.client;

import org.xjtujavacourse.common.Base64Serializer;
import org.xjtujavacourse.common.JaWaDocument;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileSystemView;
import javax.swing.text.*;
import javax.swing.text.html.HTMLEditorKit;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class EditorFrame extends JFrame implements ActionListener, MouseListener, DocumentListener {
    // Menu and menu options
    private JMenuBar menuBar;

    private JMenu fileMenu;
    private boolean isRemote;
    private JMenuItem newFileMenu, openRemoteMenu, openFileMenu, saveFileMenu;

    private JMenu helpMenu;
    private JMenuItem debugMenu, aboutMenu;

    private JMenu editMenu;
    private JMenuItem findMenu, findAndReplaceMenu;

    // Find and replace utils
    private FindAndReplacePanel findAndReplaceFrame;
    private Document nowDocument;

    // Editor container and style tools
    private JTextPane textArea;
    private JPanel toolPanel;
    private TextStyleCheckbox boldCheckBox, italicCheckBox, underlineCheckBox;
    private ColorComboBox textColorCombo;
    private SizeComboBox textSizeComboBox;
    private HTMLEditorKit textKit;

    private DebugFrame debugFrame;

    private File documentFile;
    private String serverHost;
    private int serverPort;
    private String docName;

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
        openRemoteMenu = new JMenuItem("Open Remote");
        openRemoteMenu.addActionListener(fileMenuListener);
        saveFileMenu = new JMenuItem("Save");
        saveFileMenu.addActionListener(fileMenuListener);
        saveFileMenu.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK));
        fileMenu.add(newFileMenu);
        fileMenu.add(openFileMenu);
        fileMenu.add(openRemoteMenu);
        fileMenu.add(saveFileMenu);

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
        findMenu.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, KeyEvent.CTRL_DOWN_MASK));
        findAndReplaceMenu = new JMenuItem("Replace");
        findAndReplaceMenu.addActionListener(this);
        findAndReplaceMenu.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, KeyEvent.CTRL_DOWN_MASK));
        editMenu.add(findMenu);
        editMenu.add(findAndReplaceMenu);
        isLocationsValid = false;

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
        nowDocument.addDocumentListener(this); // for updating find and replace
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
                new TextStyleCheckbox("<html><b>B </b><html>", false, new StyledEditorKit.BoldAction());
        italicCheckBox =
                new TextStyleCheckbox("<html><i>I </i><html>", false, new StyledEditorKit.ItalicAction());
        underlineCheckBox
                = new TextStyleCheckbox("<html><u>U</u> <html>", false, new StyledEditorKit.UnderlineAction());

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
        Action[] size_actions = new Action[30-5];
        for (int i = 6; i <= 30; i++) {
            size_actions[i-6] = new StyledEditorKit.FontSizeAction(String.valueOf(i), i);
        }
        textSizeComboBox = new SizeComboBox(size_actions);
        toolPanel.add(boldCheckBox);
        toolPanel.add(italicCheckBox);
        toolPanel.add(underlineCheckBox);
        JLabel colorLabel = new JLabel("    Color:");
        toolPanel.add(colorLabel);
        toolPanel.add(textColorCombo);
        JLabel sizeLabel = new JLabel("Size:");
        toolPanel.add(sizeLabel);
        toolPanel.add(textSizeComboBox);

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
        textSizeComboBox.setSelectedSize(StyleConstants.getFontSize(attributeSet));
        if (docName == null || docName.equals("")) {
            this.setTitle("JaWa Editor - " + "Default Document");
        }
        else {
            this.setTitle("JaWa Editor - " + docName);
        }
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
        // refreshStyleToolStatus();
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // refreshStyleToolStatus();
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        isLocationsValid = false;
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        isLocationsValid = false;
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        isLocationsValid = false;
    }

    private void documentFindingUpdated() {
        findInText(findAndReplaceFrame.findField.getText());
    }

    private class FileMenuListener implements ActionListener {
        JFileChooser fileChooser;
        FileSystemView fsv;
        FileMenuListener() {
            fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            fileChooser.setFileFilter(new FileFilter() {
                @Override
                public boolean accept(File f) {
                    String fName = f.getName();
                    int suffixPos = fName.lastIndexOf(".");
                    if (suffixPos == -1) {
                        return false;
                    }
                    return fName.substring(fName.lastIndexOf(".") + 1).equals("JaWa");
                }

                @Override
                public String getDescription() {
                    return "JaWa Document";
                }
            });
            fsv = FileSystemView.getFileSystemView();
            fileChooser.setCurrentDirectory(fsv.getHomeDirectory());
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            // "File Menu" options
            if (e.getSource() == newFileMenu) {
                isRemote = false;
                textArea.setText("");
            }
            else if (e.getSource() == openRemoteMenu) {
                isRemote = true;

                String s = JOptionPane.showInputDialog(EditorFrame.this, "Remote URL:");
                URI uri = null;
                try {
                    uri = new URI(s);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    return;
                }

                if (!uri.getScheme().equals("jawa"))
                    return;

                serverHost = uri.getHost();
                serverPort = uri.getPort();
                if (serverPort == -1)
                    serverPort = 8848;
                docName = uri.getPath().substring(1);

                System.out.println(serverHost + " " + serverPort + " " + docName);

                try {
                    URL url = new URL("http://" + serverHost + ":" + serverPort + "/download?" + docName);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setDoInput(true);
                    conn.connect();
                    int code = conn.getResponseCode();
                    System.out.println("Remote open response = " + code);
                    if (code == 200) {
                        InputStream in = conn.getInputStream();
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        byte[] buffer = new byte[1024];
                        int length = 0;
                        while ((length = in.read(buffer)) != -1) {
                            baos.write(buffer, 0, length);
                        }
                        JaWaDocument doc = (JaWaDocument) Base64Serializer.convertFromBytes(baos.toByteArray());
                        docName = doc.name;
                        textArea.setText(doc.content);
                    } else {
                        textArea.setText("");
                    }
                    conn.disconnect();
                } catch (IOException | ClassNotFoundException ex) {
                    ex.printStackTrace();
                }
            }
            else if (e.getSource() == openFileMenu) {
                isRemote = false;
                int result = fileChooser.showOpenDialog(EditorFrame.this);
                if (result == JFileChooser.APPROVE_OPTION) {
                    documentFile = fileChooser.getSelectedFile();
                    docName = documentFile.getName();
                    refreshStyleToolStatus();
                } else {
                    return;
                }

                try {
                    FileInputStream fis = new FileInputStream(documentFile);
                    byte[] arr = new byte[(int) documentFile.length()];
                    fis.read(arr);
                    fis.close();
                    textArea.setText(new String(arr, StandardCharsets.UTF_8));
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            else if (e.getSource() == saveFileMenu) {
                // TODO
                if (isRemote) {
                    URL url = null;

                    JaWaDocument doc = new JaWaDocument();
                    doc.prevVersion = "";
                    doc.name = docName;
                    doc.content = textArea.getText();

                    try {
                        url = new URL("http://" + serverHost + ":" + serverPort + "/upload");
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.setRequestMethod("POST");
                        conn.setDoOutput(true);
                        conn.connect();
                        OutputStream os = conn.getOutputStream();
                        os.write(Base64Serializer.convertToBytes(doc));
                        os.flush();
                        os.close();

                        System.out.println("Remote save response = " + conn.getResponseCode());
                        conn.disconnect();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }

                } else {
                    int result = fileChooser.showSaveDialog(EditorFrame.this);
                    if (result == JFileChooser.APPROVE_OPTION) {
                        documentFile = fileChooser.getSelectedFile();
                        docName = documentFile.getName();
                        refreshStyleToolStatus();
                    } else {
                        return;
                    }

                    try {
                        FileOutputStream fos = new FileOutputStream(documentFile);
                        fos.write(textArea.getText().getBytes(StandardCharsets.UTF_8));
                        fos.close();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }
    }

    boolean isLocationsValid;
    private ArrayList<ArrayList<Integer>> foundTextLocations;
    int nowIndicateFoundTextArrayIndex, nowReplacedIncrement;

    private synchronized void findInText(String pattern) {
        int startOffset = nowDocument.getDefaultRootElement().getStartOffset();
        int length = nowDocument.getDefaultRootElement().getEndOffset() - startOffset;
        String text = "";
        try {
            text = nowDocument.getText(startOffset, length);
        } catch (Exception e) {
            e.printStackTrace();
        }
        foundTextLocations = new ArrayList<ArrayList<Integer>>();
        nowIndicateFoundTextArrayIndex = -1;
        nowReplacedIncrement = 0;
        int indFound = 0;
        while ((indFound = text.indexOf(pattern, indFound)) != -1) {
            ArrayList<Integer> a = new ArrayList<Integer>();
            a.add(indFound + startOffset);
            a.add(indFound + startOffset + pattern.length());
            foundTextLocations.add(a);
            indFound += pattern.length();
        }
        isLocationsValid = true;
    }

    private void showNextFoundText() {
        showFoundText();
        nowIndicateFoundTextArrayIndex += 1;
        if (nowIndicateFoundTextArrayIndex == foundTextLocations.size()) {
            nowIndicateFoundTextArrayIndex = 0;
        }
    }

    private void showFoundText() {
        if (nowIndicateFoundTextArrayIndex == -1) {
            nowIndicateFoundTextArrayIndex = 0;
        }
        ArrayList<Integer> found = foundTextLocations.get(nowIndicateFoundTextArrayIndex);
        textArea.requestFocusInWindow();
        textArea.select(found.get(0) + nowReplacedIncrement, found.get(1) + nowReplacedIncrement);
    }

    private boolean replaceFoundTextNext() {
        if (foundTextLocations.isEmpty()) {
            return false;
        }
        if (nowIndicateFoundTextArrayIndex == -1) {
            nowIndicateFoundTextArrayIndex = 1;
        }
        ArrayList<Integer> found = foundTextLocations.get(nowIndicateFoundTextArrayIndex == 0 ? foundTextLocations.size() - 1 : nowIndicateFoundTextArrayIndex - 1);
        textArea.requestFocusInWindow();
        textArea.select(found.get(0) + nowReplacedIncrement, found.get(1) + nowReplacedIncrement);
        String altText = findAndReplaceFrame.replaceField.getText();
        textArea.replaceSelection(altText);
        nowReplacedIncrement += altText.length() - findAndReplaceFrame.findField.getText().length();
        showNextFoundText();
        isLocationsValid = true;
        return true;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // "Debug Menu" options
        if (e.getSource() == debugMenu) {
            debugFrame.setVisible(true);
        }
        // "Edit Menu" options
        else if (e.getSource() == findMenu) {
            if (findAndReplaceFrame != null) {
                findAndReplaceFrame.setVisible(false);
                findAndReplaceFrame = null;
            }
            findAndReplaceFrame = new FindAndReplacePanel(FindAndReplacePanel.FIND, this);
            findAndReplaceFrame.findField.getDocument().addDocumentListener(this);
            this.add(findAndReplaceFrame, BorderLayout.SOUTH);
            this.revalidate();
        }
        else if (findAndReplaceFrame != null && e.getSource() == findAndReplaceMenu) {
            if (findAndReplaceFrame != null) {
                findAndReplaceFrame.setVisible(false);
                findAndReplaceFrame = null;
            }
            findAndReplaceFrame = new FindAndReplacePanel(FindAndReplacePanel.REPLACE, this);
            findAndReplaceFrame.findField.getDocument().addDocumentListener(this);
            this.add(findAndReplaceFrame, BorderLayout.SOUTH);
            this.revalidate();
        }
        // "Find and Replace Panel" options
        else if (findAndReplaceFrame != null && e.getSource() == findAndReplaceFrame.exitButton) {
            this.remove(findAndReplaceFrame);
            this.revalidate();
        }
        else if (findAndReplaceFrame != null && e.getSource() == findAndReplaceFrame.findNextButton) {
            if (!isLocationsValid) {
                documentFindingUpdated();
            }
            showNextFoundText();
        }
        else if (findAndReplaceFrame != null && e.getSource() == findAndReplaceFrame.replaceNextButton) {
            if (!isLocationsValid) {
                documentFindingUpdated();
            }
            replaceFoundTextNext();
        }
        else if (findAndReplaceFrame != null && e.getSource() == findAndReplaceFrame.replaceAllButton) {
            while (replaceFoundTextNext());
        }
        // "Help Menu" options
        else if (e.getSource() == aboutMenu) {
            JOptionPane.showMessageDialog(this, "JaWa Editor v1.0");
        }
    }
}
