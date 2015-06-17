package com.github.yihtserns.test.swing.experimental;

import com.github.yihtserns.test.swing.PropertiesResolver;
import com.github.yihtserns.test.swing.Property;
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
public abstract class DslPropertiesResolver implements PropertiesResolver {

    protected List<Property> allProperties;
    protected When initial;

    protected DslPropertiesResolver(When initial) {
        this.initial = initial;
        this.allProperties = extractAllPropertiesFrom(initial);
    }

    private static List<Property> extractAllPropertiesFrom(When when) {
        Set<Property> properties = new LinkedHashSet<>();
        when.addTo(properties);

        return new ArrayList<>(properties);
    }

    @Override
    public List<Property> resolveProperties(PresentationModel pm) {
        return initial.eval(pm);
    }

    @Override
    public List<Property> resolveAllProperties() {
        return allProperties;
    }

    protected static When when(Property property, Is... is) {
        return new When(property, is);
    }

    protected static Is is(Enum value, Returns returns) {
        return new Is(value, returns);
    }

    protected static Is is(boolean value, Returns returns) {
        return new Is(value, returns);
    }

    protected static Is is(Enum value, And and) {
        return new Is(value, and);
    }

    protected static And and(Property property, Is... is) {
        return new And(property, is);
    }

    protected static Returns returns(Property... properties) {
        return new Returns(properties);
    }

    protected static class When extends MyBeanPropertiesResolver.And {

        public When(Property property, MyBeanPropertiesResolver.Is... is) {
            super(property, is);
        }
    }

    protected static class And {

        private Property property;
        private MyBeanPropertiesResolver.Is[] is;

        public And(Property property, MyBeanPropertiesResolver.Is... is) {
            super();
            this.property = property;
            this.is = is;
        }

        public void addTo(Collection<Property> properties) {
            properties.add(property);
            for (MyBeanPropertiesResolver.Is i : is) {
                i.addTo(properties);
            }
        }

        public List<Property> eval(PresentationModel pm) {
            Object value = pm.getValue(property.name());
            for (MyBeanPropertiesResolver.Is i : is) {
                List<Property> props = i.eval(value, pm);
                if (!props.isEmpty()) {
                    return props;
                }
            }
            // Should not happen
            throw new RuntimeException();
        }
    }

    protected static class Is {

        private Object value;
        private MyBeanPropertiesResolver.Returns returns;
        private MyBeanPropertiesResolver.And and;

        public Is(Object value, MyBeanPropertiesResolver.Returns returns) {
            super();
            this.value = value;
            this.returns = returns;
        }

        public Is(Enum value, MyBeanPropertiesResolver.And and) {
            super();
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

    protected static class Returns {

        private List<Property> properties;

        public Returns(Property... properties) {
            super();
            this.properties = Arrays.asList(properties);
        }

        public void addTo(Collection<Property> properties) {
            properties.addAll(this.properties);
        }
    }
}