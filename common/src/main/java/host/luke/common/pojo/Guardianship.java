package host.luke.common.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;
@TableName("t_guardianship")
@Data
public class Guardianship {
    @TableId
    Integer guardianshipId;
    Long wardId;
    Long guardianId;
    String relationship;
    Date bindDate;
}
