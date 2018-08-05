package org.litespring.aop;

/**
 * @author jintao.wang
 */
public interface Pointcut {


    MethodMatcher getMethodMatcher();

    String getExpression();
}
