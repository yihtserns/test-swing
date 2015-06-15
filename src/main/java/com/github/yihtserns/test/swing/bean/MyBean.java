package com.github.yihtserns.test.swing.bean;

/**
 *
 * @author yihtserns
 */
public class MyBean implements Bean {

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
        this.option = option;
    }

    @Override
    public boolean isCheckedOption() {
        return checkedOption;
    }

    @Override
    public void setCheckedOption(boolean checkedOption) {
        this.checkedOption = checkedOption;
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

    @Override
    public String toString() {
        return "MyBean{" + "option=" + option + ", checkedOption=" + checkedOption + ", url=" + url + ", url2=" + url2 + ", url3=" + url3 + '}';
    }
}
