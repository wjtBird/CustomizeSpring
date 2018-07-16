package org.litespring.core.io;

import org.litespring.util.Assert;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by wjt on 2018/6/23.
 */
public class FileSystemResource implements Resource {


    private String filePath;
    private File file;

    public FileSystemResource(File file) {
        this.filePath = file.getPath();
        this.file = file;
    }
    public FileSystemResource(String path) {
        Assert.notNull(path, "Path must not be null");
        this.file = new File(path);
        this.filePath = path;
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
