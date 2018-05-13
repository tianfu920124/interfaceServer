package cn.tf.project.common;

import com.chinamobile.openmas.client.Sms;
import com.chinamobile.openmas.entity.SmsMessage;
import org.apache.axis2.AxisFault;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Title: SmsProvider.java<br>
 * Description: 短信接口获取<br>
 * Copyright (c) 融创信息版权所有 2012	<br>
 * Create DateTime: Jun 6, 2012 9:52:29 AM <br>
 *
 * @author ln
 */
public class SmsProvider {

    private static Sms SMS;
    public static String APPLICATIONID;
    public static String PASSWORD;
    private static String WEBSERVICE_URL;

    //初始化短信接口
    static {
        //通过反射机制获取访问db.properties文件
        InputStream is = SmsProvider.class.getResourceAsStream("/paramConfig.properties");
        Properties prop = new Properties();
        try {
            //加载db.properties文件
            prop.load(is);
            //获取paramConfig.properties文件中的数据库连接信息
            APPLICATIONID = prop.getProperty("paramConfig.openMas[ApplicationID]");
            PASSWORD = prop.getProperty("paramConfig.openMas[Password]");
            WEBSERVICE_URL = prop.getProperty("paramConfig.openMas[serverUrl]");

            SMS = new Sms(WEBSERVICE_URL);
        } catch (AxisFault e) {
            //TODO 添加日志记录
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取短信接口
     *
     * @return 返回短信Mms
     */
    public static Sms getSms() {
        if (SMS == null) {
            throw new RuntimeException("SMS init failure");
        }
        return SMS;
    }

    /**
     * 获取短信详情
     *
     * @param messageId
     * @return
     */
    public static SmsMessage getMmsMessage(String messageId) {
        return getSms().GetMessage(messageId);
    }
}
