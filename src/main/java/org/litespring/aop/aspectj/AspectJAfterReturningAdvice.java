package org.litespring.aop.aspectj;

import org.aopalliance.intercept.MethodInvocation;

import java.lang.reflect.Method;

/**
 * @author jintao.wang
 */
public class AspectJAfterReturningAdvice extends AbstractAspectJAdvice{

    public AspectJAfterReturningAdvice(Method adviceMethod, AspectJExpressionPointcut pointcut, Object adviceObject) {
        super(adviceMethod, pointcut, adviceObject);
    }


    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {

        Object proceed = methodInvocation.proceed();

        super.invokeAdviceMethod();

        return proceed;
    }
}
