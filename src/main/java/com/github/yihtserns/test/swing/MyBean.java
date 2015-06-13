package com.github.yihtserns.test.swing;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 *
 * @author yihtserns
 */
public class MyBean {

    private PropertyChangeSupport pcs = new PropertyChangeSupport(this);
    private Option option = Option.First;
    private boolean checkedOption;
    private String url;
    private String url2;
    private String url3;

    public Option getOption() {
        return option;
    }

    public void setOption(Option option) {
        Option oldValue = getOption();

        this.option = option;

        pcs.firePropertyChange(Property.OPTION, oldValue, this.option);
    }

    public boolean isCheckedOption() {
        return checkedOption;
    }

    public void setCheckedOption(boolean checkedOption) {
        boolean oldValue = isCheckedOption();

        this.checkedOption = checkedOption;

        pcs.firePropertyChange(Property.CHECKED_OPTION, oldValue, this.checkedOption);
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl2() {
        return url2;
    }

    public void setUrl2(String url2) {
        this.url2 = url2;
    }

    public String getUrl3() {
        return url3;
    }

    public void setUrl3(String url3) {
        this.url3 = url3;
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        this.pcs.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        this.pcs.removePropertyChangeListener(listener);
    }

    @Override
    public String toString() {
        return "MyBean{" + "pcs=" + pcs + ", option=" + option + ", checkedOption=" + checkedOption + ", url=" + url + ", url2=" + url2 + ", url3=" + url3 + '}';
    }

    public enum Option {

        First,
        Second
    }

    public static final class Property {

        public static final String OPTION = "option";
        public static final String CHECKED_OPTION = "checkedOption";
        public static final String URL = "url";
        public static final String URL2 = "url2";
        public static final String URL3 = "url3";
    }
}
