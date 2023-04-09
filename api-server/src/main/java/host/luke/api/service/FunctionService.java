package host.luke.api.service;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import host.luke.common.pojo.Consumption;
import host.luke.common.utils.TypeUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;

@Component
public class FunctionService {

    @Value("${textin.APPID}")
    private String APPID;

    @Value("${textin.SECRETCODE}")
    private String SECRETCODE;


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


    public Consumption commonReceipt(String fileName) throws ParseException {
        String filePath = judgeEnvPath();
        // 商铺小票识别
        String url = "https://api.textin.com/robot/v1.0/api/bills_crop";
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

        JSONArray list = resJson.getJSONObject("result").getJSONArray("object_list").getJSONObject(0).getJSONArray("item_list");
        //取kind 然后使用自写的util来转为id
        consumption.setTypeId(TypeUtil.getTypeId(resJson.getJSONObject("result").getJSONArray("object_list").getJSONObject(0).getString("kind")));

        for(int i=0;i<list.size();i++){
            JSONObject json = list.getJSONObject(i);
            if(json.getString("key").equals("money")){
                consumption.setAmount(json.getDouble("value"));
            }
            if(json.getString("key").equals("shop")){
                consumption.setStore(json.getString("value"));
            }
            if(json.getString("key").equals("sku")){
                consumption.setConsumptionName(json.getString("value"));
            }
            if(json.getString("key").equals("date")){
                try {
                    consumption.setConsumeTime(sdf.parse(json.getString("value")));
                } catch (ParseException e){
                    e.printStackTrace();
                }
            }
            if(json.getString("key").equals("no")){
                consumption.setDescription("订单号："+json.getString("value"));
            }
        }
        consumption.setCredential(fileName);

        return consumption;

    }


    public Consumption shopReceipt(String fileName) throws ParseException {
        String filePath = judgeEnvPath();
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

        System.out.println(resJson.toJSONString());

        JSONArray list = resJson.getJSONObject("result").getJSONArray("item_list");
        //没有Kind
        //consumption.setTypeId(TypeUtil.getTypeId(resJson.getJSONObject("result").getJSONArray("object_list").getJSONObject(0).getString("kind")));

        for(int i=0;i<list.size();i++){
            JSONObject json = list.getJSONObject(i);
            if(json.getString("key").equals("money")){
                consumption.setAmount(json.getDouble("value"));
            }
            if(json.getString("key").equals("shop")){
                consumption.setStore(json.getString("value"));
            }
            if(json.getString("key").equals("sku")){
                consumption.setConsumptionName(json.getString("value"));
            }
            if(json.getString("key").equals("date")){
                try {
                    consumption.setConsumeTime(sdf.parse(json.getString("value")));
                } catch (ParseException e){
                    e.printStackTrace();
                }
            }
            if(json.getString("key").equals("no")){
                consumption.setDescription("订单号："+json.getString("value"));
            }
        }
        consumption.setCredential(fileName);

        return consumption;

    }



    public Consumption trainTicket(String fileName){
        String filePath = judgeEnvPath();
        // 火车票识别
        String url = "https://api.textin.com/robot/v1.0/api/train_ticket";
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

        String ticketNumber = null;
        String departureStation = null;
        String trainNumber = null;
        String arrivalStation = null;
        String departureDate = null;
        Double price = null;
        String ticketClass = null;
        String passengerName = null;
        String seatNumber = null;



        for(int i=0;i<list.size();i++){
            JSONObject json = list.getJSONObject(i);
            if(json.getString("key").equals("ticket_number")){
                ticketNumber = json.getString("value");
            }
            if(json.getString("key").equals("departure_station")){
                departureStation = json.getString("value");
            }
            if(json.getString("key").equals("train_number")){
                trainNumber = json.getString("value");
            }
            if(json.getString("key").equals("arrival_station")){
                arrivalStation = json.getString("value");
            }
            if(json.getString("key").equals("price")){
                price = json.getDouble("value");
            }
            if(json.getString("key").equals("departure_date")){
                departureDate = json.getString( "value");
            }
            if(json.getString("key").equals("ticket_class")){
                ticketClass = json.getString("value");
            }
            if(json.getString("key").equals("passenger_name")){
                passengerName = json.getString("value");
            }
            if(json.getString("key").equals("seat_number")){
                seatNumber = json.getString("value");
            }
        }

        consumption.setAmount(price);
        consumption.setTypeId(13);
        consumption.setConsumptionName("从"+departureStation+"出发到"+arrivalStation+"的火车"+ticketClass);
        consumption.setDescription("火车票红色编码:"+ticketNumber+","+
                "出发站:"+departureStation+","+
                "车次号:"+trainNumber+","+
                "目的地:"+arrivalStation+","+
                "出发时间:"+departureDate+","+
                "价格:"+price+","+
                "车票等级:"+ticketClass+","+
                "乘车人姓名:"+passengerName+","+
                "车座号:"+seatNumber);
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
