package host.luke.api.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import host.luke.api.aop.IDCheck;
import host.luke.api.dao.ConsumptionMapper;
import host.luke.api.service.impl.ConsumptionServiceImpl;
import host.luke.api.service.impl.GoalServiceImpl;
import host.luke.common.pojo.Goal;
import host.luke.common.utils.DateUtil;
import host.luke.common.utils.ResponseResult;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/goal")
public class GoalController {

    @Resource
    GoalServiceImpl goalService;
    @Resource
    ConsumptionServiceImpl consumptionService;
    @Resource
    ConsumptionMapper consumptionMapper;

    @GetMapping("")
    @IDCheck
    public ResponseResult getGoalsByUserId(HttpServletRequest request){
        Long userId = Long.valueOf(request.getHeader("userId"));

        QueryWrapper<Goal> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",userId);
        List list = goalService.list(queryWrapper);
        //包装
        Map<String,Object> map = new HashMap<>();
        map.put("list",list);

        return new ResponseResult(200,"success",map);
    }

    @PostMapping("")
    @IDCheck
    public ResponseResult addNewGoal(@RequestBody Goal goal){

        if(goalService.save(goal)){
            //包装
            Map<String,Object> map = new HashMap<>();
            map.put("goal",goal);
            return new ResponseResult(200,"success",map);
        }
        return new ResponseResult(405,"failed");

    }

    @PutMapping ("")
    @IDCheck
    public ResponseResult updateGoal(@RequestBody Goal goal){

        if(goalService.updateById(goal)){
            //包装
            Map<String,Object> map = new HashMap<>();
            map.put("goal",goal);
            return new ResponseResult(200,"success",map);
        }
        return new ResponseResult(405,"failed");

    }

    @DeleteMapping("")
    @IDCheck
    public ResponseResult removeGoal(@RequestBody Goal goal){

        if(goalService.removeById(goal)){
            return ResponseResult.success();
        }
        return new ResponseResult(405,"failed");

    }

    @GetMapping("/{goalId}/analyse")
    @IDCheck
    public ResponseResult analyseGoalById(@PathVariable Integer goalId){

        Goal goal = goalService.getById(goalId);

        Double balanceMoney = consumptionMapper.getBalanceOfDateTime(goal.getUserId(),goal.getCreateDate(),new Date());

        //包装
        Map<String,Object> map = new HashMap<>();
        map.put("balance_money",balanceMoney);
        map.put("total_money",goal.getMoney());
        map.put("finish_rate",balanceMoney / goal.getMoney());
        map.put("last_day", DateUtil.getYearDayIndex(goal.getDeadline())-DateUtil.getYearDayIndex(new Date()));

        return new ResponseResult(200,"success",map);

    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        //转换日期
        DateFormat dateFormat =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat , true));
    }


}
