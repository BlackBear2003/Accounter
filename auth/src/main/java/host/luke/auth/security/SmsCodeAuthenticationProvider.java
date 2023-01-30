package host.luke.auth.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class SmsCodeAuthenticationProvider implements AuthenticationProvider {
    @Getter
    @Setter
    @Autowired
    private UserDetailServiceMobileImpl userDetailsService;
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        SmsCodeAuthenticationToken smsCodeAuthenticationToken = (SmsCodeAuthenticationToken) authentication;
        UserDetails user = userDetailsService.loadUserByUsername((String)smsCodeAuthenticationToken.getPrincipal());

        if(user == null){
            throw new InternalAuthenticationServiceException("用户不存在");
        }

        //能查到就说明验证通过
        SmsCodeAuthenticationToken authenticationResult = new SmsCodeAuthenticationToken(user,user.getAuthorities());
        authenticationResult.setDetails(smsCodeAuthenticationToken.getDetails());

        return authenticationResult;
    }

    //说明支持SmsCodeAuthenticationToken这一类的Token
    @Override
    public boolean supports(Class<?> authentication) {
        return SmsCodeAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
