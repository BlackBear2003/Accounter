package host.luke.api.aop;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson2.JSON;
import host.luke.common.pojo.MyApiLog;
import host.luke.common.utils.ResponseResult;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;
import java.util.Date;
import java.util.Objects;

@Aspect
@Component
public class ApiLogAspect {

    @Resource
    RabbitTemplate rabbitTemplate;

    //@Pointcut("@annotation(host.luke.api.aop.ApiLog)")
    //public void log(){}

    @Pointcut("within(host.luke.api.controller.*)")
    public void log(){}

    @AfterReturning("log()")
    public void afterReturning(JoinPoint jp) throws Throwable {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        StringBuffer url = request.getRequestURL();

        MyApiLog myApiLog = new MyApiLog();
        myApiLog.setApi(url.toString());
        myApiLog.setCallTime(new Date());
        myApiLog.setUserId(Long.valueOf(request.getHeader("userId")));

        rabbitTemplate.convertAndSend("api_log_exchange","api.log", JSON.toJSONString(myApiLog));
    }
}
