package shop.controller;

import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.function.BiPredicate;

public class ComboBoxFilterDecorator<T> {
    private Popup filterPopup;
    private JLabel filterLabel;
    private JComboBox<T> comboBox;
    private BiPredicate<T, String> userFilter;
    java.util.List<T> items;
    private TextHandler textHandler = new TextHandler();
    private Object selectedItem;

    public ComboBoxFilterDecorator(JComboBox<T> comboBox, BiPredicate<T, String> userFilter) {
        this.comboBox = comboBox;
        this.userFilter = userFilter;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
	public static <T> ComboBoxFilterDecorator<T> decorate(JComboBox<T> comboBox,
                                                          BiPredicate<T, String> userFilter) {
        ComboBoxFilterDecorator decorator = new ComboBoxFilterDecorator(comboBox, userFilter);
        decorator.init();
        return decorator;
    }

    private void init() {
    	
        prepareComboFiltering();
        initFilterLabel();
        initComboPopupListener();
        initComboKeyListener();
    }

    private void prepareComboFiltering() {
        DefaultComboBoxModel<T> model = (DefaultComboBoxModel<T>) comboBox.getModel();
        items = new ArrayList<>();
        for (int i = 0; i < model.getSize(); i++) {
            items.add(model.getElementAt(i));
        }

    }

    private void initComboKeyListener() {
        comboBox.addKeyListener(
                new KeyAdapter() {
                    @Override
                    public void keyPressed(KeyEvent e) {
                        char keyChar = e.getKeyChar();
                        if (!Character.isDefined(keyChar)) {
                            return;
                        }
                        int keyCode = e.getKeyCode();
                        switch (keyCode) {
                            case KeyEvent.VK_DELETE:
                                return;
                            case KeyEvent.VK_ENTER:
                                resetFilterPopup();
                                return;
                            case KeyEvent.VK_ESCAPE:
                                if (selectedItem != null) {
                                    comboBox.setSelectedItem(selectedItem);
                                }
                                resetFilterPopup();
                                return;
                            case KeyEvent.VK_BACK_SPACE:
                                textHandler.removeCharAtEnd();
                                break;
                            default:
                                textHandler.add(keyChar);
                        }

                        if (!comboBox.isPopupVisible()) {
                            comboBox.showPopup();
                        }

                        if (!textHandler.text.isEmpty()) {
                            showFilterPopup();
                            performFilter();
                        } else {
                            resetFilterPopup();
                        }
                        e.consume();
                    }
                }
        );
    }

    private void initFilterLabel() {
        filterLabel = new JLabel();
        filterLabel.setOpaque(true);
        filterLabel.setBackground(new Color(255, 248, 220));
        filterLabel.setFont(filterLabel.getFont().deriveFont(Font.PLAIN));
        filterLabel.setBorder(BorderFactory.createLineBorder(Color.gray));
    }

    public JLabel getFilterLabel() {
        return filterLabel;
    }

    private void initComboPopupListener() {
        comboBox.addPopupMenuListener(new PopupMenuListener() {
            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
            }

            @Override
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
                resetFilterPopup();
            }

            @Override
            public void popupMenuCanceled(PopupMenuEvent e) {
                resetFilterPopup();
            }
        });
    }

    private void showFilterPopup() {
        if (textHandler.getText().isEmpty()) {
            return;
        }
        if (filterPopup == null) {
            Point p = new Point(0, 0);
            SwingUtilities.convertPointToScreen(p, comboBox);
            Dimension comboSize = comboBox.getPreferredSize();
            filterLabel.setPreferredSize(new Dimension(comboSize));
            filterPopup = PopupFactory.getSharedInstance().getPopup(comboBox, filterLabel, p.x,
                    p.y - filterLabel.getPreferredSize().height);
            selectedItem = comboBox.getSelectedItem();

        }
        filterPopup.show();
    }

    private void resetFilterPopup() {
        if (!textHandler.isEditing()) {
            return;
        }
        if (filterPopup != null) {
            filterPopup.hide();
            filterPopup = null;
            filterLabel.setText("");
            textHandler.reset();

            //add items in the original order
            Object selectedItem = comboBox.getSelectedItem();
            DefaultComboBoxModel<T> model = (DefaultComboBoxModel<T>) comboBox.getModel();
            model.removeAllElements();
            for (T item : items) {
                model.addElement(item);
            }
            //preserve the selection
            model.setSelectedItem(selectedItem);
            this.selectedItem = selectedItem;
        }
    }

    private void performFilter() {
        filterLabel.setText(textHandler.getText());
        //System.out.println("'" + textHandler.getText() + "'");
        DefaultComboBoxModel<T> model = (DefaultComboBoxModel<T>) comboBox.getModel();
        model.removeAllElements();
        java.util.List<T> filteredItems = new ArrayList<>();
        //add matched items first
        for (T item : items) {
            if (userFilter.test(item, textHandler.getText())) {
                model.addElement(item);
            } else {
                filteredItems.add(item);
            }
        }
        if (model.getSize() == 0) {
            //if no match then red font
            filterLabel.setForeground(Color.red);
        } else {
            filterLabel.setForeground(Color.blue);
        }
        //add unmatched items
        filteredItems.forEach(model::addElement);
    }

    private static class TextHandler {
        private String text = "";
        private boolean editing;

        public void add(char c) {
            text += c;
            editing = true;
        }

        public void removeCharAtEnd() {
            if (text.length() > 0) {
                text = text.substring(0, text.length() - 1);
                editing = true;
            }
        }

        public void reset() {
            text = "";
            editing = false;
        }

        public String getText() {
            return text;
        }

        public boolean isEditing() {
            return editing;
        }
    }
}
