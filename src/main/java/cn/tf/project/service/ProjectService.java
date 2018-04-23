package cn.tf.project.service;

import cn.tf.project.jdbc.JDBCUtils;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.Map;

/**
 * File: ProjectService.java
 * Description: 发改委投资项目2.0数据服务接口类
 *
 * @author 田福
 * Version: 1.0.0
 */
@Service
public class ProjectService {
    /**
     * 获取项目信息服务
     *
     * @param projectCode 项目副码
     * @return
     */
    public JSONObject getProjectInfo(String projectCode) throws SQLException {
        if (projectCode == null || projectCode.isEmpty()) return null;

        JSONObject projectInfo = new JSONObject();
        JDBCUtils jdbcUtils = new JDBCUtils();
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            conn = jdbcUtils.getConnection();
            String sql = "SELECT * FROM tzxm_apply_project_info where project_code = ?";
            pst = conn.prepareStatement(sql);
            pst.setString(1, projectCode);
            rs = pst.executeQuery();
            while (rs.next()) {
                ResultSetMetaData md = rs.getMetaData();
                int num = md.getColumnCount();
                for (int i = 1; i <= num; i++) {
                    projectInfo.put(md.getColumnName(i), rs.getObject(i));
                }
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        } finally {
            jdbcUtils.release(conn, pst, rs);
        }
        return projectInfo;
    }
}
