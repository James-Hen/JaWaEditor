package org.xjtujavacourse.client;

import javax.swing.*;
import javax.swing.text.StyledEditorKit;

public class ColorComboItem {
    Action itemSelectedListener;
    String text;
    ColorComboItem(Action act) {
        itemSelectedListener = act;
        text = (String)act.getValue(Action.NAME);
    }

    @Override
    public String toString() {
        return text;
    }
}
