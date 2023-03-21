package host.luke.api.controller;

import host.luke.api.aop.IDCheck;
import host.luke.api.dao.ConsumptionMapper;
import host.luke.api.dao.MultiLedgerMapper;
import host.luke.api.dto.SafetyUser;
import host.luke.api.service.impl.ConsumptionServiceImpl;
import host.luke.api.service.impl.MultiLedgerServiceImpl;
import host.luke.api.service.impl.UserServiceImpl;
import host.luke.common.pojo.MultiLedger;
import host.luke.common.utils.ResponseResult;
import io.swagger.v3.oas.models.security.SecurityScheme;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public ResponseResult getUserAllMLedger(HttpServletRequest request){

        Long userId = Long.valueOf(request.getHeader("userId"));

        List<Integer> idList = multiLedgerMapper.getMLedgerIdByUserId(userId);
        List<MultiLedger> list = multiLedgerService.listByIds(idList);

        //包装
        Map<String,Object> map = new HashMap<>();
        map.put("list",list);

        return new ResponseResult(200,"success",map);

    }

    @PostMapping("")
    @IDCheck
    public ResponseResult addNewMultiLedger(MultiLedger multiLedger){



    }



//    List<SafetyUser> list = null;
//        for (Integer id:
//    idList) {
//        list.add(new SafetyUser(userService.getById(id)));
//    }


}
