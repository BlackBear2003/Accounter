package host.luke.api.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import host.luke.api.aop.ApiLog;
import host.luke.api.aop.IDCheck;
import host.luke.api.service.impl.ConsumptionServiceImpl;
import host.luke.api.service.impl.TypeServiceImpl;
import host.luke.common.pojo.Consumption;
import host.luke.common.pojo.Type;
import host.luke.common.utils.ResponseResult;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/api")
public class TypeController {

    @Resource
    TypeServiceImpl typeService;
    @Resource
    ConsumptionServiceImpl consumptionService;

    @GetMapping("/consumption/type")
    @IDCheck
    public ResponseResult getConsByType(HttpServletRequest request, Integer typeId){

        Long userId = Long.valueOf(request.getHeader("userId"));

        List<Consumption> list = typeService.getConsByType(userId,typeId);

        Map<String,Object> map = new HashMap<>();
        map.put("list",list);

        return new ResponseResult(200,"success",map);
    }

    @GetMapping("/type")
    public ResponseResult getTypes(){

        List list = typeService.list();

        Map<String,Object> map = new HashMap<>();
        map.put("list",list);

        return new ResponseResult(200,"success",map);
    }

    @GetMapping("/type/analysis/month")
    @IDCheck
    public ResponseResult getMonthTypeAnalysis(HttpServletRequest request, Date date){

        Long userId = Long.valueOf(request.getHeader("userId"));

        List<Consumption> list = consumptionService.getCurMonthCons(userId,date);

        int[] cnts = new int[17];

        for (Consumption c:
            list) {
            cnts[c.getTypeId()]++;
        }

        TreeMap<Integer,Double> map = new TreeMap<>();
        for(int i=1;i<17;i++){
            if(cnts[i]!=0){
                map.put(i,cnts[i]*1.0/list.size());
            }
        }

        return new ResponseResult(200,"success",map);

    }



    @InitBinder
    public void initBinder(WebDataBinder binder) {
        //转换日期
        DateFormat dateFormat =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat , true));
    }

}
