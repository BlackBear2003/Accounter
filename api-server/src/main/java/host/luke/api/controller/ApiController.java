package host.luke.api.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import host.luke.api.aop.IDCheck;
import host.luke.api.dao.ConsumptionMapper;
import host.luke.api.service.impl.ConsumptionServiceImpl;
import host.luke.common.pojo.Consumption;
import host.luke.common.utils.ResponseResult;
import host.luke.common.utils.SnowFlake;
import jakarta.annotation.Resource;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ApiController {
    @Resource
    private ConsumptionServiceImpl consumptionService;
    @Resource
    private ConsumptionMapper consumptionMapper;



    @GetMapping("/consumption")
    @IDCheck
    public ResponseResult getAllConsByUserId(Long userId){
        Long start = System.currentTimeMillis();
        List<Consumption> list = consumptionMapper.getConsByUserId(userId);
        Long end = System.currentTimeMillis();
        return new ResponseResult(200,"success in "+(end-start)+"ms",list);
    }

    @GetMapping("/consumption/{c_id}")
    @IDCheck
    public ResponseResult getSingleConsByCid(Long userId, @PathVariable String c_id){
        Long start = System.currentTimeMillis();
        if(userId!=consumptionMapper.findConsOwner(Long.valueOf(c_id))){
            return new ResponseResult(401,"无权查看");
        }
        Long end = System.currentTimeMillis();
        return new ResponseResult(200,"success in "+(end-start)+"ms",consumptionService.getById(c_id));
    }

    @PostMapping("/consumption")
    @Transactional
    @IDCheck
    public ResponseResult addNewCons(Long userId,Consumption consumption){
        Long start = System.currentTimeMillis();
        SnowFlake snowFlake = new SnowFlake(0,0);
        consumption.setConsumptionId(snowFlake.nextId());
        consumptionService.save(consumption);
        consumptionMapper.addCons(userId,consumption.getConsumptionId());
        Long end = System.currentTimeMillis();
        Map<String,Object> map = new HashMap<>();
        map.put("consumptionId",consumption.getConsumptionId());
        return new ResponseResult(200,"success in "+(end-start)+"ms",map);
    }

    @GetMapping("/consumption/amount")
    @IDCheck
    public ResponseResult getConsByAmount(Long userId,double low,double high){
        Long start = System.currentTimeMillis();
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.between("amount",low,high);
        wrapper.inSql("consumption_id","select consumption_id from t_user_consumption where user_id = "+userId);
        List<Consumption> list = consumptionMapper.selectList(wrapper);
        Long end = System.currentTimeMillis();
        return new ResponseResult(200,"success in "+(end-start)+"ms",list);
    }
    @GetMapping("/consumption/type")
    @IDCheck
    public ResponseResult getConsByType(Long userId,String type){
        Long start = System.currentTimeMillis();
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("type",type);
        wrapper.inSql("consumption_id","select consumption_id from t_user_consumption where user_id = "+userId);
        List<Consumption> list = consumptionMapper.selectList(wrapper);
        Long end = System.currentTimeMillis();
        return new ResponseResult(200,"success in "+(end-start)+"ms",list);
    }
    @GetMapping("/consumption/year")
    @IDCheck
    public ResponseResult getConsByYear(Long userId,Integer year){
        Long start = System.currentTimeMillis();
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("year",year);
        wrapper.inSql("consumption_id","select consumption_id from t_user_consumption where user_id = "+userId);
        List<Consumption> list = consumptionMapper.selectList(wrapper);
        Long end = System.currentTimeMillis();
        return new ResponseResult(200,"success in "+(end-start)+"ms",list);
    }
    @GetMapping("/consumption/month")
    @IDCheck
    public ResponseResult getConsByMonth(Long userId,Integer month,Integer year){
        Long start = System.currentTimeMillis();
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("month",month);
        wrapper.eq("year",year);
        wrapper.inSql("consumption_id","select consumption_id from t_user_consumption where user_id = "+userId);
        List<Consumption> list = consumptionMapper.selectList(wrapper);
        Long end = System.currentTimeMillis();
        return new ResponseResult(200,"success in "+(end-start)+"ms",list);
    }
    @GetMapping("/consumption/day")
    @IDCheck
    public ResponseResult getConsByDay(Long userId,Integer day,Integer month,Integer year){
        Long start = System.currentTimeMillis();
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("day",day);
        wrapper.eq("month",month);
        wrapper.eq("year",year);
        wrapper.inSql("consumption_id","select consumption_id from t_user_consumption where user_id = "+userId);
        List<Consumption> list = consumptionMapper.selectList(wrapper);
        Long end = System.currentTimeMillis();
        return new ResponseResult(200,"success in "+(end-start)+"ms",list);
    }



}
