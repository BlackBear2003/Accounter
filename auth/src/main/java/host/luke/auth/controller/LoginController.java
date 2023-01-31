package host.luke.auth.controller;

import host.luke.auth.security.LoginServiceImpl;
import host.luke.common.pojo.User;
import host.luke.common.service.impl.UserServiceImpl;
import host.luke.common.utils.ResponseResult;
import host.luke.common.utils.SnowFlake;
import jakarta.annotation.Resource;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RequestMapping("/auth")
@RestController
public class LoginController {

    @Resource
    LoginServiceImpl loginService;
    @Resource
    UserServiceImpl userService;

    @GetMapping("/login/mobile")
    public ResponseResult getSmsCode(String mobile){
        String smsCode = loginService.storeSmsCode(mobile);
        System.out.println("模拟发送短信到手机号："+mobile+"------验证码为"+smsCode);
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

    @PostMapping("/register/mobile")
    public ResponseResult mobileRegister(String mobile,String smsCode){
        User user = new User();
        user.setMobile(mobile);
        SnowFlake snowFlake = new SnowFlake(0,0);
        user.setUserId(snowFlake.nextId());
        user.setUsername(mobile+RandomStringUtils.randomAlphanumeric(4));
        user.setPassword(RandomStringUtils.randomAlphanumeric(8));
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
        System.out.println("模拟发送短信到手机号："+mobile+"------验证码为"+smsCode);
        return new ResponseResult<>(200,"success");
    }


}
