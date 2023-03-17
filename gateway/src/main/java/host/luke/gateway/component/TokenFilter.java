package host.luke.gateway.component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import host.luke.common.pojo.User;
import host.luke.common.utils.JwtUtil;
import host.luke.common.utils.ResponseResult;
import io.jsonwebtoken.Claims;
import jakarta.annotation.Resource;
import org.apache.commons.lang.StringUtils;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.server.RequestPath;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class TokenFilter implements GlobalFilter, Ordered {

    @Resource
    RedisTemplate redisTemplate;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        String path = request.getURI().getPath();

        System.out.println(path);

        String token = request.getHeaders().getFirst("token");

        //白名单
        if(path.contains("/auth")){
            return chain.filter(exchange);
        }
        //userId为自定义验证头，不能让第三方使用
        if(request.getHeaders().containsKey("userId")){
            return getVoidMono(response,new ResponseResult(401,"非法请求头userId"));
        }
        //除auth服务外 都需要token进行认证
        if(StringUtils.isBlank(token)){
            return getVoidMono(response,new ResponseResult(401,"token为空"));
        }

        try{
            Claims claims = JwtUtil.parseJWT(token);
            String userId = claims.getSubject();

            String json = JSONObject.toJSONString(redisTemplate.opsForValue().get("login:" + userId));
            System.out.println(json);
            JSONObject loginUser=JSONObject.parseObject(json);
            User user = loginUser.getJSONObject("user").toJavaObject(User.class);
                    //JSONObject.toJavaObject(((JSONObject) loginUser.get("user")),User.class);
            System.out.println(user);
            List<String> authList = loginUser.getJSONArray("permissions").toJavaList(String.class);
            System.out.println(authList);
            /*
                规定：
                url第一个词缀为微服务定位词
                判断权限能否通过
             */
            if(judgeAuth(authList,path.split("/")[1])){
                ServerHttpRequest mutableReq = request.mutate().header("userId", userId).build();
                ServerWebExchange mutableExchange = exchange.mutate().request(mutableReq).build();
                return chain.filter(mutableExchange);
            }
            else{
                return getVoidMono(response,new ResponseResult(401,"权限不足"));
            }
        }catch (com.alibaba.fastjson.JSONException e){
            e.printStackTrace();
            return getVoidMono(response,new ResponseResult(401,"token解析失败"));
        }
        catch (io.jsonwebtoken.JwtException e){
            return getVoidMono(response,new ResponseResult(401,"token失效"));
        }
        catch (Exception e) {
            e.printStackTrace();
            return getVoidMono(response,new ResponseResult(500,"未知错误,请联系后端排查"));
        }
    }

    private Boolean judgeAuth(List<String> auth,String service){
        System.out.println("service called : "+service);
        /*
            暂时先写的简单一点
            TODO：完善权限判断 RDBC
         */
        if(true)
            return true;
        for (String a:
             auth) {
            if(service.equals(a)){
                return true;
            }
        }
        return false;
    }


    //响应式的response包装
    private Mono<Void> getVoidMono(ServerHttpResponse serverHttpResponse,ResponseResult responseResult) {
        serverHttpResponse.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
        DataBuffer dataBuffer = serverHttpResponse.bufferFactory().wrap(JSON.toJSONString(responseResult).getBytes());
        return serverHttpResponse.writeWith(Flux.just(dataBuffer));
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
