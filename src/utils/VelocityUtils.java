/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package utils;

import java.io.StringWriter;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;

public class VelocityUtils {

    /**
     * velocity模板工具类
     * @param params 渲染页面需要的参数
     * @param viewName 视图名称(不带后缀)
     */
    public static void render(Map<String, Object> params, String viewName) {
        //初始化并取得Velocity引擎
        VelocityEngine ve = new VelocityEngine();
        //ve.init();//第一种
        Properties prop = new Properties();
        prop.setProperty(Velocity.ENCODING_DEFAULT, "UTF-8");
        prop.setProperty(Velocity.INPUT_ENCODING, "UTF-8");
        prop.setProperty(Velocity.OUTPUT_ENCODING, "UTF-8");
        ve.init(prop);//第二种,目的是解决页面中文乱码。
        //ve.init(propsFilename);//属性文件的位置
        //取得velocity的模板
        Template template = ve.getTemplate(viewName + ".vm");
        //取得velocity的上下文context
        VelocityContext context = new VelocityContext();
        //把数据填入上下文
        for (Entry<String, Object> entry : params.entrySet()) {
            context.put(entry.getKey(), entry.getValue());
        }
        //输出流 
        StringWriter writer = new StringWriter();
        //转换输出 
        template.merge(context, writer);
        System.out.println(writer.toString());
    }

}
