package tomato.dto.focus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 结束专注请求
 */
@Data
public class FocusEndRequest {

    /** 会话ID */
    @NotBlank(message = "会话ID不能为空")
    private String sessionId;

    /** 实际专注时长（分钟） */
    @NotNull(message = "专注时长不能为空")
    private Integer duration;
}
