package org.listspring.beans.factory.config;

/**
 * Created by wjt on 2018/6/17.
 */
public interface SingletonBeanRegistry {

    void registerSingletonBean(String beanId, Object singletonObject);

    Object getSingleton(String beanId);
}
