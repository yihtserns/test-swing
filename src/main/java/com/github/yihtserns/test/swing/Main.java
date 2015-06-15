package com.github.yihtserns.test.swing;

import com.github.yihtserns.test.swing.bean.Bean;
import com.github.yihtserns.test.swing.bean.MyBean;
import com.github.yihtserns.test.swing.bean.MyBeanPropertiesResolver;
import com.jgoodies.binding.PresentationModel;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author yihtserns
 */
public class Main {

    public static void main(String[] args) throws Exception {
        Bean bean = BeanProxy.proxyFor(new MyBean());
        show(bean);
        print(bean);
    }

    private static void show(Bean bean) throws Exception {
        PropertiesResolver propsResolver = new MyBeanPropertiesResolver();
        List<Property> allProps = propsResolver.resolveAllProperties();

        Map<Property, UiControl> property2UiControl = new LinkedHashMap<>();
        for (Property property : allProps) {
            property2UiControl.put(property, UiControl.createFor(property));
        }

        PresentationModel pm = new PresentationModel(bean);
        JPanel panel = new JPanel();
        PropertiesLayout layout = new PropertiesLayout(panel);
        panel.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        for (UiControl uiObject : property2UiControl.values()) {
            uiObject.addTo(layout);
            uiObject.bindTo(pm);
        }

        PropertyChangeListener updateUiControls = (PropertyChangeEvent evt) -> {
            List<Property> relevantProps = propsResolver.resolveProperties(pm);

            List<Property> irrelevantProps = new ArrayList<>(allProps);
            irrelevantProps.removeAll(relevantProps);

            for (Property prop : relevantProps) {
                property2UiControl.get(prop).setVisible(true);
            }

            for (Property prop : irrelevantProps) {
                property2UiControl.get(prop).setVisible(false);
            }
        };

        pm.addBeanPropertyChangeListener(Bean.Property.option.name(), updateUiControls);
        pm.addBeanPropertyChangeListener(Bean.Property.checkedOption.name(), updateUiControls);

        updateUiControls.propertyChange(null);
        JOptionPane.showMessageDialog(null, panel);
    }

    private static void print(Bean bean) {
        PresentationModel pm = new PresentationModel(bean);
        List<Property> properties = new MyBeanPropertiesResolver().resolveProperties(pm);

        System.out.println("########################");
        for (Property property : properties) {
            System.out.printf("%s: %s\n", property.displayName(), pm.getValue(property.name()));
        }
        System.out.println("########################");
    }
}
