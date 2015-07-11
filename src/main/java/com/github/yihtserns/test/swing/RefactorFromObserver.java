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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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

            property2UiControl.get(checkedOption).setVisible(optionVal == Second);
            property2UiControl.get(url).setVisible(optionVal == First || checkedOptionVal);
            property2UiControl.get(url2).setVisible(optionVal == Second && !checkedOptionVal);
            property2UiControl.get(url3).setVisible(optionVal == Second && checkedOptionVal);
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
