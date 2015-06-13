package com.github.yihtserns.test.swing;

import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.adapter.Bindings;
import com.jgoodies.binding.list.SelectionInList;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;
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
        MyBean bean = new MyBean();
        final PresentationModel pm = new PresentationModel(bean);

        JPanel panel = new JPanel();
        PropertiesLayout layout = new PropertiesLayout(panel);
        panel.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        Map<String, Property> name2Property = new LinkedHashMap<>();
        {
            JLabel label = new JLabel("Option:");
            JComboBox comboBox = new JComboBox();
            name2Property.put(MyBean.Property.OPTION, new Property(label, comboBox));

            Bindings.bind(comboBox, new SelectionInList(MyBean.Option.values(), pm.getComponentModel(MyBean.Property.OPTION)));
        }
        {
            JLabel label = new JLabel("Checked Option:");
            JCheckBox checkBox = new JCheckBox();
            name2Property.put(MyBean.Property.CHECKED_OPTION, new Property(label, checkBox));

            Bindings.bind(checkBox, pm.getComponentModel(MyBean.Property.CHECKED_OPTION));
        }
        {
            JLabel label = new JLabel("URL:");
            JTextField textField = new JTextField();
            name2Property.put(MyBean.Property.URL, new Property(label, textField));

            Bindings.bind(textField, pm.getComponentModel(MyBean.Property.URL));
        }
        {
            JLabel label = new JLabel("URL 2:");
            JTextField textField = new JTextField();
            name2Property.put(MyBean.Property.URL2, new Property(label, textField));

            Bindings.bind(textField, pm.getComponentModel(MyBean.Property.URL2));
        }
        {
            JLabel label = new JLabel("URL 3:");
            JTextField textField = new JTextField();
            name2Property.put(MyBean.Property.URL3, new Property(label, textField));

            Bindings.bind(textField, pm.getComponentModel(MyBean.Property.URL3));
        }

        for (Property property : name2Property.values()) {
            property.addTo(layout);
        }

        List<String> variableProperties = Arrays.asList(
                MyBean.Property.CHECKED_OPTION,
                MyBean.Property.URL,
                MyBean.Property.URL2,
                MyBean.Property.URL3);

        Runnable r = () -> {
            List<String> currentProperties = new ArrayList<>();

            Evaluator evaluator = new Evaluator();
            evaluator.when(() -> pm.getValue(MyBean.Property.OPTION) == MyBean.Option.First)
                    .then(() -> currentProperties.add(MyBean.Property.URL));
            evaluator.when(() -> pm.getValue(MyBean.Property.OPTION) == MyBean.Option.Second)
                    .then(() -> {
                        boolean checked = (Boolean) pm.getValue(MyBean.Property.CHECKED_OPTION);
                        if (checked) {
                            currentProperties.add(MyBean.Property.URL3);
                        } else {
                            currentProperties.add(MyBean.Property.URL2);
                        }
                        currentProperties.add(MyBean.Property.CHECKED_OPTION);
                    });

            evaluator.evaluate();

            List<String> irrelevantProperties = new ArrayList(variableProperties);
            irrelevantProperties.removeAll(currentProperties);

            for (String irrelevantProperty : irrelevantProperties) {
                name2Property.get(irrelevantProperty).setVisible(false);
            }
            for (String currentProperty : currentProperties) {
                name2Property.get(currentProperty).setVisible(true);
            }
        };
        r.run();

        PropertyChangeListener listener = (PropertyChangeEvent evt) -> r.run();
        pm.getComponentModel(MyBean.Property.OPTION).addValueChangeListener(listener);
        pm.getComponentModel(MyBean.Property.CHECKED_OPTION).addValueChangeListener(listener);

        JOptionPane.showConfirmDialog(null, panel);
        System.out.println(bean);
    }

    private static class Evaluator {

        private List<Rule> rules = new ArrayList<>();

        public Rule when(Condition condition) {
            Rule rule = new Rule(condition);
            rules.add(rule);

            return rule;
        }

        public void evaluate() {
            for (Rule rule : rules) {
                if (rule.condition.matches()) {
                    rule.action.run();
                    return;
                }
            }
        }
    }

    private interface Condition {

        boolean matches();
    }

    private static class Rule {

        private Condition condition;
        private Runnable action;

        public Rule(Condition condition) {
            this.condition = condition;
        }

        public void then(Runnable action) {
            this.action = action;
        }
    }

    private static class Property {

        private JLabel label;
        private JComponent value;

        public Property(JLabel label, JComponent value) {
            this.label = label;
            this.value = value;
        }

        public void setVisible(boolean visibility) {
            label.setVisible(visibility);
            value.setVisible(visibility);
        }

        public void addTo(PropertiesLayout layout) {
            layout.addProperty(label, value);
        }
    }
}
