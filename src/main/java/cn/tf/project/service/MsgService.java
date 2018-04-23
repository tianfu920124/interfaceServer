package cn.tf.project.service;

import cn.tf.project.common.SmsProvider;
import com.chinamobile.openmas.client.Sms;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;

/**
 * File: MsgService.java
 * Description: 住建局短信服务接口类
 *
 * @author 田福
 * Version: 1.0.0
 */
@Service
public class MsgService {
    /**
     * 获取证照库接口URL
     *
     * @param destinationAddresses 手机号码数组
     * @return
     * @throws Exception
     */
    public JSONObject sendMsg(String[] destinationAddresses, String message) throws Exception {
        if (destinationAddresses == null || destinationAddresses.length <= 0
                || message == null || message.isEmpty()) return null;

        JSONObject sendResult = new JSONObject();
        try {
            Sms sms = SmsProvider.getSms();
            String extendCode = "0101"; //自定义扩展代码（模块）
            String GateWayid = sms.SendMessage(destinationAddresses, message, extendCode, SmsProvider.APPLICATIONID, SmsProvider.PASSWORD);
            sendResult.put("status", "0");
            sendResult.put("GateWayid", GateWayid);
        } catch (Exception ex) {
            sendResult.put("status", "1");
            sendResult.put("errmassage", "发送失败！" + ex.getMessage());
        }

        return sendResult;
    }

    /**
     * MD5加密算法
     *
     * @param s
     * @return
     * @throws Exception
     */
    public static String getMD5(String s) throws Exception {
        if (s == null)
            return "";
        else
            return getMD5(s.getBytes("utf-8"));
    }

    public static String getMD5(byte abyte0[]) {
        String s = null;
        char ac[] = {
                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f'
        };
        try {
            MessageDigest messagedigest = MessageDigest.getInstance("MD5");
            messagedigest.update(abyte0);
            byte abyte1[] = messagedigest.digest();
            char ac1[] = new char[32];
            int i = 0;
            for (int j = 0; j < 16; j++) {
                byte byte0 = abyte1[j];
                ac1[i++] = ac[byte0 >>> 4 & 15];
                ac1[i++] = ac[byte0 & 15];
            }

            s = new String(ac1);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return s;
    }
}
