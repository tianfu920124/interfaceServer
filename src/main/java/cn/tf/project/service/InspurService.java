package cn.tf.project.service;

import org.json.JSONObject;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.Iterator;
import java.util.Map;

import cn.tf.project.configuration.PropertiesConfig;

/**
 * File: InspurService.java
 * Description: 浪潮（证照库、一窗受理）服务接口类
 *
 * @author 田福
 * Version: 1.0.0
 */
@Service
public class InspurService {
    /*
     *对象定义
     */
    @Autowired
    private PropertiesConfig propertiesConfig;

    /**
     * 获取证照模板列表接口服务
     *
     * @param itemCode 事项编码
     * @return
     * @throws Exception
     */
    public String getLicenceTypeByItemCode(String itemCode) throws Exception {
        if (itemCode == null || itemCode.isEmpty()) return null;

        // 服务方法名和参数列表
        String serviceName = "getLicenceTypeByItemCode";
        String paramList = "itemCode=" + URLEncoder.encode(itemCode, "utf-8");

        RestTemplate restTemplate = new RestTemplate();
        URI uri = getWebLicenseServiceUri(serviceName, paramList);
        String result = restTemplate.getForObject(uri, String.class);

        return result;
    }

    /**
     * 获取模板照面信息接口服务
     *
     * @param licenseTypeCode 证照模板编码
     * @param holderType      持证者类型
     * @param itemCode        事项编码
     * @return
     * @throws Exception
     */
    public String getPaginateSurfaceByType(String licenseTypeCode, String holderType, String itemCode) throws Exception {
        if (licenseTypeCode == null || licenseTypeCode.isEmpty()
                || holderType == null || holderType.isEmpty()
                || itemCode == null || itemCode.isEmpty()) return null;

        // 获取服务方法名和参数列表
        String serviceName = "getPaginateSurfaceByType";
        String paramList = "licenseTypeCode=" + URLEncoder.encode(licenseTypeCode, "utf-8") +
                "&holderType=" + URLEncoder.encode(holderType, "utf-8") +
                "&itemCode=" + URLEncoder.encode(itemCode, "utf-8");

        RestTemplate restTemplate = new RestTemplate();
        URI uri = getWebLicenseServiceUri(serviceName, paramList);
        String result = restTemplate.getForObject(uri, String.class);

        return result;
    }

    /**
     * 获取证照分页列表服务（根据查询条件）
     *
     * @param queryMap 查询条件
     * @return
     * @throws Exception
     */
    public String getLicenceListByPagination(JSONObject queryMap, Boolean isPagination, Integer page, Integer rows) throws Exception {
        if (queryMap == null) return null;

        // 获取服务URL
        String serviceName = "getLicenceListByPagination";
        String paramList = "queryMap=" + URLEncoder.encode(queryMap.toString(), "utf-8");
        if (isPagination != null) paramList += "&isPagination=" + isPagination;
        if (page != null) paramList += "&page=" + page;
        if (rows != null) paramList += "&rows=" + rows;

        RestTemplate restTemplate = new RestTemplate();
        URI uri = getWebLicenseServiceUri(serviceName, paramList);
        String result = restTemplate.getForObject(uri, String.class);

        return result;
    }

    /**
     * 获取证照列表（根据证照编号、部门编码、持证人名称或证照类型）
     *
     * @param queryMap 查询条件 license_number、holder_name、certificate_no中至少传递一个（且非空值）
     * @return
     * @throws Exception
     */
    public String getLicenceListByMutliRequirement(JSONObject queryMap) throws Exception {
        if (queryMap == null) return null;

        // 获取服务URL
        String serviceName = "getLicenceListByMutliRequirement";
        String paramList = "queryMap=" + URLEncoder.encode(queryMap.toString(), "utf-8");

        RestTemplate restTemplate = new RestTemplate();
        URI uri = getWebLicenseServiceUri(serviceName, paramList);
        String result = restTemplate.getForObject(uri, String.class);

        return result;
    }

    /**
     * 获取证照信息及附件（根据证照标识）
     *
     * @param licenseNo  证照标识
     * @param operatorId 操作人员ID（可选）
     * @param itemName   事项名称（可选）
     * @param itemCode   事项编码（可选）
     * @return
     * @throws Exception
     */
    public String getLicenceInfoAndFile(String licenseNo, String operatorId, String itemName, String itemCode) throws Exception {
        if (licenseNo == null || licenseNo.isEmpty()) return null;

        // 获取服务URL
        String serviceName = "getLicenceInfoAndFile";
        String paramList = "licenseNo=" + URLEncoder.encode(licenseNo, "utf-8");
        if (operatorId != null && !operatorId.isEmpty())
            paramList += "&operatorId=" + URLEncoder.encode(operatorId, "utf-8");
        if (itemName != null && !itemName.isEmpty())
            paramList += "&itemName=" + URLEncoder.encode(itemName, "utf-8");
        if (itemCode != null && !itemCode.isEmpty())
            paramList += "&itemCode=" + URLEncoder.encode(itemCode, "utf-8");

        RestTemplate restTemplate = new RestTemplate();
        URI uri = getWebLicenseServiceUri(serviceName, paramList);
        String result = restTemplate.getForObject(uri, String.class);

        return result;
    }

    /**
     * 根据事项CODE获取所有事项相关信息(基本要素、申请材料、法律依据、办理流程、事项收费、受理条件)
     *
     * @param itemCode 事项编码
     * @param type     信息类型
     *                 基本要素	info
     *                 申请材料	material
     *                 标准文书	document
     *                 法律依据	legalbasis
     *                 办理流程	handlingprocess
     *                 事项收费	charge
     *                 受理条件	condition
     *                 办理地址	window
     *                 平台流程	process
     *                 前置审批	frontitem
     *                 外部流程图	outmap
     *                 预约时间	reserve
     * @return
     * @throws Exception
     */
    public String getItemInfoByItemCode(String itemCode, String type) throws Exception {
        if (itemCode == null || itemCode.isEmpty()) return null;

        // 获取服务URL
        String serviceName = "getItemInfoByItemCode";
        String paramList = "itemCode=" + URLEncoder.encode(itemCode, "utf-8");
        if (type != null && !type.isEmpty()) paramList += "&type=" + type;

        RestTemplate restTemplate = new RestTemplate();
        URI uri = getWebLicenseServiceUri(serviceName, paramList);
        String result = restTemplate.getForObject(uri, String.class);

        return result;
    }

    /**
     * 获取表单信息（根据事项ID）
     *
     * @param itemId 事项ID
     * @return
     * @throws Exception
     */
    public String getFormInfo(String itemId) throws Exception {
        if (itemId == null || itemId.isEmpty()) return null;

        // 获取服务URL
        String serviceName = "getFormInfo";
        String paramList = "itemId=" + URLEncoder.encode(itemId, "utf-8");

        RestTemplate restTemplate = new RestTemplate();
        URI uri = getCloudAcceptServiceUri(serviceName, paramList);
        String result = restTemplate.getForObject(uri, String.class);

        return result;
    }

    /**
     * 网上申报
     *
     * @param postData 提交数据
     * @return
     * @throws Exception
     */
    public String webApply(JSONObject postData) throws Exception {
        if (postData == null) return null;

        // 获取服务URL
        String serviceName = "webApply";
        String paramList = "postData=" + URLEncoder.encode(postData.toString(), "utf-8");

        RestTemplate restTemplate = new RestTemplate();
        URI uri = getCloudAcceptServiceUri(serviceName, paramList);
        String result = restTemplate.getForObject(uri, String.class);

        return result;
    }

    /**
     * 获取Inspur证照库服务URI
     *
     * @param serviceName 服务方法名称
     * @param paramList   参数列表(参数值要进行编码，URLEncoder.encode(paramValue,"utf-8"))
     * @return
     * @throws Exception
     */
    private URI getWebLicenseServiceUri(String serviceName, String paramList) throws Exception {
        Map<String, String> webLicenseMap = propertiesConfig.getWebLicense();
        String url = webLicenseMap.get("serverBaseUrl");
        String appCode = webLicenseMap.get("appCode");
        String accessToken = webLicenseMap.get("accessToken");
        if (url == null || url.isEmpty()
                || appCode == null || appCode.isEmpty()
                || accessToken == null || accessToken.isEmpty()) return null;

        if (paramList != null && !paramList.isEmpty())
            paramList += "&";

        // 获取参数和签名
        long timeStamp = System.currentTimeMillis();
        String sign = getMD5(paramList + "appCode=" + appCode + "&time=" + timeStamp + "&accessToken=" + accessToken);

        // 获取服务URI
        url += serviceName + "?" + paramList + "appCode=" + appCode + "&time=" + timeStamp + "&sign=" + sign;
        URI uri = new URI(url);

        return uri;
    }

    /**
     * 获取浪潮一窗受理服务URI
     *
     * @param serviceName 服务方法名称
     * @param paramList   参数列表(参数值要进行编码，URLEncoder.encode(paramValue,"utf-8"))
     * @return
     * @throws Exception
     */
    private URI getCloudAcceptServiceUri(String serviceName, String paramList) throws Exception {
        Map<String, String> cloudAcceptMap = propertiesConfig.getCloudAccept();
        String url = cloudAcceptMap.get("serverBaseUrl");
        if (url == null || url.isEmpty()) return null;

        if (paramList != null && !paramList.isEmpty())
            paramList += "&";

        // 获取参数和签名
//        long timeStamp = System.currentTimeMillis();
//        String sign = getMD5(paramList + "appCode=" + appCode + "&time=" + timeStamp + "&accessToken=" + accessToken);

        // 获取服务URI
//        url += serviceName + "?" + paramList + "appCode=" + appCode + "&time=" + timeStamp + "&sign=" + sign;
//        URI uri = new URI(url);
        url += serviceName + "?" + paramList;
        URI uri = new URI(url);

        return uri;
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
