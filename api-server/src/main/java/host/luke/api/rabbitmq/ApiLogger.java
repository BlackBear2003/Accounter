package host.luke.api.rabbitmq;

import com.alibaba.fastjson2.JSON;
import host.luke.common.pojo.MyApiLog;

import org.springframework.data.mongodb.core.MongoTemplate;

public class ApiLogger implements Runnable{

    private MongoTemplate mongoTemplate;
    private MyApiLog myApiLog;

    public ApiLogger(String myApiLog,MongoTemplate mongoTemplate) {
        //System.out.println(myApiLog);
        this.myApiLog = JSON.parseObject(myApiLog, MyApiLog.class);
        this.mongoTemplate = mongoTemplate;
    }

    public ApiLogger() {
    }

    @Override
    public void run() {

        mongoTemplate.insert(myApiLog);
        System.out.println(myApiLog.toString());
    }
}
