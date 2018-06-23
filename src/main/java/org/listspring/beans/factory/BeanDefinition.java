package org.listspring.beans.factory;

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

}