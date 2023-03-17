package host.luke.common.pojo;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Document(collection = "api_logs")
public class MyApiLog {

    private Long userId;
    private String api;
    private Date callTime;

}
