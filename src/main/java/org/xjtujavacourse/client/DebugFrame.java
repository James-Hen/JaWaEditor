package org.xjtujavacourse.client;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;
import javax.swing.text.html.HTMLEditorKit;
import java.io.StringWriter;

public class DebugFrame extends JFrame implements DocumentListener {
    private JTextArea text;
    private JTextPane textPane;

    DebugFrame(JTextPane textPane) {
        this.textPane = textPane;
        this.setSize(800, 600);
        this.setTitle("Debug");
        text = new JTextArea();
        JScrollPane scroll = new JScrollPane(text);
        this.add(scroll);
        this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
    }


    @Override
    public void insertUpdate(DocumentEvent e) { onChange(e); }

    @Override
    public void removeUpdate(DocumentEvent e) { onChange(e); }

    @Override
    public void changedUpdate(DocumentEvent e) { onChange(e); }

    private void onChange(DocumentEvent e) {
        this.text.setText(textPane.getText());
    }
}
