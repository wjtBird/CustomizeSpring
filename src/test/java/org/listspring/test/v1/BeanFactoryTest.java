package org.listspring.test.v1;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.listspring.beans.factory.BeanDefinition;
import org.listspring.beans.factory.support.DefaultBeanFactory;
import org.listspring.beans.factory.xml.XmlBeanDefinitionReader;
import org.listspring.core.io.ClassPathResource;
import org.listspring.exception.BeanCreationException;
import org.listspring.exception.BeanDefinitionStoreException;
import org.listspring.util.ClassUtils;
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


        this.reader.loadBeanDefinitions(new ClassPathResource("petstore-v1.xml"));

        BeanDefinition beanDefinition = this.factory.getBeanDefinition("petStore2");

    }

    @Test(expected = BeanCreationException.class)
    public void beanCreationExceptionTest() {
        this.reader.loadBeanDefinitions(new ClassPathResource("petstore-v1.xml"));

        BeanDefinition beanDefinition = this.factory.getBeanDefinition("petStore1");

        PetStoreService petStoreService = (PetStoreService) this.factory.getBean("petStore1");


    }

    @Test
    public void test(){

        ClassUtils.getDefaultClassLoader().getResource("");
    }


}
