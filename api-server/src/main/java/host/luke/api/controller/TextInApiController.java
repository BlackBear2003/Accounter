package host.luke.api.controller;

import host.luke.common.pojo.Consumption;
import host.luke.common.utils.ResponseResult;
import host.luke.api.service.FunctionService;
import host.luke.api.service.PreprocessService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api")
@SuppressWarnings({"unchecked","rawtypes"})
public class TextInApiController {

    @Resource
    PreprocessService preprocessService;
    @Resource
    FunctionService functionService;

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

    @PostMapping("/crop_enhance")
    public ResponseResult cropAndEnhance(String fileName){

        String newFilePath = null;

        try {
            newFilePath = preprocessService.cropAndEnhance(fileName);
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

    @PostMapping("/demoire")
    public ResponseResult demoire(String fileName){

        String newFilePath = null;

        try {
            newFilePath = preprocessService.demoire(fileName);
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

    @PostMapping("/water_mark")
    public ResponseResult waterMarkRemove(String fileName){

        String newFilePath = null;

        try {
            newFilePath = preprocessService.watermarkRemove(fileName);
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


    @PostMapping("/handwritten_erase")
    public ResponseResult handwrittenErase(String fileName){

        String newFilePath = null;

        try {
            newFilePath = preprocessService.handwrittenErase(fileName);
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


    @PostMapping("/common_receipt")
    public ResponseResult commonReceipt(String fileName){

        Consumption consumption = null;

        try {
            consumption = functionService.commonReceipt(fileName);
        }catch (RuntimeException e){
            e.printStackTrace();
            return ResponseResult.error();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Map res = new HashMap();
        res.put("consumption",consumption);

        return new ResponseResult(200,"success",res);
    }


    @PostMapping("/train_ticket")
    public ResponseResult trainTicket(String fileName){

        Consumption consumption = null;

        try {
            consumption = functionService.trainTicket(fileName);
        }catch (RuntimeException e){
            e.printStackTrace();
            return ResponseResult.error();
        }
        Map res = new HashMap();
        res.put("consumption",consumption);

        return new ResponseResult(200,"success",res);
    }

}
