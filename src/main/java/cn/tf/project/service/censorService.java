package cn.tf.project.service;

import cn.tf.project.common.WebServiceClient;
import cn.tf.project.configuration.PropertiesConfig;
import org.apache.axiom.om.*;
import org.apache.axis2.AxisFault;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.rpc.client.RPCServiceClient;
import org.apache.axis2.transport.http.HTTPConstants;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import javax.xml.namespace.QName;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;

/**
 * File: CheckDrawService.java
 * Description: 电子审图服务接口类
 *
 * @author 田福
 * Version: 1.0.0
 */
@org.springframework.stereotype.Service
public class CensorService {
    /*
     *对象定义
     */
    @Autowired
    private PropertiesConfig propertiesConfig;

    @Autowired
    private WebServiceClient webServiceClient; // 调用WebService客户端

    /**
     * 调用Interface类型的服务
     *
     * @param serviceName 方法名
     * @param param       参数
     * @return
     * @throws JSONException
     * @throws AxisFault
     */
    public JSONObject callCensorService(String serviceName, JSONObject param, String serviceType) throws JSONException, AxisFault {
        Map<String, String> censorMap = propertiesConfig.getCensor();
        String namespace = censorMap.get("namespace");
        namespace = (namespace == null || namespace.isEmpty()) ? "http://tempuri.org/" : namespace;
        String url = null;
        if (serviceType == "" || serviceType == null || serviceType.equals("Interface"))
            url = censorMap.get("interfaceService");
        else if (serviceType.equals("CheckDraw"))
            url = censorMap.get("checkDrawService");
        else if (serviceType.equals("Material"))
            url = censorMap.get("fileService");
        if (url == null || url.isEmpty() || serviceName == null || serviceName.isEmpty()) return null;

        JSONObject result = new JSONObject();

        OMElement omResult = webServiceClient.callSoapService(url, namespace, serviceName, param);
        Iterator iterator = omResult.getChildElements();
        while (iterator.hasNext()) {
            OMElement om = (OMElement) iterator.next();
            if (om.getLocalName() == "strMsg") {
                result.put("status", "1");
                result.put(om.getLocalName(), om.getText());
                break;
            } else {
                result.put("status", "0");
                result.put(om.getLocalName(), om.getText());
            }
        }

        return result;
    }
}
