package com.app.framework.login.webservice.security.config

import com.app.framework.login.webservice.security.filter.{JWTAuthenticationFilter, JWTAuthorizationFilter, XSSFilter}
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.{HttpSecurity, WebSecurity}
import org.springframework.security.config.annotation.web.configuration.{EnableWebSecurity, WebSecurityConfigurerAdapter}
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
class AppWebSecurity(a_userDetailsService: UserDetailsService) extends WebSecurityConfigurerAdapter {

    @throws[Exception]
    override def configure(auth: AuthenticationManagerBuilder): Unit = {
        auth.userDetailsService(a_userDetailsService).passwordEncoder(new BCryptPasswordEncoder())
    }
    
    /**
      * This api allows the mentioned urls to not to be processed by the Authentication or Authorization filters
      *
      * @param a_web: WebSecurity
      */
    @throws[Exception]
    override def configure(a_web: WebSecurity): Unit = {
        
        a_web.ignoring.antMatchers(HttpMethod.GET, "/core/user/getUserImage/**")
        a_web.ignoring.antMatchers("/core/enterprise/create","/core/enterprise/update","/swagger/getSwaggerConfig",
            "/core/media/read/**", "/core/media/create")
        a_web.ignoring.mvcMatchers("/swagger-ui.html/**", "/configuration/**", "/swagger-resources/**", "/v2/api-docs",
            "/webjars/**")
    }
    
    /**
      * This api adds Authentication and authorization filters for all request
      * except those mentioned in configure(a_web: WebSecurity) api
      * CSRF is disabled and CORS is enabled for entire application
      *
      * @param a_http - HttpSecurity
      */
    override protected def configure(a_http: HttpSecurity): Unit = {
        
        a_http.csrf.disable.authorizeRequests
            .anyRequest.authenticated.and()
            .addFilter(new JWTAuthenticationFilter(authenticationManager))
            .addFilter(new JWTAuthorizationFilter(authenticationManager))
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
    }

    @Bean
    def xssPreventFilter(): FilterRegistrationBean[XSSFilter] = {
        val w_registrationBean : FilterRegistrationBean[XSSFilter] = new FilterRegistrationBean[XSSFilter]()
        w_registrationBean.setFilter(new XSSFilter())
        w_registrationBean.addUrlPatterns("/*")
        w_registrationBean
    }
}
