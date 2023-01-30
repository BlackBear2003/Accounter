package host.luke.auth.security;

import com.alibaba.fastjson.JSON;
import host.luke.common.utils.ResponseResult;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        //给前端ResponseResult 的json
        ResponseResult responseResult = new ResponseResult(HttpStatus.UNAUTHORIZED.value(), "登陆认证失败了，请重新登陆！");
        response.setStatus(200);
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        String json = JSON.toJSONString(responseResult);
        response.getWriter().print(json);
    }
}
