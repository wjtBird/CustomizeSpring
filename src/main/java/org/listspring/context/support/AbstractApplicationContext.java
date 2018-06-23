package org.listspring.context.support;

import org.listspring.beans.factory.support.DefaultBeanFactory;
import org.listspring.beans.factory.xml.XmlBeanDefinitionReader;
import org.listspring.context.ApplicationContext;
import org.listspring.core.io.Resource;
import org.listspring.util.ClassUtils;

/**
 * Created by wjt on 2018/6/17.
 */
public abstract class AbstractApplicationContext implements ApplicationContext {

    private DefaultBeanFactory factory;

    private ClassLoader classLoader;

    public AbstractApplicationContext(String config) {
        factory = new DefaultBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(factory);
        Resource resource = this.getResource(config);
        reader.loadBeanDefinitions(resource);
        factory.setBeanClassLoader(this.getBeanClassLoader());
    }

    protected abstract Resource getResource(String config);

    @Override
    public Object getBean(String beanId) {
        return this.factory.getBean(beanId);
    }

    @Override
    public void setBeanClassLoader(ClassLoader beanClassLoader) {
        this.classLoader = beanClassLoader;
    }

    @Override
    public ClassLoader getBeanClassLoader() {
        return this.classLoader == null ? ClassUtils.getDefaultClassLoader() : this.classLoader;
    }

}
