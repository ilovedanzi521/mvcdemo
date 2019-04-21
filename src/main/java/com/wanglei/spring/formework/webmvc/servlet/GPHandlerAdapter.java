package com.wanglei.spring.formework.webmvc.servlet;

import com.wanglei.spring.formework.annotation.GPRequestParam;
import com.wanglei.spring.formework.webmvc.GPModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName GPHandlerAdapter
 * @Description TODO
 * @Author yuman
 * @Date 2019/4/19 13:45
 * @菜鸡加油 run run run
 */
public class GPHandlerAdapter {
    public boolean supports(Object handler){
        return (handler instanceof GPHandlerMapping);
    }

    public GPModelAndView handle(HttpServletRequest req, HttpServletResponse resp,
                                 Object Handler)throws Exception{
        GPHandlerMapping handlerMapping = (GPHandlerMapping) Handler;
        Map<String,Integer> paramMapping = new HashMap<String, Integer>();
        Annotation[][] pa = handlerMapping.getMethod().getParameterAnnotations();
        int i=0;
        for (Annotation[] annotations : pa) {
            i++;
            for (Annotation annotation : annotations) {
                if(annotation instanceof GPRequestParam){
                    String paramName = ((GPRequestParam) annotation).value();
                    if(!"".equals(paramName.trim())){
                        paramMapping.put(paramName,i);
                    }
                }
            }
        }
        Class<?>[] paramTypes = handlerMapping.getMethod().getParameterTypes();
        int j=0;
        for (Class<?> paramType : paramTypes) {
            if (paramType==HttpServletRequest.class||
            paramType==HttpServletResponse.class){
                paramMapping.put(paramType.getName(),j++);
            }
        }


        Map<String,String[]> reqParameterMap = req.getParameterMap();
        Object[] paramValues = new Object[paramTypes.length];

        for (Map.Entry<String, String[]> param : reqParameterMap.entrySet()) {

            String value = Arrays.toString(param.getValue()).replace("\\[|\\]","")
                    .replaceAll("\\s","");
            if(!paramMapping.containsKey(param.getKey())) continue;
            int index = paramMapping.get(param.getKey());
            paramValues[index] = caseStringValue(value,paramTypes[index]);
        }
        if(paramMapping.containsKey(HttpServletRequest.class.getName())){
            int reqIndex = paramMapping.get(HttpServletRequest.class.getName());
            paramValues[reqIndex]=req;
        }
        if(paramMapping.containsKey(HttpServletResponse.class.getName())){
            int reqIndex = paramMapping.get(HttpServletResponse.class.getName());
            paramValues[reqIndex]=resp;
        }
        Object result = handlerMapping.getMethod().invoke(handlerMapping.getController(),
                paramTypes);
        if (result == null) return null;
        boolean isModelAndView = handlerMapping.getMethod().getReturnType()==GPModelAndView.class;
        if(isModelAndView){
            return (GPModelAndView) result;
        }else{
            return null;
        }

    }

    private Object caseStringValue(String value, Class<?> clazz) {
        if (clazz==String.class) return value;
        else if (clazz==Integer.class) return Integer.valueOf(value);
        else if (clazz == int.class) return Integer.valueOf(value).intValue();
        else return null;
    }
}
