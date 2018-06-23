package org.listspring.beans.factory.support;

import org.listspring.beans.factory.config.SingletonBeanRegistry;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by wjt on 2018/6/17.
 */
public class DefaultSingletonBeanRegistry implements SingletonBeanRegistry {

    private Map<String, Object> singletonMap = new ConcurrentHashMap<>();

    @Override
    public void registerSingletonBean(String beanId, Object singletonObject) {
        Object registryObject = this.singletonMap.get(beanId);

        if (registryObject != null) {
            throw new IllegalStateException("Could not register object [" + singletonObject +
                    "] under bean name '" + beanId + "': there is already object [" + registryObject + "] bound");
        }

        this.singletonMap.put(beanId, singletonObject);

    }

    @Override
    public Object getSingleton(String beanId) {

        return this.singletonMap.get(beanId);
    }

}
