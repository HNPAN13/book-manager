package com.example.bookmanager.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import java.util.Arrays;

/**
 * 跨域配置类
 * 解决前端vue(localhost:5173)访问后端springboot(localhost:8080)跨域问题
 */
@Configuration
public class CorsConfig {
    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        // 允许来自http://localhost:5173的前端访问
        config.addAllowedOriginPattern("http://localhost:5173");
        // 允许跨域请求携带Cookie、Session凭证
        config.setAllowCredentials(true);
        // 允许所有请求头
        config.setAllowedHeaders(Arrays.asList("*"));
        // 允许所有请求方式 GET POST PUT DELETE OPTIONS
        config.setAllowedMethods(Arrays.asList("*"));
        // 预检请求有效期，单位秒
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // 对全部接口路径生效
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
