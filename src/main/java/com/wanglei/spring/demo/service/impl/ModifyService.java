package com.wanglei.spring.demo.service.impl;

import com.wanglei.spring.demo.service.IModifyService;
import com.wanglei.spring.formework.annotation.GPService;

/**
 * @ClassName ModifyService
 * @Description TODO
 * @Author yuman
 * @Date 2019/4/14 13:44
 * @菜鸡加油 run run run
 */
@GPService
public class ModifyService implements IModifyService {
    @Override
    public String add(String name, String addr) {
        return "add,name ="+name +",addr="+addr;
    }

    @Override
    public String edit(Integer id, String name) {
        return "edit,id="+id+",name"+name;
    }

    @Override
    public String remove(Integer id) {
        return "remove,id="+id;
    }
}
