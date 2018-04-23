package cn.tf.project.configuration;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.WebApplicationInitializer;

/**
 * 项目启动入口，配置包根路径
 */
@SpringBootApplication
@ComponentScan(basePackages = "cn.tf.project")
public class Entry extends SpringBootServletInitializer implements WebApplicationInitializer {
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application){
        // 设置启动类，用于独立tomcat运行的入口
        return application.sources(Entry.class);
    }

//    public static void main(String[] args) throws Exception {
//        SpringApplication.run(Entry.class, args);
//    }
}