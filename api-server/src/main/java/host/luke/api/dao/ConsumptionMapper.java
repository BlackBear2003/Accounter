package host.luke.api.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import host.luke.common.pojo.Consumption;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ConsumptionMapper extends BaseMapper<Consumption> {
    public List<Consumption> getConsByUserId(Long userId);
    public int addCons(Long userId,Long consumptionId);
}
