package tomato.dto.focus;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class FocusStartRequest {

    private Long taskId;

    @Min(value = 1, message = "计划专注时长至少为 1 分钟")
    @Max(value = 300, message = "计划专注时长不能超过 300 分钟")
    private Integer plannedMinutes;
}
