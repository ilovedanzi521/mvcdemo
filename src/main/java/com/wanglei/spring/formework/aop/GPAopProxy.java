package com.wanglei.spring.formework.aop;

/**
 * @ClassName GPAopProxy
 * @Description TODO
 * @Author yuman
 * @Date 2019/4/14 21:52
 * @菜鸡加油 run run run
 */
public interface GPAopProxy {
    Object getProxy();
    Object getProxy(ClassLoader classLoader);
}
