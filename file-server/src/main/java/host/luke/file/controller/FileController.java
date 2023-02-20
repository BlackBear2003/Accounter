package host.luke.file.controller;

import host.luke.common.utils.ResponseResult;
import host.luke.file.service.FileService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
public class FileController {

    @Resource
    FileService fileService;

    @PostMapping("/file")
    public ResponseResult upload(MultipartFile[] multipartFiles){

        List list = fileService.upload(multipartFiles);

        return new ResponseResult(200,"success",list);

    }


}
