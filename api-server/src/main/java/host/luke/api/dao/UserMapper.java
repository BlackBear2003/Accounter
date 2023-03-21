package host.luke.api.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import host.luke.common.pojo.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
