package host.luke.api.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import host.luke.api.dao.UserMapper;
import host.luke.api.service.UserService;
import host.luke.common.pojo.User;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
