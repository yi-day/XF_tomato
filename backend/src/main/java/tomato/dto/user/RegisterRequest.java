package tomato.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 用户注册请求
 */
@Data
public class RegisterRequest {

    /** 账号 */
    @NotBlank(message = "账号不能为空")
    @Size(min = 4, max = 20, message = "账号长度需在 4 到 20 位之间")
    private String account;

    /** 密码 */
    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 20, message = "密码长度需在 6 到 20 位之间")
    private String password;

    /** 昵称 */
    @NotBlank(message = "昵称不能为空")
    private String nickname;
}
