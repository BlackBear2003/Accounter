package host.luke.auth.config;

import host.luke.auth.security.AuthenticationEntryPointImpl;
import host.luke.auth.security.PwdAuthenticationProvider;
import host.luke.auth.security.SmsCodeAuthenticationProvider;
import host.luke.auth.security.SmsCodeFilter;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {




    @Autowired
    AuthenticationEntryPointImpl authenticationEntryPoint;

    @Resource
    SmsCodeAuthenticationSecurityConfig smsCodeAuthenticationSecurityConfig;

    @Autowired
    SmsCodeFilter smsCodeFilter;



    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        //开启跨域
        //把smsCode校验过滤器添加到过滤器链中
        http.addFilterAfter(smsCodeFilter, AnonymousAuthenticationFilter.class);
        http.cors().and()
                //关闭csrf
                .csrf().disable()
                //不通过Session获取SecurityContext
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                // 对于登录接口 允许匿名访问

                .requestMatchers("/auth/login/pwd","/auth/login/mobile").permitAll()//login
                .requestMatchers("/auth/register/pwd","/auth/register/mobile","/auth/test",
                // Swagger的资源路径需要允许访问
                        "/context-path/swagger-ui.html",
                        "/context-path/*",
                        "/*.html",
                        "/favicon.ico",
                        "/*/*.html",
                        "/*/*.css",
                        "/*/*.js",
                        "/context-path/swagger-resources/*",
                        "/context-path/v3/api-docs",
                        "/context-path/v3/api-docs/*"
                ).permitAll()
                .anyRequest().permitAll();


        http.headers().frameOptions().sameOrigin();




        //告诉security如何处理异常
        http.exceptionHandling().authenticationEntryPoint(authenticationEntryPoint);
        http.apply(smsCodeAuthenticationSecurityConfig);

        return http.build();
    }


    @Autowired
    private AuthenticationConfiguration authenticationConfiguration;

    @Bean
    public AuthenticationManager authenticationManager() throws Exception{

        AuthenticationManager authenticationManager = authenticationConfiguration.getAuthenticationManager();
        return authenticationManager;
    }

}
