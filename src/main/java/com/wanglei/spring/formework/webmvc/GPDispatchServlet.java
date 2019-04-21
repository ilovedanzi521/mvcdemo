package com.wanglei.spring.formework.webmvc;

import com.wanglei.spring.formework.annotation.GPController;
import com.wanglei.spring.formework.annotation.GPRequestMapping;
import com.wanglei.spring.formework.context.GPApplicationContext;
import com.wanglei.spring.formework.webmvc.servlet.GPHandlerAdapter;
import com.wanglei.spring.formework.webmvc.servlet.GPHandlerMapping;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @ClassName GPDispatchServlet
 * @Description TODO
 * @Author yuman
 * @Date 2019/4/18 22:08
 * @菜鸡加油 run run run
 *
 */
@Slf4j
public class GPDispatchServlet extends HttpServlet {
    private static String INIT_PRAMS = "contextConfigLocations";
    private Map<GPHandlerMapping, GPHandlerAdapter> handlerAdapters = new HashMap<GPHandlerMapping,GPHandlerAdapter>();


    private  List<GPHandlerMapping> handler = new ArrayList<GPHandlerMapping>();

    private  List<GPViewResolver>  vrs = new ArrayList<GPViewResolver>();


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doGet(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            this.doDispather(req, resp);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void doDispather(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        GPHandlerMapping handlerMapping = getHandler(req);
        if(null==handlerMapping) return;
        GPHandlerAdapter ha = getHandlerAdapter(handlerMapping);
        //存储要放到页面的值和
        GPModelAndView mv= ha.handle(req,resp,handler);

        processDispatchResult(req,resp,mv);


    }

    private void processDispatchResult(HttpServletRequest req, HttpServletResponse resp, GPModelAndView mv) {

    }

    private GPHandlerAdapter getHandlerAdapter(GPHandlerMapping handlerMapping) {
       if (this.handlerAdapters.isEmpty()) return null;
       GPHandlerAdapter ha = this.handlerAdapters.get(handlerMapping);
       if (ha.supports(handlerMapping))return ha;

        return null;
    }

    private GPHandlerMapping getHandler(HttpServletRequest req) {
        if (this.handler.isEmpty()) return null;
        String url = req.getRequestURI();
        String contextPath = req.getContextPath();
        url = url.replace(contextPath,"").replaceAll("/+","/");
        for (GPHandlerMapping handlerMapping : this.handler) {
            Matcher matcher = handlerMapping.getPattern().matcher(url);
            if (!matcher.matches()) continue;
            return handlerMapping;
        }

        return null;
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        String configfile = config.getInitParameter(INIT_PRAMS);
        GPApplicationContext context = new GPApplicationContext(configfile);
        initStrategies(context);

    }

    /**
     * spring mvc 九大组件
     * @param context
     */
    protected void initStrategies(GPApplicationContext context) {
        this.initMultipartResolver(context);
        this.initLocaleResolver(context);
        this.initThemeResolver(context);
        //1实现
        this.initHandlerMappings(context);
        //2实现
        this.initHandlerAdapters(context);
        this.initHandlerExceptionResolvers(context);
        this.initRequestToViewNameTranslator(context);
        //3实现
        this.initViewResolvers(context);
        this.initFlashMapManager(context);
    }

    private void initFlashMapManager(GPApplicationContext context) {

    }

    private void initViewResolvers(GPApplicationContext context) {
        String templateRoot = context.getConfig().getProperty("templateRoot");
        String templateRootPath = this
                .getClass().getClassLoader()
                .getResource(templateRoot).getFile();
        File templdateRoootdir = new File(templateRootPath);
        for (File tempfile : templdateRoootdir.listFiles()) {
            this.vrs.add(new GPViewResolver(templateRoot));
        }


    }

    private void initRequestToViewNameTranslator(GPApplicationContext context) {
        
    }

    private void initHandlerExceptionResolvers(GPApplicationContext context) {
        
    }

    /**
     * 初始化ha
     * @param context
     */
    private void initHandlerAdapters(GPApplicationContext context) {
        for (GPHandlerMapping handlerMapping : this.handler) {
            this.handlerAdapters.put(handlerMapping,new GPHandlerAdapter());
        }


    }

    /**
     * 初始化handlermapping
     * @param context
     */
    private void initHandlerMappings(GPApplicationContext context) {
       String[] beanNames = context.getbeanDefinitionNames();
       try{
           for (String beanName : beanNames) {
               Object controller = context.getBean(beanName);
               Class<?> clazz = controller.getClass();
               if (!clazz.isAnnotationPresent(GPController.class)) continue;
               String baseurl ="";
               if(clazz.isAnnotationPresent(GPRequestMapping.class)){
                   GPRequestMapping requestMapping = clazz.getAnnotation(GPRequestMapping.class);
                   baseurl = requestMapping.value();
               }
               Method[] methods = clazz.getMethods();
               for (Method method : methods) {
                   if(!method.isAnnotationPresent(GPRequestMapping.class)) continue;
                    GPRequestMapping requestMapping = method.getAnnotation(GPRequestMapping.class);
                    String regex = ("/"+baseurl+requestMapping.value().
                            replaceAll("\\*",".").replaceAll("/+","/"));
                   Pattern pattern = Pattern.compile(regex);
                   this.handler.add(new GPHandlerMapping(pattern,controller,method));
//                   log.info("Mapping:"+regex+","+method);
               }


           }

       }catch (Exception e){
           e.printStackTrace();
       }


    }

    private void initThemeResolver(GPApplicationContext context) {
        
    }

    private void initLocaleResolver(GPApplicationContext context) {

    }

    private void initMultipartResolver(GPApplicationContext context) {
        
    }
}
