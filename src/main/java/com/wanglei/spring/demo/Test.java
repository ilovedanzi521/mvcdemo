package com.wanglei.spring.demo;

import com.wanglei.spring.formework.context.GPApplicationContext;

/**
 * @ClassName Test
 * @Description TODO
 * @Author yuman
 * @Date 2019/4/12 10:10
 * @菜鸡加油 run run run
 */
public class Test {

    private static Integer i;
    private static Object object;
    public static void main(String[] args) {
        GPApplicationContext context = new GPApplicationContext("classpath:application.properties");
        System.out.println(context);
        System.out.println(context.getBean("MyAction"));

    }

}
