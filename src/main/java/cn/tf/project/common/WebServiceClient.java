package cn.tf.project.common;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.axis2.AxisFault;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.rpc.client.RPCServiceClient;
import org.apache.axis2.transport.http.HTTPConstants;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Iterator;
import java.util.Map;

@Component
public class WebServiceClient {
    /**
     * 定义公共属性方法
     */
    @Value("${webService.timeOutInMilliSeconds}")
    private String timeOutInMilliSeconds;

    /**
     * 定义Rest服务相关属性方法
     */
    private static SimpleClientHttpRequestFactory requestFactory = null; // 利用客户端设置超时
    private static RestTemplate restTemplate = null;

    private SimpleClientHttpRequestFactory getRequestFactory() {
        if (requestFactory == null) {
            int timeOut = Integer.parseInt(this.timeOutInMilliSeconds);
            requestFactory = new SimpleClientHttpRequestFactory();
            requestFactory.setConnectTimeout(timeOut);
            requestFactory.setReadTimeout(timeOut);
        }
        return requestFactory;
    }

    private RestTemplate getRestTemplate() {
        if (restTemplate == null)
            restTemplate = new RestTemplate(this.getRequestFactory());
        return restTemplate;
    }

    /**
     * 调用Rest的WebService方法
     * @param uri 统一资源标志符
     * @return
     */
    public String callRestService(URI uri){
        RestTemplate restTemplate =this.getRestTemplate();
        return restTemplate.getForObject(uri, String.class);
    }

    /**
     * 定义Soap服务相关属性方法
     */
    private static RPCServiceClient serviceClient = null; // 使用RPC方式调用SoapService
    private static OMFactory omFactory = null; // 由抽象OM工厂获取OM工厂，创建request SOAP包

    private RPCServiceClient getServiceClient() throws AxisFault {
        if (serviceClient == null)
            serviceClient = new RPCServiceClient();
        return serviceClient;
    }

    private OMFactory getOmFactory() {
        if (omFactory == null)
            omFactory = OMAbstractFactory.getOMFactory();
        return omFactory;
    }

    /**
     * 调用Soap的WebService方法
     *
     * @param serviceName 服务方法名
     * @param param       传入参数
     * @return
     * @throws JSONException
     */
    public OMElement callSoapService(String url, String namespace, String serviceName, JSONObject param) throws JSONException, AxisFault {
        String namespaceURI = namespace;
        long timeOut = Long.parseLong(this.timeOutInMilliSeconds);
        if (url == null || url.isEmpty() || serviceName == null || serviceName.isEmpty()
                || namespaceURI == null || namespaceURI.isEmpty()) return null;

        RPCServiceClient client = this.getServiceClient();
        OMFactory fac = this.getOmFactory();

        // 设置参数
        EndpointReference targetEPR = new EndpointReference(url); // 指定调用WebService的URL
        Options options = client.getOptions(); //创建request soap包 请求选项
        options.setTo(targetEPR); //设置request soap包的端点引用(接口地址)
        options.setCallTransportCleanup(true); // 自动释放传输
        options.setProperty(HTTPConstants.CHUNKED, false); //把chunk关掉后，会自动加上Content-Length。
        options.setAction(namespaceURI + serviceName); // 设置options的soapAction
        options.setTimeOutInMilliSeconds(timeOut); // 设置超时时间

        // 创建命名空间
        OMNamespace nms = fac.createOMNamespace(namespaceURI, serviceName);
        //创建OMElement方法 元素，并指定其在nms指代的名称空间中
        OMElement method = fac.createOMElement(serviceName, nms);
        if (param != null) {
            Iterator paramKeys = param.keys();
            while (paramKeys.hasNext()) {
                String key = (String) paramKeys.next();
                String value = param.getString(key);

                //创建方法参数OMElement元素
                OMElement om = fac.createOMElement(key, nms);
                //设置键值对 参数值
                om.setText(value);
                //讲方法元素 添加到method方法元素中
                method.addChild(om);
            }
        }

        // 返回结果
        return client.sendReceive(method);
    }
}
