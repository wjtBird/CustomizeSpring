package org.litespring.core.io;

import org.litespring.util.Assert;
import org.litespring.util.ClassUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by wjt on 2018/6/23.
 */
public class FileSystemResource implements Resource {


    private String filePath;
    private ClassLoader classLoader;

    public FileSystemResource(String filePath) {
        this(filePath, null);
    }

    public FileSystemResource(String filePath, ClassLoader classLoader) {
        this.filePath = filePath;
        this.classLoader = classLoader == null ? ClassUtils.getDefaultClassLoader() : classLoader;
    }

    @Override
    public InputStream getInputStream() throws IOException {

        Assert.notNull(filePath,"file path is null");
        return new FileInputStream(this.filePath);
    }

    @Override
    public String getDescription() {
        return this.filePath;
    }
}
