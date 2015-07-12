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
        thrown.expectMessage("Unspecified condition for when property enumValue is [UNUSED, UNUSED2]");
        new DslPropertiesResolver(when(MyProperty.enumValue, is(EnumValue.USED, returns(MyProperty.someProp)))) {
        };
    }

    private enum MyProperty implements Property {

        enumValue(EnumValue.class),
        someProp(String.class);

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
