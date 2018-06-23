package org.listspring.beans.factory.support;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.listspring.beans.factory.BeanDefinition;
import org.listspring.beans.factory.config.ConfigurableBeanFactory;
import org.listspring.exception.BeanCreationException;
import org.listspring.exception.BeanDefinitionStoreException;
import org.listspring.util.ClassUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by wjt on 2018/6/9.
 */
public class DefaultBeanFactory extends DefaultSingletonBeanRegistry
        implements ConfigurableBeanFactory,BeanDefinitionRegistry {

    private static final String ID_ATTRIBUTE = "id";
    private static final String CLASS_ATTRIBUTE = "class";
    private final Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<String, BeanDefinition>();
    private ClassLoader beanClassLoader;


    public DefaultBeanFactory() {
    }

    public DefaultBeanFactory(String configFile) {
        this.loadBeanDefinition(configFile);
    }

    private void loadBeanDefinition(String configFile) {

        ClassLoader classLoader = ClassUtils.getDefaultClassLoader();

        InputStream inputStream=null;

        try {
            inputStream = classLoader.getResourceAsStream(configFile);

            SAXReader saxReader = new SAXReader();

            Document document = saxReader.read(inputStream);

            Element root = document.getRootElement();


            Iterator iterator = root.elementIterator();

            while (iterator.hasNext()) {
                Element beanElement = (Element) iterator.next();

                String beanId = beanElement.attributeValue(ID_ATTRIBUTE);
                String beanCls = beanElement.attributeValue(CLASS_ATTRIBUTE);

                BeanDefinition beanDefinition = new GenericBeanDefinition(beanId, beanCls);

                this.beanDefinitionMap.put(beanId, beanDefinition);
            }

        } catch (Exception e) {
            throw new BeanDefinitionStoreException();
        }finally {
            if (inputStream == null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    ;
                }

            }
        }

    }

    @Override
    public BeanDefinition getBeanDefinition(String beanId) {
        BeanDefinition beanDefinition = this.beanDefinitionMap.get(beanId);

        if (beanDefinition == null) {
            throw new BeanDefinitionStoreException();
        }
        return beanDefinition;
    }

    @Override
    public void registerBeanDefinition(String beanID, BeanDefinition bd) {
        this.beanDefinitionMap.put(beanID, bd);
    }

    @Override
    public Object getBean(String beanId) {

        BeanDefinition beanDefinition = this.beanDefinitionMap.get(beanId);
        if (beanDefinition == null) {
            throw new BeanCreationException("No such beanDefinition");
        }
        if (beanDefinition.isSingleton()) {
            Object bean = super.getSingleton(beanId);
            if (bean == null) {
                bean = this.createBean(beanDefinition);
                super.registerSingletonBean(beanId, bean);
            }
            return bean;
        }

       return this.createBean(beanDefinition);
    }

    private Object createBean(BeanDefinition beanDefinition) {

        String beanClassName = beanDefinition.getBeanClassName();

        ClassLoader classLoader = this.beanClassLoader;
        try {
            Class cls = classLoader.loadClass(beanClassName);
            return cls.newInstance();
        } catch (Exception e) {
            throw new BeanCreationException();

        }
    }

    @Override
    public void setBeanClassLoader(ClassLoader beanClassLoader) {
        this.beanClassLoader = beanClassLoader;
    }

    @Override
    public ClassLoader getBeanClassLoader() {
        return this.beanClassLoader == null ? ClassUtils.getDefaultClassLoader() : this.beanClassLoader;
    }
}
