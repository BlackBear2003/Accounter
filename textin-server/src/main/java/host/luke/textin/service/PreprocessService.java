package host.luke.textin.service;

import com.alibaba.fastjson2.JSONObject;
import host.luke.common.utils.Base64Util;
import jakarta.annotation.Resource;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Component
public class PreprocessService {

    // 此种方式可以在创建时 设置一些默认值

    @Value("${textin.APPID}")
    private String APPID;

    @Value("${textin.SECRETCODE}")
    private String SECRETCODE;

    @Value("${file.win.path}")
    String winPath ;
    @Value("${file.mac.path}")
    String macPath ;
    @Value("${file.linux.path}")
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


    /**
     * 通过传入文件名称找到对应图片，发送到官方接口处，进行响应校验并返回处理过的图片地址
     * @param fileName
     * @return
     * @throws IOException
     */
    public String trimAndCorrect(String fileName) throws IOException {
        String filePath = judgeEnvPath();

        // 文档图像切边矫正
        String url = "https://api.textin.com/ai/service/v1/dewarp";
        // 请登录后前往 “工作台-账号设置-开发者信息” 查看 x-ti-app-id
        BufferedReader in = null;
        DataOutputStream out = null;
        String result = "";
        try {
            byte[] imgData = readFile(filePath+fileName); // image
            URL realUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection)realUrl.openConnection();
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("Content-Type", "application/octet-stream");
            conn.setRequestProperty("x-ti-app-id", APPID);
            conn.setRequestProperty("x-ti-secret-code", SECRETCODE);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod("POST"); // 设置请求方式
            out = new DataOutputStream(conn.getOutputStream());
            out.write(imgData);
            out.flush();
            out.close();
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！" + e);
            e.printStackTrace();
        }
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        System.out.println(result);
        String newName = generateFileName(fileName);

        JSONObject resJson = JSONObject.parseObject(result);
        if(resJson.getInteger("code")!=200){
            throw new RuntimeException("API出现错误");
        }
        String image = resJson.getJSONObject("result").getString("image");

        Base64Util.GenerateImage(image,filePath+newName);


        return "http://luke.host/images/"+newName;
    }


    /**
     * 通过传入文件名称找到对应图片，发送到官方接口处，进行响应校验并返回处理过的图片地址
     * @param fileName
     * @return
     * @throws IOException
     */
    public String cropAndEnhance(String fileName) throws IOException {
        String filePath = judgeEnvPath();

        // 图片切边增强
        String url = "https://api.textin.com/ai/service/v1/crop_enhance_image";
        // 请登录后前往 “工作台-账号设置-开发者信息” 查看 x-ti-app-id
        BufferedReader in = null;
        DataOutputStream out = null;
        String result = "";
        try {
            byte[] imgData = readFile(filePath+fileName); // image
            URL realUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection)realUrl.openConnection();
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("Content-Type", "application/octet-stream");
            conn.setRequestProperty("x-ti-app-id", APPID);
            conn.setRequestProperty("x-ti-secret-code", SECRETCODE);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod("POST"); // 设置请求方式
            out = new DataOutputStream(conn.getOutputStream());
            out.write(imgData);
            out.flush();
            out.close();
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！" + e);
            e.printStackTrace();
        }
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        System.out.println(result);
        String newName = generateFileName(fileName);

        JSONObject resJson = JSONObject.parseObject(result);
        if(resJson.getInteger("code")!=200){
            throw new RuntimeException("API出现错误");
        }
        String image = resJson.getJSONObject("result").getJSONArray("image_list").getJSONObject(0).getString("image");

        Base64Util.GenerateImage(image,filePath+newName);


        return "http://luke.host/images/"+newName;
    }

    /**
     * 通过传入文件名称找到对应图片，发送到官方接口处，进行响应校验并返回处理过的图片地址
     * @param fileName
     * @return
     * @throws IOException
     */
    public String demoire(String fileName) throws IOException {
        String filePath = judgeEnvPath();

        // 去屏幕纹
        String url = "https://api.textin.com/ai/service/v1/demoire";
        // 请登录后前往 “工作台-账号设置-开发者信息” 查看 x-ti-app-id
        BufferedReader in = null;
        DataOutputStream out = null;
        String result = "";
        try {
            byte[] imgData = readFile(filePath+fileName); // image
            URL realUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection)realUrl.openConnection();
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("Content-Type", "application/octet-stream");
            conn.setRequestProperty("x-ti-app-id", APPID);
            conn.setRequestProperty("x-ti-secret-code", SECRETCODE);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod("POST"); // 设置请求方式
            out = new DataOutputStream(conn.getOutputStream());
            out.write(imgData);
            out.flush();
            out.close();
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！" + e);
            e.printStackTrace();
        }
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        System.out.println(result);
        String newName = generateFileName(fileName);

        JSONObject resJson = JSONObject.parseObject(result);
        if(resJson.getInteger("code")!=200){
            throw new RuntimeException("API出现错误");
        }
        String image = resJson.getJSONObject("result").getString("image");

        Base64Util.GenerateImage(image,filePath+newName);


        return "http://luke.host/images/"+newName;
    }


    /**
     * 通过传入文件名称找到对应图片，发送到官方接口处，进行响应校验并返回处理过的图片地址
     * @param fileName
     * @return
     * @throws IOException
     */
    public String watermarkRemove(String fileName) throws IOException {
        String filePath = judgeEnvPath();

        // 图像水印去除
        String url = "https://api.textin.com/ai/service/v1/image/watermark_remove";
        // 请登录后前往 “工作台-账号设置-开发者信息” 查看 x-ti-app-id
        BufferedReader in = null;
        DataOutputStream out = null;
        String result = "";
        try {
            byte[] imgData = readFile(filePath+fileName); // image
            URL realUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection)realUrl.openConnection();
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("Content-Type", "application/octet-stream");
            conn.setRequestProperty("x-ti-app-id", APPID);
            conn.setRequestProperty("x-ti-secret-code", SECRETCODE);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod("POST"); // 设置请求方式
            out = new DataOutputStream(conn.getOutputStream());
            out.write(imgData);
            out.flush();
            out.close();
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！" + e);
            e.printStackTrace();
        }
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        System.out.println(result);
        String newName = generateFileName(fileName);

        JSONObject resJson = JSONObject.parseObject(result);
        if(resJson.getInteger("code")!=200){
            throw new RuntimeException("API出现错误");
        }
        String image = resJson.getJSONObject("result").getString("image");

        Base64Util.GenerateImage(image,filePath+newName);


        return "http://luke.host/images/"+newName;
    }

    /**
     * 通过传入文件名称找到对应图片，发送到官方接口处，进行响应校验并返回处理过的图片地址
     * @param fileName
     * @return
     * @throws IOException
     */
    public String handwrittenErase(String fileName) throws IOException {
        String filePath = judgeEnvPath();
        // 自动擦除手写文字
        String url = "https://api.textin.com/ai/service/v1/handwritten_erase";
        // 请登录后前往 “工作台-账号设置-开发者信息” 查看 x-ti-app-id
        BufferedReader in = null;
        DataOutputStream out = null;
        String result = "";
        try {
            byte[] imgData = readFile(filePath+fileName); // image
            URL realUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection)realUrl.openConnection();
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("Content-Type", "application/octet-stream");
            conn.setRequestProperty("x-ti-app-id", APPID);
            conn.setRequestProperty("x-ti-secret-code", SECRETCODE);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod("POST"); // 设置请求方式
            out = new DataOutputStream(conn.getOutputStream());
            out.write(imgData);
            out.flush();
            out.close();
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！" + e);
            e.printStackTrace();
        }
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        System.out.println(result);
        String newName = generateFileName(fileName);

        JSONObject resJson = JSONObject.parseObject(result);
        if(resJson.getInteger("code")!=200){
            throw new RuntimeException("API出现错误");
        }
        String image = resJson.getJSONObject("result").getString("image");

        Base64Util.GenerateImage(image,filePath+newName);


        return "http://luke.host/images/"+newName;
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


    public String generateFileName(String fileName){

        String[] t = Objects.requireNonNull(fileName.split("\\."));
        String suffix = t[t.length-1];
        System.out.println(suffix);
        UUID uuid = UUID.randomUUID();
        return uuid+"."+suffix;

    }

}
