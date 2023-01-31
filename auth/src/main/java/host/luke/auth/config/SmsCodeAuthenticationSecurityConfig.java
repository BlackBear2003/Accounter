package host.luke.auth.config;

import host.luke.auth.security.PwdAuthenticationProvider;
import host.luke.auth.security.SmsCodeAuthenticationProvider;
import host.luke.auth.security.UserDetailServiceMobileImpl;
import host.luke.auth.security.UserDetailServicePwdImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.stereotype.Component;

@Component
public class SmsCodeAuthenticationSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    @Autowired
    private UserDetailServiceMobileImpl userDetailServiceMobile;

    @Autowired
    private UserDetailServicePwdImpl userDetailServicePwd;


    @Override
    public void configure(HttpSecurity builder) throws Exception {


        //设置SmsCodeAuthenticationProvider的UserDetailsService
        SmsCodeAuthenticationProvider smsCodeAuthenticationProvider = new SmsCodeAuthenticationProvider();
        smsCodeAuthenticationProvider.setUserDetailsService(userDetailServiceMobile);

        PwdAuthenticationProvider pwdAuthenticationProvider = new PwdAuthenticationProvider();
        pwdAuthenticationProvider.setUserDetailServicePwd(userDetailServicePwd);

        builder.authenticationProvider(pwdAuthenticationProvider)
        .authenticationProvider(smsCodeAuthenticationProvider);
    }


}
