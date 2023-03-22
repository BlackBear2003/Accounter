package host.luke.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import host.luke.api.dao.ConsumptionMapper;
import host.luke.api.dao.TypeMapper;
import host.luke.api.service.TypeService;
import host.luke.common.pojo.Type;
import jakarta.annotation.Resource;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TypeServiceImpl extends ServiceImpl<TypeMapper, Type> implements TypeService {

    @Resource
    TypeMapper typeMapper;
    @Resource
    ConsumptionMapper consumptionMapper;

    @Override
    @Cacheable(cacheNames = "TypeCache")
    public List<Type> getTypes(Integer parentId){

        List<Type> list = this.list();

        return list;
    }

    @Override
    @Cacheable(cacheNames = "TypeCache")
    public Integer getTypeIdByName(String typeName){
        QueryWrapper<Type> wrapper = new QueryWrapper<>();
        wrapper.eq("type_name",typeName);
        Integer id = typeMapper.selectOne(wrapper).getTypeId();
        return id;
    }


    @Override
    public List getConsByType(Long userId, Integer typeId){

        //先查出对应名称的Type的Id

        QueryWrapper wrapper = new QueryWrapper();
        wrapper.inSql("consumption_id","select consumption_id from t_user_consumption where user_id = "+userId);
        wrapper.eq("type_id",typeId);
        wrapper.orderByDesc("consume_time");
        return consumptionMapper.selectList(wrapper);

    }


}
