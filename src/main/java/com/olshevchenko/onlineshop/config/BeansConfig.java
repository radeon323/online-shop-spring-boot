package com.olshevchenko.onlineshop.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;

/**
 * @author Oleksandr Shevchenko
 */
@Configuration
public class BeansConfig {

    @Bean
    public FreeMarkerConfigurer freeMarkerConfigurer() {
        FreeMarkerConfigurer freemarkerConfig = new FreeMarkerConfigurer();
        freemarkerConfig.setTemplateLoaderPath("classpath:templates/");
        freemarkerConfig.setDefaultEncoding("UTF-8");
        return freemarkerConfig;
    }

    @Bean
    public FreeMarkerViewResolver freeMarkerViewResolver() {
        FreeMarkerViewResolver freeMarkerViewResolver = new FreeMarkerViewResolver();
        freeMarkerViewResolver.setPrefix("");
        freeMarkerViewResolver.setSuffix(".html");
        freeMarkerViewResolver.setContentType("text/html; charset=utf-8");
        return freeMarkerViewResolver;
    }


}
