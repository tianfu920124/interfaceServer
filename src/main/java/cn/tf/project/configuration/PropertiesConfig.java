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
    private Map<String, String> webLicense;
    private Map<String, String> cloudAccept;
    private Map<String, String> openMas;
    private Map<String, String> project;
}
