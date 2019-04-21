package com.wanglei.spring.demo.service.impl;

import com.wanglei.spring.demo.service.IQueryService;
import com.wanglei.spring.formework.annotation.GPService;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @ClassName QueryService
 * @Description TODO
 * @Author yuman
 * @Date 2019/4/14 13:45
 * @菜鸡加油 run run run
 */
@GPService
public class QueryService implements IQueryService {
    @Override
    public String query(String name) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String json ="{name:"+name+",data:"+sdf.format(new Date())+"}";
        return json;
    }
}
