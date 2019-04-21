package com.wanglei.spring.formework.context;

import com.wanglei.spring.formework.annotation.GPAutowired;
import com.wanglei.spring.formework.annotation.GPController;
import com.wanglei.spring.formework.annotation.GPService;
import com.wanglei.spring.formework.beans.GPBeanFactory;
import com.wanglei.spring.formework.beans.GPBeanWrapper;
import com.wanglei.spring.formework.beans.config.GPBeanDefinition;
import com.wanglei.spring.formework.beans.support.GPBeanDefinitionReader;
import com.wanglei.spring.formework.beans.support.GPDefaultListableBeanFactory;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @ClassName GPApplicationContext
 * @Description TODO
 * @Author yuman
 * @Date 2019/4/11 13:45
 * @菜鸡加油 run run run
 */
public class GPApplicationContext extends GPDefaultListableBeanFactory implements GPBeanFactory {
    private String[] configLoaction;
    private GPBeanDefinitionReader reader;

    private Map<String,Object> singletonObjects = new ConcurrentHashMap<String,Object>();
    //ioc容器
    private Map<String,GPBeanWrapper> factoryBeanInstanceCache = new ConcurrentHashMap<String,GPBeanWrapper>();


    public GPApplicationContext(String... configLoaction) {
        this.configLoaction = configLoaction;
        try {
            refresh();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void refresh() throws Exception {
        //1 定位
        reader = new GPBeanDefinitionReader(this.configLoaction);

        //2 加载
        List<GPBeanDefinition> beanDefinitions = reader.loadBeanDefinitions();
        //3注册
        doRegisterBeanDefinition(beanDefinitions);
        
        //4 NOT LAZY INIT
        doAutowrited();
    }
    //not lazy init
    private void doAutowrited() {
        for (Map.Entry<String, GPBeanDefinition> beanDefinitionEntry : super.beanDefinitionMap.entrySet()) {
             String beanName = beanDefinitionEntry.getKey();
             if(!beanDefinitionEntry.getValue().isLazyInit()){
                 getBean(beanName);
             }

        }

    }

    private void doRegisterBeanDefinition(List<GPBeanDefinition> beanDefinitions) throws Exception {
        for (GPBeanDefinition beanDefinition : beanDefinitions) {
            if(super.beanDefinitionMap.containsKey(beanDefinition.getFactoryBeanName())){
                throw  new Exception("the "+beanDefinition.getFactoryBeanName()+"is exsit");
            }
            super.beanDefinitionMap.put(beanDefinition.getBeanClassName(),beanDefinition);
            super.beanDefinitionMap.put(beanDefinition.getFactoryBeanName(),beanDefinition);
        }

    }

    @Override
    public Object getBean(String beanname) {
        //1 初始化

        GPBeanWrapper beanWrapper =
                instantiateBean(beanname,this.beanDefinitionMap.get(beanname));

//        if(this.factoryBeanInstanceCache.containsKey(beanname)){
//            try {
//                throw new Exception("the "+beanname +"is exists");
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
        this.factoryBeanInstanceCache.put(beanname,beanWrapper);
        //2 注入
        //
        populateBean(beanname,new GPBeanDefinition(),beanWrapper);


        return  this.factoryBeanInstanceCache.get(beanname);
    }



    private GPBeanWrapper instantiateBean(String beanname, GPBeanDefinition gpBeanDefinition) {

        //强吻，直接用反射生成object，spring使用代理
        Object beanInstance = null;
        String classname = gpBeanDefinition.getBeanClassName();
        try {
            if(this.singletonObjects.containsKey(classname)){
                beanInstance = this.singletonObjects.get(classname);
            }else{


            Class<?> clazz = Class.forName(classname);
            beanInstance = clazz.newInstance();
            this.singletonObjects.put(classname,beanInstance);
            this.singletonObjects.put(beanname,beanInstance);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        GPBeanWrapper beanWrapper =  new GPBeanWrapper(beanInstance);
        return beanWrapper;

    }

    private void populateBean(String beanname, GPBeanDefinition gpBeanDefinition, GPBeanWrapper gpBeanWrapper) {

        Object instance = gpBeanWrapper.getWrappedInstance();
       Class<?> clazz= gpBeanWrapper.getWrappedClass();
        //对加了注解的类，执行注入
        if(!(clazz.isAnnotationPresent(GPController.class)||
        clazz.isAnnotationPresent(GPService.class))){
            return;
        }
       Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if(!field.isAnnotationPresent(GPAutowired.class)){continue;}
           GPAutowired autowired = field.getAnnotation(GPAutowired.class);
           String autowireBeanname = autowired.value().trim();
           if ("".equals(autowireBeanname)){
               autowireBeanname = field.getType().getName();
           }
           field.setAccessible(true);
            try {
                field.set(instance,this.factoryBeanInstanceCache.get(autowireBeanname));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

    }

    public Properties getConfig() {
        return this.reader.getConfig();
    }
}
