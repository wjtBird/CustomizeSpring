package org.listspring.beans.factory.xml;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.listspring.beans.factory.BeanDefinition;
import org.listspring.beans.factory.support.BeanDefinitionRegistry;
import org.listspring.beans.factory.support.GenericBeanDefinition;
import org.listspring.core.io.Resource;
import org.listspring.exception.BeanDefinitionStoreException;
import org.listspring.util.Assert;

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


    private BeanDefinitionRegistry registry;


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

            Element rootEl = document.getRootElement();

            Iterator iterator = rootEl.elementIterator();

            while (iterator.hasNext()) {
                Element element = (Element) iterator.next();

                String className = element.attributeValue(CLASS_ATTRIBUTE);
                String id = element.attributeValue(ID_ATTRIBUTE);

                BeanDefinition beanDefinition = new GenericBeanDefinition(id, className);
                if (element.attribute(SCOPE_ATTRIBUTE)!=null) {
                    beanDefinition.setScope(element.attributeValue(SCOPE_ATTRIBUTE));
                }

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
}
