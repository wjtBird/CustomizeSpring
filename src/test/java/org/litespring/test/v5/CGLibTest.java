package org.litespring.test.v5;

import org.junit.Test;
import org.litespring.service.v5.PetStoreService;
import org.litespring.tx.TransactionManager;
import org.springframework.cglib.proxy.Callback;
import org.springframework.cglib.proxy.CallbackFilter;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;
import org.springframework.cglib.proxy.NoOp;

import java.lang.reflect.Method;

/**
 * @author jintao.wang
 */
public class CGLibTest {

    @Test
    public void testCallBack(){

        Enhancer enhancer = new Enhancer();

        enhancer.setSuperclass(PetStoreService.class);

        enhancer.setCallback( new TransactionInterceptor() );
        PetStoreService petStore = (PetStoreService)enhancer.create();
        petStore.placeOrder();


    }


    public static class TransactionInterceptor implements MethodInterceptor {
        TransactionManager txManager = new TransactionManager();
        public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {

            txManager.start();
            Object result = proxy.invokeSuper(obj, args);
            txManager.commit();

            return result;
        }
    }

    @Test
    public void  testFilter(){

        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(PetStoreService.class);

        enhancer.setInterceptDuringConstruction(false);

        //NoOp.INSTANCE cgLib 内置，什么都不做都 interceptor
        Callback[] callbacks = new Callback[]{new TransactionInterceptor(),NoOp.INSTANCE};

        Class<?>[] types = new Class<?>[callbacks.length];
        for (int x = 0; x < types.length; x++) {
            types[x] = callbacks[x].getClass();
        }



        enhancer.setCallbackFilter(new ProxyCallbackFilter());
        enhancer.setCallbacks(callbacks);
        enhancer.setCallbackTypes(types);


        PetStoreService petStore = (PetStoreService)enhancer.create();
        petStore.placeOrder();
        System.out.println(petStore.toString());

    }

    private static class ProxyCallbackFilter implements CallbackFilter {

        public ProxyCallbackFilter() {

        }

        public int accept(Method method) {
            if(method.getName().startsWith("place")){
                //call back interceptor 选择第1个
                return 0;
            } else{
                //call back interceptor 选择第2个
                return 1;
            }

        }

    }
}
