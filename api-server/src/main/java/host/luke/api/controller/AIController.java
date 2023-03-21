package host.luke.api.controller;

import host.luke.api.service.AIService;
import host.luke.common.utils.ResponseResult;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AIController {

    @Resource
    AIService aiService;

    @GetMapping("/shop_receipt")
    public ResponseResult shopReceipt(String fileName){
        return new ResponseResult(200,"success",aiService.shopReceipt(fileName));
    }

}
