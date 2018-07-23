package org.litespring.beans.factory.annotation;

import java.util.LinkedList;
import java.util.List;

/**
 * @author jintao.wang
 */
public class InjectionMetadata {

    Class<?> clz;

    LinkedList<InjectionElement> elements;

    public InjectionMetadata(Class<?> clz, LinkedList<InjectionElement> elements) {
        this.clz = clz;
        this.elements = elements;
    }


    public void inject(Object targetObj) {

        if (this.elements == null || this.elements.isEmpty()) {
            return;
        }

        for (InjectionElement element : this.elements) {

            element.inject(targetObj);
        }


    }

    public List<InjectionElement> getInjectionElements() {
        return this.elements;
    }
}
