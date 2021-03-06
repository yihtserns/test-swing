package com.github.yihtserns.test.swing;

import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.adapter.Bindings;
import com.jgoodies.binding.list.SelectionInList;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTextField;

/**
 *
 * @author yihtserns
 */
abstract class UiControl {

    private JLabel label;
    private JComponent value;

    public UiControl(JLabel label, JComponent value) {
        this.label = label;
        this.value = value;
    }

    public void addTo(PropertiesLayout layout) {
        layout.addProperty(label, value);
    }

    public void setVisible(boolean visibility) {
        label.setVisible(visibility);
        value.setVisible(visibility);
    }

    public abstract void bindTo(PresentationModel pm);

    public abstract void onChange(ChangeHandler changeHandler);

    public static UiControl createFor(Property property) {
        Class<?> propertyType = property.type();
        if (propertyType.isEnum()) {
            return UiControl.forEnum(property);
        } else if (propertyType == Boolean.class || propertyType == boolean.class) {
            return UiControl.forBoolean(property);
        } else if (propertyType == String.class) {
            return UiControl.forString(property);
        } else {
            String msg = String.format("Unsupported type '%s' for property '%s'", propertyType, property.name());
            throw new UnsupportedOperationException(msg);
        }
    }

    private static UiControl forEnum(final Property property) {
        final JLabel label = new JLabel(property.displayName() + ":");
        final JComboBox comboBox = new JComboBox();
        return new UiControl(label, comboBox) {
            @Override
            public void bindTo(PresentationModel pm) {
                SelectionInList selectionInList = new SelectionInList(
                        property.type().getEnumConstants(),
                        pm.getModel(property.name()));
                Bindings.bind(comboBox, selectionInList);
            }

            @Override
            public void onChange(ChangeHandler changeHandler) {
                comboBox.addItemListener((evt) -> changeHandler.handle(new ChangeEvent(property)));
            }
        };
    }

    private static UiControl forString(final Property property) {
        final JLabel label = new JLabel(property.displayName() + ":");
        final JTextField textField = new JTextField();
        return new UiControl(label, textField) {
            @Override
            public void bindTo(PresentationModel pm) {
                Bindings.bind(textField, pm.getModel(property.name()));
            }

            @Override
            public void onChange(ChangeHandler changeHandler) {
                textField.addKeyListener(new KeyAdapter() {

                    @Override
                    public void keyTyped(KeyEvent ke) {
                        changeHandler.handle(new ChangeEvent(property));
                    }
                });
            }
        };
    }

    private static UiControl forBoolean(final Property property) {
        final JLabel label = new JLabel(property.displayName() + ":");
        final JCheckBox checkBox = new JCheckBox();
        return new UiControl(label, checkBox) {
            @Override
            public void bindTo(PresentationModel pm) {
                Bindings.bind(checkBox, pm.getModel(property.name()));
            }

            @Override
            public void onChange(ChangeHandler changeHandler) {
                checkBox.addItemListener((evt) -> changeHandler.handle(new ChangeEvent(property)));
            }
        };
    }

    interface ChangeHandler {

        void handle(ChangeEvent evt);
    }

    public static class ChangeEvent {

        public final Object source;

        public ChangeEvent(Object source) {
            this.source = source;
        }
    }
}
