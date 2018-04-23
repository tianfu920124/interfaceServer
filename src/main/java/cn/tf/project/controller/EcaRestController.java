package cn.tf.project.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import org.json.JSONException;
import org.json.JSONObject;

import cn.tf.project.entity.Sys_EcaFiles;
import cn.tf.project.service.EcaService;

/**
 * Eca控制层
 */
@Api(value = "EcaRestController", description = "ECA相关接口")
@RestController
@RequestMapping(value = "/eca/")
public class EcaRestController {
    @Autowired
    private EcaService ecaService;

    /**
     * 获取附件信息接口
     * Test: http://localhost:8090/eca/getEcaFileInfo?fileGuid=fc408778-b1cb-4a78-98fe-376e0d714a02
     *
     * @param fileGuid 文件GUID
     * @return
     */
    @ApiOperation(value = "获取附件信息", notes = "获取附件信息接口", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "fileGuid", value = "文件GUID", required = true, dataType = "string")
    })
    @RequestMapping(value = "getEcaFileInfo")
    @ResponseBody
    public String getEcaFileInfo(@RequestParam(value = "fileGuid") String fileGuid) throws JSONException {
        Sys_EcaFiles ecaFile = ecaService.getEcaFileInfo(fileGuid);

        JSONObject fileInfoResult = new JSONObject();
        if (ecaFile != null) {
            fileInfoResult.put("affixId", ecaFile.getAffixid());
            fileInfoResult.put("fileName", ecaFile.getFilename());
            fileInfoResult.put("fileType", ecaFile.getFiletype());
            fileInfoResult.put("fileSize", ecaFile.getFilesize());
            fileInfoResult.put("createUserId", ecaFile.getCreateuserid());
            fileInfoResult.put("createDate", ecaFile.getCreatedate());
            fileInfoResult.put("virtualPath", ecaFile.getVirtualpath());
            fileInfoResult.put("actualName", ecaFile.getActualname());
            fileInfoResult.put("groupGuid", ecaFile.getGroupguid());
            fileInfoResult.put("rowGuid", ecaFile.getRow_guid());
            fileInfoResult.put("remark", ecaFile.getRemark());
            fileInfoResult.put("isCrypted", ecaFile.getIscrypted());
            fileInfoResult.put("isDeleted", ecaFile.getIsdeleted());
            fileInfoResult.put("uploadUserId", ecaFile.getUploaduserid());
            fileInfoResult.put("uploadUserName", ecaFile.getUploadusername());
            fileInfoResult.put("fileGuid", ecaFile.getFileguid());
        }

        return fileInfoResult.toString();
    }

    /**
     * 清空PDF.js预览文件夹缓存接口
     * Test: http://localhost:8090/eca/clearPDFPreviewCacheFile?path=D:%5cpreviewCatch&fileGuid=128c0d76-031d-4aca-bb5f-18b0cea3a652
     *
     * @param path     预览文件夹路径
     * @param fileGuid 文件GUID
     * @return
     */
    @ApiOperation(value = "清空PDF.js预览文件夹缓存（删除文件）", notes = "清空PDF.js预览文件夹缓存接口（删除文件）", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "path", value = "预览文件夹路径", required = true, dataType = "string"),
            @ApiImplicitParam(name = "fileGuid", value = "文件GUID", required = true, dataType = "string")
    })
    @RequestMapping(value = "clearPDFPreviewCacheFile")
    @ResponseBody
    public String clearPDFPreviewCacheFile(@RequestParam(value = "path") String path,
                                           @RequestParam(value = "fileGuid") String fileGuid) throws JSONException {
        JSONObject result = ecaService.clearPDFPreviewCache(path, fileGuid);
        return result.toString();
    }

    /**
     * 清空PDF.js预览文件夹缓存
     * Test: http://localhost:8090/eca/clearPDFPreviewCache?path=D:%5cpreviewCatch
     *
     * @param path       预览文件夹路径
     * @param deleteRoot 是否保留预览文件夹根目录
     * @return
     */
    @ApiOperation(value = "清空PDF.js预览文件夹缓存（删除文件夹）", notes = "清空PDF.js预览文件夹缓存接口（删除文件夹）", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "path", value = "预览文件夹路径", required = true, dataType = "string"),
            @ApiImplicitParam(name = "deleteRoot", value = "是否保留预览文件夹根目录", dataType = "boolean")
    })
    @RequestMapping(value = "clearPDFPreviewCache")
    @ResponseBody
    public String clearPDFPreviewCache(@RequestParam(value = "path") String path,
                                       @RequestParam(value = "deleteRoot", required = false) Boolean deleteRoot) throws JSONException {
        if (deleteRoot == null) deleteRoot = false;
        JSONObject result = ecaService.clearPDFPreviewCache(path, deleteRoot);
        return result.toString();
    }

    /**
     * 修改附件信息接口
     * Test: http://localhost:8090/eca/modifyEcaFileInfo?fileGuid=fc408778-b1cb-4a78-98fe-376e0d714a02&fieldName=actualname&fieldValue=国土土地复核结果的函2_Sign20180313.jpg
     *
     * @param fileGuid   文件Guid
     * @param fieldName  字段名称
     * @param fieldValue 字段值
     * @return
     * @throws JSONException
     */
    @ApiOperation(value = "修改附件信息", notes = "修改附件信息接口", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "fileGuid", value = "文件Guid", required = true, dataType = "string"),
            @ApiImplicitParam(name = "fieldName", value = "字段名称", required = true, dataType = "string"),
            @ApiImplicitParam(name = "fieldValue", value = "字段值", required = true, dataType = "string")
    })
    @RequestMapping(value = "modifyEcaFileInfo")
    @ResponseBody
    public String modifyEcaFileInfo(@RequestParam(value = "fileGuid") String fileGuid,
                                    @RequestParam(value = "fieldName") String fieldName,
                                    @RequestParam(value = "fieldValue") String fieldValue) throws JSONException {
        JSONObject result = ecaService.modifyEcaFileInfo(fileGuid, fieldName, fieldValue);
        return result.toString();
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
