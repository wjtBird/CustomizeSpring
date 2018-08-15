package org.litespring.aop.aspectj;

import org.aopalliance.intercept.MethodInvocation;
import org.litespring.aop.config.AspectInstanceFactory;

import java.lang.reflect.Method;

/**
 * @author jintao.wang
 */
public class AspectJAfterReturningAdvice extends AbstractAspectJAdvice{

    public AspectJAfterReturningAdvice(Method adviceMethod, AspectJExpressionPointcut pointcut, AspectInstanceFactory adviceObjectFactory) {
        super(adviceMethod, pointcut, adviceObjectFactory);
    }


    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {

        Object proceed = methodInvocation.proceed();

        super.invokeAdviceMethod();

        return proceed;
    }
}
