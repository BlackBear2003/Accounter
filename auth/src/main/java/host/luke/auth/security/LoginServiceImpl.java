package host.luke.auth.security;

import host.luke.common.pojo.User;
import host.luke.common.utils.JwtUtil;
import host.luke.common.utils.ResponseResult;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
@Service
public class LoginServiceImpl {
    @Autowired
    AuthenticationManager authenticationManager;



    @Autowired
    RedisTemplate redisTemplate;

    public ResponseResult loginByPassword(User user) {
        //3使用ProviderManager auth方法进行验证
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(user.getUsername(),user.getPassword());
        Authentication authenticate = authenticationManager.authenticate(usernamePasswordAuthenticationToken);

        //校验失败了
        if(Objects.isNull(authenticate)){
            throw new RuntimeException("用户名或密码错误！");
        }

        //4自己生成jwt给前端
        LoginUser loginUser= (LoginUser)(authenticate.getPrincipal());
        String userId = loginUser.getUser().getUserId().toString();
        String jwt = JwtUtil.createJWT(userId);
        Map<String,String> map=new HashMap();
        map.put("token",jwt);
        map.put("id",loginUser.getUser().getUserId().toString());
        map.put("username",loginUser.getUsername());
        //5系统用户相关所有信息放入redis
        redisTemplate.opsForValue().set("login:"+userId,loginUser);

        return new ResponseResult(200,"登陆成功",map);
    }

    public ResponseResult loginByMobile(String mobile){
        SmsCodeAuthenticationToken smsCodeAuthenticationToken = new SmsCodeAuthenticationToken(mobile);
        Authentication authenticate = authenticationManager.authenticate(smsCodeAuthenticationToken);

        //校验失败了
        if(Objects.isNull(authenticate)){
            throw new RuntimeException("验证码错误或手机号未注册！");
        }

        //4自己生成jwt给前端
        LoginUser loginUser= (LoginUser)(authenticate.getPrincipal());
        String userId = loginUser.getUser().getUserId().toString();
        String jwt = JwtUtil.createJWT(userId);
        Map<String,String> map=new HashMap();
        map.put("token",jwt);
        map.put("id",loginUser.getUser().getUserId().toString());
        map.put("username",loginUser.getUsername());
        //5系统用户相关所有信息放入redis
        redisTemplate.opsForValue().set("login:"+userId,loginUser);
        System.out.println(loginUser);

        return new ResponseResult(200,"登陆成功",map);
    }

    public String storeSmsCode(String mobile){
        String smsCode = RandomStringUtils.randomNumeric(6);
        redisTemplate.opsForValue().set("code-of-"+mobile,smsCode, Duration.ofMinutes(5));
        return smsCode;
    }



    public ResponseResult pwdLogout() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        Long userId = Long.valueOf(loginUser.getUser().getUserId());
        redisTemplate.delete("login:"+userId);

        return new ResponseResult(200,"退出成功！");
    }
}
