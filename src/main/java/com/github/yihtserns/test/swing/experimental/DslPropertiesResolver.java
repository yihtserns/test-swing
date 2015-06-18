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

    protected static class When extends And {

        public When(Property property, Is... is) {
            super(property, is);
        }
    }

    protected static class And implements Eval {

        private Property property;
        private Is[] is;

        public And(Property property, Is... is) {
            super();
            this.property = property;
            this.is = is;
        }

        @Override
        public void addTo(Collection<Property> properties) {
            properties.add(property);
            for (Is i : is) {
                i.addTo(properties);
            }
        }

        @Override
        public List<Property> eval(PresentationModel pm) {
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

    protected static class Is {

        private Object value;
        private Eval eval;

        public Is(Object value, Eval eval) {
            this.value = value;
            this.eval = eval;
        }

        public void addTo(Collection<Property> properties) {
            eval.addTo(properties);
        }

        public List<Property> eval(Object value, PresentationModel pm) {
            if (this.value.equals(value)) {
                return eval.eval(pm);
            }
            return Collections.emptyList();
        }
    }

    protected static class Returns implements Eval {

        private List<Property> properties;

        public Returns(Property... properties) {
            super();
            this.properties = Arrays.asList(properties);
        }

        @Override
        public void addTo(Collection<Property> properties) {
            properties.addAll(this.properties);
        }

        @Override
        public List<Property> eval(PresentationModel pm) {
            return properties;
        }
    }

    protected interface Eval {

        void addTo(Collection<Property> properties);

        List<Property> eval(PresentationModel pm);
    }
}
