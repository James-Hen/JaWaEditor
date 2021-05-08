package org.xjtujavacourse.client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.HashMap;

public class ColorComboBox extends JComboBox<String> implements ItemListener {
    private HashMap<String, Action> colorActionMap;
    private HashMap<String, Color> stringColorMap;
    private HashMap<Color, String> colorStringMap;
    private static String intToHexValue(int number) {
        String result = Integer.toHexString(number & 0xff);
        while (result.length() < 2) {
            result = "0" + result;
        }
        return result.toUpperCase();
    }
    private static String colorToHexValue(Color color) {
        return "#" + intToHexValue(color.getRed()) + intToHexValue(color.getGreen()) + intToHexValue(color.getBlue());
    }
    ColorComboBox(Action[] actions) {
        super();
        colorActionMap = new HashMap<String, Action>();
        stringColorMap = new HashMap<String, Color>();
        colorStringMap = new HashMap<Color, String>();
        for (Action act : actions) {
            String str = act.getValue(Action.NAME).toString();
            Color actColor;
            try {
                actColor = (Color)Color.class.getField(str).get(Color.class);
            } catch (Exception e) {
                e.printStackTrace();
                actColor = Color.BLACK;
            }
            String showStr = "<html><font color=\"" + colorToHexValue(actColor) + "\">" + str + "<html>";
            addItem(showStr);
            colorActionMap.put(showStr, act);
            stringColorMap.put(showStr, actColor);
            colorStringMap.put(actColor, showStr);
        }
        addItemListener(this);
    }

    public Color getSelectedColor() {
        return stringColorMap.get(getSelectedItem().toString());
    }

    public void setSelectedColor(Color c) {
        setSelectedItem(colorStringMap.get(c));
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getStateChange() == ItemEvent.SELECTED) {
            colorActionMap.get(e.getItem().toString()).actionPerformed(
                    new ActionEvent(e.getSource(), e.getID(), e.getItem().toString())
            );
        }
    }
}
