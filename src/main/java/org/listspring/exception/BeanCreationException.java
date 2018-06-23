package org.listspring.exception;

/**
 * 创建bean异常
 * Created by wjt on 2018/6/9.
 */
public class BeanCreationException extends BeanException {

    public BeanCreationException() {
    }

    public BeanCreationException(String message) {
        super(message);
    }

    public BeanCreationException(String message, Throwable cause) {
        super(message, cause);
    }

    public BeanCreationException(Throwable cause) {
        super(cause);
    }

    public BeanCreationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
