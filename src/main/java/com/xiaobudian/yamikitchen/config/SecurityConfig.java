package com.xiaobudian.yamikitchen.config;

import com.xiaobudian.yamikitchen.common.auth.CustomAuthenticationProvider;
import com.xiaobudian.yamikitchen.common.auth.RestAuthFailureHandler;
import com.xiaobudian.yamikitchen.common.auth.RestAuthSuccessHandler;
import com.xiaobudian.yamikitchen.common.auth.UnauthorizedAuthenticationEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

import javax.inject.Inject;

/**
 * Created by Johnson on 2015/4/22.
 */
@EnableWebSecurity
@Configuration
@ComponentScan(basePackages = "com.xiaobudian.yamikitchen.common")
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Inject
    private RestAuthSuccessHandler restAuthSuccessHandler;
    @Inject
    private RestAuthFailureHandler restAuthFailureHandler;
    @Inject
    private CustomAuthenticationProvider customAuthenticationProvider;

    @Inject
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(customAuthenticationProvider);
    }

    @Override
    @Bean(name = "authenticationManager")
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.NEVER).and()
                .authorizeRequests().antMatchers("/api/register", "/api/voice/**", "/api/sms/**", "/api/changePwd", "/api/merchants**").permitAll()
                .and().authorizeRequests().antMatchers("/api/*").authenticated()
                .and()
                .formLogin()
                .loginPage("/login").permitAll()
                .successHandler(restAuthSuccessHandler)
                .failureHandler(restAuthFailureHandler)
                .and().exceptionHandling().authenticationEntryPoint(new UnauthorizedAuthenticationEntryPoint());
    }
}
