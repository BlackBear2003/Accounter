package host.luke.auth.security;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import host.luke.auth.dao.UserMapper;
import host.luke.auth.pojo.User;
import host.luke.auth.service.impl.UserServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class UserDetailServiceMobileImpl implements UserDetailsService {

    @Resource
    UserServiceImpl userService;
    @Resource
    UserMapper userMapper;


    @Override
    public UserDetails loadUserByUsername(String mobile) throws UsernameNotFoundException {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("mobile",mobile);
        User user = userService.getOne(queryWrapper);


        if(Objects.isNull(user)){
            throw new RuntimeException("查无此手机号");
        }

        List<String> list = userMapper.getAuthByUid(user.getUserId());

        return new LoginUser(user,list);

    }
}
