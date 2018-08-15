package org.litespring.test.v5;

import org.aopalliance.intercept.MethodInterceptor;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.litespring.aop.aspectj.AspectJAfterReturningAdvice;
import org.litespring.aop.aspectj.AspectJAfterThrowingAdvice;
import org.litespring.aop.aspectj.AspectJBeforeAdvice;
import org.litespring.aop.config.AspectInstanceFactory;
import org.litespring.aop.framework.ReflectiveMethodInvocation;
import org.litespring.beans.factory.BeanFactory;
import org.litespring.service.v5.PetStoreService;
import org.litespring.tx.TransactionManager;
import org.litespring.util.MessageTracker;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @author jintao.wang
 */
public class ReflectiveMethodInvocationTest extends AbstractV5Test {


    private AspectJBeforeAdvice beforeAdvice = null;
    private AspectJAfterReturningAdvice afterAdvice = null;
    private AspectJAfterThrowingAdvice afterThrowingAdvice = null;
    private PetStoreService petStoreService = null;

    private BeanFactory beanFactory;
    private AspectInstanceFactory aspectInstanceFactory;

    @Before
    public  void setUp() throws Exception{
        petStoreService = new PetStoreService();

        this.beanFactory = super.getBeanFactory("petstore-v5.xml");
        this.aspectInstanceFactory = super.getAspectInstanceFactory("tx");
        this.aspectInstanceFactory.setBeanFactory(beanFactory);

        MessageTracker.clearMsgs();
        beforeAdvice = new AspectJBeforeAdvice(
                TransactionManager.class.getMethod("start"),
                null,
                this.aspectInstanceFactory);

        afterAdvice = new AspectJAfterReturningAdvice(
                TransactionManager.class.getMethod("commit"),
                null,
                this.aspectInstanceFactory);

        afterThrowingAdvice = new AspectJAfterThrowingAdvice(
                TransactionManager.class.getMethod("rollback"),
                null,
                this.aspectInstanceFactory
        );

    }


    @Test
    public void testMethodInvocation() throws Throwable {
        Method placeOrder = PetStoreService.class.getMethod("placeOrder");

        List<MethodInterceptor> methodInterceptorList = new ArrayList<MethodInterceptor>();

        methodInterceptorList.add(this.beforeAdvice);
        methodInterceptorList.add(this.afterAdvice);

        ReflectiveMethodInvocation methodInvocation = new ReflectiveMethodInvocation(this.petStoreService, placeOrder, null, methodInterceptorList);

        methodInvocation.proceed();

        List<String> msgs = MessageTracker.getMsgs();
        Assert.assertEquals(3, msgs.size());
        Assert.assertEquals("start tx", msgs.get(0));
        Assert.assertEquals("place order", msgs.get(1));
        Assert.assertEquals("commit tx", msgs.get(2));
    }


    @Test
    public void testMethodInvocation2() throws Throwable{


        Method targetMethod = PetStoreService.class.getMethod("placeOrder");

        List<MethodInterceptor> interceptors = new ArrayList<MethodInterceptor>();
        interceptors.add(afterAdvice);
        interceptors.add(beforeAdvice);



        ReflectiveMethodInvocation mi = new ReflectiveMethodInvocation(petStoreService,targetMethod,new Object[0],interceptors);

        mi.proceed();


        List<String> msgs = MessageTracker.getMsgs();
        Assert.assertEquals(3, msgs.size());
        Assert.assertEquals("start tx", msgs.get(0));
        Assert.assertEquals("place order", msgs.get(1));
        Assert.assertEquals("commit tx", msgs.get(2));

    }

    @Test
    public void testAfterThrowing() throws Throwable{


        Method targetMethod = PetStoreService.class.getMethod("placeOrderWithException");

        List<MethodInterceptor> interceptors = new ArrayList<MethodInterceptor>();
        interceptors.add(afterThrowingAdvice);
        interceptors.add(beforeAdvice);



        ReflectiveMethodInvocation mi = new ReflectiveMethodInvocation(petStoreService,targetMethod,new Object[0],interceptors);
        try{
            mi.proceed();

        }catch(Throwable t){
            List<String> msgs = MessageTracker.getMsgs();
            Assert.assertEquals(2, msgs.size());
            Assert.assertEquals("start tx", msgs.get(0));
            Assert.assertEquals("rollback tx", msgs.get(1));
            return;
        }


        Assert.fail("No Exception thrown");


    }
}
