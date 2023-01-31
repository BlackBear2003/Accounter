package host.luke.auth.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class PwdAuthenticationProvider implements AuthenticationProvider {

    @Getter
    @Setter
    @Autowired
    UserDetailServicePwdImpl userDetailServicePwd;


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = (UsernamePasswordAuthenticationToken) authentication;

        UserDetails userDetails = userDetailServicePwd.loadUserByUsername((String) usernamePasswordAuthenticationToken.getPrincipal());
        if(userDetails == null){
            throw new InternalAuthenticationServiceException("用户不存在");
        }

        String rawPassword = (String) usernamePasswordAuthenticationToken.getCredentials();
        String encodedPassword = userDetails.getPassword();
        System.out.println("userDetails password: "+encodedPassword);
        System.out.println("token password :"+rawPassword);

        if(rawPassword.equals(encodedPassword)){
            UsernamePasswordAuthenticationToken authenticationResult = new UsernamePasswordAuthenticationToken(userDetails,userDetails.getAuthorities());
            authenticationResult.setDetails(usernamePasswordAuthenticationToken.getDetails());
            return authenticationResult;
        }

        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
