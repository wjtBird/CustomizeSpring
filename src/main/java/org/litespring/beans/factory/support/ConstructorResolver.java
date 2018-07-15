package org.litespring.beans.factory.support;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.litespring.beans.BeanDefinition;
import org.litespring.beans.ConstructorArgument;
import org.litespring.beans.SimpleTypeConverter;
import org.litespring.beans.factory.config.ConfigurableBeanFactory;
import org.litespring.exception.BeanCreationException;

import java.lang.reflect.Constructor;
import java.util.List;

/**
 * Created by wjt on 2018/6/30.
 */
public class ConstructorResolver {


    private final Log logger = LogFactory.getLog(this.getClass());

    private final ConfigurableBeanFactory factory;


    public ConstructorResolver(ConfigurableBeanFactory factory) {
        this.factory = factory;
    }

    public Object autoWriteConstructor(final BeanDefinition beanDefinition) {

        Class cls;
        try {
            cls = this.factory.getBeanClassLoader().loadClass(beanDefinition.getBeanClassName());
        } catch (ClassNotFoundException e) {
            throw new BeanCreationException( beanDefinition.getID(), "Instantiation of bean failed, can't resolve class", e);

        }

        Constructor[] candidates = cls.getConstructors();

        ConstructorArgument constructorArgument = beanDefinition.getConstructorArgument();

        BeanDefinitionValueResolver valueResolver = new BeanDefinitionValueResolver(this.factory);

        SimpleTypeConverter typeConverter = new SimpleTypeConverter();

        Object[] argsToUse = null;
        Constructor<?> constructorToUse = null;

        for (int i = 0; i < candidates.length; i++) {

           Class<?>[] parameterTypes = candidates[i].getParameterTypes();

            if (parameterTypes.length != constructorArgument.getArgumentCount()) {
                continue;
            }

            argsToUse = new Object[parameterTypes.length];

            boolean result = this.valuesMatchTypes(parameterTypes,
                    constructorArgument.getArgumentValues(),
                    argsToUse,
                    valueResolver,
                    typeConverter);

            if(result){
                constructorToUse = candidates[i];
                break;
            }
        }

        //找不到一个合适的构造函数
        if(constructorToUse == null){
            throw new BeanCreationException(beanDefinition.getID(), "can't find a apporiate constructor");
        }

        try {
            return constructorToUse.newInstance(argsToUse);
        } catch (Exception e) {
            throw new BeanCreationException( beanDefinition.getID(), "can't find a create instance using "+constructorToUse);
        }


    }

    private boolean valuesMatchTypes(Class<?>[] parameterTypes, List<ConstructorArgument.ValueHolder> argumentValues,
                                     Object[] argsToUse, BeanDefinitionValueResolver valueResolver,
                                     SimpleTypeConverter typeConverter) {



        for(int i=0;i<parameterTypes.length;i++){
            ConstructorArgument.ValueHolder valueHolder
                    = argumentValues.get(i);
            //获取参数的值，可能是TypedStringValue, 也可能是RuntimeBeanReference
            Object originalValue = valueHolder.getValue();

            try{
                //获得真正的值
                Object resolvedValue = valueResolver.resolveValueIfNecessary( originalValue);
                //如果参数类型是 int, 但是值是字符串,例如"3",还需要转型
                //如果转型失败，则抛出异常。说明这个构造函数不可用
                Object convertedValue = typeConverter.convertIfNecessary(resolvedValue, parameterTypes[i]);
                //转型成功，记录下来
                argsToUse[i] = convertedValue;
            }catch(Exception e){
                logger.error(e);
                return false;
            }
        }
        return true;
    }
}