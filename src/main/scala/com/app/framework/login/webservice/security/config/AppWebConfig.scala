package com.app.framework.login.webservice.security.config

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.{CorsRegistry, WebMvcConfigurer}

@Configuration
class AppWebConfig extends WebMvcConfigurer {
    
    override def addCorsMappings(registry: CorsRegistry): Unit = {
        registry.addMapping("/**").allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
    }
}