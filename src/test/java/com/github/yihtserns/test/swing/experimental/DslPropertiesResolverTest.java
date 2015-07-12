package com.github.yihtserns.test.swing.experimental;

import com.github.yihtserns.test.swing.Property;
import static com.github.yihtserns.test.swing.experimental.DslPropertiesResolver.is;
import static com.github.yihtserns.test.swing.experimental.DslPropertiesResolver.returns;
import static com.github.yihtserns.test.swing.experimental.DslPropertiesResolver.when;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 *
 * @author yihtserns
 */
public class DslPropertiesResolverTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void shouldThrowWhenNotAllEnumValuesAreSpecified() {
        thrown.expectMessage("Unspecified condition for when property enumProp is [UNUSED, UNUSED2]");
        new DslPropertiesResolver(when(MyProperty.enumProp, is(EnumValue.USED, returns(MyProperty.someProp)))) {
        };
    }

    @Test
    public void shouldThrowWhenNotAllBooleanValuesAreSpecified() throws Exception {
        thrown.expectMessage("Unspecified condition for when property boolProp is [false]");
        new DslPropertiesResolver(when(MyProperty.boolProp, is(true, returns(MyProperty.someProp)))) {
        };
    }

    private enum MyProperty implements Property {

        enumProp(EnumValue.class),
        someProp(String.class),
        boolProp(Boolean.class);

        private Class<?> type;

        private MyProperty(Class<?> type) {
            this.type = type;
        }

        @Override
        public String displayName() {
            return name();
        }

        @Override
        public Class<?> type() {
            return type;
        }
    }

    private enum EnumValue {

        USED,
        UNUSED,
        UNUSED2,
    }
}
