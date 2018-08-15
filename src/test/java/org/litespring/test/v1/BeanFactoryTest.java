package org.litespring.test.v1;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.litespring.beans.BeanDefinition;
import org.litespring.beans.factory.support.DefaultBeanFactory;
import org.litespring.beans.factory.xml.XmlBeanDefinitionReader;
import org.litespring.core.io.ClassPathResource;
import org.litespring.exception.BeanCreationException;
import org.litespring.exception.BeanDefinitionStoreException;
import org.litespring.util.ClassUtils;
import org.litespring.service.v1.PetStoreService;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by wjt on 2018/6/9.
 */
public class BeanFactoryTest {

    XmlBeanDefinitionReader reader = null;

    DefaultBeanFactory factory = null;


    @Before
    public void setUp() {
        factory = new DefaultBeanFactory();
        reader = new XmlBeanDefinitionReader(factory);
    }


    @Test
    public void beanFactoryFunctionTest() {
        this.reader.loadBeanDefinitions(new ClassPathResource("petstore-v1.xml"));

        BeanDefinition beanDefinition = this.factory.getBeanDefinition("petStore");

        Assert.assertEquals("org.litespring.service.v1.PetStoreService", beanDefinition.getBeanClassName());

        PetStoreService petStoreService = (PetStoreService) this.factory.getBean("petStore");

        Assert.assertNotNull(petStoreService);

        assertTrue(beanDefinition.isSingleton());

        assertFalse(beanDefinition.isPrototype());

        assertEquals(BeanDefinition.SCOPE_DEFAULT,beanDefinition.getScope());

        assertEquals("org.litespring.service.v1.PetStoreService",beanDefinition.getBeanClassName());

        PetStoreService petStore = (PetStoreService)factory.getBean("petStore");

        assertNotNull(petStore);

        PetStoreService petStore1 = (PetStoreService)factory.getBean("petStore");

        assertTrue(petStore.equals(petStore1));



    }

    @Test(expected = BeanDefinitionStoreException.class)
    public void beanDefinitionStoreExceptionTest() {


        this.reader.loadBeanDefinitions(new ClassPathResource("xxxxxx.xml"));

        BeanDefinition beanDefinition = this.factory.getBeanDefinition("petStore2");

    }

    @Test(expected = BeanCreationException.class)
    public void beanCreationExceptionTest() {
        this.reader.loadBeanDefinitions(new ClassPathResource("petstore-v1.xml"));

        BeanDefinition beanDefinition = this.factory.getBeanDefinition("invalidBean");

        PetStoreService petStoreService = (PetStoreService) this.factory.getBean("invalidBean");


    }

    @Test
    public void test(){

        ClassUtils.getDefaultClassLoader().getResource("");
    }


}
