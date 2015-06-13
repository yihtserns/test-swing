package com.github.yihtserns.test.swing.bean;

/**
 *
 * @author yihtserns
 */
public interface Bean {

    Option getOption();

    void setOption(Option option);

    String getUrl();

    void setUrl(String url);

    String getUrl2();

    void setUrl2(String url2);

    String getUrl3();

    void setUrl3(String url3);

    boolean isCheckedOption();

    void setCheckedOption(boolean checkedOption);

    enum Option {

        First,
        Second
    }

    enum Property implements com.github.yihtserns.test.swing.Property {

        option("Option"),
        checkedOption("Checked option"),
        url("URL"),
        url2("URL 2"),
        url3("URL 3");

        private String displayName;

        Property(String displayName) {
            this.displayName = displayName;
        }

        @Override
        public String displayName() {
            return this.displayName;
        }
    }
}
