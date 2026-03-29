package tomato.vo.task;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TaskVO {

    private Long id;
    private String title;
    private String description;
    private Integer expectedFocusMinutes;
    private Boolean finished;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
