package org.litespring.beans.factory.support;

import org.litespring.beans.BeanDefinition;
import org.litespring.beans.BeansException;
import org.litespring.beans.PropertyValue;
import org.litespring.beans.SimpleTypeConverter;
import org.litespring.beans.factory.BeanFactoryAware;
import org.litespring.beans.factory.NoSuchBeanDefinitionException;
import org.litespring.beans.factory.config.BeanPostProcessor;
import org.litespring.beans.factory.config.DependencyDescriptor;
import org.litespring.beans.factory.config.InstantiationAwareBeanPostProcessor;
import org.litespring.exception.BeanCreationException;
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
public class DefaultBeanFactory extends AbstractBeanFactory
        implements BeanDefinitionRegistry {

    private final Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<String, BeanDefinition>();
    private ClassLoader beanClassLoader;
    private List<BeanPostProcessor> beanPostProcessors = new ArrayList<BeanPostProcessor>();



    public DefaultBeanFactory() {
    }

    @Override
    public List<Object> getBeansByType(Class<?> type) {
        List<Object> result = new ArrayList<Object>();
        List<String> beanIds = this.getBeanIdsByType(type);

        for (String beanId : beanIds) {
            result.add(this.getBean(beanId));
        }
        return result;
    }

    private List<String> getBeanIdsByType(Class<?> type) {
        List<String> result = new ArrayList<String>();
        for (String beanName : this.beanDefinitionMap.keySet()) {
            if (type.isAssignableFrom(this.getType(beanName))) {
                result.add(beanName);
            }
        }
        return result;
    }

    @Override
    public BeanDefinition getBeanDefinition(String beanId) {
        BeanDefinition beanDefinition = this.beanDefinitionMap.get(beanId);

//        if (beanDefinition == null) {
//            throw new BeanDefinitionStoreException();
//        }
        return beanDefinition;
    }

    @Override
    public void registerBeanDefinition(String beanID, BeanDefinition bd) {
        this.beanDefinitionMap.put(beanID, bd);
    }

    @Override
    public Object getBean(String beanId) {

        BeanDefinition beanDefinition = this.getBeanDefinition(beanId);
        if (beanDefinition == null) {
            return null;
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


    @Override
    protected Object createBean(BeanDefinition beanDefinition) {

        //创建bean
        Object bean= this.instantiateBean(beanDefinition);

        //初始化bean的属性
        this.populateBean(beanDefinition, bean);

        bean = this.initializeBean(beanDefinition, bean);

        return bean;

    }

    protected Object initializeBean(BeanDefinition bd, Object bean)  {
        invokeAwareMethods(bean);
        //Todo，调用Bean的init方法，暂不实现
        if(!bd.isSynthetic()){
            return applyBeanPostProcessorsAfterInitialization(bean,bd.getID());
        }
        return bean;
    }
    public Object applyBeanPostProcessorsAfterInitialization(Object existingBean, String beanName)
            throws BeansException {

        Object result = existingBean;
        for (BeanPostProcessor beanProcessor : getBeanPostProcessors()) {
            result = beanProcessor.afterInitialization(result, beanName);
            if (result == null) {
                return result;
            }
        }
        return result;
    }
    private void invokeAwareMethods(final Object bean) {
        if (bean instanceof BeanFactoryAware) {
            ((BeanFactoryAware) bean).setBeanFactory(this);
        }
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
    public void addBeanPostProcessor(BeanPostProcessor postProcessor) {
        this.beanPostProcessors.add(postProcessor);
    }

    @Override
    public Class<?> getType(String name) throws NoSuchBeanDefinitionException {
        BeanDefinition bd = this.getBeanDefinition(name);
        if(bd == null){
            throw new NoSuchBeanDefinitionException(name);
        }
        this.resolveBeanClass(bd);
        return bd.getBeanClass();
    }
}
