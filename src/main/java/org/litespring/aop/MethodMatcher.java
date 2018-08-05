package org.litespring.aop;

import java.lang.reflect.Method;

/**
 * @author jintao.wang
 */
public interface MethodMatcher {

    boolean matches(Method method/*, Class<?> targetClass*/);

}
