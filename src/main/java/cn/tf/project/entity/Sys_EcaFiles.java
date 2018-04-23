package cn.tf.project.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "sys_ecafiles")
@Data
public class Sys_EcaFiles {
    // 附件ID，主键
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer affixid;
    // 文件名称
    private String filename;
    // 文件类型
    private String filetype;
    // 文件大小
    private Integer filesize;
    // 创建人员ID
    private Integer createuserid;
    // 创建日期
    private Date createdate;
    // 文件虚拟路径
    private String virtualpath;
    // 文件实际名称
    private String actualname;
    // 分组Guid，关联sys_ecafilegroupinfo.pkid
    private String groupguid;
    // 唯一标识Guid
    private String row_guid;
    // 备注
    private String remark;
    // 是否加密，默认0，1为加密
    private Integer iscrypted;
    // 是否删除，1为删除
    private Integer isdeleted;
    // 上传人员ID
    private String uploaduserid;
    // 上传人员姓名
    private String uploadusername;
    // 文件Guid
    private String fileguid;
}
