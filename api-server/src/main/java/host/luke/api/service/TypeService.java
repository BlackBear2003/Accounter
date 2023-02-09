package host.luke.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import host.luke.common.pojo.Type;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;

public interface TypeService extends IService<Type> {
    @Cacheable
    List<Type> getSonListByParentId(Integer parentId);

    @Cacheable(key = "'type_level_one'")
    List<Type> getLevelOneTypes();

    List getConsByType(Long userId, String typeName);
}
