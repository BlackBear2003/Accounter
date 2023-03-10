package host.luke.api.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import host.luke.api.aop.IDCheck;
import host.luke.api.dao.BudgetMapper;
import host.luke.api.service.impl.BudgetServiceImpl;
import host.luke.common.pojo.Budget;
import host.luke.common.pojo.Ledger;
import host.luke.common.utils.ResponseResult;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/budget")
public class BudgetController {
    @Resource
    BudgetServiceImpl budgetService;
    @Resource
    BudgetMapper budgetMapper;

    @GetMapping("")
    @IDCheck
    public ResponseResult getUserAllBudget(HttpServletRequest request){
        Long userId = Long.valueOf(request.getHeader("userId"));

        List<Integer> idList = budgetMapper.getAllBudgetIdByUserId(userId);
        List list = budgetService.listByIds(idList);
        //包装
        Map<String,Object> map = new HashMap<>();
        map.put("list",list);

        return new ResponseResult(200,"success",map);
    }

    @PostMapping("")
    @Transactional
    @IDCheck
    public ResponseResult addNewBudget(HttpServletRequest request,@RequestBody Budget budget){
        Long userId = Long.valueOf(request.getHeader("userId"));

        if(budgetMapper.insert(budget)==1){
            if(budgetMapper.addBudgetForUser(budget.getBudgetId(),userId)==1){
                //包装
                Map<String,Object> map = new HashMap<>();
                map.put("budget",budget);
                return new ResponseResult(200,"success",map);
            }
        }
        return new ResponseResult(405,"failed");
    }

    @DeleteMapping("")
    @IDCheck
    @Transactional
    public ResponseResult removeBudget(HttpServletRequest request,@RequestBody Budget budget){
        Long userId = Long.valueOf(request.getHeader("userId"));

        if(budgetService.removeById(budget)){
            if(budgetMapper.dropBudgetForUser(budget.getBudgetId(),userId)==1){
                return ResponseResult.success();
            }
        }
        return new ResponseResult(405,"failed");
    }

    @PutMapping("")
    @IDCheck
    public ResponseResult updateBudget(@RequestBody Budget budget){

        if(budgetService.updateById(budget)){
            //包装
            Map<String,Object> map = new HashMap<>();
            map.put("budget",budget);
            return new ResponseResult(200,"success",map);
        }
        return new ResponseResult(405,"failed");

    }

    @GetMapping("/sum")
    @IDCheck
    public ResponseResult getThisMonthBudget(HttpServletRequest request){

        Long userId = Long.valueOf(request.getHeader("userId"));


        List<Integer> idList = budgetMapper.getAllBudgetIdByUserId(userId);
        List<Budget> budgetList = budgetService.listByIds(idList);

        double sum = 0;

        for(Budget budget
          : budgetList){
            sum+=budget.getMoney();
        }

        //包装
        Map<String,Object> map = new HashMap<>();
        map.put("sum",sum);

        return new ResponseResult(200,"success",map);

    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        //转换日期
        DateFormat dateFormat =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat , true));
    }


}
