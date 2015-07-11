package com.github.yihtserns.test.swing;

import com.github.yihtserns.test.swing.UiControl.ChangeEvent;
import com.github.yihtserns.test.swing.UiControl.ChangeHandler;
import com.github.yihtserns.test.swing.bean.Bean;
import com.github.yihtserns.test.swing.bean.Bean.Option;
import static com.github.yihtserns.test.swing.bean.Bean.Option.First;
import static com.github.yihtserns.test.swing.bean.Bean.Option.Second;
import static com.github.yihtserns.test.swing.bean.Bean.Property.checkedOption;
import static com.github.yihtserns.test.swing.bean.Bean.Property.option;
import static com.github.yihtserns.test.swing.bean.Bean.Property.url;
import static com.github.yihtserns.test.swing.bean.Bean.Property.url2;
import static com.github.yihtserns.test.swing.bean.Bean.Property.url3;
import com.github.yihtserns.test.swing.bean.MyBean;
import com.github.yihtserns.test.swing.experimental.MyBeanPropertiesResolver;
import com.jgoodies.binding.PresentationModel;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author yihtserns
 */
public class RefactorFromObserver {

    private static PropertiesResolver propsResolver = new MyBeanPropertiesResolver();

    public static void main(String[] args) {
        Bean bean = new MyBean();

        List<Property> allProps = propsResolver.resolveAllProperties();

        final Map<Property, UiControl> property2UiControl = new LinkedHashMap<>();
        for (Property property : allProps) {
            property2UiControl.put(property, UiControl.createFor(property));
        }

        final PresentationModel pm = new PresentationModel(bean);
        JPanel panel = new JPanel();
        PropertiesLayout layout = new PropertiesLayout(panel);
        panel.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        ChangeHandler changeHandler = (evt) -> {
            Bean.Property property = (Bean.Property) evt.source;
            if (property != null && property != option && property != checkedOption) {
                return;
            }

            Option optionVal = (Option) pm.getValue(option.name());
            boolean checkedOptionVal = (boolean) pm.getValue(checkedOption.name());

            Set<Bean.Property> relevantProps = new HashSet<>();
            relevantProps.add(option);
            if (optionVal == First) {
                relevantProps.add(url);
            } else if (optionVal == Second) {
                relevantProps.add(checkedOption);
                if (checkedOptionVal) {
                    relevantProps.add(url);
                    relevantProps.add(url3);
                } else {
                    relevantProps.add(url2);
                }
            }
            Set<Bean.Property> irrelevantProps = EnumSet.complementOf(EnumSet.copyOf(relevantProps));

            for (Bean.Property relevantProp : relevantProps) {
                property2UiControl.get(relevantProp).setVisible(true);
            }
            for (Bean.Property irrelevantProp : irrelevantProps) {
                property2UiControl.get(irrelevantProp).setVisible(false);
            }
        };

        for (UiControl uiObject : property2UiControl.values()) {
            uiObject.addTo(layout);
            uiObject.bindTo(pm);
            uiObject.onChange(changeHandler);
        }

        changeHandler.handle(new ChangeEvent(null));
        JOptionPane.showMessageDialog(null, panel);
    }
}
