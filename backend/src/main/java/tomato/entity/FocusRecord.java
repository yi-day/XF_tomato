package tomato.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FocusRecord {

    private Long id;
    private Long userId;
    private Long taskId;
    private Integer plannedMinutes;
    private Integer actualMinutes;
    private String status;
    private LocalDateTime startedAt;
    private LocalDateTime endedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
