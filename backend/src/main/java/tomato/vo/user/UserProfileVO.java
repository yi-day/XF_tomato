package tomato.vo.user;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserProfileVO {

    private Long userId;
    private String username;
    private String nickname;
    private String phone;
    private String avatar;
    private LocalDateTime createdAt;
}
