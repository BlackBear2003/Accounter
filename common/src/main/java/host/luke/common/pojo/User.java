package host.luke.common.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
@TableName("t_user")
public class User {
    @TableId
    private Long userId;
    private String username;
    private String password;
    private String mobile;
    private String nickname;
}
