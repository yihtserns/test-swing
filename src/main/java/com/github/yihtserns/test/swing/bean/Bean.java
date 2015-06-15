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

        option("Option", Option.class),
        checkedOption("Checked option", boolean.class),
        url("URL", String.class),
        url2("URL 2", String.class),
        url3("URL 3", String.class);

        private String displayName;
        private Class<?> type;

        Property(String displayName, Class<?> type) {
            this.displayName = displayName;
            this.type = type;
        }

        @Override
        public String displayName() {
            return this.displayName;
        }

        @Override
        public Class<?> type() {
            return type;
        }
    }
}
