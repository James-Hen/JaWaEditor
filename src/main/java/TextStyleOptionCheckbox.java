import javax.swing.*;

public class TextStyleOptionCheckbox extends JCheckBox {
    TextStyleOptionCheckbox(String text, boolean selected, Action act) {
        super(text, selected);
        super.addActionListener(act);
    }
}
