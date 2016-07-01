package com.cx.szsh.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.cx.szsh.auth.Authority.AuthorityName;
import com.cx.szsh.auth.CustomerAuthEntryPoint;
import com.cx.szsh.filter.JwtAuthTokenFilter;
import com.cx.szsh.filter.SimpleCORSFilter;
import com.cx.szsh.services.UserService;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter  {

    @Autowired
    private CustomerAuthEntryPoint unauthorizedHandler;

    @Autowired
    private UserService userService;

    @Autowired
    public void configureAuthentication(AuthenticationManagerBuilder builder) throws Exception {
    	builder.userDetailsService(userService).passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public JwtAuthTokenFilter jwtAuthTokenFilterBean() throws Exception {
        JwtAuthTokenFilter authenticationTokenFilter = new JwtAuthTokenFilter();
        authenticationTokenFilter.setAuthenticationManager(authenticationManagerBean());
        return authenticationTokenFilter;
    }
    
    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                // we don't need CSRF because our token is invulnerable
                .csrf().disable()

                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()

                // don't create session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()

                .authorizeRequests()
                //.antMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                // allow anonymous resource requests
                .antMatchers(
                    HttpMethod.GET,
                    "/",
                    "/*.html",
                    "/favicon.ico",
                    "/**/*.html",
                    "/**/*.css",
                    "/**/*.js",
                    "/upload/**"
                ).permitAll()
                .antMatchers(
                    HttpMethod.OPTIONS,
                    "/**/*"
                ).permitAll()
                /*测试使用，放开post方法的访问权限*/
                .antMatchers(
                    HttpMethod.POST,
                    "/**/*"
                ).permitAll()
                .antMatchers(
                    HttpMethod.PUT,
                    "/**/*"
                ).permitAll()
                .antMatchers(
                    HttpMethod.PATCH,
                    "/**/*"
                ).permitAll()
                .antMatchers(
                    HttpMethod.DELETE,
                    "/**/*"
                ).permitAll()
                
                .antMatchers(
            		HttpMethod.GET,
            		"/api/**/**"
                ).permitAll()
                .antMatchers(
            		"/api/admin/token/**",
            		"/api/admin/users"
                ).permitAll()
                .anyRequest().authenticated();

        
        
        // 必须把跨域配置放在鉴权过滤器前，否则401响应将无法添加跨域设置，导致跨域问题
        httpSecurity.addFilterBefore(new SimpleCORSFilter(), UsernamePasswordAuthenticationFilter.class);
        httpSecurity.addFilterBefore(jwtAuthTokenFilterBean(), UsernamePasswordAuthenticationFilter.class);
        
        // disable page caching
        httpSecurity.headers().cacheControl();
    }
}
