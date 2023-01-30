package host.luke.auth.config;

import host.luke.auth.security.AuthenticationEntryPointImpl;
import host.luke.auth.security.SmsCodeFilter;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {


    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Autowired
    AuthenticationEntryPointImpl authenticationEntryPoint;

    @Resource
    SmsCodeAuthenticationSecurityConfig smsCodeAuthenticationSecurityConfig;

    @Autowired
    SmsCodeFilter smsCodeFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        //开启跨域

        http.cors().and()
                //关闭csrf
                .csrf().disable()
                //不通过Session获取SecurityContext
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                // 对于登录接口 允许匿名访问

                .requestMatchers("/auth/login/pwd","/auth/login/mobile").anonymous()//login
                .requestMatchers("/auth/register/pwd","/auth/register/mobile").anonymous()//register
                .requestMatchers(HttpMethod.GET, // Swagger的资源路径需要允许访问
                        "/",
                        "/swagger-ui.html",
                        "/swagger-ui/",
                        "/*.html",
                        "/favicon.ico",
                        "/*/*.html",
                        "/*/*.css",
                        "/*/*.js",
                        "/swagger-resources/*",
                        "/v3/api-docs/*"
                )
                .permitAll()
                .anyRequest().authenticated();


        http.headers().frameOptions().sameOrigin();

        //把smsCode校验过滤器添加到过滤器链中
        http.addFilterBefore(smsCodeFilter, UsernamePasswordAuthenticationFilter.class);


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