package com.github.yihtserns.test.swing.bean;

import com.github.yihtserns.test.swing.PropertiesResolver;
import com.github.yihtserns.test.swing.Property;
import com.github.yihtserns.test.swing.bean.Bean.Option;
import com.jgoodies.binding.PresentationModel;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author yihtserns
 */
public class MyBeanPropertiesResolver implements PropertiesResolver {

    @Override
    public List<Property> resolveProperties(PresentationModel pm) {
        Object option = pm.getValue(Bean.Property.option.name());

        if (Option.First.equals(option)) {
            return Arrays.asList(
                    Bean.Property.option,
                    Bean.Property.url);
        } else if (Bean.Option.Second.equals(option)) {
            Object checkedOption = pm.getValue(Bean.Property.checkedOption.name());

            if (Boolean.TRUE.equals(checkedOption)) {
                return Arrays.asList(
                        Bean.Property.option,
                        Bean.Property.checkedOption,
                        Bean.Property.url3);
            } else {
                return Arrays.asList(
                        Bean.Property.option,
                        Bean.Property.checkedOption,
                        Bean.Property.url2);

            }
        } else {
            throw new UnsupportedOperationException("Unsupported option: " + option);
        }
    }
}
