package org.listspring.beans.factory.support;

import org.listspring.beans.factory.BeanDefinition;

/**
 * Created by wjt on 2018/6/9.
 */
public class GenericBeanDefinition implements BeanDefinition {

    private String beanId;
    private String beanClassName;
    private boolean isSingleton = true;
    private boolean isPrototype = false;
    private String scope = SCOPE_DEFAULT;

    public GenericBeanDefinition() {
    }

    public GenericBeanDefinition(String beanId, String beanClassName) {
        this.beanId = beanId;
        this.beanClassName = beanClassName;

    }


    @Override
    public String getBeanClassName() {
        return this.beanClassName;
    }

    @Override
    public boolean isSingleton() {
        return this.isSingleton;
    }

    @Override
    public boolean isPrototype() {
        return this.isPrototype;
    }

    @Override
    public String getScope() {
        return this.scope;
    }

    @Override
    public void setScope(String scope) {
        this.scope = scope;
        this.isSingleton = SCOPE_DEFAULT.equals(scope) || SCOPE_SINGLETON.equals(scope);
        this.isPrototype = SCOPE_PROTOTYPE.equals(scope);
    }
}
