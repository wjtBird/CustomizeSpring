package org.litespring.core.type.classreading;

import org.litespring.core.io.Resource;
import org.litespring.core.type.AnnotationMetadata;
import org.litespring.core.type.ClassMetadata;
import org.springframework.asm.ClassReader;

import java.io.IOException;

/**
 * Created by jinTao.wang on 2018/7/15
 */
public class SimpleMetadataReader implements MetadataReader{


    private Resource resource;

    private ClassMetadata classMetadata;

    private AnnotationMetadata annotationMetadata;

    public SimpleMetadataReader(Resource resource) throws IOException {
        ClassReader reader = new ClassReader(resource.getInputStream());

        AnnotationMetadataReadingVisitor visitor = new AnnotationMetadataReadingVisitor();

        reader.accept(visitor, ClassReader.SKIP_DEBUG);
        this.resource = resource;
        this.classMetadata = visitor;
        this.annotationMetadata = visitor;
    }


    @Override
    public Resource getResource() {
        return this.resource;
    }

    @Override
    public ClassMetadata getClassMetadata()  {
        return this.classMetadata;

    }

    @Override
    public AnnotationMetadata getAnnotationMetadata()  {
        return this.annotationMetadata;
    }

}
