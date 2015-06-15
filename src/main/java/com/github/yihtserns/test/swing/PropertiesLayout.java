package com.github.yihtserns.test.swing;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager2;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Group;

/**
 *
 * @author yihtserns
 */
public class PropertiesLayout implements LayoutManager2 {

    private GroupLayout layout;
    private Group horizontalIdentifierGroup;
    private Group horizontalValueGroup;
    private Group verticalGroup;
    private Group horizontalIdentifiableValueGroup;

    public PropertiesLayout(Container container) {
        this.layout = new GroupLayout(container);

        this.horizontalIdentifierGroup = layout.createParallelGroup();
        this.horizontalValueGroup = layout.createParallelGroup();
        this.horizontalIdentifiableValueGroup = layout.createParallelGroup();
        this.verticalGroup = layout.createSequentialGroup();

        this.layout.setHorizontalGroup(layout.createParallelGroup()
                .addGroup(layout.createSequentialGroup()
                        .addGroup(horizontalIdentifierGroup)
                        .addGroup(horizontalValueGroup)
                )
                .addGroup(horizontalIdentifiableValueGroup)
        );
        this.layout.setVerticalGroup(verticalGroup);
    }

    public void addProperty(Component identifier, Component value) {
        this.horizontalIdentifierGroup.addComponent(identifier);
        this.horizontalValueGroup.addComponent(value);

        final int minHeight = GroupLayout.PREFERRED_SIZE;
        final int preferredHeight = GroupLayout.DEFAULT_SIZE;
        final int maxHeight = GroupLayout.PREFERRED_SIZE;
        this.verticalGroup.addGroup(layout.createParallelGroup()
                .addComponent(identifier, minHeight, preferredHeight, maxHeight)
                .addComponent(value, minHeight, preferredHeight, maxHeight)
        );
    }

    public void addProperty(Component identifiableValue) {
        this.horizontalIdentifiableValueGroup.addComponent(identifiableValue);

        this.verticalGroup.addComponent(identifiableValue);
    }

    public void setAutoCreateGaps(boolean bln) {
        layout.setAutoCreateGaps(bln);
    }

    public void setAutoCreateContainerGaps(boolean bln) {
        layout.setAutoCreateContainerGaps(bln);
    }

    @Override
    public void addLayoutComponent(String string, Component cmpnt) {
        layout.addLayoutComponent(string, cmpnt);
    }

    @Override
    public void removeLayoutComponent(Component cmpnt) {
        layout.removeLayoutComponent(cmpnt);
    }

    @Override
    public Dimension preferredLayoutSize(Container cntnr) {
        return layout.preferredLayoutSize(cntnr);
    }

    @Override
    public Dimension minimumLayoutSize(Container cntnr) {
        return layout.minimumLayoutSize(cntnr);
    }

    @Override
    public void layoutContainer(Container cntnr) {
        layout.layoutContainer(cntnr);
    }

    @Override
    public void addLayoutComponent(Component cmpnt, Object o) {
        layout.addLayoutComponent(cmpnt, o);
    }

    @Override
    public Dimension maximumLayoutSize(Container cntnr) {
        return layout.maximumLayoutSize(cntnr);
    }

    @Override
    public float getLayoutAlignmentX(Container cntnr) {
        return layout.getLayoutAlignmentX(cntnr);
    }

    @Override
    public float getLayoutAlignmentY(Container cntnr) {
        return layout.getLayoutAlignmentY(cntnr);
    }

    @Override
    public void invalidateLayout(Container cntnr) {
        layout.invalidateLayout(cntnr);
    }

    @Override
    public String toString() {
        return layout.toString();
    }
}
