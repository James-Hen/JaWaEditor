package org.xjtujavacourse.client;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.HashMap;

public class ColorComboBox extends JComboBox<String> implements ItemListener {
    HashMap<String, Action> colorActionMap;
    ColorComboBox() {
        super();
    }
    ColorComboBox(Action[] actions) {
        super();
        colorActionMap = new HashMap<String, Action>();
        for (Action act : actions) {
            String str = act.getValue(Action.NAME).toString();
            addItem(str);
            colorActionMap.put(str, act);
        }
        addItemListener(this);
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getStateChange() == ItemEvent.SELECTED) {
            System.err.println(e.getItem().toString());
            colorActionMap.get(e.getItem().toString()).actionPerformed(
                    new ActionEvent(e.getSource(), e.getID(), e.getItem().toString())
            );
        }
    }
}
