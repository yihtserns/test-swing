package com.github.yihtserns.test.swing.experimental;

import com.github.yihtserns.test.swing.bean.Bean;
import static com.github.yihtserns.test.swing.bean.Bean.Property.checkedOption;
import static com.github.yihtserns.test.swing.bean.Bean.Property.option;
import static com.github.yihtserns.test.swing.bean.Bean.Property.url;
import static com.github.yihtserns.test.swing.bean.Bean.Property.url2;
import static com.github.yihtserns.test.swing.bean.Bean.Property.url3;
import com.github.yihtserns.test.swing.bean.MyBean;
import com.jgoodies.binding.PresentationModel;
import java.util.Arrays;
import org.junit.Test;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author yihtserns
 */
public class MyBeanPropertiesResolverTest {

    private MyBeanPropertiesResolver propsResolver = new MyBeanPropertiesResolver();
    private MyBean bean = new MyBean();
    private PresentationModel pm = new PresentationModel(bean);

    @Test
    public void allProperties() throws Exception {
        assertThat(propsResolver.resolveAllProperties(),
                is(Arrays.asList(option, url, checkedOption, url2, url3)));
    }

    @Test
    public void testSomeMethod() {
        bean.setOption(Bean.Option.First);

        assertThat(propsResolver.resolveProperties(pm),
                is(Arrays.asList(option, url)));
    }

    @Test
    public void testName() throws Exception {
        bean.setOption(Bean.Option.Second);

        assertThat(propsResolver.resolveProperties(pm),
                is(Arrays.asList(option, checkedOption, url2)));
    }

    @Test
    public void testName2() throws Exception {
        bean.setOption(Bean.Option.Second);
        bean.setCheckedOption(true);

        assertThat(propsResolver.resolveProperties(pm),
                is(Arrays.asList(option, checkedOption, url, url3)));
    }
}
