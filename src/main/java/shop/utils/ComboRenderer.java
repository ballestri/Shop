package shop.utils;

import javax.swing.*;
import java.awt.*;

@SuppressWarnings("serial")
public class ComboRenderer extends DefaultListCellRenderer {
    public static final Color background = new Color(46, 134, 193);
    private static final Color defaultBackground = new Color(128, 0, 128);
    private static final Color defaultForeground = Color.WHITE;

    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
                                                  boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        setHorizontalAlignment(CENTER);

        String emp = (String) value;
        if (emp == null) {
            return this;
        }
        String text = getProcessDisplayText(emp);
        this.setText(text);
        if (!isSelected) {
            this.setBackground(index % 2 == 0 ? background : defaultBackground);
        }

        this.setForeground(defaultForeground);

        return this;
    }

    public static String getProcessDisplayText(String video) {
        if (video == null) {
            return "";
        }
        return String.format("%s", video);
    }

}