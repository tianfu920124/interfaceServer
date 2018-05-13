package cn.tf.project.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 读取配置文件
 */
@Data
@Component
@Configuration
@ConfigurationProperties(prefix = "paramConfig")
@PropertySource("classpath:paramConfig.properties")
public class PropertiesConfig {
    private Map<String, String> webLicense; // 证照库系统接口相关参数
    private Map<String, String> cloudAccept; // 一窗受理系统接口相关参数
    private Map<String, String> openMas; // 短信机接口相关参数
    private Map<String, String> project; // 投资项目2.0系统接口相关参数
    private Map<String,String> censor; // 电子审图系统接口相关参数
    private Map<String,String> powerManager; // 事项系统接口相关参数
}
