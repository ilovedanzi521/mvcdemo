package com.wanglei.spring.formework.aop;

import com.wanglei.spring.formework.aop.support.GPAdvisedSupport;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @ClassName JdkDynamicAopProxy
 * @Description TODO
 * @Author yuman
 * @Date 2019/4/14 21:54
 * @菜鸡加油 run run run
 */
public class GPJdkDynamicAopProxy implements GPAopProxy, InvocationHandler {

    private GPAdvisedSupport config;
    public GPJdkDynamicAopProxy(GPAdvisedSupport config) {
        this.config= config;
    }

    @Override
    public Object getProxy() {
        return getProxy(this.config.getTargetClass().getClassLoader());
    }

    @Override
    public Object getProxy(ClassLoader classLoader) {
        return Proxy.newProxyInstance(classLoader,
                this.config.getTargetClass().getInterfaces(),this);

    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
//       MethodInvocation
        return null;
    }
}
