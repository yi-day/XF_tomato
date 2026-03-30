package tomato.dto.task;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

/**
 * 更新任务请求
 */
@Data
public class TaskUpdateRequest {

    /** 任务标题 */
    @NotBlank(message = "任务标题不能为空")
    private String title;

    /** 优先级: HIGH, MEDIUM, LOW */
    @NotBlank(message = "优先级不能为空")
    private String priority;

    /** 预估番茄数 */
    @NotNull(message = "预估番茄数不能为空")
    private Integer estimatedPomodoros;

    /** 截止日期 */
    private LocalDate deadline;
}
