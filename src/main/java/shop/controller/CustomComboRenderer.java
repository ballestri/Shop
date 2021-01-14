package shop.controller;

import javax.swing.*;
import java.awt.*;

public class CustomComboRenderer extends DefaultListCellRenderer {
    public static final Color background =new Color(46, 134, 193);
    private static final Color defaultBackground = new Color( 128, 0, 128);
    private static final Color defaultForeground = Color.WHITE;

    private final JLabel searchLabel;

    public CustomComboRenderer(JLabel filterLabel) {
        this.searchLabel = filterLabel;
    }


    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                  boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        
		setOpaque(true);

        String emp = (String) value;
        if (emp == null) {
            return this;
        }
        String text = getProcessDisplayText(emp);
        text = HtmlHighlighter.highlightText(text, searchLabel.getText());
        this.setText(text);
        if (!isSelected) {
            this.setBackground(index % 2 == 0 ? background : defaultBackground);
        }
        
        this.setForeground(defaultForeground);
        
        return this;
    }

    public static String getProcessDisplayText(String country) {
        if (country == null) {
            return "";
        }
        return String.format("%s", country);
    }
}