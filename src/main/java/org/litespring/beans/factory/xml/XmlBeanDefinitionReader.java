package org.litespring.beans.factory.xml;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.litespring.beans.BeanDefinition;
import org.litespring.beans.PropertyValue;
import org.litespring.beans.factory.config.RuntimeBeanReference;
import org.litespring.beans.factory.config.TypedStringValue;
import org.litespring.beans.factory.support.BeanDefinitionRegistry;
import org.litespring.beans.factory.support.GenericBeanDefinition;
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

                String className = beanElement.attributeValue(CLASS_ATTRIBUTE);
                String id = beanElement.attributeValue(ID_ATTRIBUTE);

                BeanDefinition beanDefinition = new GenericBeanDefinition(id, className);
                if (beanElement.attribute(SCOPE_ATTRIBUTE)!=null) {
                    beanDefinition.setScope(beanElement.attributeValue(SCOPE_ATTRIBUTE));
                }

                this.parsePropertyElement(beanElement, beanDefinition);

                this.registry.registerBeanDefinition(id, beanDefinition);

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
}
