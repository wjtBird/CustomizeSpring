package org.litespring.context.support;

import org.litespring.core.io.ClassPathResource;
import org.litespring.core.io.Resource;

/**
 * Created by wjt on 2018/6/17.
 */
public class ClassPathXmlApplicationContext extends AbstractApplicationContext {


    public ClassPathXmlApplicationContext(String config) {
        super(config);
    }


    @Override
    public Resource getResource(String config) {
        return new ClassPathResource(config, super.getBeanClassLoader());
    }

}
