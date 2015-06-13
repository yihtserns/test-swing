package com.github.yihtserns.test.swing;

import com.jgoodies.binding.PresentationModel;
import java.util.List;

/**
 *
 * @author yihtserns
 */
public interface PropertiesResolver {

    List<Property> resolveProperties(PresentationModel pm);
}
