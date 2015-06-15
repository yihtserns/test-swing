package com.github.yihtserns.test.swing.bean;

import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.value.ValueModel;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 *
 * @author yihtserns
 */
public class MyBean implements Bean {

    private PropertyChangeSupport pcs = new PropertyChangeSupport(this);
    private Option option = Option.First;
    private boolean checkedOption;
    private String url;
    private String url2;
    private String url3;

    @Override
    public Option getOption() {
        return option;
    }

    @Override
    public void setOption(Option option) {
        // Fire property changes so Presentation/ValueModel listener can work
        // see ValueModel#addValueChangeListener(PropertyChangeListener)
        // see PresentationModel#addBeanPropertyChangeListener(String, PropertyChangeListener)
        pcs.firePropertyChange(
                Property.option.name(),
                this.option,
                this.option = option);
    }

    @Override
    public boolean isCheckedOption() {
        return checkedOption;
    }

    @Override
    public void setCheckedOption(boolean checkedOption) {
        // Fire property changes so Presentation/ValueModel listener can work
        // see ValueModel#addValueChangeListener(PropertyChangeListener)
        // see PresentationModel#addBeanPropertyChangeListener(String, PropertyChangeListener)
        pcs.firePropertyChange(
                Property.checkedOption.name(),
                this.checkedOption,
                this.checkedOption = checkedOption);
    }

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String getUrl2() {
        return url2;
    }

    @Override
    public void setUrl2(String url2) {
        this.url2 = url2;
    }

    @Override
    public String getUrl3() {
        return url3;
    }

    @Override
    public void setUrl3(String url3) {
        this.url3 = url3;
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        this.pcs.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        this.pcs.removePropertyChangeListener(listener);
    }

    public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        this.pcs.addPropertyChangeListener(propertyName, listener);
    }

    public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        this.pcs.removePropertyChangeListener(propertyName, listener);
    }

    @Override
    public String toString() {
        return "MyBean{" + "pcs=" + pcs + ", option=" + option + ", checkedOption=" + checkedOption + ", url=" + url + ", url2=" + url2 + ", url3=" + url3 + '}';
    }
}
