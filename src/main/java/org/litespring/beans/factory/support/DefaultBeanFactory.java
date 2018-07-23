package org.litespring.beans.factory.support;

import org.litespring.beans.BeanDefinition;
import org.litespring.beans.PropertyValue;
import org.litespring.beans.SimpleTypeConverter;
import org.litespring.beans.factory.annotation.AutowiredAnnotationProcessor;
import org.litespring.beans.factory.config.BeanPostProcessor;
import org.litespring.beans.factory.config.ConfigurableBeanFactory;
import org.litespring.beans.factory.config.DependencyDescriptor;
import org.litespring.beans.factory.config.InstantiationAwareBeanPostProcessor;
import org.litespring.exception.BeanCreationException;
import org.litespring.exception.BeanDefinitionStoreException;
import org.litespring.util.ClassUtils;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.ArrayList;
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
    private List<BeanPostProcessor> beanPostProcessors = new ArrayList<BeanPostProcessor>();



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

    private void populateBean(BeanDefinition bd, Object bean) {

        for(BeanPostProcessor processor : this.getBeanPostProcessors()){
            if(processor instanceof InstantiationAwareBeanPostProcessor){
                ((InstantiationAwareBeanPostProcessor)processor).postProcessPropertyValues(bean, bd.getID());
            }
        }

        List<PropertyValue> pvs = bd.getPropertyValues();

        if (pvs == null || pvs.isEmpty()) {
            return;
        }

        BeanDefinitionValueResolver valueResolver = new BeanDefinitionValueResolver(this);
        SimpleTypeConverter converter = new SimpleTypeConverter();
        try{
            for (PropertyValue pv : pvs){
                String propertyName = pv.getName();
                Object originalValue = pv.getValue();
                Object resolvedValue = valueResolver.resolveValueIfNecessary(originalValue);

                BeanInfo beanInfo = Introspector.getBeanInfo(bean.getClass());
                PropertyDescriptor[] pds = beanInfo.getPropertyDescriptors();
                for (PropertyDescriptor pd : pds) {
                    if(pd.getName().equals(propertyName)){
                        Object convertedValue = converter.convertIfNecessary(resolvedValue, pd.getPropertyType());
                        pd.getWriteMethod().invoke(bean, convertedValue);
                        break;
                    }
                }


            }
        }catch(Exception ex){
            throw new BeanCreationException("Failed to obtain BeanInfo for class [" + bd.getBeanClassName() + "]", ex);
        }


    }

    public List<BeanPostProcessor> getBeanPostProcessors() {
        return this.beanPostProcessors;
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

    @Override
    public Object resolveDependency(DependencyDescriptor dependencyDescriptor) {

        Class<?> dependencyType = dependencyDescriptor.getDependencyType();

        for (BeanDefinition beanDefinition : this.beanDefinitionMap.values()) {

            this.resolveBeanClass(beanDefinition);

            Class<?> beanClass = beanDefinition.getBeanClass();

            if (dependencyType.isAssignableFrom(beanClass)) {
                return this.getBean(beanDefinition.getID());
            }
        }
        return null;
    }

    public void resolveBeanClass(BeanDefinition bd) {
        if(bd.hasBeanClass()){
            return;
        } else{
            try {
                bd.resolveBeanClass(this.getBeanClassLoader());
            } catch (ClassNotFoundException e) {
                throw new RuntimeException("can't load class:"+bd.getBeanClassName());
            }
        }
    }

    @Override
    public void addBeanPostProcessor(AutowiredAnnotationProcessor postProcessor) {
        this.beanPostProcessors.add(postProcessor);
    }
}
