package org.listspring.beans.factory.support;


import org.listspring.beans.factory.BeanDefinition;

public interface BeanDefinitionRegistry {

	BeanDefinition getBeanDefinition(String beanID);

	void registerBeanDefinition(String beanID, BeanDefinition bd);
}
