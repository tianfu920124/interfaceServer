package cn.tf.project.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value = "SignInfo", description = "签章信息类，用于接收JSON格式传入的签章信息")
@Data
public class SignInfo {
    // 1.单页  2.多页 3.骑缝章 4.关键字，默认1
    @ApiModelProperty(value = "签章类型", notes = "1.单页  2.多页 3.骑缝章 4.关键字，默认1")
    private String signType;
    // 印章ID，可空，为0表示用默认印章签署，默认0
    @ApiModelProperty(value = "电子印章ID", notes = "可空，为0表示用默认印章签署，默认0")
    private String sealId;
    // 输入文件、输出文件 如果传入文件流则必须为空
    @ApiModelProperty(value = "输入文件", notes = "如果传入文件流则必须为空")
    private String srcPdfFile;
    @ApiModelProperty(value = "输出文件", notes = "如果传入文件流则必须为空")
    private String dstPdfFile;
    // 文件流 如果传入文件则必须为空
    @ApiModelProperty(value = "文件流", notes = "如果传入文件则必须为空")
    private String fileByte;
    // 邮件地址或手机号码，可空
    @ApiModelProperty(value = "邮件地址或手机号码", notes = "可空")
    private String email;
    // 定义盖章类型，0.坐标盖章；1.关键字盖章 默认0，可空
    @ApiModelProperty(value = "盖章类型", notes = "0.坐标盖章；1.关键字盖章 默认0")
    private String posType;
    // 签署页码，可空，若为多页签章，支持页码格式“1 1-3 5,8“，若为坐标定位时不可空
    @ApiModelProperty(value = "签署页码", notes = "可空，若为多页签章，支持页码格式“1 1-3 5,8“，若为坐标定位时不可空")
    private String posPage;
    // 签署位置X、Y坐标，若为关键字定位，相对于关键字的坐标偏移量，默认0
    @ApiModelProperty(value = "签署位置X坐标", notes = "若为关键字定位，相对于关键字的坐标偏移量，默认0")
    private String posX;
    @ApiModelProperty(value = "签署位置Y坐标", notes = "若为关键字定位，相对于关键字的坐标偏移量，默认0")
    private String posY;
    // 关键字，仅限关键字签章时有效，若为关键字定位时，不可空
    @ApiModelProperty(value = "关键字", notes = "仅限关键字签章时有效，若为关键字定位时，不可空")
    private String posKey;
}
