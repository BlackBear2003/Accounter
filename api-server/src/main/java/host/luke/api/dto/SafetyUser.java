package host.luke.api.dto;

import host.luke.common.pojo.User;
import lombok.Data;

@Data
public class SafetyUser {

    private Long userId;
    private String username;
    private String nickname;
    private String mobile;

    public SafetyUser() {
    }

    public SafetyUser(User user) {
        this.mobile = user.getMobile();
        this.userId = user.getUserId();
        this.nickname = user.getNickname();
        this.username = user.getUsername();
    }
}
