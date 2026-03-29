package tomato.vo.user;

import lombok.Data;

@Data
public class LoginResponse {

    private Long userId;
    private String username;
    private String nickname;
    private String phone;
    private String avatar;
    private String token;
}
