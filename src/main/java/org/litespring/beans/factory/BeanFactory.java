package org.litespring.beans.factory;

/**
 * Created by wjt on 2018/6/9.
 */
public interface BeanFactory {

    Object getBean(String beanId);
}
