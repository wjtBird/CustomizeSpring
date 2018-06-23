package org.listspring.util;

/**
 * Created by wjt on 2018/6/9.
 */
public abstract class ClassUtils {


    public static ClassLoader getDefaultClassLoader() {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        if (classLoader == null) {
            classLoader = ClassUtils.class.getClassLoader();

            if (classLoader == null) {
                classLoader = ClassLoader.getSystemClassLoader();
            }
        }

        return classLoader;
    }
}
