package org.litespring.beans.factory.config;

import org.litespring.beans.factory.annotation.AutowiredAnnotationProcessor;

/**
 * Created by wjt on 2018/6/17.
 */
public interface ConfigurableBeanFactory extends AutowireCapableBeanFactory {

    void setBeanClassLoader(ClassLoader beanClassLoader);

    ClassLoader getBeanClassLoader();

    void addBeanPostProcessor(AutowiredAnnotationProcessor postProcessor);
}
