package host.luke.api.service;

import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.asr.v20190614.AsrClient;
import com.tencentcloudapi.asr.v20190614.models.*;
import ch.qos.logback.core.util.FileUtil;
import host.luke.api.dto.Sentence;
import host.luke.common.pojo.Consumption;
import host.luke.common.utils.Base64Util;
import io.jsonwebtoken.lang.Collections;
import jakarta.annotation.Resource;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
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
import java.util.ArrayList;

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
            String imgStr = Base64Util.GetImageStr(filePath+fileName);
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

    public Long getRecTask(String base64Voice){
        Long taskId = null;
        try{
            // 实例化一个认证对象，入参需要传入腾讯云账户 SecretId 和 SecretKey，此处还需注意密钥对的保密
            // 代码泄露可能会导致 SecretId 和 SecretKey 泄露，并威胁账号下所有资源的安全性。以下代码示例仅供参考，建议采用更安全的方式来使用密钥，请参见：https://cloud.tencent.com/document/product/1278/85305
            // 密钥可前往官网控制台 https://console.cloud.tencent.com/cam/capi 进行获取
            Credential cred = new Credential("AKIDjE5cts5qCS2DoOzkWxrL5rqyqf4VqzES", "s5HCtAXBiU1uSLnY3pa6XUeBMGwjWFGE");
            // 实例化一个http选项，可选的，没有特殊需求可以跳过
            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setEndpoint("asr.tencentcloudapi.com");
            // 实例化一个client选项，可选的，没有特殊需求可以跳过
            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);
            // 实例化要请求产品的client对象,clientProfile是可选的
            AsrClient client = new AsrClient(cred, "", clientProfile);
            // 实例化一个请求对象,每个接口都会对应一个request对象
            CreateRecTaskRequest req = new CreateRecTaskRequest();

            req.setSourceType(1L);
            req.setChannelNum(1L);
            req.setResTextFormat(1L);
            req.setEngineModelType("16k_zh");
            req.setData(base64Voice);

            // 返回的resp是一个CreateRecTaskResponse的实例，与请求对象对应
            CreateRecTaskResponse resp = client.CreateRecTask(req);

            taskId = resp.getData().getTaskId();

            // 输出json格式的字符串回包
            System.out.println(CreateRecTaskResponse.toJsonString(resp));
        } catch (TencentCloudSDKException e) {
            System.out.println(e.toString());
        }
        return taskId;
    }

    public Sentence describeTask(Long taskId){
        try{
            // 实例化一个认证对象，入参需要传入腾讯云账户 SecretId 和 SecretKey，此处还需注意密钥对的保密
            // 代码泄露可能会导致 SecretId 和 SecretKey 泄露，并威胁账号下所有资源的安全性。以下代码示例仅供参考，建议采用更安全的方式来使用密钥，请参见：https://cloud.tencent.com/document/product/1278/85305
            // 密钥可前往官网控制台 https://console.cloud.tencent.com/cam/capi 进行获取
            Credential cred = new Credential("AKIDjE5cts5qCS2DoOzkWxrL5rqyqf4VqzES", "s5HCtAXBiU1uSLnY3pa6XUeBMGwjWFGE");
            // 实例化一个http选项，可选的，没有特殊需求可以跳过
            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setEndpoint("asr.tencentcloudapi.com");
            // 实例化一个client选项，可选的，没有特殊需求可以跳过
            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);
            // 实例化要请求产品的client对象,clientProfile是可选的
            AsrClient client = new AsrClient(cred, "", clientProfile);
            // 实例化一个请求对象,每个接口都会对应一个request对象
            DescribeTaskStatusRequest req = new DescribeTaskStatusRequest();

            req.setTaskId(taskId);

            // 返回的resp是一个DescribeTaskStatusResponse的实例，与请求对象对应
            DescribeTaskStatusResponse resp = client.DescribeTaskStatus(req);

            // 输出json格式的字符串回包
            System.out.println(CreateRecTaskResponse.toJsonString(resp));

            while(resp.getData().getStatus()==1L||resp.getData().getStatus()==0L){
                resp = client.DescribeTaskStatus(req);
            }

            if(resp.getData().getStatus()==3L){
                return null;
            }

            Sentence sentence = new Sentence();
            sentence.setBody(resp.getData().getResultDetail()[0].getFinalSentence());
            sentence.setSlicedBody(resp.getData().getResultDetail()[0].getSliceSentence());

            ArrayList<String> words = new ArrayList<>();
            int n = resp.getData().getResultDetail()[0].getWordsNum().intValue();
            for(int i=0;i<n;i++){
                words.add(resp.getData().getResultDetail()[0].getWords()[i].getWord());
            }

            sentence.setWords(words);

            return sentence;

        } catch (TencentCloudSDKException e) {
            System.out.println(e.toString());
        }
        return null;
    }

    public static void main(String [] args) {

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
