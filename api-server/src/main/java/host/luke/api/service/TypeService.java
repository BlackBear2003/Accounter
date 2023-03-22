package host.luke.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import host.luke.common.pojo.Type;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;

public interface TypeService extends IService<Type> {


    @Cacheable(cacheNames = "TypeCache")
    List<Type> getTypes(Integer parentId);

    @Cacheable(cacheNames = "TypeCache")
    Integer getTypeIdByName(String typeName);

    List getConsByType(Long userId, Integer typeId);
}
