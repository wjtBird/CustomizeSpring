package org.listspring.exception;

/**
 * 读取bean xml文件异常
 * Created by wjt on 2018/6/9.
 */
public class BeanDefinitionStoreException extends BeanException {

    public BeanDefinitionStoreException() {
    }

    public BeanDefinitionStoreException(String message) {
        super(message);
    }

    public BeanDefinitionStoreException(String message, Throwable cause) {
        super(message, cause);
    }

    public BeanDefinitionStoreException(Throwable cause) {
        super(cause);
    }

    public BeanDefinitionStoreException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
