package host.luke.api.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import host.luke.api.aop.IDCheck;
import host.luke.api.dao.LedgerMapper;
import host.luke.api.service.impl.ConsumptionServiceImpl;
import host.luke.api.service.impl.LedgerServiceImpl;
import host.luke.common.pojo.Consumption;
import host.luke.common.pojo.Ledger;
import host.luke.common.utils.ResponseResult;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.ibatis.annotations.Param;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ledger")
public class LedgerController {


    @Resource
    LedgerServiceImpl ledgerService;
    @Resource
    LedgerMapper ledgerMapper;
    @Resource
    ConsumptionServiceImpl consumptionService;



    @PostMapping("/")
    @Transactional
    @IDCheck
    public ResponseResult addNewLedger(HttpServletRequest request,Ledger ledger){

        Long userId = Long.valueOf(request.getHeader("userId"));

        if(ledger.getUserId()==userId){
            return new ResponseResult(403,"权限不足");
        }

        ledger.setUpdateTime(new Date());
        if(ledgerService.save(ledger)){
            return ResponseResult.success();
        }
        return new ResponseResult(405,"failed");
    }

    @GetMapping("/")
    @IDCheck
    public ResponseResult getUsersLedger(HttpServletRequest request){

        Long userId = Long.valueOf(request.getHeader("userId"));

        QueryWrapper<Ledger> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id",userId);
        List list = ledgerService.list(wrapper);
        //包装
        Map<String,Object> map = new HashMap<>();
        map.put("list",list);

        return new ResponseResult(200,"success",map);

    }

    @DeleteMapping("/")
    @IDCheck
    public ResponseResult removeLedger(HttpServletRequest request,Ledger ledger){
        Long userId = Long.valueOf(request.getHeader("userId"));

        if(ledger.getUserId()==userId){
            return new ResponseResult(403,"权限不足");
        }

        ledger.setUpdateTime(new Date());
        if(ledgerService.removeById(ledger)){
            return ResponseResult.success();
        }
        return new ResponseResult(405,"failed");
    }

    @PutMapping("/")
    @IDCheck
    public ResponseResult updateLedger(HttpServletRequest request,Ledger ledger){

        Long userId = Long.valueOf(request.getHeader("userId"));

        if(ledger.getUserId()==userId){
            return new ResponseResult(403,"权限不足");
        }

        ledger.setUpdateTime(new Date());
        if(ledgerService.updateById(ledger)){
            return ResponseResult.success();
        }
        return new ResponseResult(405,"failed");
    }

    /**
     * 注意：需要先把消费记录保存好再调用此接口
     *          ------此接口仅为保存 账本--消费记录 二元关系！！！
     * @param ledgerId
     * @param ConsumptionId
     * @return
     */
    @PostMapping("/consumption")
    @IDCheck
    public ResponseResult addConsForLedger(Integer ledgerId, Long ConsumptionId){

        if(ledgerMapper.addConsForLedger(ledgerId, ConsumptionId)==1){
            return ResponseResult.success();
        }
        return new ResponseResult(405,"failed");
    }

    @GetMapping("/consumption")
    @IDCheck
    public ResponseResult getLedgerConsumption(Integer ledgerId){

        List<Long> consId = ledgerMapper.getConsIdByLedgerId(ledgerId);
        List<Consumption> list = consumptionService.listByIds(consId);
        //包装
        Map<String,Object> map = new HashMap<>();
        map.put("list",list);

        return new ResponseResult(200,"success",map);

    }

    @DeleteMapping("/consumption")
    @Transactional
    @IDCheck
    public ResponseResult dropConsFromLedger(Integer ledgerId, Long consumptionId){

        if(ledgerMapper.dropConsForLedger(ledgerId,consumptionId)==1){
            if(ledgerService.removeById(ledgerId)){
                return ResponseResult.success();
            }
        }

        return new ResponseResult(405,"failed");
    }

    @GetMapping("/balance")
    @IDCheck
    public ResponseResult getLedgerBalance(Integer ledgerId){

        Double balance = ledgerMapper.getBalance(ledgerId);

        Map<String,Object> map = new HashMap<>();
        map.put("balance",balance);

        return new ResponseResult(200,"success",map);

    }


}
