package host.luke.common.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("t_type")
public class Type {

    @TableId
    private Integer typeId;

    private String typeName;
    private Integer level;
    private Integer parentId;


}
