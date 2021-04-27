package org.xjtujavacourse.client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.HashMap;

public class SizeComboBox extends JComboBox<String> implements ItemListener {
    private HashMap<String, Action> sizeActionMap;
    SizeComboBox(Action[] actions) {
        super();
        sizeActionMap = new HashMap<String, Action>();
        for (Action act : actions) {
            String str = act.getValue(Action.NAME).toString();
            addItem(str);
            sizeActionMap.put(str, act);
        }
        addItemListener(this);
    }

    public int getSelectedSize() {
        return Integer.parseInt(getSelectedItem().toString());
    }

    public void setSelectedSize(int c) {
        setSelectedItem(String.valueOf(c));
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getStateChange() == ItemEvent.SELECTED) {
            sizeActionMap.get(e.getItem().toString()).actionPerformed(
                    new ActionEvent(e.getSource(), e.getID(), e.getItem().toString())
            );
        }
    }
}
