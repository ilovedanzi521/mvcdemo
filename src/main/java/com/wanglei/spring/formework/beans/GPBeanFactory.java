package com.wanglei.spring.formework.beans;

/**
 * @InterfaceName GPBeanFactory
 * @Description BeanFactory
 * @Author yuman
 * @Date 2019/4/11 13:38
 * @菜鸡加油 run run run
 */
public interface GPBeanFactory {
    /**
     * 从ioc容器中获取一个实例bean
     * @param beanname
     * @return
     */
    public Object getBean(String beanname);
}
