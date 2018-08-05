package org.litespring.aop.aspectj;

import org.aopalliance.intercept.MethodInvocation;

import java.lang.reflect.Method;

/**
 * @author jintao.wang
 */
public class AspectJAfterThrowingAdvice extends AbstractAspectJAdvice{

    public AspectJAfterThrowingAdvice(Method adviceMethod, AspectJExpressionPointcut pointcut, Object adviceObject) {
        super(adviceMethod, pointcut, adviceObject);
    }

    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {

        try {
            return methodInvocation.proceed();
        } catch (Throwable throwable) {
            super.invokeAdviceMethod();
            throw throwable;
        }
    }
}
