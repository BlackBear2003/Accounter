package host.luke.api.service;

import ch.qos.logback.core.util.FileUtil;
import host.luke.common.pojo.Consumption;
import jakarta.annotation.Resource;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.util.Base64Util;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.support.HttpRequestWrapper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;

@Service
public class AIService {

    private final static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Value("${file.path.win}")
    String winPath ;
    @Value("${file.path.mac}")
    String macPath ;
    @Value("${file.path.linux}")
    String linuxPath ;



    public String judgeEnvPath(){
        String osName = System.getProperty("os.name");
        osName = osName.toLowerCase();
        if(osName.startsWith("win")){
            return winPath;
        }
        if(osName.startsWith("mac")){
            return macPath;
        }
        if(osName.startsWith("linux")){
            return linuxPath;
        }
        return null;
    }

    public Consumption shopReceipt(String fileName){
        CloseableHttpClient httpClient = HttpClients.createDefault();


        // 请求url
        String url = "https://aip.baidubce.com/rest/2.0/ocr/v1/shopping_receipt?access_token=24.784cd12fcf6583ddac0c574805b6e102.2592000.1681741642.282335-31415609";
        try {
            // 本地文件路径
            String filePath = judgeEnvPath();
            byte[] imgData = readFile(filePath+fileName);
            String imgStr = Base64Util.encode(fileName);
            String imgParam = URLEncoder.encode(imgStr, "UTF-8");

            String param = "image=" + imgParam;

            HttpPost post = new HttpPost(url);
            post.setHeader("Content-Type","application/x-www-form-urlencoded");
            post.setEntity(new StringEntity("image=", ContentType.parse(imgParam)));

            HttpResponse httpResponse = httpClient.execute(post);
            HttpEntity entity = httpResponse.getEntity();
            String str = EntityUtils.toString(entity);
            System.out.println(str);
            return null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    public static byte[] readFile(String path)
    {
        String imgFile = path;
        InputStream in = null;
        byte[] data = null;
        try
        {
            in = new FileInputStream(imgFile);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }


}
