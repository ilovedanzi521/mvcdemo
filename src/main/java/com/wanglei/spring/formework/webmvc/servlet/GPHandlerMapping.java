package com.wanglei.spring.formework.webmvc.servlet;

import lombok.Data;

import java.lang.reflect.Method;
import java.util.regex.Pattern;

/**
 * @ClassName GPHandleMapping
 * @Description TODO
 * @Author yuman
 * @Date 2019/4/19 9:10
 * @菜鸡加油 run run run
 */
@Data
public class GPHandlerMapping {
    private Object controller;
    private Method method;
    private Pattern pattern;

    public GPHandlerMapping(Pattern pattern,Object controller, Method method ) {
        this.controller = controller;
        this.method = method;
        this.pattern = pattern;
    }
}
