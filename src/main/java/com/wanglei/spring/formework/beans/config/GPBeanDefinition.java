package com.wanglei.spring.formework.beans.config;


import lombok.Data;

/**
 * @ClassName BeanDefinition
 * @Description TODO
 * @Author yuman
 * @Date 2019/4/11 15:03
 * @菜鸡加油 run run run
 */
@Data
public class GPBeanDefinition {
    private String beanClassName;
    private boolean lazyInit = false;
    private String factoryBeanName;
    private boolean isSington = true;
}
