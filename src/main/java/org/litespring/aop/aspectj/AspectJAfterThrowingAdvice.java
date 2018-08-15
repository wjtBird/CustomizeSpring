package org.litespring.aop.aspectj;

import org.aopalliance.intercept.MethodInvocation;
import org.litespring.aop.config.AspectInstanceFactory;

import java.lang.reflect.Method;

/**
 * @author jintao.wang
 */
public class AspectJAfterThrowingAdvice extends AbstractAspectJAdvice{

    public AspectJAfterThrowingAdvice(Method adviceMethod, AspectJExpressionPointcut pointcut, AspectInstanceFactory adviceObjectFactory) {
        super(adviceMethod, pointcut, adviceObjectFactory);
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
