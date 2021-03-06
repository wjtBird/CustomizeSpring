package org.litespring.context.support;

import org.litespring.core.io.FileSystemResource;
import org.litespring.core.io.Resource;

/**
 * Created by wjt on 2018/6/23.
 */
public class FileSystemXmlApplicationContext extends AbstractApplicationContext {


    public FileSystemXmlApplicationContext(String config) {
        super(config);
    }

    @Override
    protected Resource getResource(String config) {

        return new FileSystemResource(config);
    }
}
