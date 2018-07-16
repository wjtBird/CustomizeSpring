package org.litespring.beans.factory.xml;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.litespring.beans.BeanDefinition;
import org.litespring.beans.ConstructorArgument;
import org.litespring.beans.PropertyValue;
import org.litespring.beans.factory.config.RuntimeBeanReference;
import org.litespring.beans.factory.config.TypedStringValue;
import org.litespring.beans.factory.support.BeanDefinitionRegistry;
import org.litespring.beans.factory.support.GenericBeanDefinition;
import org.litespring.context.annotation.ClassPathBeanDefinitionScanner;
import org.litespring.core.io.Resource;
import org.litespring.exception.BeanDefinitionStoreException;
import org.litespring.util.Assert;
import org.litespring.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

/**
 * Created by wjt on 2018/6/17.
 */
public class XmlBeanDefinitionReader {

    public static final String ID_ATTRIBUTE = "id";

    public static final String CLASS_ATTRIBUTE = "class";

    public static final String SCOPE_ATTRIBUTE = "scope";

    public static final String NAME_ATTRIBUTE = "name";

    public static final String VALUE_ATTRIBUTE = "value";

    public static final String REF_ATTRIBUTE = "ref";

    public static final String PROPERTY_ELEMENT = "property";

    public static final String CONSTRUCTOR_ARG_ELEMENT = "constructor-arg";

    public static final String TYPE_ATTRIBUTE = "type";

    public static final String BEANS_NAMESPACE_URI = "http://www.springframework.org/schema/beans";

    public static final String CONTEXT_NAMESPACE_URI = "http://www.springframework.org/schema/context";

    private static final String BASE_PACKAGE_ATTRIBUTE = "base-package";


    private BeanDefinitionRegistry registry;

    private final Log logger =  LogFactory.getLog(this.getClass());

    public XmlBeanDefinitionReader() {

    }

    public XmlBeanDefinitionReader(BeanDefinitionRegistry registry) {
        this.registry = registry;
    }


    public void loadBeanDefinitions(Resource pathResource) {

        Assert.notNull(pathResource, "class path resource is null");

        InputStream inputStream = null;

        try {
            inputStream = pathResource.getInputStream();

            SAXReader reader = new SAXReader();

            Document document = reader.read(inputStream);

            Element rootElement = document.getRootElement();

            Iterator iterator = rootElement.elementIterator();

            while (iterator.hasNext()) {
                Element beanElement = (Element) iterator.next();
                String namespaceUri = beanElement.getNamespaceURI();
                if(this.isDefaultNamespace(namespaceUri)){
                    this.parseDefaultElement(beanElement); //普通的bean
                } else if(this.isContextNamespace(namespaceUri)){
                    this.parseComponentElement(beanElement); //例如<context:component-scan>
                }
            }
        } catch (Exception e) {
            throw new BeanDefinitionStoreException("get class path resource exception");
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    ;
                }
            }
        }

    }

    private void parseConstructorArgumentElements(Element beanElement, BeanDefinition beanDefinition) {
        Iterator constructorIt = beanElement.elementIterator(CONSTRUCTOR_ARG_ELEMENT);

        while (constructorIt.hasNext()) {
            Element constructorElement = (Element) constructorIt.next();
            this.parseConstructorArgumentElement(constructorElement, beanDefinition);
        }
    }

    private void parseConstructorArgumentElement(Element constructorElement, BeanDefinition beanDefinition) {

        String typeAttribute = constructorElement.attributeValue(TYPE_ATTRIBUTE);

        String nameAttribute = constructorElement.attributeValue(NAME_ATTRIBUTE);

        Object value = this.parsePropertyValue(constructorElement, beanDefinition, CONSTRUCTOR_ARG_ELEMENT);

        ConstructorArgument.ValueHolder valueHolder = new ConstructorArgument.ValueHolder(value);

        if (StringUtils.hasLength(typeAttribute)) {
            valueHolder.setType(typeAttribute);
        } else if (StringUtils.hasLength(nameAttribute)) {
            valueHolder.setName(nameAttribute);
        }

        beanDefinition.getConstructorArgument().addArgumentValue(valueHolder);

    }

    private void parsePropertyElement(Element beanElement, BeanDefinition beanDefinition) {
        Iterator beanIterator = beanElement.elementIterator(PROPERTY_ELEMENT);

        while (beanIterator.hasNext()) {
            Element propertyElement = (Element) beanIterator.next();

            String propertyName = propertyElement.attributeValue(NAME_ATTRIBUTE);
            if (!StringUtils.hasLength(propertyName)) {
                logger.fatal("Tag 'property' must have a 'name' attribute");
                return;
            }

            Object val = this.parsePropertyValue(propertyElement, beanDefinition, propertyName);

            beanDefinition.getPropertyValues().add(new PropertyValue(propertyName, val));
        }
    }

    private Object parsePropertyValue(Element propertyElement, BeanDefinition beanDefinition, String propertyName) {

        boolean hasRefAttribute = propertyElement.attributeValue(REF_ATTRIBUTE) != null;

        boolean hasValueAttribute = propertyElement.attributeValue(VALUE_ATTRIBUTE) != null;

        if (hasRefAttribute) {

            String refName = propertyElement.attributeValue(REF_ATTRIBUTE);
            if (!StringUtils.hasText(refName)) {
                logger.error(propertyName + " contains empty 'ref' attribute");
            }
            return new RuntimeBeanReference(refName);

        } else if (hasValueAttribute) {
            return new TypedStringValue(propertyElement.attributeValue(VALUE_ATTRIBUTE));
        } else {
            throw new RuntimeException(propertyName + " must specify a ref or value");
        }
    }
    public boolean isDefaultNamespace(String namespaceUri) {
        return (!StringUtils.hasLength(namespaceUri) || BEANS_NAMESPACE_URI.equals(namespaceUri));
    }
    public boolean isContextNamespace(String namespaceUri){
        return (!StringUtils.hasLength(namespaceUri) || CONTEXT_NAMESPACE_URI.equals(namespaceUri));
    }

    private void parseDefaultElement(Element ele) {
        String id = ele.attributeValue(ID_ATTRIBUTE);
        String beanClassName = ele.attributeValue(CLASS_ATTRIBUTE);
        BeanDefinition bd = new GenericBeanDefinition(id,beanClassName);
        if (ele.attribute(SCOPE_ATTRIBUTE)!=null) {
            bd.setScope(ele.attributeValue(SCOPE_ATTRIBUTE));
        }
        this.parseConstructorArgElements(ele,bd);
        this.parsePropertyElement(ele,bd);
        this.registry.registerBeanDefinition(id, bd);

    }
    public void parseConstructorArgElements(Element beanEle, BeanDefinition bd) {
        Iterator iter = beanEle.elementIterator(CONSTRUCTOR_ARG_ELEMENT);
        while(iter.hasNext()){
            Element ele = (Element)iter.next();
            this.parseConstructorArgElement(ele, bd);
        }

    }
    public void parseConstructorArgElement(Element ele, BeanDefinition bd) {

        String typeAttr = ele.attributeValue(TYPE_ATTRIBUTE);
        String nameAttr = ele.attributeValue(NAME_ATTRIBUTE);
        Object value = parsePropertyValue(ele, bd, null);
        ConstructorArgument.ValueHolder valueHolder = new ConstructorArgument.ValueHolder(value);
        if (StringUtils.hasLength(typeAttr)) {
            valueHolder.setType(typeAttr);
        }
        if (StringUtils.hasLength(nameAttr)) {
            valueHolder.setName(nameAttr);
        }

        bd.getConstructorArgument().addArgumentValue(valueHolder);
    }
    private void parseComponentElement(Element ele) {
        String basePackages = ele.attributeValue(BASE_PACKAGE_ATTRIBUTE);
        ClassPathBeanDefinitionScanner scanner = new ClassPathBeanDefinitionScanner(registry);
        scanner.doScan(basePackages);

    }

}
