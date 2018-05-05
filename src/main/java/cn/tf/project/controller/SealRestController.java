package cn.tf.project.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
//import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

// Import Project
import cn.tf.project.service.SealService;
import cn.tf.project.entity.SignInfo;

import java.io.UnsupportedEncodingException;

/**
 * Seal控制层
 */
@Api(value = "SealRestController", description = "电子签章相关接口")
@RestController
@RequestMapping(value = "/seal/")
public class SealRestController {
    @Autowired
    private SealService sealService;

    /**
     * 登录接口
     * TestUrl: http://localhost:8090/seal/login?ukey=18668212948
     *
     * @param ukey 账户名
     * @return
     */
    @ApiOperation(value = "证书/云证书登录", notes = "证书/云证书登录接口", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ukey", value = "证书序列号", required = true, dataType = "string")
    })
    @RequestMapping(value = "login", method = RequestMethod.GET)
    @ResponseBody
    public String login(@RequestParam(value = "ukey") String ukey) throws JSONException {
        JSONObject loginResult = sealService.login(ukey);
        return loginResult.toString();
    }

    /**
     * 获取登录用户信息接口
     *
     * @return
     */
    @ApiOperation(value = "获取登录用户信息", notes = "获取登录用户信息接口", httpMethod = "GET",hidden = true)
    @RequestMapping(value = "getSelfInfo", method = RequestMethod.GET)
    @ResponseBody
    public String getSelfInfo() throws JSONException {
        JSONObject selfInfoResult = sealService.getSelfInfo();
        return selfInfoResult.toString();
    }

    /**
     * 获取登录用户印章信息接口
     *
     * @return
     */
    @ApiOperation(value = "获取登录用户印章信息", notes = "获取登录用户印章信息接口", httpMethod = "GET")
    @RequestMapping(value = "getSealInfo", method = RequestMethod.GET)
    @ResponseBody
    public String getSealInfo() throws JSONException {
        JSONArray sealInfoResult = sealService.getSealInfo();
        return sealInfoResult.toString();
    }

    /**
     * 本地PDF签章接口
     * Header: Content-Type : application/json
     * TestUrl: http://localhost:8080/seal/localSignPDF?param={"srcPdfFile":"d:\11.pdf","dstPdfFile":"d:\22.pdf","posType":"0","posPage":"1","posX":"300","posY":"200"}
     *
     * @param signInfo Json参数，用实体类接收，@RequestBody将Json格式字符串自动转换成实体对象
     * @return Json对象字符串，@RestController自动转换成Json对象
     * @throws JSONException
     */
    @ApiOperation(value = "本地PDF签章", notes = "本地PDF签章接口", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "signInfo", value = "签章信息", required = true, dataType = "SignInfo")
    })
    @RequestMapping(value = "localSignPDF", method = RequestMethod.POST)
    @ResponseBody
    public String localSignPDF(@RequestBody SignInfo signInfo) throws JSONException {
        JSONObject param = new JSONObject();
        if (signInfo.getSignType() != null) param.put("signType", signInfo.getSignType());
        if (signInfo.getSealId() != null) param.put("sealId", signInfo.getSealId());
        if (signInfo.getSrcPdfFile() != null) param.put("srcPdfFile", signInfo.getSrcPdfFile());
        if (signInfo.getDstPdfFile() != null) param.put("dstPdfFile", signInfo.getDstPdfFile());
        if (signInfo.getFileByte() != null) param.put("fileByte", signInfo.getFileByte());
        if (signInfo.getEmail() != null) param.put("email", signInfo.getEmail());
        if (signInfo.getPosType() != null) param.put("posType", signInfo.getPosType());
        if (signInfo.getPosPage() != null) param.put("posPage", signInfo.getPosPage());
        if (signInfo.getPosX() != null) param.put("posX", signInfo.getPosX());
        if (signInfo.getPosY() != null) param.put("posY", signInfo.getPosY());
        if (signInfo.getPosKey() != null) param.put("posKey", signInfo.getPosKey());
        JSONObject signResult = sealService.localSignPDF(param);
        return signResult.toString();
    }

    /**
     * 本地PDF文件验签接口
     *
     * @param filePath 文件路径
     * @return Json对象字符串
     * @throws JSONException
     */
    @ApiOperation(value = "本地PDF验签", notes = "本地PDF验签接口", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "filePath", value = "文件路径", required = true, dataType = "string")
    })
    @RequestMapping(value = "getSignInfoForPDF", method = RequestMethod.GET)
    @ResponseBody
    public String getSignInfoForPDF(@RequestParam(value = "filePath") String filePath) throws JSONException, UnsupportedEncodingException {
        JSONObject signResult = sealService.getSignInfoForPDF(filePath);
        return signResult.toString();
    }

    /**
     * 跨域配置方法
     *
     * @return
     */
    private CorsConfiguration buildConfig() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOrigin("*");
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.addAllowedMethod("*");
        return corsConfiguration;
    }

    /**
     * 跨域过滤器
     *
     * @return
     */
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", buildConfig()); // 4
        return new CorsFilter(source);
    }
}
