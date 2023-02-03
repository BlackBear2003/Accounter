package host.luke.api.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import host.luke.api.dao.ConsumptionMapper;
import host.luke.api.service.ConsumptionService;
import host.luke.common.pojo.Consumption;
import org.springframework.stereotype.Service;

@Service
public class ConsumptionServiceImpl extends ServiceImpl<ConsumptionMapper, Consumption> implements ConsumptionService {

}
