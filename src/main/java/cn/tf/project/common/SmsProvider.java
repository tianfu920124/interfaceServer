package cn.tf.project.common;

import com.chinamobile.openmas.client.Sms;
import com.chinamobile.openmas.entity.SmsMessage;
import org.apache.axis2.AxisFault;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Title: SmsProvider.java<br>
 * Description: ���Žӿڻ�ȡ<br>
 * Copyright (c) �ڴ���Ϣ��Ȩ���� 2012	<br>
 * Create DateTime: Jun 6, 2012 9:52:29 AM <br>
 *
 * @author ln
 */
public class SmsProvider {

    private static Sms SMS;
    public static String APPLICATIONID;
    public static String PASSWORD;
    private static String WEBSERVICE_URL;

    //��ʼ�����Žӿ�
    static {
        //ͨ��������ƻ�ȡ����db.properties�ļ�
        InputStream is = SmsProvider.class.getResourceAsStream("/paramConfig.properties");
        Properties prop = new Properties();
        try {
            //����db.properties�ļ�
            prop.load(is);
            //��ȡparamConfig.properties�ļ��е����ݿ�������Ϣ
            APPLICATIONID = prop.getProperty("paramConfig.openMas[ApplicationID]");
            PASSWORD = prop.getProperty("paramConfig.openMas[Password]");
            WEBSERVICE_URL = prop.getProperty("paramConfig.openMas[serverUrl]");

            SMS = new Sms(WEBSERVICE_URL);
        } catch (AxisFault e) {
            //TODO �����־��¼
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * ��ȡ���Žӿ�
     *
     * @return ���ض���Mms
     */
    public static Sms getSms() {
        if (SMS == null) {
            throw new RuntimeException("SMS init failure");
        }
        return SMS;
    }

    /**
     * ��ȡ��������
     *
     * @param messageId
     * @return
     */
    public static SmsMessage getMmsMessage(String messageId) {
        return getSms().GetMessage(messageId);
    }
}
