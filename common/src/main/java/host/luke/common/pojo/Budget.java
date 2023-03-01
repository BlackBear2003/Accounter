package host.luke.common.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("t_budget")
public class Budget {
    @TableId
    private Integer budgetId;
    private String budgetName;
    private String budgetDescription;
    private Double money;
    private Integer budgetCycle;
    private Date beginDate;
}
