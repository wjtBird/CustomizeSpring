package org.litespring.test.v3;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.litespring.beans.BeanDefinition;
import org.litespring.beans.factory.support.ConstructorResolver;
import org.litespring.beans.factory.support.DefaultBeanFactory;
import org.litespring.beans.factory.xml.XmlBeanDefinitionReader;
import org.litespring.core.io.ClassPathResource;
import org.litespring.service.v3.PetStoreService;
import org.litespring.service.v3.PetStoreService2;

/**
 * Created by wjt on 2018/6/30.
 */
public class ConstructorResolverTest {

    private DefaultBeanFactory factory;

    @Before
    public void init() {
        factory = new DefaultBeanFactory();

        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(factory);

        reader.loadBeanDefinitions(new ClassPathResource("petstore-v3.xml"));
    }


    @Test
    public void constructorResolverTest() {



        BeanDefinition beanDefinition = factory.getBeanDefinition("petStore");

        ConstructorResolver resolver = new ConstructorResolver(factory);

        PetStoreService petStore = (PetStoreService) resolver.autoWriteConstructor(beanDefinition);

        // 验证参数version 正确地通过此构造函数做了初始化
        // PetStoreService(AccountDao accountDao, ItemDao itemDao,int version)
        Assert.assertEquals(Integer.valueOf(1), petStore.getVersion());

        Assert.assertNotNull(petStore.getAccountDao());
        Assert.assertNotNull(petStore.getItemDao());
    }


    @Test
    public void constructorResolverTest2() {
        BeanDefinition beanDefinition = this.factory.getBeanDefinition("petStore2");

        ConstructorResolver resolver = new ConstructorResolver(this.factory);

        PetStoreService2 petStore = (PetStoreService2) resolver.autoWriteConstructor(beanDefinition);

        Assert.assertEquals(Integer.valueOf(7500000), Integer.valueOf(petStore.getAnInt()));

        Assert.assertEquals("42", petStore.getaString());

    }
}
