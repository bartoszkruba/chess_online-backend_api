package com.company.chess_online_bakend_api.config;

import com.company.chess_online_bakend_api.config.handler.MySavedRequestAwareAuthenticationSuccessHandler;
import com.company.chess_online_bakend_api.config.handler.MySimpleUrlAuthenticationFailureHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

@Configuration
@EnableWebSecurity
public class MyWebMvcConfigurer extends WebSecurityConfigurerAdapter {

    private final MyUserDetailsService myUserDetailsService;
    private final RestAuthenticationEntryPoint restAuthenticationEntryPoint;
    private final MySavedRequestAwareAuthenticationSuccessHandler mySuccessHandler;
    private final MySimpleUrlAuthenticationFailureHandler myFailureHandler;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public MyWebMvcConfigurer(MyUserDetailsService myUserDetailsService, RestAuthenticationEntryPoint
            restAuthenticationEntryPoint, MySavedRequestAwareAuthenticationSuccessHandler mySuccessHandler,
                              MySimpleUrlAuthenticationFailureHandler myFailureHandler,
                              BCryptPasswordEncoder bCryptPasswordEncoder) {

        this.myUserDetailsService = myUserDetailsService;
        this.restAuthenticationEntryPoint = restAuthenticationEntryPoint;
        this.mySuccessHandler = mySuccessHandler;
        this.myFailureHandler = myFailureHandler;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .exceptionHandling()
                .authenticationEntryPoint(restAuthenticationEntryPoint)
                .and()
                .formLogin().permitAll()
                .successHandler(mySuccessHandler)
                .failureHandler(myFailureHandler)
                .loginProcessingUrl("/authentication/login")
                .and()
                .logout().logoutUrl("/authentication/logout").permitAll()
                .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler(HttpStatus.OK));

        declareSecuredRoutes(http);
    }

    private void declareSecuredRoutes(HttpSecurity http) throws Exception {
        http.authorizeRequests();
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(myUserDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }
}
