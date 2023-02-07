package host.luke.common.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("t_consumption")
public class Consumption {
    @TableId
    private Long consumptionId;
    private Double amount;
    private String type;//dinning travel entertainment study daily others
    private String store;
    private Date consumeTime;

}
