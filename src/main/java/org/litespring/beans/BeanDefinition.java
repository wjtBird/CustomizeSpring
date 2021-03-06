package org.litespring.beans;

import java.util.List;

/**
 * Created by wjt on 2018/6/9.
 */
public interface BeanDefinition {
     String SCOPE_SINGLETON = "singleton";
     String SCOPE_PROTOTYPE = "prototype";
     String SCOPE_DEFAULT = "";

     boolean isSingleton();

     boolean isPrototype();

     String getScope();

     void setScope(String scope);

     String getBeanClassName();

     List<PropertyValue> getPropertyValues();

     ConstructorArgument getConstructorArgument();

     boolean hasConstructorArgument();

     String getID();

     Class<?> resolveBeanClass(ClassLoader classLoader) throws ClassNotFoundException;

     Class<?> getBeanClass() throws IllegalStateException;

     boolean hasBeanClass();

     boolean isSynthetic();

}
