package cn.tf.project.controller;

import cn.tf.project.service.InspurService;
import cn.tf.project.service.ProjectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.net.URISyntaxException;
import java.sql.SQLException;

/**
 * Inspur控制层
 */
@Api(value = "InspurRestController", description = "浪潮（证照库、一窗受理、投资项目）相关接口")
@RestController
@RequestMapping(value = "/inspur/")
public class InspurRestController {
    @Autowired
    private InspurService inspurService;

    @Autowired
    private ProjectService projectService;

    /**
     * 获取证照模板列表接口（根据事项code）
     * Test: http://localhost:8090/inspur/getWebLisenceInfo?itemCode=39a5dc14-8833-4d53-ae4c-ec155817a20a
     *
     * @param itemCode 事项编码
     * @return
     */
    @ApiOperation(value = "获取证照模板列表", notes = "获取证照模板列表接口（根据事项code）", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "itemCode", value = "事项编码", required = true, dataType = "string")
    })
    @RequestMapping(value = "getLicenceTypeByItemCode")
    @ResponseBody
    public String getLicenceTypeByItemCode(@RequestParam(value = "itemCode") String itemCode) throws Exception {
        String result = inspurService.getLicenceTypeByItemCode(itemCode);

        return result;
    }

    /**
     * 获取模板照面信息接口（根据证照类型、持有者类型、事项）
     *
     * @param licenseTypeCode 证照模板编码
     * @param holderType      持证者类型
     * @param itemCode        事项编码
     * @return
     */
    @ApiOperation(value = "获取模板照面信息", notes = "获取模板照面信息接口（根据证照类型、持有者类型、事项）", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "licenseTypeCode", value = "证照模板编码", required = true, dataType = "string"),
            @ApiImplicitParam(name = "holderType", value = "持证者类型", required = true, dataType = "string"),
            @ApiImplicitParam(name = "itemCode", value = "事项编码", required = true, dataType = "string")
    })
    @RequestMapping(value = "getPaginateSurfaceByType")
    @ResponseBody
    public String getPaginateSurfaceByType(@RequestParam(value = "licenseTypeCode") String licenseTypeCode,
                                           @RequestParam(value = "holderType") String holderType,
                                           @RequestParam(value = "itemCode") String itemCode) throws Exception {
        String result = inspurService.getPaginateSurfaceByType(licenseTypeCode, holderType, itemCode);

        return result;
    }

    /**
     * 获取证照分页列表接口（根据查询条件）
     *
     * @param queryMap 查询条件（JSON格式的字符串）
     *                 @param isPagination
     *                 @param page
     *                 @param rows
     * @return
     */
    @ApiOperation(value = "获取证照分页列表", notes = "获取证照分页列表接口（根据查询条件）", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "queryMap", value = "查询条件（JSON格式的字符串）", required = true, dataType = "string"),
            @ApiImplicitParam(name = "isPagination", value = "是否分页", dataType = "boolean"),
            @ApiImplicitParam(name = "page", value = "页码", dataType = "integer"),
            @ApiImplicitParam(name = "rows", value = "每页记录数", dataType = "integer")
    })
    @RequestMapping(value = "getLicenceListByPagination")
    @ResponseBody
    public String getLicenceListByPagination(@RequestParam(value = "queryMap") String queryMap,
                                             @RequestParam(value = "isPagination", required = false) Boolean isPagination,
                                             @RequestParam(value = "page", required = false) Integer page,
                                             @RequestParam(value = "rows", required = false) Integer rows) throws Exception {
        JSONObject jsonObject = new JSONObject(queryMap);
        String result = inspurService.getLicenceListByPagination(jsonObject,isPagination,page,rows);

        return result;
    }

    /**
     * 获取证照列表接口（根据查询条件）
     *
     * @param queryMap 查询条件（JSON格式的字符串） license_number、holder_name、certificate_no中至少传递一个（且非空值）
     * @return
     */
    @ApiOperation(value = "获取证照列表", notes = "获取证照列表接口（根据查询条件）", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "queryMap", value = "查询条件（JSON格式的字符串）", required = true, dataType = "string")
    })
    @RequestMapping(value = "getLicenceListByMutliRequirement")
    @ResponseBody
    public String getLicenceListByMutliRequirement(@RequestParam(value = "queryMap") String queryMap) throws Exception {
        JSONObject jsonObject = new JSONObject(queryMap);
        String result = inspurService.getLicenceListByMutliRequirement(jsonObject);

        return result;
    }

    /**
     * 获取证照信息及附件接口（根据证照标识）
     *
     * @param licenseNo  证照标识
     * @param operatorId 操作人员ID（可选）
     * @param itemName   事项名称（可选）
     * @param itemCode   事项编码（可选）
     * @return
     */
    @ApiOperation(value = "获取证照信息及附件", notes = "获取证照信息及附件接口（根据证照标识）", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "licenseNo", value = "证照标识", required = true, dataType = "string"),
            @ApiImplicitParam(name = "operatorId", value = "操作人员ID", dataType = "string"),
            @ApiImplicitParam(name = "itemName", value = "事项名称", dataType = "string"),
            @ApiImplicitParam(name = "itemCode", value = "事项编码", dataType = "string")
    })
    @RequestMapping(value = "getLicenceInfoAndFile")
    @ResponseBody
    public String getLicenceInfoAndFile(@RequestParam(value = "licenseNo") String licenseNo,
                                        @RequestParam(value = "operatorId", required = false) String operatorId,
                                        @RequestParam(value = "itemName", required = false) String itemName,
                                        @RequestParam(value = "itemCode", required = false) String itemCode) throws Exception {
        String result = inspurService.getLicenceInfoAndFile(licenseNo, operatorId, itemName, itemCode);

        return result;
    }

    /**
     * 获取所有事项相关信息接口（根据事项CODE）
     *
     * @param itemCode 事项编码
     * @param type     信息类型（可选）
     * @return
     */
    @ApiOperation(value = "获取所有事项相关信息", notes = "获取所有事项相关信息(基本要素、申请材料、法律依据、办理流程、事项收费、受理条件)接口（根据事项CODE）", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "itemCode", value = "事项编码", required = true, dataType = "string"),
            @ApiImplicitParam(name = "type", value = "信息类型", dataType = "string")
    })
    @RequestMapping(value = "getItemInfoByItemCode")
    @ResponseBody
    public String getItemInfoByItemCode(@RequestParam(value = "itemCode") String itemCode,
                                        @RequestParam(value = "type", required = false) String type) throws Exception {
        String result = inspurService.getItemInfoByItemCode(itemCode, type);

        return result;
    }

    /**
     * 获取表单信息接口（根据事项ID）
     *
     * @param itemId 事项ID
     * @return
     */
    @ApiOperation(value = "获取表单信息", notes = "获取表单信息接口（根据事项ID）", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "itemId", value = "事项ID", required = true, dataType = "string")
    })
    @RequestMapping(value = "getFormInfo")
    @ResponseBody
    public String getFormInfo(@RequestParam(value = "itemId") String itemId) throws Exception {
        String result = inspurService.getFormInfo(itemId);

        return result;
    }

    /**
     * 网上申报接口
     *
     * @param postData 提交数据（JSON格式的字符串）
     * @return
     */
    @ApiOperation(value = "获取证照分页列表", notes = "获取证照分页列表接口（根据查询条件）", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "postData", value = "提交数据（JSON格式的字符串）", required = true, dataType = "string")
    })
    @RequestMapping(value = "webApply")
    @ResponseBody
    public String webApply(@RequestParam(value = "postData") String postData) throws Exception {
        JSONObject jsonObject = new JSONObject(postData);
        String result = inspurService.webApply(jsonObject);

        return result;
    }

    /**
     * 获取项目信息接口
     *
     * @param projectCode 项目副码
     * @return
     */
    @ApiOperation(value = "获取项目信息", notes = "获取项目信息接口", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "projectCode", value = "项目副码", required = true, dataType = "string")
    })
    @RequestMapping(value = "getProjectInfo")
    @ResponseBody
    public String getProjectInfo(@RequestParam(value = "projectCode") String projectCode) throws JSONException, SQLException {
        JSONObject projectInfo = projectService.getProjectInfo(projectCode);

        return projectInfo.toString();
    }

    /**
     * 获取项目信息（验证副码）接口
     *
     * @param projectCode 项目副码
     * @return
     */
    @ApiOperation(value = "获取项目信息（验证副码）", notes = "获取项目信息（验证副码）接口", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "projectCode", value = "项目副码", required = true, dataType = "string")
    })
    @RequestMapping(value = "getProjectInfoByCheck")
    @ResponseBody
    public String getProjectInfoByCheck(@RequestParam(value = "projectCode") String projectCode) throws URISyntaxException {
        String projectInfo = projectService.getProjectInfoByCheck(projectCode);

        return projectInfo;
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
