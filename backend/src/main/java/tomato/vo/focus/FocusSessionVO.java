package tomato.vo.focus;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FocusSessionVO {

    private Long id;
    private Long taskId;
    private Integer plannedMinutes;
    private Integer actualMinutes;
    private String status;
    private LocalDateTime startedAt;
    private LocalDateTime endedAt;
}
