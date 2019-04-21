package com.wanglei.spring.formework.webmvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @ClassName GPView
 * @Description TODO
 * @Author yuman
 * @Date 2019/4/19 21:35
 * @菜鸡加油 run run run
 */
public class GPView {
    public static final String DEFAULT_CONTENT_TYPE = "text/html;charset=utf-8";
    private File viewFile;

    public GPView(File templateFile) {
        this.viewFile = templateFile;
    }
    private String getContentType(){
        return DEFAULT_CONTENT_TYPE;
    }

    public void render(Map<String,?> model, HttpServletRequest req, HttpServletResponse response) throws IOException {

        StringBuffer sb = new StringBuffer();
        RandomAccessFile ra=null;
        try {
             ra = new RandomAccessFile(this.viewFile,"r");
            String line = null;
            while(null!=(line=ra.readLine())){
                line = new String(line.getBytes("ISO-8859-1"),"utf-8");
                Pattern pattern = Pattern.compile("￥\\{[^\\}]+\\}}",Pattern.CASE_INSENSITIVE) ;
                Matcher matcher = pattern.matcher(line);
                while(matcher.find()){
                    String paramName = matcher.group();
                    paramName = paramName.replaceAll("￥\\{|\\}","");
                    Object paramValue = model.get(paramName);
                    if(null==paramValue) continue;
                    line = matcher.replaceFirst(makeStringForRegExp(paramValue.toString()));
                    matcher = pattern.matcher(line);

                }
                sb.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(ra!=null) {
                ra.close();
            }
        }
        req.setCharacterEncoding("utf-8");
        response.getWriter().write(sb.toString());

    }

    private String makeStringForRegExp(String str) {
        return str.replace("\\","\\\\").replace("*","\\*")
                .replace("+","\\+").replace("|","\\|")
                .replace("{","\\{").replace("}","\\}")
                .replace("(","\\(").replace(")","\\)")
                .replace("^","\\^").replace("$","\\$")
                .replace("[","\\[").replace("]","\\]")
                .replace("?","\\?").replace(",","\\,")
                .replace(".","\\.").replace("&","\\&");
    }
}
