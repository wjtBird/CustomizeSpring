package org.listspring.test.v1;

import org.junit.Assert;
import org.junit.Test;
import org.listspring.context.ApplicationContext;
import org.listspring.context.support.ClassPathXmlApplicationContext;
import org.listspring.context.support.FileSystemXmlApplicationContext;
import org.litespring.service.v1.PetStoreService;

public class ApplicationContextTest {

	@Test
	public void testGetBean() {
		ApplicationContext ctx = new ClassPathXmlApplicationContext("petstore-v1.xml");
		PetStoreService petStore = (PetStoreService)ctx.getBean("petStore");
		Assert.assertNotNull(petStore);
	}
    @Test 
	public void testGetBeanFromFileSystemContext(){
//	    注意啊，这里仍然是hardcode了一个本地路径，这是不好的实践!! 如何处理，留作作业 使用相对路径
		ApplicationContext ctx = new FileSystemXmlApplicationContext("src/test/resources/petstore-v1.xml");
		PetStoreService petStore = (PetStoreService)ctx.getBean("petStore");
		Assert.assertNotNull(petStore);
	}

}