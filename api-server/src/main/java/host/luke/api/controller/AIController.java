package host.luke.api.controller;

import host.luke.api.dto.Sentence;
import host.luke.api.service.AIService;
import host.luke.common.utils.Base64Util;
import host.luke.common.utils.ResponseResult;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class AIController {

    @Resource
    AIService aiService;

    @GetMapping("/shop_receipt")
    public ResponseResult shopReceipt(String fileName){
        return new ResponseResult(200,"success",aiService.shopReceipt(fileName));
    }

    @PostMapping("/speech_recog")
    public ResponseResult speechRecognition(MultipartFile speech) {
        Sentence sentence = null;
        try{
            Long taskId = aiService.getRecTask(Base64Util.fileToBase64(speech.getInputStream()));
            sentence = aiService.describeTask(taskId);
        }
        catch (IOException e){
            e.printStackTrace();
        }
       return new ResponseResult(200,"success",sentence);
    }

}
