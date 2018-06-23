package org.listspring.beans.factory.config;

import org.listspring.beans.factory.BeanFactory;

/**
 * Created by wjt on 2018/6/17.
 */
public interface ConfigurableBeanFactory extends BeanFactory {

    void setBeanClassLoader(ClassLoader beanClassLoader);

    ClassLoader getBeanClassLoader();

}
