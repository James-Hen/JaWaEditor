package org.xjtujavacourse.client;

import javax.swing.*;

public class TextStyleCheckbox extends JCheckBox {
    TextStyleCheckbox(String text, boolean selected, Action act) {
        super(text, selected);
        super.addActionListener(act);
    }
}
