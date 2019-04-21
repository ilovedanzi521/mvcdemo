package com.wanglei.spring.formework.webmvc;

import lombok.Data;

import java.util.Map;

/**
 * @ClassName GPModelAndView
 * @Description TODO
 * @Author yuman
 * @Date 2019/4/14 13:53
 * @菜鸡加油 run run run
 */
@Data
public class GPModelAndView {
    private String viewName;
    private Map<String,?> model;

    public GPModelAndView(String viewName) {
        this.viewName = viewName;
    }

    public GPModelAndView(String viewName, Map<String, ?> model) {
        this.viewName = viewName;
        this.model = model;
    }
}
