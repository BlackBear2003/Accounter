package host.luke.file.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Component
public class FileService {

    @Value("${file.path}")
    private String filePath;



    public List<String> upload(MultipartFile[] multipartFiles){

        List<String> fileURLList = new ArrayList<>();
        for (MultipartFile file :
                multipartFiles) {
            if (file.isEmpty()){
                continue;
            }
            if (file.getSize()<=0){
                continue;
            }
            String newName = generateFileName(file);
            System.out.println(newName);
            FileOutputStream fileOutputStream = null;
            try {
                fileOutputStream = new FileOutputStream(filePath+newName);
                fileOutputStream.write(file.getBytes());
                fileOutputStream.flush();
                fileOutputStream.close();

                fileURLList.add("http://luke.host/images/"+newName);

            } catch (java.io.IOException e) {
                e.printStackTrace();
            }
        }

        return fileURLList;

    }











    public String generateFileName(MultipartFile multipartFile){
        System.out.println(multipartFile.getOriginalFilename());
        String[] t = Objects.requireNonNull(multipartFile.getOriginalFilename()).split("\\.");
        String suffix = t[t.length-1];
        System.out.println(suffix);
        UUID uuid = UUID.randomUUID();
        return uuid+"."+suffix;
    }

}
