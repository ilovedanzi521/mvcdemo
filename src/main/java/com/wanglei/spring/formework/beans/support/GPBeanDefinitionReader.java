package com.wanglei.spring.formework.beans.support;

import com.wanglei.spring.formework.beans.config.GPBeanDefinition;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ListResourceBundle;
import java.util.Properties;

/**
 * @ClassName GPBeanDefinitionReader
 * @Description TODO
 * @Author yuman
 * @Date 2019/4/11 18:50
 * @菜鸡加油 run run run
 */
public class GPBeanDefinitionReader {
    private List<String> registyBeanClassName = new ArrayList<String>();
    private Properties config = new Properties();

    private final  String SCAN_PACKAGE="scanPackage";
    public GPBeanDefinitionReader(String... configLoaction) {
        InputStream is =this.getClass().getClassLoader().
                getResourceAsStream(configLoaction[0].replace("classpath:",""));
        try {
            config.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(null!=is){
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        doScanner(config.getProperty(SCAN_PACKAGE));

    }

    private void doScanner(String scanpackage) {
        URL url = this.getClass().getClassLoader().
                getResource("/"+scanpackage.replaceAll("\\.","/"));
        File classpath = new File(url.getFile());
        for (File file : classpath.listFiles()) {
            if(file.isDirectory()){
                doScanner(scanpackage+"."+file.getName());
            }
            else{
                if(!file.getName().endsWith(".class")){
                    continue;
                }
                String className = scanpackage +"."+file.getName().replace(".class","");
                registyBeanClassName.add(className);
            }
        }

    }

    public List<GPBeanDefinition> loadBeanDefinitions() {
        List<GPBeanDefinition> result = new ArrayList<GPBeanDefinition>();
        for (String classname : registyBeanClassName) {
            GPBeanDefinition beanDefinition = doCreateBeanDefinition(classname);
            if(beanDefinition==null){
                continue;
            }
            result.add(beanDefinition);


        }

        return result;
    }

    /**
     * 配置信息解析成beanDefinition
     * @param classname
     * @return
     */
    private GPBeanDefinition doCreateBeanDefinition(String classname) {
        try {
            Class<?> beanclass = Class.forName(classname);
            //可能是个接口
            if(!beanclass.isInterface()){
               GPBeanDefinition beanDefinition = new GPBeanDefinition();
               beanDefinition.setBeanClassName(classname);
               //beanName
               beanDefinition.setFactoryBeanName(beanclass.getSimpleName());
               return beanDefinition;
            }else{
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Properties getConfig() {
        return config;
    }
}
