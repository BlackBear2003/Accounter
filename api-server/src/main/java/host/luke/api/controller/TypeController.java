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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class TypeController {

    @Resource
    TypeServiceImpl typeService;
    @Resource
    ConsumptionServiceImpl consumptionService;

    @GetMapping("/consumption/type")
    @IDCheck
    @ApiLog
    public ResponseResult getConsByType(HttpServletRequest request, Integer typeId){

        Long userId = Long.valueOf(request.getHeader("userId"));

        List<Consumption> list = typeService.getConsByType(userId,typeId);

        Map<String,Object> map = new HashMap<>();
        map.put("list",list);

        return new ResponseResult(200,"success",map);
    }

    @GetMapping("/type")
    @ApiLog
    public ResponseResult getTypes(){

        List list = typeService.list();

        Map<String,Object> map = new HashMap<>();
        map.put("list",list);

        return new ResponseResult(200,"success",map);
    }

}
