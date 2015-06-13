package com.github.yihtserns.test.swing;

import com.github.yihtserns.test.swing.bean.Bean;
import com.github.yihtserns.test.swing.bean.MyBean;
import com.github.yihtserns.test.swing.bean.MyBeanPropertiesResolver;
import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.adapter.Bindings;
import com.jgoodies.binding.list.SelectionInList;
import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author yihtserns
 */
public class Main {

    public static void main(String[] args) throws Exception {
        Bean bean = new MyBean();
        bean.setOption(Bean.Option.Second);
        PropertiesResolver propsResolver = new MyBeanPropertiesResolver();
        final PresentationModel pm = new PresentationModel(bean);

        JPanel panel = new JPanel();
        PropertiesLayout layout = new PropertiesLayout(panel);
        panel.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        Map<String, UiControl> propertyName2UiControl = new LinkedHashMap<>();
        propertyName2UiControl.put(
                Bean.Property.option.name(),
                forEnum(Bean.Property.option, Bean.Option.class));
        propertyName2UiControl.put(
                Bean.Property.checkedOption.name(),
                forBoolean(Bean.Property.checkedOption));
        propertyName2UiControl.put(
                Bean.Property.url.name(),
                forString(Bean.Property.url));
        propertyName2UiControl.put(
                Bean.Property.url2.name(),
                forString(Bean.Property.url2));
        propertyName2UiControl.put(
                Bean.Property.url3.name(),
                forString(Bean.Property.url3));

        for (UiControl uiObject : propertyName2UiControl.values()) {
            uiObject.addTo(layout);
            uiObject.bindTo(pm);
        }

        BeanInfo beanInfo = Introspector.getBeanInfo(Bean.class);
        List<String> allProps = Arrays.asList(beanInfo.getPropertyDescriptors())
                .stream()
                .map((PropertyDescriptor desc) -> desc.getName())
                .collect(Collectors.toList());

        PropertyChangeListener updateUiControls = (PropertyChangeEvent evt) -> {
            List<String> relevantProps = propsResolver.resolveProperties(pm)
                    .stream()
                    .map((Property prop) -> prop.name())
                    .collect(Collectors.toList());

            List<String> irrelevantProps = new ArrayList(allProps);
            irrelevantProps.removeAll(relevantProps);

            for (String prop : relevantProps) {
                propertyName2UiControl.get(prop).setVisible(true);
            }

            for (String prop : irrelevantProps) {
                propertyName2UiControl.get(prop).setVisible(false);
            }
        };

        pm.addBeanPropertyChangeListener(Bean.Property.option.name(), updateUiControls);
        pm.addBeanPropertyChangeListener(Bean.Property.checkedOption.name(), updateUiControls);

        updateUiControls.propertyChange(null);
        JOptionPane.showConfirmDialog(null, panel);
    }

    private static UiControl forString(final Property property) {
        final JLabel label = new JLabel(property.displayName() + ":");
        final JTextField textField = new JTextField();
        return new UiControl() {

            @Override
            public void addTo(PropertiesLayout layout) {
                layout.addProperty(label, textField);
            }

            @Override
            public void setVisible(boolean visibility) {
                label.setVisible(visibility);
                textField.setVisible(visibility);
            }

            @Override
            public void bindTo(PresentationModel pm) {
                Bindings.bind(textField, pm.getModel(property.name()));
            }
        };
    }

    private static UiControl forBoolean(final Property property) {
        final JLabel label = new JLabel(property.displayName() + ":");
        final JCheckBox checkBox = new JCheckBox();
        return new UiControl() {

            @Override
            public void addTo(PropertiesLayout layout) {
                layout.addProperty(label, checkBox);
            }

            @Override
            public void setVisible(boolean visibility) {
                label.setVisible(visibility);
                checkBox.setVisible(visibility);
            }

            @Override
            public void bindTo(PresentationModel pm) {
                Bindings.bind(checkBox, pm.getModel(property.name()));
            }
        };
    }

    private static UiControl forEnum(final Property property, final Class<? extends Enum> enumClass) {
        final JLabel label = new JLabel(property.displayName() + ":");
        final JComboBox comboBox = new JComboBox();
        return new UiControl() {

            @Override
            public void addTo(PropertiesLayout layout) {
                layout.addProperty(label, comboBox);
            }

            @Override
            public void setVisible(boolean visibility) {
                label.setVisible(visibility);
                comboBox.setVisible(visibility);
            }

            @Override
            public void bindTo(PresentationModel pm) {
                SelectionInList selectionInList = new SelectionInList(
                        enumClass.getEnumConstants(),
                        pm.getModel(property.name()));
                Bindings.bind(comboBox, selectionInList);
            }
        };
    }

    private interface UiControl {

        void addTo(PropertiesLayout layout);

        void setVisible(boolean visibility);

        void bindTo(PresentationModel pm);
    }
}
