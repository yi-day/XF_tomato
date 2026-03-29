package tomato.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateProfileRequest {

    @NotBlank(message = "昵称不能为空")
    @Size(max = 20, message = "昵称长度不能超过 20 位")
    private String nickname;

    @Pattern(regexp = "^1\\d{10}$", message = "手机号格式不正确")
    private String phone;

    @Size(max = 100, message = "头像地址长度不能超过 100 位")
    private String avatar;
}
