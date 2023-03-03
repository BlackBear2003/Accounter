package host.luke.api.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import host.luke.api.dao.GoalMapper;
import host.luke.api.service.GoalService;
import host.luke.common.pojo.Goal;
import org.springframework.stereotype.Service;

@Service
public class GoalServiceImpl extends ServiceImpl<GoalMapper, Goal> implements GoalService {
}
