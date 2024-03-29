package host.luke.auth.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import host.luke.auth.component.SmsCodeSender;
import host.luke.auth.security.LoginServiceImpl;
import host.luke.common.pojo.User;
import host.luke.auth.service.impl.UserServiceImpl;
import host.luke.common.utils.ResponseResult;
import host.luke.common.utils.SnowFlake;
import jakarta.annotation.Resource;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RequestMapping("/auth")
@RestController
public class LoginController {

    @Resource
    LoginServiceImpl loginService;
    @Resource
    UserServiceImpl userService;
    @Resource
    SmsCodeSender smsCodeSender;
    @Resource
    RedisTemplate redisTemplate;

    @GetMapping("/test1")
    public String test(){
        return "test!!!!";
    }

    @GetMapping("/login/mobile")
    public ResponseResult getSmsCode(String mobile){
        String smsCode = loginService.storeSmsCode(mobile);
        smsCodeSender.send(mobile,smsCode);
        System.out.println("发送短信到手机号："+mobile+"------验证码为"+smsCode);
        return new ResponseResult<>(200,"success");
    }

    @PostMapping("/login/mobile")
    public ResponseResult mobileLogin(String mobile,String smsCode){
        System.out.println("传入手机号："+mobile+"------验证码为"+smsCode);
        return loginService.loginByMobile(mobile);
    }

    @PostMapping("/login/pwd")
    public ResponseResult pwdLogin(String username,String password){
        System.out.println("传入账号："+username+"------密码为"+password);
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        return loginService.loginByPassword(user);
    }

    @PostMapping("/register/pwd")
    public ResponseResult ordinaryRegister(User user){
        SnowFlake snowFlake = new SnowFlake(0,0);
        user.setUserId(snowFlake.nextId());
        QueryWrapper mobileWrapper = new QueryWrapper();
        mobileWrapper.eq("mobile",user.getMobile());
        if(!Objects.isNull(userService.getOne(mobileWrapper))||user.getMobile()=="-1"){
            return new ResponseResult<>(405,"手机号已注册");
        }

        try{
            userService.save(user);
        }catch (org.mybatis.spring.MyBatisSystemException e){
            e.printStackTrace();
            return new ResponseResult(405,"用户名重复");
        }

        Map<String, Object> map = new HashMap<>();
        map.put("user_id",user.getUserId());
        map.put("username",user.getUsername());
        map.put("mobile",user.getMobile());
        map.put("nickname",user.getNickname());
        return new ResponseResult<>(200,"success",map);
    }

    @PostMapping("/register/mobile")
    public ResponseResult mobileRegister(String mobile,String smsCode,String password){

        //redisTemplate.opsForValue().set("code-of-"+mobile,smsCode, Duration.ofMinutes(5));
        String actualCode = null;
        try {
            actualCode = ((String) redisTemplate.opsForValue().get("code-of-" + mobile));
            if(!actualCode.equals(smsCode)){
                return new ResponseResult(401,"wrong code");
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        QueryWrapper mobileWrapper = new QueryWrapper();
        mobileWrapper.eq("mobile",mobile);
        if(!Objects.isNull(userService.getOne(mobileWrapper))){
            return new ResponseResult<>(405,"手机号已注册");
        }

        User user = new User();
        user.setMobile(mobile);
        SnowFlake snowFlake = new SnowFlake(0,0);
        user.setUserId(snowFlake.nextId());
        user.setUsername(mobile);
        user.setPassword(password);

        try{
            userService.save(user);
        }catch (org.mybatis.spring.MyBatisSystemException e){
            e.printStackTrace();
            return new ResponseResult(405,"用户名重复");
        }

        Map<String, Object> map = new HashMap<>();
        map.put("user_id",user.getUserId());
        map.put("username",user.getUsername());
        map.put("mobile",user.getMobile());
        return new ResponseResult<>(200,"success",map);
    }
    @GetMapping("/register/mobile")
    public ResponseResult getRegisterCode(String mobile){
        String smsCode = loginService.storeSmsCode(mobile);
        smsCodeSender.send(mobile,smsCode);
        System.out.println("模拟发送短信到手机号："+mobile+"------验证码为"+smsCode);
        return new ResponseResult<>(200,"success");
    }


}
