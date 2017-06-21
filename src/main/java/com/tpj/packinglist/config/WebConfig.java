/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tpj.packinglist.config;

import io.github.jhipster.config.JHipsterProperties;
import org.apache.catalina.Context;
import org.apache.catalina.webresources.StandardRoot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.XmlViewResolver;

/**
 *
 * @author ustadho
 */
@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {
    private final Logger logger = LoggerFactory.getLogger(WebConfig.class);
    private final Environment env;
    private final JHipsterProperties jHipsterProperties;

    public WebConfig(Environment env, JHipsterProperties jHipsterProperties) {
        this.env = env;
        this.jHipsterProperties = jHipsterProperties;
    }
    
    
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login").setViewName("login");
        registry.setOrder(0);
    }
    
    @Bean
public EmbeddedServletContainerFactory servletContainer() {
    TomcatEmbeddedServletContainerFactory tomcatFactory = new TomcatEmbeddedServletContainerFactory() {
        @Override
        protected void postProcessContext(Context context) {
            final int cacheSize = 40 * 1024;
            StandardRoot standardRoot = new StandardRoot(context);
            standardRoot.setCacheMaxSize(cacheSize);
            context.setResources(standardRoot); // This is what made it work in my case.

            logger.info(String.format("New cache size (KB): %d", context.getResources().getCacheMaxSize()));
        }
        
    };
    return tomcatFactory;
}

//    @Bean
//    public XmlViewResolver viewResolver() {
//        XmlViewResolver vr = new XmlViewResolver();
//        Resource resource = new ClassPathResource("jasper-views.xml");
//        vr.setLocation(resource);
//        vr.setOrder(1);
//        return vr;
//    }
    
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = jHipsterProperties.getCors();
        if (config.getAllowedOrigins() != null && !config.getAllowedOrigins().isEmpty()) {
            logger.debug("Registering CORS filter");
            source.registerCorsConfiguration("/api/**", config);
            source.registerCorsConfiguration("/v2/api-docs", config);
            source.registerCorsConfiguration("/oauth/**", config);
        }
        return new CorsFilter(source);
    }
}