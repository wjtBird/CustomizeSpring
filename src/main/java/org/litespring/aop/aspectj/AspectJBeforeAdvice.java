package org.litespring.aop.aspectj;


import org.aopalliance.intercept.MethodInvocation;
import org.litespring.aop.config.AspectInstanceFactory;

import java.lang.reflect.Method;

/**
 * @author jintao.wang
 */
public class AspectJBeforeAdvice extends AbstractAspectJAdvice {


    public AspectJBeforeAdvice(Method adviceMethod, AspectJExpressionPointcut pointcut, AspectInstanceFactory adviceObjectFactory) {
        super(adviceMethod, pointcut, adviceObjectFactory);
    }

    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        super.invokeAdviceMethod();

        Object proceed = methodInvocation.proceed();

        return proceed;
    }
}
