package org.litespring.beans.factory.config;

import org.litespring.beans.factory.BeanFactory;

/**
 * @author jintao.wang
 */
public interface AutowireCapableBeanFactory extends BeanFactory {

    Object resolveDependency(DependencyDescriptor dependencyDescriptor) ;
}
