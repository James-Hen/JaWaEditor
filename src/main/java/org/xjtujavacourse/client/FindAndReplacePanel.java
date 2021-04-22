package org.xjtujavacourse.client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class FindAndReplacePanel extends JPanel {
    public static final int FIND = 0, REPLACE = 1;

    public JTextField findField, replaceField;

    public JButton findNextButton, replaceAllButton, replaceNextButton, exitButton;

    private int thisFunctionType;

    private ActionListener parentListener;

    FindAndReplacePanel(int functionType, ActionListener actionlistener) {
        super();
        thisFunctionType = functionType;
        parentListener = actionlistener;

        GridLayout layout = new GridLayout(functionType == FIND ? 2 : 3, 0, 1, 1);
        this.setLayout(layout);
        findField = new JTextField();
        findField.addActionListener(parentListener);
        findField.addFocusListener(new JTextFieldHintListener(findField, "Find"));
        this.add(findField);

        JPanel optionButtonsPanel = new JPanel();

        findNextButton = new JButton("Find Next");
        findNextButton.addActionListener(parentListener);
        optionButtonsPanel.add(findNextButton);
        if (thisFunctionType == REPLACE) {
            replaceField = new JTextField();
            replaceField.addActionListener(parentListener);
            replaceField.addFocusListener(new JTextFieldHintListener(replaceField, "Replace"));
            this.add(replaceField);

            replaceAllButton = new JButton("Replace All");
            replaceAllButton.addActionListener(parentListener);
            optionButtonsPanel.add(replaceAllButton);
            replaceNextButton = new JButton("Replace Next");
            replaceNextButton.addActionListener(parentListener);
            optionButtonsPanel.add(replaceNextButton);
        }

        exitButton = new JButton("Finish");
        exitButton.addActionListener(parentListener);
        optionButtonsPanel.add(exitButton);
        this.add(optionButtonsPanel);
    }

    private class JTextFieldHintListener implements FocusListener {
        private String hintText;
        private JTextField textField;
        public JTextFieldHintListener(JTextField jTextField,String hintText) {
            this.textField = jTextField;
            this.hintText = hintText;
            jTextField.setText(hintText);
            jTextField.setForeground(Color.GRAY);
        }

        @Override
        public void focusGained(FocusEvent e) {
            String temp = textField.getText();
            if(temp.equals(hintText)) {
                textField.setText("");
                textField.setForeground(Color.BLACK);
            }
        }

        @Override
        public void focusLost(FocusEvent e) {
            String temp = textField.getText();
            if(temp.equals("")) {
                textField.setForeground(Color.GRAY);
                textField.setText(hintText);
            }
        }
    }
}
