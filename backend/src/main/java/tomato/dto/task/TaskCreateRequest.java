package tomato.dto.task;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class TaskCreateRequest {

    @NotBlank(message = "任务标题不能为空")
    @Size(max = 50, message = "任务标题长度不能超过 50 位")
    private String title;

    @Size(max = 255, message = "任务描述长度不能超过 255 位")
    private String description;

    @Max(value = 600, message = "预估专注时长不能超过 600 分钟")
    private Integer expectedFocusMinutes;
}
