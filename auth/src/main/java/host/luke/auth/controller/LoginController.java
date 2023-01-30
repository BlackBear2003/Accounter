package host.luke.auth.controller;

import host.luke.auth.security.LoginServiceImpl;
import host.luke.common.utils.ResponseResult;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RequestMapping("/auth")
@RestController
public class LoginController {

    @Resource
    LoginServiceImpl loginService;

    @GetMapping("/login/mobile")
    public ResponseResult getSmsCode(String mobile){
        String smsCode = loginService.storeSmsCode(mobile);
        System.out.println("模拟发送短信到手机号："+mobile+"------验证码为"+smsCode);
        return new ResponseResult<>(200,"success");
    }

    @PostMapping("/login/mobile")
    public ResponseResult getSmsCode(String mobile,String smsCode){
        System.out.println("传入手机号："+mobile+"------验证码为"+smsCode);
        return loginService.loginByMobile(mobile);
    }


}
