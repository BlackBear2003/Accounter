package host.luke.api.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import host.luke.api.aop.IDCheck;
import host.luke.api.dao.ConsumptionMapper;
import host.luke.api.dao.MultiLedgerMapper;
import host.luke.api.dto.SafetyUser;
import host.luke.api.service.impl.ConsumptionServiceImpl;
import host.luke.api.service.impl.MultiLedgerServiceImpl;
import host.luke.api.service.impl.UserServiceImpl;
import host.luke.common.pojo.Consumption;
import host.luke.common.pojo.MultiLedger;
import host.luke.common.utils.ResponseResult;
import io.swagger.v3.oas.models.security.SecurityScheme;
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
@RequestMapping("/api/multiLedger")
public class MultiLedgerController {

    @Resource
    MultiLedgerMapper multiLedgerMapper;
    @Resource
    MultiLedgerServiceImpl multiLedgerService;
    @Resource
    UserServiceImpl userService;
    @Resource
    ConsumptionServiceImpl consumptionService;

    @GetMapping("")
    @IDCheck
    public ResponseResult getUMultiLedgerByUserId(HttpServletRequest request){

        Long userId = Long.valueOf(request.getHeader("userId"));

        List<Integer> idList = multiLedgerMapper.getMLedgerIdByUserId(userId);
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.orderByDesc("modify_time");
        wrapper.in("multi_ledger_id",idList);
        List<MultiLedger> list = multiLedgerService.list(wrapper);

        //包装
        Map<String,Object> map = new HashMap<>();
        map.put("list",list);

        return new ResponseResult(200,"success",map);

    }

    @PostMapping("")
    @IDCheck
    @Transactional
    public ResponseResult addMultiLedger(HttpServletRequest request,@RequestBody MultiLedger multiLedger){

        Long userId = Long.valueOf(request.getHeader("userId"));

        multiLedgerMapper.insert(multiLedger);
        //System.out.println(multiLedger);
        multiLedgerMapper.addLedgerUserBind(userId,multiLedger.getMultiLedgerId());

        //包装
        Map<String,Object> map = new HashMap<>();
        map.put("multiLedger",multiLedger);

        return new ResponseResult(200,"success",map);
    }

    @PostMapping("/join")
    @IDCheck
    public ResponseResult joinMultiLedger(HttpServletRequest request,String password){

        Long userId = Long.valueOf(request.getHeader("userId"));

        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("password",password);
        MultiLedger multiLedger = multiLedgerService.getOne(wrapper);

        //System.out.println(multiLedger);
        multiLedgerMapper.addLedgerUserBind(userId,multiLedger.getMultiLedgerId());
        multiLedger.setModifyTime(new Date());

        //包装
        Map<String,Object> map = new HashMap<>();
        map.put("multiLedger",multiLedger);

        return new ResponseResult(200,"success",map);
    }

    @DeleteMapping("")
    @IDCheck
    @Transactional
    public ResponseResult quitMultiLedger(HttpServletRequest request,Integer multiLedgerId){

        Long userId = Long.valueOf(request.getHeader("userId"));

        multiLedgerMapper.deleteLedgerUserBind(userId,multiLedgerId);
        multiLedgerMapper.deleteById(multiLedgerId);

        return ResponseResult.success();

    }

    @PutMapping("")
    @IDCheck
    public ResponseResult updateMultiLedger(@RequestBody MultiLedger multiLedger){

        multiLedgerService.updateById(multiLedger);
        //包装
        Map<String,Object> map = new HashMap<>();
        map.put("multiLedger",multiLedger);

        return new ResponseResult(200,"success",map);
    }

    @DeleteMapping("/quit")
    @IDCheck
    public ResponseResult deleteMultiLedger(HttpServletRequest request,Integer multiLedgerId){

        Long userId = Long.valueOf(request.getHeader("userId"));

        multiLedgerMapper.deleteLedgerUserBind(userId,multiLedgerId);
        multiLedgerMapper.deleteById(multiLedgerId);

        return ResponseResult.success();

    }

    @GetMapping("/user")
    @IDCheck
    public ResponseResult getUsersByMultiLedgerId(Integer multiLedgerId){

        List<Long> idList = multiLedgerMapper.getLedgerAllUser(multiLedgerId);
        List<SafetyUser> list = new ArrayList<>();
        for (Long id:
            idList){
            list.add(new SafetyUser(userService.getById(id)));
        }

        //包装
        Map<String,Object> map = new HashMap<>();
        map.put("list",list);

        return new ResponseResult(200,"success",map);
    }

    @GetMapping("/consumption")
    @IDCheck
    public ResponseResult getConsumptionsByMultiLedgerId(Integer multiLedgerId){

        List<Consumption> list = multiLedgerMapper.getLedgerConsList(multiLedgerId);

        //包装
        Map<String,Object> map = new HashMap<>();
        map.put("list",list);

        return new ResponseResult(200,"success",map);

    }

    @GetMapping("/balance")
    @IDCheck
    public ResponseResult getBalanceByMultiLedgerId(Integer multiLedgerId){

        double balance = multiLedgerMapper.getBalance(multiLedgerId);

        //包装
        Map<String,Object> map = new HashMap<>();
        map.put("balance",balance);

        return new ResponseResult(200,"success",map);

    }

    @PostMapping("/consumption")
    @IDCheck
    public ResponseResult addConsToMultiLedger(HttpServletRequest request,Long consId,Integer multiLedgerId){

        Long userId = Long.valueOf(request.getHeader("userId"));

        multiLedgerMapper.addMultiCons(userId,consId,multiLedgerId);

        UpdateWrapper wrapper = new UpdateWrapper();
        wrapper.eq("multi_ledger_id",multiLedgerId);
        wrapper.set("modify_time",new Date());
        multiLedgerService.update(wrapper);

        return ResponseResult.success();
    }

    @DeleteMapping("/consumption")
    @IDCheck
    public ResponseResult deleteConsFromMultiLedger(HttpServletRequest request,Long consId,Integer multiLedgerId){

        Long userId = Long.valueOf(request.getHeader("userId"));

        multiLedgerMapper.deleteMultiCons(userId,consId,multiLedgerId);

        return ResponseResult.success();
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        //转换日期
        DateFormat dateFormat =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat , true));
    }



}
