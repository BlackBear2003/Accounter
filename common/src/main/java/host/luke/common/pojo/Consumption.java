package host.luke.common.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@TableName("t_consumption")
public class Consumption {
    @TableId
    private Long consumptionId;
    private String consumptionName;
    private String description;
    private Double amount;
    private Integer typeId;
    private String store;
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date consumeTime;
    private String credential;

}
