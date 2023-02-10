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
    @Cacheable(cacheNames = "TypeCache",key = "'type_son_of_'+#parentId")
    public List<Type> getSonListByParentId(Integer parentId){

        QueryWrapper<Type> queryWrapper = new QueryWrapper();
        queryWrapper.eq("parent_id",parentId);
        List<Type> list = typeMapper.selectList(queryWrapper);

        return list;
    }

    @Override
    @Cacheable(cacheNames = "TypeCache",key = "'type_level_one'")
    public List<Type> getLevelOneTypes(){

        QueryWrapper<Type> queryWrapper = new QueryWrapper();
        queryWrapper.eq("level",1);
        List<Type> list = typeMapper.selectList(queryWrapper);

        return list;
    }


    public static List<Integer> getSonIdList(List<Type> list){
        List<Integer> res = new ArrayList<>();
        for (Type t :
                list) {
            res.add(t.getTypeId());
        }
        return res;
    }

    @Override
    public List getConsByType(Long userId, String typeName){

        //先查出对应名称的Type的Id
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("type_name",typeName);
        Type type = typeMapper.selectOne(queryWrapper);
        System.out.println(type);

        //level = 1 , should get all son
        if(type.getLevel()==1){
            List<Integer> idList = getSonIdList(this.getSonListByParentId(type.getTypeId()));

            QueryWrapper wrapper = new QueryWrapper();
            wrapper.inSql("consumption_id","select consumption_id from t_user_consumption where user_id = "+userId);
            wrapper.in("type_id",idList);

            return consumptionMapper.selectList(wrapper);
        }
        else if(type.getLevel()==2){
            QueryWrapper wrapper = new QueryWrapper();
            wrapper.inSql("consumption_id","select consumption_id from t_user_consumption where user_id = "+userId);
            wrapper.eq("type_id",type.getTypeId());

            return consumptionMapper.selectList(wrapper);
        }
        return null;
    }


}
