package host.luke.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import host.luke.api.dao.ConsumptionMapper;
import host.luke.api.service.ConsumptionService;
import host.luke.common.pojo.Consumption;
import host.luke.common.utils.DateUtil;
import host.luke.common.utils.SnowFlake;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@SuppressWarnings(value = { "unchecked", "rawtypes" })
public class ConsumptionServiceImpl extends ServiceImpl<ConsumptionMapper, Consumption> implements ConsumptionService {

    @Resource
    ConsumptionMapper consumptionMapper;

    @Override
    public List getAllConsByUserId(Long userId){

        return consumptionMapper.getConsByUserId(userId);
    }



    @Override
    public Consumption getSingleConsByCid(String c_id){

        return consumptionMapper.selectById(c_id);
    }


    @Override
    public Double getBalanceOfAll(Long userId){
        return consumptionMapper.getBalanceOfAll(userId);
    }

    @Override
    public Double getBalanceOfDateTime(Long userId, Date startDate, Date endDate){
        return consumptionMapper.getBalanceOfDateTime(userId,startDate,endDate);
    }


    @Override
    @Transactional
    public Consumption addNewCons(Long userId, Consumption consumption){

        SnowFlake snowFlake = new SnowFlake(0,0);
        consumption.setConsumptionId(snowFlake.nextId());
        int ins = consumptionMapper.insert(consumption);
        int add = consumptionMapper.addCons(userId,consumption.getConsumptionId());

        if(ins>0&&add>0){
            return consumption;
        }
        else{
            throw new RuntimeException("插入失败");
        }
    }

    @Override
    public List getConsByAmount(Long userId, double low, double high){

        QueryWrapper wrapper = new QueryWrapper();
        wrapper.between("amount",low,high);
        wrapper.inSql("consumption_id","select consumption_id from t_user_consumption where user_id = "+userId);

        return (List<Consumption>) consumptionMapper.selectList(wrapper);
    }





    @Override
    public List getCurYearCons(Long userId, Date date){

        QueryWrapper wrapper = new QueryWrapper();
        wrapper.between("consume_time", DateUtil.getYearStartTime(date),DateUtil.getYearEndTime(date));
        wrapper.inSql("consumption_id","select consumption_id from t_user_consumption where user_id = "+userId);

        return consumptionMapper.selectList(wrapper);
    }


    @Override
    public List getCurMonthCons(Long userId, Date date){

        QueryWrapper wrapper = new QueryWrapper();
        wrapper.between("consume_time", DateUtil.getMonthStartTime(date),DateUtil.getMonthEndTime(date));
        wrapper.inSql("consumption_id","select consumption_id from t_user_consumption where user_id = "+userId);


        return consumptionMapper.selectList(wrapper);
    }

    @Override
    public List getLastMonthCons(Long userId, Date date){

        QueryWrapper wrapper = new QueryWrapper();
        wrapper.between("consume_time", DateUtil.getLastAMonthTime(date),date);
        wrapper.inSql("consumption_id","select consumption_id from t_user_consumption where user_id = "+userId);


        return consumptionMapper.selectList(wrapper);
    }
    @Override
    public List getLastWeekCons(Long userId, Date date){

        QueryWrapper wrapper = new QueryWrapper();
        wrapper.between("consume_time", DateUtil.getLastAWeekTime(date),date);
        wrapper.inSql("consumption_id","select consumption_id from t_user_consumption where user_id = "+userId);


        return consumptionMapper.selectList(wrapper);
    }
    @Override
    public List getLastYearCons(Long userId, Date date){

        QueryWrapper wrapper = new QueryWrapper();
        wrapper.between("consume_time", DateUtil.getLastAYearTime(date),date);
        wrapper.inSql("consumption_id","select consumption_id from t_user_consumption where user_id = "+userId);


        return consumptionMapper.selectList(wrapper);
    }

    @Override
    public List getCurDayCons(Long userId, Date date){

        QueryWrapper wrapper = new QueryWrapper();
        wrapper.between("consume_time",DateUtil.getDayStartTime(date),DateUtil.getDayEndTime(date));
        wrapper.inSql("consumption_id","select consumption_id from t_user_consumption where user_id = "+userId);

        return consumptionMapper.selectList(wrapper);
    }

    @Override
    public List getCurWeekCons(Long userId, Date date){

        QueryWrapper wrapper = new QueryWrapper();
        wrapper.between("consume_time",DateUtil.getWeekStartTime(date),DateUtil.getWeekEndTime(date));
        wrapper.inSql("consumption_id","select consumption_id from t_user_consumption where user_id = "+userId);

        return consumptionMapper.selectList(wrapper);
    }

    @Override
    public List getCurQuarterCons(Long userId, Date date){

        QueryWrapper wrapper = new QueryWrapper();
        wrapper.between("consume_time",DateUtil.getQuarterStartTime(date),DateUtil.getQuarterEndTime(date));
        wrapper.inSql("consumption_id","select consumption_id from t_user_consumption where user_id = "+userId);

        return consumptionMapper.selectList(wrapper);
    }
}
