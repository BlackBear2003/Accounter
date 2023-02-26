package host.luke.api.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import host.luke.api.aop.IDCheck;
import host.luke.api.dao.ConsumptionMapper;
import host.luke.api.service.impl.ConsumptionServiceImpl;
import host.luke.api.service.impl.GuardianshipServiceImpl;
import host.luke.common.pojo.Consumption;
import host.luke.common.pojo.Guardianship;
import host.luke.common.utils.ResponseResult;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/api/guardianship")
public class GuardianshipController {
    @Resource
    GuardianshipServiceImpl guardianshipService;
    @Resource
    ConsumptionServiceImpl consumptionService;

    @Resource
    ConsumptionMapper consumptionMapper;

    @PostMapping("")
    @IDCheck
    public ResponseResult addBind(HttpServletRequest request,String code){

        Long guardianUserId = Long.valueOf(request.getHeader("userId"));
        Long wardUserId = guardianshipService.getUserIdByCode(code);

        if(wardUserId!=0){
            Guardianship guardianship = new Guardianship();
            guardianship.setGuardianId(guardianUserId);
            guardianshipService.save(guardianship);
            Map res = new HashMap();
            res.put("guardianship",guardianship);
            return new ResponseResult(200,"success",res);
        }
        return ResponseResult.error();
    }

    @PutMapping("")
    @IDCheck
    public ResponseResult update(HttpServletRequest request,Guardianship guardianship){

        Long userId = Long.valueOf(request.getHeader("userId"));

        if(userId!=guardianship.getGuardianId()){
            return new ResponseResult(403,"权限不足");
        }

        if(guardianshipService.updateById(guardianship)){
            Map res = new HashMap();
            res.put("guardianship",guardianship);
            return new ResponseResult(200,"success",res);
        }
        return ResponseResult.error();
    }

    @DeleteMapping("")
    @IDCheck
    public ResponseResult deleteBind(HttpServletRequest request,Guardianship guardianship){

        Long userId = Long.valueOf(request.getHeader("userId"));

        if(userId!=guardianship.getGuardianId()){
            return new ResponseResult(403,"权限不足");
        }

        if(guardianshipService.removeById(guardianship)){
            return ResponseResult.success();
        }
        return ResponseResult.error();
    }

    @GetMapping("")
    @IDCheck
    public ResponseResult getWards(HttpServletRequest request){
        Long userId = Long.valueOf(request.getHeader("userId"));

        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("guardian_id",userId);

        List list = guardianshipService.list(wrapper);

        Map res = new HashMap();
        res.put("list",list);
        return new ResponseResult(200,"success",res);
    }

    /**
     * @info  分页获取被监护人的消费信息
     * @param request
     * @param wardId
     * @param size
     * @param current
     * @return list ,pages and total
     */
    @GetMapping("/consumption")
    @IDCheck
    public ResponseResult getWardConsumption(HttpServletRequest request,Long wardId,Integer size,Integer current){
        Long userId = Long.valueOf(request.getHeader("userId"));

        if(!guardianshipService.hasGuardianRight(userId,wardId)){
            return new ResponseResult<>(403,"权限不足");
        }

        Page<Consumption> page = new Page<>(current,size);
        QueryWrapper wrapper = new QueryWrapper();
        //select by ward!!!
        wrapper.inSql("consumption_id","select consumption_id from t_user_consumption where user_id = "+wardId);
        IPage<Consumption> iPage = consumptionMapper.selectPage(page ,wrapper);
        List<Consumption> list = iPage.getRecords();

        //包装
        Map<String,Object> map = new HashMap<>();
        map.put("list",list);
        map.put("pages",iPage.getPages());
        map.put("total",iPage.getTotal());

        return new ResponseResult(200,"success",map);
    }

}
