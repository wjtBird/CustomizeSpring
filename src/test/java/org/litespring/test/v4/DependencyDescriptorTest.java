package org.litespring.test.v4;

import org.junit.Assert;
import org.junit.Test;
import org.litespring.beans.factory.config.DependencyDescriptor;
import org.litespring.beans.factory.support.DefaultBeanFactory;
import org.litespring.beans.factory.xml.XmlBeanDefinitionReader;
import org.litespring.core.io.ClassPathResource;
import org.litespring.dao.v4.AccountDao;
import org.litespring.service.v4.PetStoreService;

import java.lang.reflect.Field;

/**
 * @author jintao.wang
 */
public class DependencyDescriptorTest {

    @Test
    public void testResolveDependency() throws NoSuchFieldException, ClassNotFoundException {

        DefaultBeanFactory factory = new DefaultBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(factory);
        ClassPathResource resource = new ClassPathResource("petstore-v4.xml");
        reader.loadBeanDefinitions(resource);


        Field accountDao = PetStoreService.class.getDeclaredField("accountDao");

        DependencyDescriptor descriptor = new DependencyDescriptor(accountDao, true);

        Object object = factory.resolveDependency(descriptor);

        Assert.assertTrue(object instanceof AccountDao);


    }
}
