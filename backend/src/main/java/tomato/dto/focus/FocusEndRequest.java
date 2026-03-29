package tomato.dto.focus;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class FocusEndRequest {

    @NotNull(message = "专注记录 ID 不能为空")
    private Long focusRecordId;

    private Integer actualMinutes;

    private Boolean interrupted;
}
