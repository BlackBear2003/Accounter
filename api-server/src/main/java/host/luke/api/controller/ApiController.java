package host.luke.api.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import host.luke.api.dao.ConsumptionMapper;
import host.luke.api.service.impl.ConsumptionServiceImpl;
import host.luke.common.utils.ResponseResult;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ApiController {
    @Resource
    private ConsumptionServiceImpl consumptionService;
    @Resource
    private ConsumptionMapper consumptionMapper;



    @GetMapping("/consumption/user")
    public ResponseResult getConsByUserId(Long userId){
        return new ResponseResult(200,"success",consumptionMapper.getConsByUserId(userId));
    }

    @GetMapping("/consumption/amount")
    public ResponseResult getConsByAmount(double low,double high){

        QueryWrapper wrapper = new QueryWrapper();
        wrapper.between("amount",low,high);

        return new ResponseResult(200,"success",consumptionMapper.selectList(wrapper));
    }
    @GetMapping("/consumption/type")
    public ResponseResult getConsByType(String type){

        return new ResponseResult(200,"success",consumptionMapper.getConsByUserId(userId));
    }
    @GetMapping("/consumption/year")
    public ResponseResult getConsByYear(Integer year){

        return new ResponseResult(200,"success",consumptionMapper.getConsByUserId(userId));
    }



}
