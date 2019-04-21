package com.wanglei.spring.formework.beans;

/**
 * @ClassName GPBeanWrapper
 * @Description TODO
 * @Author yuman
 * @Date 2019/4/11 18:24
 * @菜鸡加油 run run run
 */
public class GPBeanWrapper {
    private Object wrappedInstance;
    private Class<?> wrappedClass;

    public GPBeanWrapper() {
    }

    public GPBeanWrapper(Object wrappedInstance){
        this.wrappedInstance = wrappedInstance;
    }

    public Object getWrappedInstance(){
        return this.wrappedInstance;
    }

    public Class<?> getWrappedClass(){
        return this.wrappedInstance.getClass();
    }
}
