package org.litespring.beans.factory.support;

import org.litespring.beans.BeanDefinition;
import org.litespring.beans.PropertyValue;
import org.litespring.beans.SimpleTypeConverter;
import org.litespring.beans.factory.config.ConfigurableBeanFactory;
import org.litespring.exception.BeanCreationException;
import org.litespring.exception.BeanDefinitionStoreException;
import org.litespring.util.ClassUtils;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by wjt on 2018/6/9.
 */
public class DefaultBeanFactory extends DefaultSingletonBeanRegistry
        implements ConfigurableBeanFactory,BeanDefinitionRegistry {

    private final Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<String, BeanDefinition>();
    private ClassLoader beanClassLoader;


    public DefaultBeanFactory() {
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

        //创建bean
        Object bean= this.instantiateBean(beanDefinition);

        //初始化bean的属性
        this.populateBean(beanDefinition, bean);

        return bean;

    }

    private void populateBean(BeanDefinition beanDefinition, Object bean) {

        try {

            List<PropertyValue> propertyValueList = beanDefinition.getPropertyValues();


            BeanDefinitionValueResolver resolver = new BeanDefinitionValueResolver(this);

            SimpleTypeConverter typeConverter = new SimpleTypeConverter();


            for (PropertyValue propertyValue : propertyValueList) {

                String propertyName = propertyValue.getName();
                Object originalValue = propertyValue.getValue();

                Object resolveValue = resolver.resolveValueIfNecessary(originalValue);

                //获取bean的信息
                BeanInfo beanInfo = Introspector.getBeanInfo(bean.getClass());

                //获取bean属性描述
                PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();

                for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {

                    if (propertyDescriptor.getName().equals(propertyName)) {
                        Object covertValue = typeConverter.convertIfNecessary(resolveValue, propertyDescriptor.getPropertyType());

                        //使用set方法给bean属性进行设置
                        propertyDescriptor.getWriteMethod().invoke(bean, covertValue);
                        break;

                    }

                }


            }
        } catch (Exception e) {
            throw new BeanCreationException("Failed to obtain BeanInfo for class [" + beanDefinition.getBeanClassName() + "]", e);
        }


    }

    private Object instantiateBean(BeanDefinition beanDefinition) {

        if (beanDefinition.hasConstructorArgument()) {

            ConstructorResolver resolver = new ConstructorResolver(this);

            return resolver.autoWriteConstructor(beanDefinition);

        }else {
            String beanClassName = beanDefinition.getBeanClassName();

            ClassLoader classLoader = this.getBeanClassLoader();
            try {
                Class cls = classLoader.loadClass(beanClassName);
                return cls.newInstance();
            } catch (Exception e) {
                throw new BeanCreationException();

            }
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
