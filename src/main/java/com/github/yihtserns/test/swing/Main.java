package com.github.yihtserns.test.swing;

import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
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
        JComponent label1 = new JLabel("C1");
        JComponent label2 = new JTextField("C2");
        JComponent label3 = new JLabel("C3");
        JComponent label4 = new JTextField("C4");
        JComponent label5 = new JLabel("C5");
        JComponent label6 = new JTextField("C6");

        JComponent label7 = new JPanel();
        label7.setLayout(new BoxLayout(label7, BoxLayout.LINE_AXIS));
        label7.add(new JTextField("C7"));
        label7.setBorder(BorderFactory.createTitledBorder("C7"));

        JPanel panel = new JPanel();
        PropertiesLayout layout = new PropertiesLayout(panel);
        panel.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        layout.addProperty(label1, label2);
        layout.addProperty(label3, label4);
        layout.addProperty(label7);
        layout.addProperty(label5, label6);

        JOptionPane.showConfirmDialog(null, panel);
    }
}
