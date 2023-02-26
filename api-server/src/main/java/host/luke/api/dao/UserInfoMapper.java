package host.luke.api.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import host.luke.common.pojo.UserInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserInfoMapper extends BaseMapper<UserInfo> {
}
