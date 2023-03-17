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
    public ResponseResult getConsByType(HttpServletRequest request, String typeName){

        Long userId = Long.valueOf(request.getHeader("userId"));

        List<Consumption> list = typeService.getConsByType(userId,typeName);

        Map<String,Object> map = new HashMap<>();
        map.put("list",list);

        return new ResponseResult(200,"success",map);
    }

    @GetMapping("/type")
    @ApiLog
    public ResponseResult getLevelOneTypes(){

        List list = typeService.getLevelOneTypes();

        Map<String,Object> map = new HashMap<>();
        map.put("list",list);

        return new ResponseResult(200,"success",map);
    }

    @GetMapping("/type/son")
    @ApiLog
    public ResponseResult getLevelTwoTypes(String levelOneType){

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("type_name",levelOneType);
        Type type = typeService.getOne(queryWrapper);
        System.out.println(type);

        List list = typeService.getSonListByParentId(type.getTypeId());

        Map<String,Object> map = new HashMap<>();
        map.put("list",list);

        return new ResponseResult(200,"success",map);
    }




}
