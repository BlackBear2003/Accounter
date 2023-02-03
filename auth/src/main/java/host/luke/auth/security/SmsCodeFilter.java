package host.luke.auth.security;


import com.alibaba.fastjson.JSON;
import host.luke.common.utils.ResponseResult;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/**
 * @Author: zy
 * @Description:  手机验证码过滤器，用于对手机验证码进行校验
 * @Date: 2020-2-9
 */
@Service
public class SmsCodeFilter  extends OncePerRequestFilter {


    @Autowired
    RedisTemplate redisTemplate;

    /**
     * 如果请求是/login/mobile、对图片验证码进行校验
     * @param httpServletRequest
     * @param httpServletResponse
     * @param filterChain
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                    FilterChain filterChain) throws ServletException, IOException {
        //判断请求页面是否为/auth/login/mobile、该路径对应登录form表单的action路径，请求的方法是否为POST，是的话进行验证码校验逻辑，否则直接执行filterChain.doFilter让代码往下走
        System.out.println(httpServletRequest.getRequestURI());
        if ((StringUtils.equalsIgnoreCase("/auth/login/mobile", httpServletRequest.getRequestURI())
                ||StringUtils.equalsIgnoreCase("/auth/register/mobile", httpServletRequest.getRequestURI()))
                && StringUtils.equalsIgnoreCase(httpServletRequest.getMethod(), "post")) {
            try {
                //验证验证码的正确性 失败直接返回请求 正确就继续往下走doFilter
                validateSmsCode(new ServletWebRequest(httpServletRequest));
            } catch (ValidateCodeException e) {
                System.out.println("验证码校验失败!");
                ResponseResult responseResult = new ResponseResult(HttpStatus.UNAUTHORIZED.value(), e.getMessage());
                httpServletResponse.setStatus(200);
                httpServletResponse.setContentType("application/json");
                httpServletResponse.setCharacterEncoding("utf-8");
                String json = JSON.toJSONString(responseResult);
                httpServletResponse.getWriter().print(json);
                return;
            }
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    /**
     * 对手机验证码进行校验
     * @param servletWebRequest：请求参数 包含表单提交的手机验证码信息
     * @throws ServletRequestBindingException
     */
    private void validateSmsCode(ServletWebRequest servletWebRequest) throws ValidateCodeException, ServletRequestBindingException {
        //获取表单提交的手机号
        String mobile = ServletRequestUtils.getStringParameter(servletWebRequest.getRequest(), "mobile");

        String codeInRedis = redisTemplate.opsForValue().get("code-of-"+mobile)!=null?redisTemplate.opsForValue().get("code-of-"+mobile).toString():null;


        //获取表单提交的手机验证码
        String codeInRequest = ServletRequestUtils.getStringParameter(servletWebRequest.getRequest(), "smsCode");

        //验证码空校验
        if (codeInRequest.equals("")) {
            throw new ValidateCodeException("验证码不能为空！");
        }

        //验证码校验
        if (Objects.isNull(codeInRedis)) {
            throw new ValidateCodeException("验证码不存在或验证码已过期！");
        }

        //判断是否相等
        if (!StringUtils.equalsIgnoreCase(codeInRedis, codeInRequest)) {
            throw new ValidateCodeException("验证码不正确！");
        }

    }
}
