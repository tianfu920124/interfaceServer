package cn.tf.project.service;

import java.io.File;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.tf.project.entity.Sys_EcaFiles;
import cn.tf.project.repository.EcaFileRepository;

/**
 * File: EcaService.java
 * Description: ECA服务接口类
 *
 * @author 田福
 * Version: 1.0.0
 */
@Service
public class EcaService {
    /*
     *对象定义
     */
    @Autowired
    private EcaFileRepository ecaFileRepository;

    /**
     * 获取附件信息服务
     *
     * @param fileGuid 文件Guid，对应sys_ecafiles.row_guid
     * @return
     */
    public Sys_EcaFiles getEcaFileInfo(String fileGuid) {
        if (fileGuid == null || fileGuid.isEmpty()) return null;

        Sys_EcaFiles ecaFile = null;
        try {
            ecaFile = ecaFileRepository.findByFileGuid(fileGuid);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return ecaFile;
    }

    /**
     * 修改附件信息服务
     *
     * @param fileGuid   文件Guid
     * @param fieldNmae  字段名称
     * @param fieldValue 字段值
     * @return
     */
    public JSONObject modifyEcaFileInfo(String fileGuid, String fieldNmae, String fieldValue) throws JSONException {
        if (fileGuid == null || fileGuid.isEmpty()
                || fieldNmae == null || fieldNmae.isEmpty() || fieldValue == null)
            return new JSONObject("{\"errormessage\":\"ParamError_NULL\"}");

        JSONObject result = new JSONObject();
        try {
            Integer affectRowCount = null;
            if (fieldNmae.toLowerCase().equals("actualname")) {
                affectRowCount = ecaFileRepository.updateActualNameByFileGuid(fileGuid, fieldValue);
                if (affectRowCount >= 0) {
                    result.put("status", "0");
                    result.put("affectRowCount", affectRowCount);
                } else {
                    result.put("status", "1");
                }
            }
        } catch (Exception ex) {
            result.put("status", "1");
            result.put("errormassage", ex.getMessage());
        }

        return result;
    }

    /**
     * 清空PDF.js预览缓存（删除文件）
     *
     * @param path     文件夹路径
     * @param fileGuid 文件Guid
     * @return
     * @throws JSONException
     */
    public JSONObject clearPDFPreviewCache(String path, String fileGuid) throws JSONException {
        if (path == null || path.isEmpty() || fileGuid == null || fileGuid.isEmpty())
            new JSONObject("{\"errormessage\":\"ParamError_NULL\"}");

        JSONObject result = new JSONObject();
        try {
            File file = new File(path + "\\" + fileGuid);
            file.delete();
            result.put("status", "0");
        } catch (Exception ex) {
            result.put("status", "1");
            result.put("errormassage", ex.getMessage());
        }

        return result;
    }

    /**
     * 清空PDF.js预览缓存（删除文件夹）
     *
     * @param path       文件夹路径
     * @param deleteRoot 是否删除预览文件夹根目录，默认为true（删除）
     * @return
     * @throws JSONException
     */
    public JSONObject clearPDFPreviewCache(String path, boolean deleteRoot) throws JSONException {
        if (path == null || path.isEmpty())
            new JSONObject("{\"errormessage\":\"ParamError_NULL\"}");

        JSONObject result = new JSONObject();
        try {
            boolean success;
            File dir = new File(path);
            success = (deleteRoot) ? deleteDir(dir) : deleteChilrenDir(dir);
            if (success) {
                result.put("status", "0");
            } else {
                result.put("status", "1");
            }
        } catch (Exception ex) {
            result.put("status", "1");
            result.put("errormassage", ex.getMessage());
        }

        return result;
    }

    /**
     * 删除子目录及文件
     *
     * @param dir 文件夹路径
     * @return
     */
    private boolean deleteChilrenDir(File dir) {
        if (dir.isDirectory()) {
            boolean success = true;
            for (File chilrenFile : dir.listFiles()) {
                if (!deleteDir(chilrenFile)) success = false;
            }
            return success;
        }
        return false;
    }

    /**
     * 递归删除目录下的所有文件及子目录下所有文件
     *
     * @param dir 将要删除的文件目录
     * @return boolean Returns "true" if all deletions were successful.
     * If a deletion fails, the method stops attempting to
     * delete and returns "false".
     */
    private boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            // 递归删除目录中的子目录下
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        // 目录此时为空，可以删除
        return dir.delete(); // File.delete()用于删除“某个文件或者空目录”！
    }
}
