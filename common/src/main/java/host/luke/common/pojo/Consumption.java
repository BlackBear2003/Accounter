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
    private String consumptionName;
    private String description;
    private Double amount;
    private Integer typeId;
    private String store;
    private Date consumeTime;

}
