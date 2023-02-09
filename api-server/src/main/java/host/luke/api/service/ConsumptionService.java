package host.luke.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import host.luke.common.pojo.Consumption;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

public interface ConsumptionService extends IService<Consumption> {


    List getAllConsByUserId(Long userId);

    Consumption getSingleConsByCid(String c_id);

    @Transactional
    Boolean addNewCons(Long userId, Consumption consumption);

    List getConsByAmount(Long userId, double low, double high);


    List getCurYearCons(Long userId, Date date);

    List getCurMonthCons(Long userId, Date date);

    List getCurDayCons(Long userId, Date date);

    List getCurWeekCons(Long userId, Date date);

    List getCurQuarterCons(Long userId, Date date);
}
