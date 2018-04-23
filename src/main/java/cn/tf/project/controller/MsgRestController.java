package cn.tf.project.controller;

import cn.tf.project.service.InspurService;
import cn.tf.project.service.MsgService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
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

/**
 * Msg控制层
 */
@Api(value = "MsgRestController", description = "短信服务相关接口")
@RestController
@RequestMapping(value = "/msg/")
public class MsgRestController {
    @Autowired
    private MsgService msgService;

    /**
     * 发送短信接口
     * Test: http://localhost:8090/msg/sendMsg?destination=15024715980,15326725622&message=测试短信发送
     *
     * @param destination 手机号码列表
     * @param message     信息内容
     * @return
     */
    @ApiOperation(value = "发送短信", notes = "发送短信接口", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "destination", value = "手机号码列表", required = true, dataType = "string"),
            @ApiImplicitParam(name = "message", value = "信息内容", required = true, dataType = "string")
    })
    @RequestMapping(value = "sendMsg")
    @ResponseBody
    public String sendMsg(@RequestParam(value = "destination") String destination,
                          @RequestParam(value = "message") String message) throws Exception {
        String[] destinationArray = destination.split(",");
        JSONObject sendResult = msgService.sendMsg(destinationArray, message);

        return sendResult.toString();
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
