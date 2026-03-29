package tomato.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {

    @NotBlank(message = "用户名不能为空")
    @Size(min = 4, max = 20, message = "用户名长度需在 4 到 20 位之间")
    private String username;

    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 20, message = "密码长度需在 6 到 20 位之间")
    private String password;

    @NotBlank(message = "昵称不能为空")
    @Size(max = 20, message = "昵称长度不能超过 20 位")
    private String nickname;

    @Pattern(regexp = "^1\\d{10}$", message = "手机号格式不正确")
    private String phone;
}
