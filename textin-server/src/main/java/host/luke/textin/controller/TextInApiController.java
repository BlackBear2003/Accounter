package host.luke.textin.controller;

import host.luke.common.utils.ResponseResult;
import host.luke.textin.service.PreprocessService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/ti")
@SuppressWarnings({"unchecked","rawtypes"})
public class TextInApiController {

    @Resource
    PreprocessService preprocessService;

    @PostMapping("/dewarp")
    public ResponseResult trimAndCorrect(String fileName){

        String newFilePath = null;

        try {
            newFilePath = preprocessService.trimAndCorrect(fileName);
        }catch (RuntimeException | IOException e){
            e.printStackTrace();
            return ResponseResult.error();
        }

        if(Objects.isNull(newFilePath)){
            return ResponseResult.error();
        }
        Map res = new HashMap();
        res.put("imgUrl",newFilePath);
        return new ResponseResult(200,"success",res);

    }






}
