package host.luke.textin.service;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import host.luke.common.pojo.Consumption;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Component
public class FunctionService {

    @Value("${textin.APPID}")
    private String APPID;

    @Value("${textin.SECRETCODE}")
    private String SECRETCODE;

    @Value("${file.path}")
    private String filePath;

    public Consumption commonReceipt(String fileName){
        // 商铺小票识别
        String url = "https://api.textin.com/robot/v1.0/api/receipt";
        // 请登录后前往 “工作台-账号设置-开发者信息” 查看 x-ti-app-id
        // 示例代码中 x-ti-app-id 非真实数据
        String appId = APPID;
        // 请登录后前往 “工作台-账号设置-开发者信息” 查看 x-ti-secret-code
        // 示例代码中 x-ti-secret-code 非真实数据
        String secretCode = SECRETCODE;
        BufferedReader in = null;
        DataOutputStream out = null;
        String result = "";
        try {
            byte[] imgData = readFile(filePath+fileName); // image
            URL realUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection)realUrl.openConnection();
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("Content-Type", "application/octet-stream");
            conn.setRequestProperty("x-ti-app-id", appId);
            conn.setRequestProperty("x-ti-secret-code", secretCode);
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

        JSONObject resJson = JSONObject.parseObject(result);

        if(resJson.getInteger("code")!=200){
            throw new RuntimeException("API出现错误");
        }

        Consumption consumption = new Consumption();

        JSONArray list = resJson.getJSONObject("result").getJSONArray("item_list");

        for(int i=0;i<list.size();i++){
            JSONObject json = list.getJSONObject(i);
            if(json.getString("key").equals("money")){
                consumption.setAmount(json.getDouble("value"));
            }
            if(json.getString("key").equals("shop")){
                consumption.setStore(json.getString("value"));
            }
            if(json.getString("key").equals("sku")){
                consumption.setDescription(json.getString("value"));
            }
            if(json.getString("key").equals("date")){
                consumption.setConsumeTime(json.getDate("value"));
            }
        }

        return consumption;

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
