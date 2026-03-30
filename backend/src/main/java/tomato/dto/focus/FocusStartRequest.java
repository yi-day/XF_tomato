package tomato.dto.focus;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 开始专注请求
 */
@Data
public class FocusStartRequest {

    /** 模式: focus, shortBreak, longBreak */
    @NotBlank(message = "模式不能为空")
    private String mode;

    /** 关联任务ID（可选） */
    private Long taskId;
}
