package host.luke.api.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import host.luke.api.aop.IDCheck;
import host.luke.api.dao.ConsumptionMapper;
import host.luke.api.service.impl.ConsumptionServiceImpl;
import host.luke.common.pojo.Consumption;
import host.luke.common.utils.DateUtil;
import host.luke.common.utils.ResponseResult;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/api")
@SuppressWarnings({"unchecked","rawtypes"})
public class ConsumptionController {
    @Resource
    private ConsumptionServiceImpl consumptionService;
    @Resource
    private ConsumptionMapper consumptionMapper;



    @GetMapping("/consumption")
    @IDCheck
    public ResponseResult getAllConsByUserId(HttpServletRequest request){

        Long userId = Long.valueOf(request.getHeader("userId"));

        List list = consumptionService.getAllConsByUserId(userId);
        //包装
        Map<String,Object> map = new HashMap<>();
        map.put("list",list);

        return new ResponseResult(200,"success",map);
    }

    @GetMapping("/consumption/{c_id}")
    @IDCheck
    public ResponseResult getSingleConsByCid(HttpServletRequest request, @PathVariable Long c_id){

        Long userId = Long.valueOf(request.getHeader("userId"));

        if(!Objects.equals(userId, consumptionMapper.findConsOwner(c_id))){
            return new ResponseResult(401,"无权查看");
        }

        Consumption consumption = consumptionService.getById(c_id);

        Map<String,Object> map = new HashMap<>();
        map.put("consumption",consumption);

        return new ResponseResult(200,"success",map);
    }

    @PostMapping("/consumption")
    @Transactional
    @IDCheck
    public ResponseResult addNewCons(HttpServletRequest request,@RequestBody Consumption consumption){

        Long userId = Long.valueOf(request.getHeader("userId"));


        Consumption insertedCons =  consumptionService.addNewCons(userId,consumption);
        Map<String,Object> map = new HashMap<>();
        map.put("consumption",insertedCons);

        return new ResponseResult(200,"success",map);

    }



    /**
     * 获取用户总的结余
     * @param request
     * @return
     */
    @GetMapping("/consumption/balance")
    @IDCheck
    public ResponseResult getBalanceOfAll(HttpServletRequest request){

        Long userId = Long.valueOf(request.getHeader("userId"));

        Double balance = consumptionService.getBalanceOfAll(userId);

        Map<String,Object> map = new HashMap<>();
        map.put("balance",balance);

        return new ResponseResult(200,"success",map);
    }

    @GetMapping("/consumption/balance/day")
    @IDCheck
    public ResponseResult getBalanceOfDay(HttpServletRequest request,Date date ){

        Long userId = Long.valueOf(request.getHeader("userId"));
        
        Double balance = consumptionService.getBalanceOfDateTime(userId,DateUtil.getDayStartTime(date),DateUtil.getDayEndTime(date));

        Map<String,Object> map = new HashMap<>();
        map.put("balance",balance);

        return new ResponseResult(200,"success",map);
    }


    @GetMapping("/consumption/out")
    @IDCheck
    public ResponseResult getOutPaidCons(HttpServletRequest request){

        Long userId = Long.valueOf(request.getHeader("userId"));

        QueryWrapper wrapper = new QueryWrapper();
        //小于0的
        wrapper.lt("amount",0);
        wrapper.inSql("consumption_id","select consumption_id from t_user_consumption where user_id = "+userId);
        List list = consumptionService.list(wrapper);

        Map<String,Object> map = new HashMap<>();
        map.put("list",list);

        return new ResponseResult(200,"success",map);
    }

    @GetMapping("/consumption/out/sum")
    @IDCheck
    public ResponseResult getOutPaidSum(HttpServletRequest request){

        Long userId = Long.valueOf(request.getHeader("userId"));



        Map<String,Object> map = new HashMap<>();
        map.put("sum",consumptionMapper.getOutPaidOfAll(userId));

        return new ResponseResult(200,"success",map);
    }

    @GetMapping("/consumption/in")
    @IDCheck
    public ResponseResult getInEarnedCons(HttpServletRequest request){

        Long userId = Long.valueOf(request.getHeader("userId"));

        QueryWrapper wrapper = new QueryWrapper();
        //大于0
        wrapper.gt("amount",0);
        wrapper.inSql("consumption_id","select consumption_id from t_user_consumption where user_id = "+userId);
        List list = consumptionService.list(wrapper);

        Map<String,Object> map = new HashMap<>();
        map.put("list",list);

        return new ResponseResult(200,"success",map);
    }

    @GetMapping("/consumption/in/sum")
    @IDCheck
    public ResponseResult getInEarnedSum(HttpServletRequest request){

        Long userId = Long.valueOf(request.getHeader("userId"));



        Map<String,Object> map = new HashMap<>();
        map.put("sum",consumptionMapper.getInEarnedOfAll(userId));

        return new ResponseResult(200,"success",map);
    }

    @GetMapping("/consumption/out/month")
    @IDCheck
    public ResponseResult getOutPaidConsOfMonth(HttpServletRequest request,Date date){

        Long userId = Long.valueOf(request.getHeader("userId"));

        QueryWrapper wrapper = new QueryWrapper();
        //小于0的
        wrapper.lt("amount",0);
        wrapper.between("consume_time", DateUtil.getMonthStartTime(date),DateUtil.getMonthEndTime(date));
        wrapper.inSql("consumption_id","select consumption_id from t_user_consumption where user_id = "+userId);
        List list = consumptionService.list(wrapper);

        Map<String,Object> map = new HashMap<>();
        map.put("list",list);

        return new ResponseResult(200,"success",map);
    }

    @GetMapping("/consumption/out/month/sum")
    @IDCheck
    public ResponseResult getOutPaidSumOfAMonth(HttpServletRequest request,Date date){

        Long userId = Long.valueOf(request.getHeader("userId"));



        Map<String,Object> map = new HashMap<>();
        map.put("sum",consumptionMapper.getOutPaidOfDateTime(userId, DateUtil.getMonthStartTime(date),DateUtil.getMonthEndTime(date)));

        return new ResponseResult(200,"success",map);
    }

    @GetMapping("/consumption/in/month")
    @IDCheck
    public ResponseResult getInEarnedConsOfMonth(HttpServletRequest request,Date date){

        Long userId = Long.valueOf(request.getHeader("userId"));

        QueryWrapper wrapper = new QueryWrapper();
        //大于0
        wrapper.gt("amount",0);
        wrapper.between("consume_time", DateUtil.getMonthStartTime(date),DateUtil.getMonthEndTime(date));
        wrapper.inSql("consumption_id","select consumption_id from t_user_consumption where user_id = "+userId);
        List list = consumptionService.list(wrapper);

        Map<String,Object> map = new HashMap<>();
        map.put("list",list);

        return new ResponseResult(200,"success",map);
    }

    @GetMapping("/consumption/in/month/sum")
    @IDCheck
    public ResponseResult getInEarnedSumOfAMonth(HttpServletRequest request,Date date){

        Long userId = Long.valueOf(request.getHeader("userId"));



        Map<String,Object> map = new HashMap<>();
        map.put("sum",consumptionMapper.getInEarnedOfDateTime(userId, DateUtil.getMonthStartTime(date),DateUtil.getMonthEndTime(date)));

        return new ResponseResult(200,"success",map);
    }


    @GetMapping("/consumption/amount")
    @IDCheck
    public ResponseResult getConsByAmount(HttpServletRequest request,double low,double high){

        Long userId = Long.valueOf(request.getHeader("userId"));

        List list = consumptionService.getConsByAmount(userId,low,high);

        Map<String,Object> map = new HashMap<>();
        map.put("list",list);

        return new ResponseResult(200,"success",map);
    }




    @GetMapping("/consumption/year")
    @IDCheck
    public ResponseResult getCurYearCons(HttpServletRequest request, Date date){

        Long userId = Long.valueOf(request.getHeader("userId"));

        List list = consumptionService.getCurYearCons(userId,date);

        Map<String,Object> map = new HashMap<>();
        map.put("list",list);

        return new ResponseResult(200,"success",map);
    }


    @GetMapping("/consumption/month")
    @IDCheck
    public ResponseResult getCurMonthCons(HttpServletRequest request, Date date){

        Long userId = Long.valueOf(request.getHeader("userId"));

        List list = consumptionService.getCurMonthCons(userId,date);

        Map<String,Object> map = new HashMap<>();
        map.put("list",list);

        return new ResponseResult(200,"success",map);
    }

    @GetMapping("/consumption/last/week")
    @IDCheck
    public ResponseResult getLastWeekCons(HttpServletRequest request,Date date){

        Long userId = Long.valueOf(request.getHeader("userId"));

        List list = consumptionService.getLastWeekCons(userId,date);

        Map<String,Object> map = new HashMap<>();
        map.put("list",list);

        return new ResponseResult(200,"success",map);

    }
    @GetMapping("/consumption/last/year")
    @IDCheck
    public ResponseResult getLastYearCons(HttpServletRequest request,Date date){

        Long userId = Long.valueOf(request.getHeader("userId"));

        List list = consumptionService.getLastYearCons(userId,date);

        Map<String,Object> map = new HashMap<>();
        map.put("list",list);

        return new ResponseResult(200,"success",map);

    }

    @GetMapping("/consumption/last/month")
    @IDCheck
    public ResponseResult getLastMonthCons(HttpServletRequest request,Date date){

        Long userId = Long.valueOf(request.getHeader("userId"));

        List list = consumptionService.getLastMonthCons(userId,date);

        Map<String,Object> map = new HashMap<>();
        map.put("list",list);

        return new ResponseResult(200,"success",map);

    }


    @GetMapping("/consumption/day")
    @IDCheck
    public ResponseResult getCurDayCons(HttpServletRequest request, Date date){

        Long userId = Long.valueOf(request.getHeader("userId"));

        List list = consumptionService.getCurDayCons(userId,date);

        Map<String,Object> map = new HashMap<>();
        map.put("list",list);

        return new ResponseResult(200,"success",map);
    }

    @GetMapping("/consumption/week")
    @IDCheck
    public ResponseResult getCurWeekCons(HttpServletRequest request, Date date){

        Long userId = Long.valueOf(request.getHeader("userId"));

        List list = consumptionService.getCurWeekCons(userId,date);

        Map<String,Object> map = new HashMap<>();
        map.put("list",list);
        map.put("weekNo",DateUtil.getYearWeekIndex(date));
        map.put("weekday",DateUtil.getWeekDay(date));

        return new ResponseResult(200,"success",map);
    }
    @GetMapping("/consumption/quarter")
    @IDCheck
    public ResponseResult getCurQuarterCons(HttpServletRequest request, Date date){

        Long userId = Long.valueOf(request.getHeader("userId"));

        List list = consumptionService.getCurQuarterCons(userId,date);

        Map<String,Object> map = new HashMap<>();
        map.put("list",list);
        map.put("quarterNo",DateUtil.getYearQuarterIndex(date));

        return new ResponseResult(200,"success",map);
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        //转换日期
        DateFormat dateFormat =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat , true));
    }


}
