package host.luke.api.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import host.luke.api.dao.BudgetMapper;
import host.luke.api.service.BudgetService;
import host.luke.common.pojo.Budget;
import org.springframework.stereotype.Service;

@Service
public class BudgetServiceImpl extends ServiceImpl<BudgetMapper, Budget> implements BudgetService {
}
