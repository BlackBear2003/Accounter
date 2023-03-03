package host.luke.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import host.luke.api.dao.GuardianshipMapper;
import host.luke.api.service.GuardianShipService;
import host.luke.common.pojo.Guardianship;
import jakarta.annotation.Resource;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;

@Service
public class GuardianshipServiceImpl extends ServiceImpl<GuardianshipMapper, Guardianship> implements GuardianShipService {

    @Resource
    GuardianshipMapper guardianshipMapper;
    @Resource
    RedisTemplate<String,Object> redisTemplate;

    public String generateRandomCodeForWard(Long wardUserId){
        String code = RandomStringUtils.randomAlphanumeric(8);
        redisTemplate.opsForValue().set(code,wardUserId.toString(), Duration.ofMinutes(10));
        return code;
    }

    public Long getUserIdByCode(String code){
        Long wardUserId = Long.valueOf((String) redisTemplate.opsForValue().get(code));
        return wardUserId;
    }


    public Boolean hasGuardianRight(Long guardianId,Long wardId){

        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("guardian_id",guardianId);
        List<Guardianship> list = this.list(wrapper);

        for (Guardianship guardianship:
                list) {
            if(guardianship.getWardId()==wardId){
                return true;
            }
        }
        return false;
    }



}
