package org.litespring.beans.factory.support;

import org.litespring.beans.factory.config.RuntimeBeanReference;
import org.litespring.beans.factory.config.TypedStringValue;

/**
 * Created by wjt on 2018/6/24.
 */
public class BeanDefinitionValueResolver {

    private final DefaultBeanFactory defaultBeanFactory;

    public BeanDefinitionValueResolver(DefaultBeanFactory defaultBeanFactory) {
        this.defaultBeanFactory = defaultBeanFactory;
    }


    public Object resolveValueIfNecessary(Object value) {

        if (value instanceof RuntimeBeanReference) {
            return this.defaultBeanFactory.getBean(((RuntimeBeanReference) value).getBeanName());
        } else if (value instanceof TypedStringValue) {
            return ((TypedStringValue) value).getValue();
        }else{
            //TODO 将来可以解析其他属性
            throw new RuntimeException("the value " + value +" has not implemented");
        }

    }
}
