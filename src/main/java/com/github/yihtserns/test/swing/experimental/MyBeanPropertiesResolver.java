package com.github.yihtserns.test.swing.experimental;

import static com.github.yihtserns.test.swing.bean.Bean.Option.First;
import static com.github.yihtserns.test.swing.bean.Bean.Option.Second;
import static com.github.yihtserns.test.swing.bean.Bean.Property.checkedOption;
import static com.github.yihtserns.test.swing.bean.Bean.Property.option;
import static com.github.yihtserns.test.swing.bean.Bean.Property.url;
import static com.github.yihtserns.test.swing.bean.Bean.Property.url2;
import static com.github.yihtserns.test.swing.bean.Bean.Property.url3;

/**
 *
 * @author yihtserns
 */
public class MyBeanPropertiesResolver extends DslPropertiesResolver {

    public MyBeanPropertiesResolver() {
        super(when(option,
                is(First, returns(url)),
                is(Second, and(checkedOption,
                                is(false, returns(url2)),
                                is(true, returns(url, url3))))));
    }
}
