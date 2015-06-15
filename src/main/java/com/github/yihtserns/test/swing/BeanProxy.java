package com.github.yihtserns.test.swing;

import java.beans.Introspector;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author yihtserns
 */
public class BeanProxy implements InvocationHandler {

    private PropertyChangeSupport pcs;
    private Object actualBean;

    /**
     * @see #proxyFor(Object)
     */
    private BeanProxy(Object actualBean) {
        this.actualBean = actualBean;
        this.pcs = new PropertyChangeSupport(actualBean);
    }

    @Override
    public Object invoke(Object instance, Method method, Object[] arguments) throws Throwable {
        if ("addPropertyChangeListener".equals(method.getName())) {
            if (arguments.length == 2) {
                this.pcs.addPropertyChangeListener((String) arguments[0], (PropertyChangeListener) arguments[1]);
            } else {
                this.pcs.addPropertyChangeListener((PropertyChangeListener) arguments[0]);
            }

            return null;
        } else if ("removePropertyChangeListener".equals(method.getName())) {
            if (arguments.length == 2) {
                this.pcs.removePropertyChangeListener((String) arguments[0], (PropertyChangeListener) arguments[1]);
            } else {
                this.pcs.removePropertyChangeListener((PropertyChangeListener) arguments[0]);
            }

            return null;
        }

        String capitalizedPropertyName = null;
        Object oldValue = null;
        if (method.getName().startsWith("set")) {
            capitalizedPropertyName = method.getName().substring(3);

            Method getter;
            try {
                getter = actualBean.getClass().getMethod("get" + capitalizedPropertyName);
            } catch (NoSuchMethodException ex) {
                getter = actualBean.getClass().getMethod("is" + capitalizedPropertyName);
            }
            oldValue = getter.invoke(actualBean);
        }

        Object result = method.invoke(actualBean, arguments);

        if (capitalizedPropertyName != null) {
            String propertyName = Introspector.decapitalize(capitalizedPropertyName);
            this.pcs.firePropertyChange(propertyName, oldValue, arguments[0]);
        }

        return result;
    }

    public static <T> T proxyFor(Object bean) {
        List<Class<?>> interfaces = new ArrayList<>(Arrays.asList(bean.getClass().getInterfaces()));
        interfaces.add(PropertyListenable.class);

        return (T) Proxy.newProxyInstance(
                bean.getClass().getClassLoader(),
                interfaces.toArray(new Class[interfaces.size()]),
                new BeanProxy(bean));
    }

    public interface PropertyListenable {

        void addPropertyChangeListener(PropertyChangeListener listener);

        void removePropertyChangeListener(PropertyChangeListener listener);

        void addPropertyChangeListener(String propertyName, PropertyChangeListener listener);

        void removePropertyChangeListener(String propertyName, PropertyChangeListener listener);
    }
}
