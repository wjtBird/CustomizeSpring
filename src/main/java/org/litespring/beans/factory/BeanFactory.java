package org.litespring.beans.factory;

import java.util.List;

/**
 * Created by wjt on 2018/6/9.
 */
public interface BeanFactory {

    Object getBean(String beanId);

    Class<?> getType(String name) throws NoSuchBeanDefinitionException;

    List<Object> getBeansByType(Class<?> type);

}
