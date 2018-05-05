package cn.tf.project.service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import com.timevale.esign.result.seal.CompressSealResult;
import com.timevale.esign.result.seal.MakeTempSealResult;
import com.timevale.esign.result.seal.SealAddResult;
import com.timevale.esign.result.seal.TemplateAddResult;
import com.timevale.esign.result.sign.SignInfoResult;
import com.timevale.esign.sdk.seal.WebSealServiceImpl;
import esign.bean.WebSealBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

// Import 电子印章SDK
import com.timevale.esign.result.account.LoginResult;
import com.timevale.esign.result.account.SealListResult;
import com.timevale.esign.result.account.SealResult;
import com.timevale.esign.result.account.SelfInfoResult;
import com.timevale.esign.sdk.account.AccountService;
import com.timevale.esign.sdk.account.AccountServiceImpl;
import esign.bean.PosBean;
import com.timevale.esign.result.file.SignPDFResult;
import com.timevale.esign.sdk.file.LocalFileService;
import com.timevale.esign.sdk.file.LocalFileServiceImpl;
import com.timevale.esign.sdk.seal.WebSealService;
import com.timevale.esign.result.BaseResult;

import javax.persistence.criteria.CriteriaBuilder;

/**
 * File: SealService.java
 * Description: 电子签章SDK服务接口类
 *
 * @author 田福
 * Version: 1.0.0
 */
@Service
public class SealService {
    /*
     *对象定义
     */
    @Autowired
    @Qualifier("AccountServiceImpl")
    private static AccountService account;// 第三方应用登录实体类

//    @Autowired
//    @Qualifier("LocalFileServiceImpl")
//    private static LocalFileService localfileservice; // 本地文件签章实体类

    @Autowired
    private static LoginResult login;//实际用来验证登录信息的类，主要通过此方法获取用户id（在天谷系统中唯一识别码）
    @Autowired
    private static String appkey;// 登录所必须的，在我方系统中唯一的参数
    @Autowired
    private static String accountId;// 定义账户ID用于获取用户所有信息

    /**
     * 登录服务
     *
     * @param ukey 帐户名
     * @return 返回结果（true: 登录成功 false: 登录失败）
     * Description: 用login（LoginResult）接收appkey，参考接口文档（3.7），返回值login（LoginResult）参考接口文档（附录二 对象解释 2.1）
     */
    public JSONObject login(String ukey) throws JSONException {
        if (ukey == null || ukey.isEmpty()) new JSONObject("{\"errormessage\":\"ParamError_NULL\"}");

        appkey = ukey;
        // appkey = "18668212948";

        account = new AccountServiceImpl();
        login = account.appLogin(appkey);

        int errcod = login.getErrCode(); // 获取登录后的代码（返回0代表成功，1代表失败）
        String errmassage = login.getMsg(); // 获取登录账号后返回的错误信息，如果登录成功就不返回

        accountId = login.getAccountId(); // 获取登录成功后的accountid，获取印章列表也是需要这个参数

        // 判断用户是否登录成功
        JSONObject loginResult = new JSONObject();
        if (errcod == 0) {
            loginResult.put("status", errcod);
            loginResult.put("accountid", accountId);
            return loginResult; // 登录成功返回accountid(用户id);
        } else {
            login = null; // 登录失败则清空登录信息
            loginResult.put("status", errcod);
            loginResult.put("errmassage", errmassage);
            return loginResult; // 登录失败后返回错误信息
        }

    }

    /**
     * 获取登录后的账户所有信息
     *
     * @return 返回账户所有信息
     * Description: 用selfinforesult接收用户信息，参考接口文档（3.9），
     * 返回值selfinforesult（SelfInfoResult），参考接口文档（附录二 对象解释 2.11）
     */
    public JSONObject getSelfInfo() throws JSONException {
        if (login == null) return new JSONObject("{\"errormessage\":\"AccountError_NULL\"}");

        //登录成功后可获取账户所有信息的类
        JSONObject selfResult = new JSONObject();
        SelfInfoResult selfinforesult = account.getSelfinfo(accountId);//通过登录成功后返回的accountid获取账户信息
        selfResult.put("result", selfinforesult.toString());
        return selfResult;
    }

    /**
     * 获取印章信息
     *
     * @return Description: 此步是为了账户下已经绑定的所有印章信息(主要获取印章id)，每一个印章都有自己唯一编号sealid
     * 参考接口文档6.1，返回值seallistresult（seallistresult），参考接口文档（附录二 对象解释 2.6）
     * 返回值sealresult（SealResult），，参考接口文档（附录二 对象解释 2.7）
     */
    public JSONArray getSealInfo() throws JSONException {
        if (login == null) return new JSONArray("{\"errormessage\":\"AccountError_NULL\"}");

        // 定义获取印章信息的实体类
        SealListResult seallistresult = account.getSealList(accountId, null, null, 0);

        JSONArray sealResult = new JSONArray();
        if (seallistresult != null) {
            List sealList = new ArrayList<SealResult>();
            sealList = seallistresult.getSeals();

            for (int i = 0; i < sealList.size(); i++) {
                JSONObject obj = new JSONObject();
                SealResult seal = (SealResult) sealList.get(i);
                obj.put("sealId", seal.getSealId());
                obj.put("name", seal.getName());
                sealResult.put(obj);
            }
        }

        return sealResult;
    }

    /**
     * 本地PDF签章
     *
     * @param param type: JSONObject
     *              keys:
     *              signType: 1.单页  2.多页 3.骑缝章 4.关键字，默认1
     *              sealId: 印章ID，可空，为0表示用默认印章签署，默认0
     *              srcPdfFile,dstPdfFile: 输入文件、输出文件 如果传入文件流则必须为空
     *              fileByte: 文件流 如果传入文件则必须为空
     *              email: 邮件地址或手机号码，可空
     *              posType: 定义盖章类型，0.坐标盖章；1.关键字盖章 默认0，可空
     *              posPage: 签署页码，可空，若为多页签章，支持页码格式“1 1-3 5,8“，若为坐标定位时不可空
     *              posX,posY: 签署位置X、Y坐标，若为关键字定位，相对于关键字的坐标偏移量，默认0
     *              posKey: 关键字，仅限关键字签章时有效，若为关键字定位时，不可空
     * @return
     * @throws JSONException
     */
    public JSONObject localSignPDF(JSONObject param) throws JSONException {
        if (login == null) return new JSONObject("{\"errormessage\":\"AccountError_NULL\"}");
        if (param == null) return new JSONObject("{\"errormessage\":\"ParamError_NULL\"}");

        /**
         * 检查参数
         */
        if (!param.has("fileByte")) {
            if (!param.has("srcPdfFile") || !param.has("dstPdfFile")) {
                return new JSONObject("{\"errormessage\":\"ParamError_srcPdfFile\"}");
            }
        } else if (param.has("srcPdfFile") || param.has("dstPdfFile")) {
            return new JSONObject("{\"errormessage\":\"ParamError_fileByte\"}");
        }
        if ((!param.has("posType") || param.get("posType") == "0")
                && !param.has("posPage")) {
            return new JSONObject("{\"errormessage\":\"ParamError_posPage\"}");
        }
        if (param.has("posType") && param.get("posType") == "1"
                && !param.has("posKey")) {
            return new JSONObject("{\"errormessage\":\"ParamError_posKey\"}");
        }

        /**
         * 设置参数
         */
        Integer signType = param.has("signType") ? Integer.parseInt((String) param.get("signType")) : null;
        Integer sealId = param.has("sealId") ? Integer.parseInt((String) param.get("sealId")) : null;
        String email = param.has("email") ? (String) param.get("email") : null;
        byte[] fileByte = param.has("fileByte") ? (byte[]) param.get("fileByte") : null;
        String srcPdfFile = param.has("srcPdfFile") ? (String) param.get("srcPdfFile") : null;
        String dstPdfFile = param.has("dstPdfFile") ? (String) param.get("dstPdfFile") : null;
        PosBean posbean = new PosBean();//定义盖章位置信息
        Integer posType = Integer.parseInt((String) param.get("posType"));
        posbean.setPosType(posType.toString());
        switch (posType) {
            case 0:
                String posPage = param.has("posPage") ? (String) param.get("posPage") : null;
                Integer posX = param.has("posX") ? Integer.parseInt((String) param.get("posX")) : null;
                Integer posY = param.has("posY") ? Integer.parseInt((String) param.get("posY")) : null;
                posbean.setPosPage(posPage);
                posbean.setPosX(posX);
                posbean.setPosY(posY);
                break;
            case 1:
                String posKey = param.has("posKey") ? (String) param.get("posKey") : null;
                posbean.setKey(posKey);
                break;
        }

        /**
         * 备份原文件
         */


        SignPDFResult signPdfResult = new SignPDFResult();
        LocalFileService localfileservice = new LocalFileServiceImpl();
        if (fileByte != null) {
            // 本地文件流签署（云证书签署）
            signPdfResult = localfileservice.localSignPDF(accountId, email, fileByte, sealId, signType, posbean);
        } else {
            // 本地文件签署（云证书签署）
            signPdfResult = localfileservice.localSignPDF(accountId, email, srcPdfFile, dstPdfFile, sealId, signType, posbean);
        }
        int errcod = signPdfResult.getErrCode();
        String errmassage = signPdfResult.getMsg();

        JSONObject signResult = new JSONObject();
        if (errcod == 0) {
            signResult.put("status", errcod);
            signResult.put("signPdfResult", signPdfResult.toString());
        } else {
            signResult.put("status", errcod);
            signResult.put("errmassage", errmassage);
        }

        return signResult;
    }

    /**
     * 本地PDF文件验签
     *
     * @param filePath 文件路径
     * @return
     * @throws JSONException
     */
    public JSONObject getSignInfoForPDF(String filePath) throws JSONException, UnsupportedEncodingException {
        if (login == null) return new JSONObject("{\"errormessage\":\"AccountError_NULL\"}");
        if (filePath == null) return new JSONObject("{\"errormessage\":\"ParamError_NULL\"}");

//        filePath = URLEncoder.encode(filePath, "utf-8");

        SignInfoResult signInfoResult = new SignInfoResult();
        LocalFileService localfileservice = new LocalFileServiceImpl();
        JSONObject signResult = new JSONObject();
        try {
            signInfoResult = localfileservice.getSignInfoForPDF(filePath);
            int errcod = signInfoResult.getErrCode();
            String errmassage = signInfoResult.getMsg();

            if (errcod == 0) {
                signResult.put("status", errcod);
                signResult.put("signInfoResult", signInfoResult.toString());
            } else {
                signResult.put("status", errcod);
                signResult.put("errmassage", errmassage);
            }
        } catch (Exception ex) {
            signResult.put("status", "500");
            signResult.put("errmassage", ex.getMessage());
        }

        return signResult;
    }
}
