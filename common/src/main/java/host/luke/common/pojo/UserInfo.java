package host.luke.common.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("t_user_info")
public class UserInfo {
    private Long userId;
    private Integer age;
    private String email;
    private String avatar;


}
