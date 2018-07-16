package org.litespring.test.v4;

import org.junit.Assert;
import org.junit.Test;
import org.litespring.core.annotation.AnnotationAttributes;
import org.litespring.core.io.ClassPathResource;
import org.litespring.core.io.Resource;
import org.litespring.core.type.AnnotationMetadata;
import org.litespring.core.type.classreading.SimpleMetadataReader;
import org.litespring.stereotype.Component;

import java.io.IOException;

/**
 * Created by jinTao.wang on 2018/7/15
 */
public class MetadataReaderTest {


    @Test
    public void testGetMetadata() throws IOException {
        Resource resource = new ClassPathResource("org/litespring/service/v4/PetStoreService.class");

        SimpleMetadataReader reader = new SimpleMetadataReader(resource);

        AnnotationMetadata annotationMetadata = reader.getAnnotationMetadata();

        String annotation = Component.class.getName();


        Assert.assertTrue(annotationMetadata.hasAnnotation(annotation));

        AnnotationAttributes attributes = annotationMetadata.getAnnotationAttributes(annotation);

        Assert.assertEquals("petStore", attributes.get("value"));

        //注：下面对class metadata的测试并不充分
        Assert.assertFalse(annotationMetadata.isAbstract());
        Assert.assertFalse(annotationMetadata.isFinal());
        Assert.assertEquals("org.litespring.service.v4.PetStoreService", annotationMetadata.getClassName());
    }
}
