package com.github.yihtserns.test.swing;

import java.util.LinkedHashMap;
import java.util.Map.Entry;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author yihtserns
 */
public class Main {

    public static void main(String[] args) {
        Property2Value property2Value = new Property2Value();
        property2Value.putProperty(Property.create("option", Option.class, "Options"), Option.First);
        property2Value.putProperty(Property.create("checkedOption", Boolean.class, "Checked option"), Boolean.TRUE);
        property2Value.putProperty(Property.create("url", String.class, "URL"), null);

        JPanel panel = new JPanel();
        PropertiesLayout layout = new PropertiesLayout(panel);
        panel.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        for (Entry<Property, Object> entry : property2Value.entrySet()) {
            Property property = entry.getKey();
            Object value = entry.getValue();

            layout.addProperty(
                    new JLabel(property.displayName + ":"),
                    property.componentWithValue(value));
        }

        JOptionPane.showConfirmDialog(null, panel);
    }

    private static class Property<T> {

        public final String name;
        public final Class<T> type;
        public final String displayName;

        private Property(String name, Class<T> type, String displayName) {
            this.name = name;
            this.type = type;
            this.displayName = displayName;
        }

        public JComponent componentWithValue(T value) {
            if (type.isEnum()) {
                JComboBox comboBox = new JComboBox(type.getEnumConstants());
                comboBox.setSelectedItem(value);

                return comboBox;
            } else if (type == Boolean.class) {
                JCheckBox checkBox = new JCheckBox();
                checkBox.setSelected((Boolean) value);

                return checkBox;
            } else if (type == String.class) {
                return new JTextField((String) value);
            } else {
                throw new UnsupportedOperationException("Unknown type: " + type);
            }
        }

        public static <T> Property<T> create(String name, Class<T> type, String displayName) {
            return new Property<T>(name, type, displayName);
        }
    }

    private static class Property2Value extends LinkedHashMap<Property, Object> {

        public <T> void putProperty(Property<T> property, T value) {
            put(property, value);
        }
    }

    private enum Option {

        First,
        Second,
        Third
    }
}
