package org.litespring.test.v3;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.litespring.beans.BeanDefinition;
import org.litespring.beans.ConstructorArgument;
import org.litespring.beans.factory.config.RuntimeBeanReference;
import org.litespring.beans.factory.config.TypedStringValue;
import org.litespring.beans.factory.support.DefaultBeanFactory;
import org.litespring.beans.factory.xml.XmlBeanDefinitionReader;
import org.litespring.core.io.ClassPathResource;

import java.util.List;

/**
 * Created by wjt on 2018/6/30.
 */
public class BeanDefinitionTestV3 {

    private DefaultBeanFactory factory;

    @Before
    public void init() {
        factory = new DefaultBeanFactory();

        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(factory);

        reader.loadBeanDefinitions(new ClassPathResource("petstore-v3.xml"));
    }

    @Test
    public void testConstructorArgument() {


        BeanDefinition beanDefinition = factory.getBeanDefinition("petStore");

        Assert.assertEquals("org.litespring.service.v3.PetStoreService", beanDefinition.getBeanClassName());

        ConstructorArgument constructorArgument = beanDefinition.getConstructorArgument();

        List<ConstructorArgument.ValueHolder> valueHolders = constructorArgument.getArgumentValues();

        Assert.assertEquals(3, constructorArgument.getArgumentCount());

        RuntimeBeanReference ref1 = (RuntimeBeanReference)valueHolders.get(0).getValue();
        Assert.assertEquals("accountDao", ref1.getBeanName());
        RuntimeBeanReference ref2 = (RuntimeBeanReference)valueHolders.get(1).getValue();
        Assert.assertEquals("itemDao", ref2.getBeanName());

        TypedStringValue strValue = (TypedStringValue)valueHolders.get(2).getValue();
        Assert.assertEquals( "1", strValue.getValue());

    }


    @Test
    public void testTypeConstructorArgument() {
        BeanDefinition beanDefinition = this.factory.getBeanDefinition("petStore2");

        Assert.assertEquals("org.litespring.service.v3.PetStoreService2", beanDefinition.getBeanClassName());

        ConstructorArgument constructorArgument = beanDefinition.getConstructorArgument();

        List<ConstructorArgument.ValueHolder> valueHolders = constructorArgument.getArgumentValues();

        Assert.assertEquals(2, constructorArgument.getArgumentCount());

        TypedStringValue ref1 = (TypedStringValue)valueHolders.get(0).getValue();

        Assert.assertEquals("7500000", ref1.getValue());

        TypedStringValue ref2 = (TypedStringValue)valueHolders.get(1).getValue();
        Assert.assertEquals("42", ref2.getValue());

    }
}
