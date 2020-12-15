package com.udacity.jwdnd.course1.cloudstorage.configurations;

import com.udacity.jwdnd.course1.cloudstorage.services.AuthenticationService;
import com.udacity.jwdnd.course1.cloudstorage.utilities.security.CustomAuthenticationFailureHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfiguration extends WebSecurityConfigurerAdapter {


    private AuthenticationService authenticationService;

    public SpringSecurityConfiguration(AuthenticationService authenticationService){
        this.authenticationService = authenticationService;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests().antMatchers("/login**","/signup","/h2-console", "/css/*", "/js/*").permitAll()
                .anyRequest().authenticated()
                .and().httpBasic()
                .and()
                .formLogin()
                .loginPage("/login")
                .failureHandler(handleAuthenticationFailure())
                .permitAll()
                .and()
                .logout()
                .permitAll();
        http.formLogin()
                .defaultSuccessUrl("/home", true);
        http.csrf().disable();
        http.headers().frameOptions().disable();
        http.logout();

    }

    @Bean
    public AuthenticationFailureHandler handleAuthenticationFailure() {
        return new SimpleUrlAuthenticationFailureHandler() {

            @Override
            public void onAuthenticationFailure(HttpServletRequest httpRequest, HttpServletResponse httpResponse,
                                                AuthenticationException authenticationException) throws IOException, ServletException {

                // custom failure code here
                httpResponse.sendRedirect("/login?error=true");
//                super.onAuthenticationFailure(httpRequest, httpResponse, authenticationException);
            }
        };
    }
}
