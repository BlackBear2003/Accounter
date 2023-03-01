package host.luke.common.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("t_ledger")
public class Ledger {
    @TableId
    private Integer ledgerId;
    private Long userId;
    private String ledgerName;
    private String cover;
    private Date createTime;
    private Date updateTime;
}
