package host.luke.common.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("t_multi_ledger")
public class MultiLedger {
    @TableId(type = IdType.AUTO)
    private Integer multiLedgerId;
    private String multiLedgerName;
    private String description;
    private String password;
}
