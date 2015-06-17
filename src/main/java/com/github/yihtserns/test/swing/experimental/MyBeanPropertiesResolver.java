package com.github.yihtserns.test.swing.experimental;

import com.github.yihtserns.test.swing.PropertiesResolver;
import com.github.yihtserns.test.swing.Property;
import static com.github.yihtserns.test.swing.bean.Bean.Option.First;
import static com.github.yihtserns.test.swing.bean.Bean.Option.Second;
import static com.github.yihtserns.test.swing.bean.Bean.Property.checkedOption;
import static com.github.yihtserns.test.swing.bean.Bean.Property.option;
import static com.github.yihtserns.test.swing.bean.Bean.Property.url;
import static com.github.yihtserns.test.swing.bean.Bean.Property.url2;
import static com.github.yihtserns.test.swing.bean.Bean.Property.url3;
import com.jgoodies.binding.PresentationModel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author yihtserns
 */
public class MyBeanPropertiesResolver implements PropertiesResolver {

    private List<Property> allProperties;
    private And initial;

    public MyBeanPropertiesResolver() {
        when(option,
                is(First, returns(option, url)),
                is(Second, and(checkedOption,
                                is(false, returns(option, checkedOption, url2)),
                                is(true, returns(option, checkedOption, url, url3)))));
    }

    @Override
    public List<Property> resolveProperties(PresentationModel pm) {
        return initial.eval(pm);
    }

    @Override
    public List<Property> resolveAllProperties() {
        return allProperties;
    }

    private void when(Property property, Is... is) {
        this.initial = new And(property, is);

        Set<Property> properties = new LinkedHashSet<>();
        this.initial.addTo(properties);

        this.allProperties = new ArrayList<>(properties);
    }

    private Is is(Enum value, Returns returns) {
        return new Is(value, returns);
    }

    private Is is(boolean value, Returns returns) {
        return new Is(value, returns);
    }

    private Is is(Enum value, And and) {
        return new Is(value, and);
    }

    private And and(Property property, Is... is) {
        return new And(property, is);
    }

    private Returns returns(Property... properties) {
        return new Returns(properties);
    }

    private class Is {

        private Object value;
        private Returns returns;
        private And and;

        public Is(Object value, Returns returns) {
            this.value = value;
            this.returns = returns;
        }

        public Is(Enum value, And and) {
            this.value = value;
            this.and = and;
        }

        public void addTo(Collection<Property> properties) {
            if (returns != null) {
                returns.addTo(properties);
            }
            if (and != null) {
                and.addTo(properties);
            }
        }

        public List<Property> eval(Object value, PresentationModel pm) {
            if (this.value.equals(value)) {
                if (returns != null) {
                    return returns.properties;
                }
                return and.eval(pm);
            }
            return Collections.emptyList();
        }
    }

    private class Returns {

        private List<Property> properties;

        public Returns(Property... properties) {
            this.properties = Arrays.asList(properties);
        }

        private void addTo(Collection<Property> properties) {
            properties.addAll(this.properties);
        }
    }

    private class And {

        private Property property;
        private Is[] is;

        private And(Property property, Is... is) {
            this.property = property;
            this.is = is;
        }

        private void addTo(Collection<Property> properties) {
            properties.add(property);
            for (Is i : is) {
                i.addTo(properties);
            }
        }

        private List<Property> eval(PresentationModel pm) {
            Object value = pm.getValue(property.name());

            for (Is i : is) {
                List<Property> props = i.eval(value, pm);
                if (!props.isEmpty()) {
                    return props;
                }
            }

            // Should not happen
            throw new RuntimeException();
        }
    }
}
