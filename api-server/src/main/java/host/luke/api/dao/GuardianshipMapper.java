package host.luke.api.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import host.luke.common.pojo.Guardianship;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface GuardianshipMapper extends BaseMapper<Guardianship> {
}
