package tomato.vo.user;

import lombok.Data;

/**
 * 登录响应
 */
@Data
public class LoginResponse {

    /** 用户ID */
    private String userId;

    /** 认证令牌 */
    private String token;

    /** 昵称 */
    private String nickname;
}
