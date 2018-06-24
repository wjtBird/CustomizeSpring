package org.litespring.core.io;

import org.litespring.util.ClassUtils;
import org.litespring.util.Assert;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by wjt on 2018/6/17.
 */
public class ClassPathResource implements Resource {

    private String xmlFilePath;

    private ClassLoader classLoader;

    public ClassPathResource(String xmlFilePath) {
        this(xmlFilePath, null);
    }


    public ClassPathResource(String xmlFilePath, ClassLoader classLoader) {
        this.xmlFilePath = xmlFilePath;
        this.classLoader = (classLoader != null ? classLoader : ClassUtils.getDefaultClassLoader());

    }

    @Override
    public InputStream getInputStream() throws IOException {

        ClassLoader classLoader = this.classLoader;

        InputStream inputStream = classLoader.getResourceAsStream(this.xmlFilePath);
        Assert.notNull(inputStream, this.xmlFilePath+"can not open");

        return inputStream;

    }

    @Override
    public String getDescription() {
        return this.xmlFilePath;
    }

}
