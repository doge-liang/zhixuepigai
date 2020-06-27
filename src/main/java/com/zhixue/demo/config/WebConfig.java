package com.zhixue.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @类名 : WebConfig
 * @说明 : 系统公共配置
 * @创建日期 : 2019/9/5
 * @作者 : Niaowuuu
 * @版本 : 1.0
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/api").setViewName("redirect:/swagger-ui.html");
    }


    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // 暂时允许所有跨域请求
        registry.addMapping("/**")
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .maxAge(3600);
    }
}
