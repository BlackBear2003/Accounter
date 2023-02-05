package host.luke.api.aop;

import host.luke.common.utils.ResponseResult;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * @Author wzl
 * @Date 2023/2/5
 * @info 定义一个切面，获取request请求头中的userId（在网关进行token认证成功后写入），与参数中的userId进行匹配验证
 *       切面通过@IDCheck注解进行围绕通知
 */
@Aspect
@Component
public class IDCheckAspect {


    @Pointcut("@annotation(host.luke.api.aop.IDCheck)")
    public void check(){}

    @Around("check() && args(userId,..)")
    public Object around(ProceedingJoinPoint pjp, Long userId) throws Throwable {
        /*先拿到Request请求体

         */
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        StringBuffer url = request.getRequestURL();

        Long login_id = Long.valueOf(request.getHeader("userId"));


        if(userId==login_id){
            return pjp.proceed();
        }
        else{
            return new ResponseResult<>(401,"权限不足");
        }
    }

}
